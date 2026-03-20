package com.wurtzitane.gregoriusdrugworkspersistence.pill.client;

import com.wurtzitane.gregoriusdrugworkspersistence.pill.ItemPillBase;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.PillItemDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketStartPillAnimation;
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

public final class GregoriusDrugworksPillClientHooks {

    private static final Map<Integer, PillAnimationState> ACTIVE = new HashMap<>();
    private static boolean initialised = false;

    private GregoriusDrugworksPillClientHooks() {
    }

    public static void preInit() {
        if (initialised) {
            return;
        }
        initialised = true;

        MinecraftForge.EVENT_BUS.register(new GregoriusDrugworksPillClientHooks());
    }

    public static boolean hasActiveSequence(int entityId) {
        return ACTIVE.containsKey(entityId);
    }

    public static void startAnimation(PacketStartPillAnimation message) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world == null) {
            return;
        }

        Entity entity = minecraft.world.getEntityByID(message.getPlayerEntityId());
        if (!(entity instanceof EntityPlayer)) {
            return;
        }

        PillAnimationState existing = ACTIVE.get(message.getPlayerEntityId());
        if (existing != null && existing.getSequenceId() >= message.getSequenceId()) {
            return;
        }

        ACTIVE.put(
                message.getPlayerEntityId(),
                PillAnimationState.capture(
                        message.getPlayerEntityId(),
                        message.getItemId(),
                        message.getHand(),
                        minecraft.world.getTotalWorldTime(),
                        message.getDurationTicks(),
                        message.getArcHeight(),
                        message.getLaunchForward(),
                        message.getMouthOffsetY(),
                        message.getSpinXPerTick(),
                        message.getSpinYPerTick(),
                        message.getSpinZPerTick(),
                        message.isLockCamera(),
                        message.getSequenceId(),
                        message.getRenderStack(),
                        (EntityPlayer) entity
                )
        );
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

        Iterator<Map.Entry<Integer, PillAnimationState>> iterator = ACTIVE.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, PillAnimationState> entry = iterator.next();
            PillAnimationState state = entry.getValue();

            Entity entity = minecraft.world.getEntityByID(state.getPlayerEntityId());
            if (!(entity instanceof EntityPlayer) || state.isExpired(worldTime)) {
                iterator.remove();
                continue;
            }

            if (state.isLockCamera()
                    && entity == minecraft.player
                    && minecraft.gameSettings.thirdPersonView == 0
                    && !isLocalFirstPersonPlayer(minecraft, (EntityPlayer) entity)) {
                applyCameraLock(minecraft.player, state, worldTime);
            }
        }
    }

    @SubscribeEvent
    public void onRenderSpecificHand(RenderSpecificHandEvent event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world == null || minecraft.player == null || minecraft.gameSettings.thirdPersonView != 0) {
            return;
        }

        PillAnimationState state = ACTIVE.get(minecraft.player.getEntityId());
        if (state == null || state.getHand() != event.getHand()) {
            return;
        }

        ItemStack renderStack = !event.getItemStack().isEmpty()
                ? event.getItemStack()
                : createRenderStack(state.getItemId());
        if (renderStack.isEmpty()) {
            return;
        }

        event.setCanceled(true);
        renderFirstPersonPill(minecraft, renderStack, state, event.getHand(), event.getPartialTicks());
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

        for (PillAnimationState state : ACTIVE.values()) {
            Entity entity = minecraft.world.getEntityByID(state.getPlayerEntityId());
            if (!(entity instanceof EntityPlayer)) {
                continue;
            }

            EntityPlayer player = (EntityPlayer) entity;
            if (isLocalFirstPersonPlayer(minecraft, player)) {
                continue;
            }

            ItemStack renderStack = state.getRenderStack();
            if (renderStack.isEmpty()) {
                renderStack = createRenderStack(state.getItemId());
            }
            if (renderStack.isEmpty()) {
                continue;
            }
            Vec3d pillPos = state.getPillPosition(player, worldTime, partialTicks);

            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            GlStateManager.enableRescaleNormal();
            RenderHelper.enableStandardItemLighting();

            GlStateManager.translate(
                    pillPos.x - renderManager.viewerPosX,
                    pillPos.y - renderManager.viewerPosY,
                    pillPos.z - renderManager.viewerPosZ
            );

            GlStateManager.rotate(state.getRotationY(worldTime, partialTicks), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(state.getRotationX(worldTime, partialTicks), 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(state.getRotationZ(worldTime, partialTicks), 0.0F, 0.0F, 1.0F);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);

            minecraft.getRenderItem().renderItem(renderStack, ItemCameraTransforms.TransformType.FIXED);

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
        }
    }

    private static void applyCameraLock(EntityPlayerSP player, PillAnimationState state, long worldTime) {
        Vec3d eye = player.getPositionEyes(1.0F);
        Vec3d pillPos = state.getPillPosition(player, worldTime, 1.0F);
        Vec3d delta = pillPos.subtract(eye);

        double horizontal = Math.sqrt(delta.x * delta.x + delta.z * delta.z);
        float targetYaw = (float) (Math.toDegrees(Math.atan2(delta.z, delta.x)) - 90.0D);
        float targetPitch = (float) (-Math.toDegrees(Math.atan2(delta.y, horizontal)));

        player.rotationYaw = approachAngle(player.rotationYaw, targetYaw, 5.0F);
        player.rotationPitch = MathHelper.clamp(approach(player.rotationPitch, targetPitch, 4.0F), -89.9F, 89.9F);
        player.prevRotationYaw = player.rotationYaw;
        player.prevRotationPitch = player.rotationPitch;
    }

    private static float approach(float current, float target, float maxStep) {
        float delta = target - current;
        if (delta > maxStep) {
            return current + maxStep;
        }
        if (delta < -maxStep) {
            return current - maxStep;
        }
        return target;
    }

    private static Item resolveRenderedItem(String itemId) {
        ResourceLocation location = itemId.indexOf(':') >= 0 ? new ResourceLocation(itemId) : new ResourceLocation("gregoriusdrugworkspersistence", itemId);
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

    private static void renderFirstPersonPill(Minecraft minecraft, ItemStack renderStack, PillAnimationState state, EnumHand hand, float partialTicks) {
        long worldTime = minecraft.world.getTotalWorldTime();
        float progress = state.getProgress(worldTime, partialTicks);
        float handSign = hand == EnumHand.MAIN_HAND ? 1.0F : -1.0F;
        Vec3d pos = getFirstPersonPillPosition(progress, handSign);

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableStandardItemLighting();

        GlStateManager.translate(pos.x, pos.y, pos.z);
        GlStateManager.rotate(state.getRotationY(worldTime, partialTicks) * 0.35F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(state.getRotationX(worldTime, partialTicks) * 0.22F + 12.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(state.getRotationZ(worldTime, partialTicks) * 0.18F - 18.0F * handSign, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(0.34F, 0.34F, 0.34F);

        minecraft.getRenderItem().renderItem(renderStack, ItemCameraTransforms.TransformType.FIXED);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    private static Vec3d getFirstPersonPillPosition(float progress, float handSign) {
        Vec3d start = new Vec3d(0.34D * handSign, -0.22D, -0.92D);
        Vec3d control = new Vec3d(0.18D * handSign, 0.10D, -0.70D);
        Vec3d apex = new Vec3d(0.08D * handSign, 0.18D, -0.58D);
        Vec3d mouth = new Vec3d(0.02D * handSign, -0.04D, -0.42D);

        if (progress <= 0.58F) {
            float launch = smooth(progress / 0.58F);
            return quadraticBezier(start, control, apex, launch);
        }

        float settle = smooth((progress - 0.58F) / 0.42F);
        return lerp(apex, mouth, settle);
    }

    private static String resolveDefinitionKey(String itemId) {
        int separator = itemId.indexOf(':');
        return separator >= 0 ? itemId.substring(separator + 1) : itemId;
    }

    private static float approachAngle(float current, float target, float maxStep) {
        float delta = MathHelper.wrapDegrees(target - current);
        if (delta > maxStep) {
            delta = maxStep;
        }
        if (delta < -maxStep) {
            delta = -maxStep;
        }
        return current + delta;
    }

    private static boolean isLocalFirstPersonPlayer(Minecraft minecraft, EntityPlayer player) {
        return player == minecraft.player && minecraft.gameSettings.thirdPersonView == 0;
    }

    private static Vec3d quadraticBezier(Vec3d start, Vec3d control, Vec3d end, float t) {
        double inverse = 1.0D - t;
        return start.scale(inverse * inverse)
                .add(control.scale(2.0D * inverse * t))
                .add(end.scale(t * t));
    }

    private static Vec3d lerp(Vec3d start, Vec3d end, float progress) {
        return new Vec3d(
                start.x + (end.x - start.x) * progress,
                start.y + (end.y - start.y) * progress,
                start.z + (end.z - start.z) * progress
        );
    }

    private static float smooth(float value) {
        float clamped = MathHelper.clamp(value, 0.0F, 1.0F);
        return clamped * clamped * (3.0F - 2.0F * clamped);
    }
}
