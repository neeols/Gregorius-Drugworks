package com.wurtzitane.gregoriusdrugworkspersistence.inhalation.client;

import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.ItemInhalationConsumable;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketCancelInhalationSequence;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketStartInhalationSequence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class GregoriusDrugworksInhalationClientHooks {

    private static final Map<Integer, InhalationSequenceState> ACTIVE = new HashMap<>();
    private static boolean initialised = false;

    private GregoriusDrugworksInhalationClientHooks() {
    }

    public static void preInit() {
        if (initialised) {
            return;
        }
        initialised = true;

        MinecraftForge.EVENT_BUS.register(new GregoriusDrugworksInhalationClientHooks());
    }

    public static boolean hasActiveSequence(int entityId) {
        return ACTIVE.containsKey(entityId);
    }

    public static void startSequence(PacketStartInhalationSequence message) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world == null) {
            return;
        }

        InhalationSequenceState existing = ACTIVE.get(message.getPlayerEntityId());
        if (existing != null && existing.getSequenceId() >= message.getSequenceId()) {
            return;
        }

        ACTIVE.put(
                message.getPlayerEntityId(),
                new InhalationSequenceState(
                        message.getPlayerEntityId(),
                        message.getItemId(),
                        message.getHand(),
                        minecraft.world.getTotalWorldTime(),
                        message.getDurationTicks(),
                        message.getInhaleStartTick(),
                        message.getInhaleEndTick(),
                        message.getExhaleStartTick(),
                        message.getExhaleEndTick(),
                        message.isLocalCameraNudge(),
                        message.getSequenceId()
                )
        );
    }

    public static void cancelSequence(PacketCancelInhalationSequence message) {
        InhalationSequenceState state = ACTIVE.get(message.getPlayerEntityId());
        if (state != null && state.getSequenceId() <= message.getSequenceId()) {
            ACTIVE.remove(message.getPlayerEntityId());
        }
    }

    @SubscribeEvent
    public void onRenderSpecificHand(RenderSpecificHandEvent event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world == null || minecraft.player == null || minecraft.gameSettings.thirdPersonView != 0) {
            return;
        }

        InhalationSequenceState state = ACTIVE.get(minecraft.player.getEntityId());
        if (state == null || state.getHand() != event.getHand()) {
            return;
        }

        InhalationDefinition definition = ItemInhalationConsumable.getDefinition(resolveDefinitionKey(state.getItemId()));
        if (definition == null || !definition.isUseCustomRenderer()) {
            return;
        }

        ItemStack renderStack = !event.getItemStack().isEmpty()
                ? event.getItemStack()
                : createRenderStack(state.getItemId());
        if (renderStack.isEmpty()) {
            return;
        }

        event.setCanceled(true);
        renderFirstPersonVape(minecraft, renderStack, state, event.getHand(), event.getPartialTicks());
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world == null) {
            return;
        }

        RenderManager renderManager = minecraft.getRenderManager();
        long worldTime = minecraft.world.getTotalWorldTime();
        float partialTicks = event.getPartialTicks();

        for (InhalationSequenceState state : ACTIVE.values()) {
            Entity entity = minecraft.world.getEntityByID(state.getPlayerEntityId());
            if (!(entity instanceof EntityPlayer)) {
                continue;
            }

            EntityPlayer player = (EntityPlayer) entity;
            if (isLocalFirstPersonPlayer(minecraft, player)) {
                continue;
            }

            InhalationDefinition definition = ItemInhalationConsumable.getDefinition(resolveDefinitionKey(state.getItemId()));
            if (definition == null || !definition.isUseCustomRenderer()) {
                continue;
            }

            Item item = resolveRenderedItem(state.getItemId());
            if (item == null) {
                continue;
            }

            Vec3d vapePos = getVapePosition(player, state, worldTime, partialTicks);
            float handSign = state.getHand() == net.minecraft.util.EnumHand.MAIN_HAND ? 1.0F : -1.0F;
            float yaw = interpolateAngle(player.prevRotationYawHead, player.rotationYawHead, partialTicks);
            float pitch = interpolate(player.prevRotationPitch, player.rotationPitch, partialTicks);

            GlStateManager.pushMatrix();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.translate(
                    vapePos.x - renderManager.viewerPosX,
                    vapePos.y - renderManager.viewerPosY,
                    vapePos.z - renderManager.viewerPosZ
            );
            GlStateManager.rotate(-yaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(10.0F * handSign, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-20.0F + pitch * 0.10F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(56.0F * handSign, 0.0F, 0.0F, 1.0F);
            GlStateManager.scale(0.76F, 0.76F, 0.76F);

            minecraft.getRenderItem().renderItem(new ItemStack(item), ItemCameraTransforms.TransformType.FIXED);

            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world == null) {
            ACTIVE.clear();
            return;
        }

        long worldTime = minecraft.world.getTotalWorldTime();

        Iterator<Map.Entry<Integer, InhalationSequenceState>> iterator = ACTIVE.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, InhalationSequenceState> entry = iterator.next();
            InhalationSequenceState state = entry.getValue();

            Entity entity = minecraft.world.getEntityByID(state.getPlayerEntityId());
            if (!(entity instanceof EntityPlayer) || state.isExpired(worldTime)) {
                iterator.remove();
                continue;
            }

            if (entity == minecraft.player && state.isLocalCameraNudge() && minecraft.gameSettings.thirdPersonView == 0) {
                applyLocalCameraNudge((EntityPlayerSP) entity, state, worldTime);
            }
        }
    }

    private static void applyLocalCameraNudge(EntityPlayerSP player, InhalationSequenceState state, long worldTime) {
        InhalationDefinition definition = ItemInhalationConsumable.getDefinition(state.getItemId());
        if (definition == null) {
            return;
        }

        float glow = state.getGlow(worldTime, 1.0F, definition.getGlowMin(), definition.getGlowMax());
        float targetPitch = -4.0F - glow * 3.0F;
        player.rotationPitch = MathHelper.clamp(player.rotationPitch + (targetPitch - player.rotationPitch) * 0.15F, -89.9F, 89.9F);
        player.prevRotationPitch = player.rotationPitch;
    }

    private static Vec3d getVapePosition(EntityPlayer player, InhalationSequenceState state, long worldTime, float partialTicks) {
        Vec3d eye = player.getPositionEyes(partialTicks);
        Vec3d look = player.getLook(partialTicks).normalize();
        Vec3d up = new Vec3d(0.0D, 1.0D, 0.0D);
        Vec3d right = look.crossProduct(up);
        if (right.lengthSquared() < 1.0E-4D) {
            right = new Vec3d(1.0D, 0.0D, 0.0D);
        } else {
            right = right.normalize();
        }

        double handSign = state.getHand() == net.minecraft.util.EnumHand.MAIN_HAND ? 1.0D : -1.0D;
        float raiseProgress = easeOut(state.getSegmentProgress(worldTime, partialTicks, 0, state.getInhaleStartTick()));
        float exhaleProgress = easeInOut(state.getSegmentProgress(worldTime, partialTicks, state.getExhaleStartTick(), state.getExhaleEndTick()));
        Vec3d mouthAnchor = eye
                .add(look.scale(0.08D))
                .subtract(up.scale(0.12D));
        double side = lerp(0.16D, 0.04D, raiseProgress) - exhaleProgress * 0.02D;
        double down = lerp(0.12D, 0.04D, raiseProgress) + exhaleProgress * 0.01D;
        double retreat = lerp(0.18D, 0.06D, raiseProgress) + exhaleProgress * 0.05D;
        double bob = Math.sin(((worldTime - state.getStartTick()) + partialTicks) * 0.18D) * 0.006D * (1.0D - exhaleProgress * 0.7D);

        return mouthAnchor
                .add(right.scale(side * handSign))
                .subtract(up.scale(down - bob))
                .subtract(look.scale(retreat));
    }

    private static void renderFirstPersonVape(Minecraft minecraft, ItemStack renderStack, InhalationSequenceState state, EnumHand hand, float partialTicks) {
        long worldTime = minecraft.world.getTotalWorldTime();
        float raiseProgress = easeOut(state.getSegmentProgress(worldTime, partialTicks, 0, state.getInhaleStartTick()));
        float exhaleProgress = easeInOut(state.getSegmentProgress(worldTime, partialTicks, state.getExhaleStartTick(), state.getExhaleEndTick()));
        float handSign = hand == EnumHand.MAIN_HAND ? 1.0F : -1.0F;

        float x = 0.40F * handSign - 0.24F * handSign * raiseProgress + 0.02F * handSign * exhaleProgress;
        float y = -0.30F + 0.22F * raiseProgress - 0.02F * exhaleProgress;
        float z = -0.98F + 0.34F * raiseProgress - 0.06F * exhaleProgress;

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableStandardItemLighting();

        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(8.0F * handSign, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-18.0F + exhaleProgress * 10.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(58.0F * handSign, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(0.76F, 0.76F, 0.76F);

        minecraft.getRenderItem().renderItem(renderStack, ItemCameraTransforms.TransformType.FIXED);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    private static Item resolveRenderedItem(String itemId) {
        ResourceLocation location = itemId.indexOf(':') >= 0
                ? new ResourceLocation(itemId)
                : new ResourceLocation("gregoriusdrugworkspersistence", itemId);
        Item item = Item.REGISTRY.getObject(location);
        if (item == null && itemId.indexOf(':') >= 0) {
            item = Item.REGISTRY.getObject(new ResourceLocation(resolveDefinitionKey(itemId)));
        }
        return item;
    }

    private static ItemStack createRenderStack(String itemId) {
        Item item = resolveRenderedItem(itemId);
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }

    private static String resolveDefinitionKey(String itemId) {
        int separator = itemId.indexOf(':');
        return separator >= 0 ? itemId.substring(separator + 1) : itemId;
    }

    private static boolean isLocalFirstPersonPlayer(Minecraft minecraft, EntityPlayer player) {
        return player == minecraft.player && minecraft.gameSettings.thirdPersonView == 0;
    }

    private static float interpolate(float previous, float current, float partialTicks) {
        return previous + (current - previous) * partialTicks;
    }

    private static float interpolateAngle(float previous, float current, float partialTicks) {
        return previous + MathHelper.wrapDegrees(current - previous) * partialTicks;
    }

    private static float easeOut(float value) {
        float clamped = MathHelper.clamp(value, 0.0F, 1.0F);
        float inverse = 1.0F - clamped;
        return 1.0F - inverse * inverse * inverse;
    }

    private static float easeInOut(float value) {
        float clamped = MathHelper.clamp(value, 0.0F, 1.0F);
        return clamped < 0.5F
                ? 4.0F * clamped * clamped * clamped
                : 1.0F - (float) Math.pow(-2.0F * clamped + 2.0F, 3.0D) / 2.0F;
    }

    private static double lerp(double start, double end, float progress) {
        return start + (end - start) * progress;
    }
}
