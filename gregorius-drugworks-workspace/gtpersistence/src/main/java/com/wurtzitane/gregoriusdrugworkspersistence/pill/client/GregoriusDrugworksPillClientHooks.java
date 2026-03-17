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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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

    public static void startAnimation(PacketStartPillAnimation message) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world == null) {
            return;
        }

        PillAnimationState existing = ACTIVE.get(message.getPlayerEntityId());
        if (existing != null && existing.getSequenceId() > message.getSequenceId()) {
            return;
        }

        ACTIVE.put(
                message.getPlayerEntityId(),
                new PillAnimationState(
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
                        message.getSequenceId()
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
                    && minecraft.gameSettings.thirdPersonView == 0) {
                applyCameraLock(minecraft.player, state, worldTime);
            }
        }
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
            PillItemDefinition definition = ItemPillBase.getDefinition(state.getItemId());
            if (definition == null) {
                continue;
            }

            Item item = Item.REGISTRY.getObject(new ResourceLocation(state.getItemId()));
            if (item == null) {
                continue;
            }

            ItemStack renderStack = new ItemStack(item);
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

        player.rotationYaw = approachAngle(player.rotationYaw, targetYaw, 12.0F);
        player.rotationPitch = MathHelper.clamp(approach(player.rotationPitch, targetPitch, 10.0F), -89.9F, 89.9F);
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
}
