package com.wurtzitane.gregoriusdrugworkspersistence.medical.client;

import com.wurtzitane.gregoriusdrugworks.common.medical.ApplicatorUseProfile;
import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.medical.GregoriusDrugworksApplicatorPayloads;
import com.wurtzitane.gregoriusdrugworkspersistence.medical.GregoriusDrugworksMedicalApplicators;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketCancelApplicatorAnimation;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketStartApplicatorAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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

public final class GregoriusDrugworksApplicatorClientHooks {

    private static final Map<Integer, ApplicatorApplicationState> ACTIVE = new HashMap<>();
    private static boolean initialised = false;

    private GregoriusDrugworksApplicatorClientHooks() {
    }

    public static void preInit() {
        if (initialised) {
            return;
        }
        initialised = true;

        GregoriusDrugworksMedicalApplicators.MEDICAL_APPLICATOR.addPropertyOverride(
                new ResourceLocation(Tags.MOD_ID, "loaded"),
                (stack, world, entity) -> GregoriusDrugworksApplicatorPayloads.hasPayload(stack) ? 1.0F : 0.0F
        );

        MinecraftForge.EVENT_BUS.register(new GregoriusDrugworksApplicatorClientHooks());
    }

    public static boolean hasActiveSequence(int entityId) {
        return ACTIVE.containsKey(entityId);
    }

    public static void start(PacketStartApplicatorAnimation message) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world == null) {
            return;
        }

        ApplicatorApplicationState existing = ACTIVE.get(message.getPlayerEntityId());
        if (existing != null && existing.getSequenceId() >= message.getSequenceId()) {
            return;
        }

        ApplicatorUseProfile profile = new ApplicatorUseProfile(
                message.getTotalUseTicks(),
                message.getRaiseEndTick(),
                message.getApplyStartTick(),
                message.getApplyEndTick(),
                message.getHoldEndTick(),
                message.getFinishTick()
        );

        ACTIVE.put(
                message.getPlayerEntityId(),
                new ApplicatorApplicationState(
                        message.getPlayerEntityId(),
                        message.getHand(),
                        minecraft.world.getTotalWorldTime(),
                        profile,
                        message.getSequenceId(),
                        message.isLocalCameraPolish()
                )
        );
    }

    public static void cancel(PacketCancelApplicatorAnimation message) {
        ApplicatorApplicationState state = ACTIVE.get(message.getPlayerEntityId());
        if (state != null && state.getSequenceId() <= message.getSequenceId()) {
            if (message.isCompleted()) {
                Minecraft minecraft = Minecraft.getMinecraft();
                if (minecraft.world != null) {
                    state.markCompleted(minecraft.world.getTotalWorldTime());
                }
            } else {
                ACTIVE.remove(message.getPlayerEntityId());
            }
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

        Iterator<Map.Entry<Integer, ApplicatorApplicationState>> iterator = ACTIVE.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, ApplicatorApplicationState> entry = iterator.next();
            ApplicatorApplicationState state = entry.getValue();

            Entity entity = minecraft.world.getEntityByID(state.getPlayerEntityId());
            if (!(entity instanceof EntityPlayer) || state.isExpired(worldTime)) {
                iterator.remove();
                continue;
            }

            if (entity == minecraft.player && state.isLocalCameraPolish() && minecraft.gameSettings.thirdPersonView == 0) {
                applyLocalCameraPolish((EntityPlayerSP) entity, state, worldTime);
            }
        }
    }

    @SubscribeEvent
    public void onRenderSpecificHand(RenderSpecificHandEvent event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world == null || minecraft.player == null || minecraft.gameSettings.thirdPersonView != 0) {
            return;
        }

        ApplicatorApplicationState state = ACTIVE.get(minecraft.player.getEntityId());
        if (state == null || state.getHand() != event.getHand()) {
            return;
        }

        ItemStack renderStack = !event.getItemStack().isEmpty()
                ? event.getItemStack()
                : new ItemStack(GregoriusDrugworksMedicalApplicators.MEDICAL_APPLICATOR);

        event.setCanceled(true);
        renderFirstPersonApplicator(minecraft, renderStack, state, event.getHand(), event.getPartialTicks());
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

        for (ApplicatorApplicationState state : ACTIVE.values()) {
            Entity entity = minecraft.world.getEntityByID(state.getPlayerEntityId());
            if (!(entity instanceof EntityPlayer)) {
                continue;
            }

            EntityPlayer player = (EntityPlayer) entity;
            if (isLocalFirstPersonPlayer(minecraft, player)) {
                continue;
            }

            ItemStack renderStack = player.getHeldItem(state.getHand());
            if (renderStack.isEmpty() || renderStack.getItem() != GregoriusDrugworksMedicalApplicators.MEDICAL_APPLICATOR) {
                renderStack = new ItemStack(GregoriusDrugworksMedicalApplicators.MEDICAL_APPLICATOR);
            }

            Vec3d pos = getApplicatorPosition(player, state, worldTime, partialTicks);

            GlStateManager.pushMatrix();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.translate(
                    pos.x - renderManager.viewerPosX,
                    pos.y - renderManager.viewerPosY,
                    pos.z - renderManager.viewerPosZ
            );

            float handSign = state.getHand() == EnumHand.MAIN_HAND ? 1.0F : -1.0F;
            float yaw = interpolateAngle(player.prevRotationYawHead, player.rotationYawHead, partialTicks);
            float pitch = interpolate(player.prevRotationPitch, player.rotationPitch, partialTicks);
            float injectionRoll = -88.0F * handSign;
            float injectionYaw = 6.0F * handSign;
            float injectionPitch = 84.0F + pitch * 0.08F;

            GlStateManager.rotate(-yaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(injectionYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(injectionPitch, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(injectionRoll, 0.0F, 0.0F, 1.0F);
            GlStateManager.scale(0.92F, 0.92F, 0.92F);

            minecraft.getRenderItem().renderItem(renderStack, ItemCameraTransforms.TransformType.FIXED);

            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }

    private static Vec3d getApplicatorPosition(EntityPlayer player, ApplicatorApplicationState state, long worldTime, float partialTicks) {
        Vec3d eye = player.getPositionEyes(partialTicks);
        Vec3d look = player.getLook(partialTicks).normalize();
        Vec3d right = look.crossProduct(new Vec3d(0.0D, 1.0D, 0.0D));
        if (right.lengthSquared() < 1.0E-4D) {
            right = new Vec3d(1.0D, 0.0D, 0.0D);
        } else {
            right = right.normalize();
        }

        double handSign = state.getHand() == EnumHand.MAIN_HAND ? 1.0D : -1.0D;
        float raiseProgress = easeOut(state.getSegmentProgress(worldTime, partialTicks, 0, state.getProfile().getRaiseEndTick()));
        float injectProgress = easeInOut(state.getSegmentProgress(
                worldTime,
                partialTicks,
                state.getProfile().getApplyStartTick(),
                state.getProfile().getApplyEndTick()
        ));
        float withdrawProgress = easeInOut(state.getSegmentProgress(
                worldTime,
                partialTicks,
                state.getProfile().getHoldEndTick(),
                state.getProfile().getFinishTick()
        ));
        float lingerProgress = state.getCompletionLingerProgress(worldTime, partialTicks);
        float settleProgress = Math.max(withdrawProgress, lingerProgress);

        Vec3d torso = eye.add(0.0D, -0.48D, 0.0D);
        double side = lerp(0.12D * handSign, 0.02D * handSign, raiseProgress) - injectProgress * 0.01D * handSign;
        double forward = lerp(0.06D, 0.14D, raiseProgress) - injectProgress * 0.06D + settleProgress * 0.08D;
        double vertical = lerp(-0.06D, -0.14D, raiseProgress) - injectProgress * 0.05D + settleProgress * 0.03D;

        return torso
                .add(right.scale(side))
                .add(look.scale(forward))
                .add(0.0D, vertical, 0.0D);
    }

    private static void renderFirstPersonApplicator(Minecraft minecraft, ItemStack renderStack, ApplicatorApplicationState state, EnumHand hand, float partialTicks) {
        long worldTime = minecraft.world.getTotalWorldTime();
        float raiseProgress = easeOut(state.getSegmentProgress(worldTime, partialTicks, 0, state.getProfile().getRaiseEndTick()));
        float injectProgress = easeInOut(state.getSegmentProgress(
                worldTime,
                partialTicks,
                state.getProfile().getApplyStartTick(),
                state.getProfile().getApplyEndTick()
        ));
        float settleProgress = Math.max(
                easeInOut(state.getSegmentProgress(worldTime, partialTicks, state.getProfile().getHoldEndTick(), state.getProfile().getFinishTick())),
                state.getCompletionLingerProgress(worldTime, partialTicks)
        );
        float handSign = hand == EnumHand.MAIN_HAND ? 1.0F : -1.0F;

        float x = 0.44F * handSign - 0.38F * handSign * raiseProgress + 0.10F * handSign * settleProgress;
        float y = -0.46F + 0.12F * raiseProgress - 0.10F * injectProgress + 0.04F * settleProgress;
        float z = -0.98F + 0.18F * raiseProgress - 0.24F * injectProgress + 0.10F * settleProgress;

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableStandardItemLighting();

        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(6.0F * handSign, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(84.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-88.0F * handSign, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(0.82F, 0.82F, 0.82F);

        minecraft.getRenderItem().renderItem(renderStack, ItemCameraTransforms.TransformType.FIXED);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    private static void applyLocalCameraPolish(EntityPlayerSP player, ApplicatorApplicationState state, long worldTime) {
        float raiseProgress = easeOut(state.getSegmentProgress(worldTime, 1.0F, 0, state.getProfile().getRaiseEndTick()));
        float injectProgress = easeInOut(state.getSegmentProgress(
                worldTime,
                1.0F,
                state.getProfile().getApplyStartTick(),
                state.getProfile().getApplyEndTick()
        ));
        float settleProgress = Math.max(
                easeInOut(state.getSegmentProgress(worldTime, 1.0F, state.getProfile().getHoldEndTick(), state.getProfile().getFinishTick())),
                state.getCompletionLingerProgress(worldTime, 1.0F)
        );
        float targetPitch = -6.0F - raiseProgress * 5.0F - injectProgress * 6.0F + settleProgress * 3.0F;
        float targetYaw = player.rotationYaw + (state.getHand() == EnumHand.MAIN_HAND ? -4.0F : 4.0F) * raiseProgress;

        player.rotationYaw = approachAngle(player.rotationYaw, targetYaw, 2.0F);
        player.prevRotationYaw = player.rotationYaw;
        player.rotationPitch = MathHelper.clamp(player.rotationPitch + (targetPitch - player.rotationPitch) * 0.16F, -89.9F, 89.9F);
        player.prevRotationPitch = player.rotationPitch;
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
}
