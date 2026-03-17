package com.wurtzitane.gregoriusdrugworkspersistence.medical.client;

import com.wurtzitane.gregoriusdrugworks.common.medical.ApplicatorUseProfile;
import com.wurtzitane.gregoriusdrugworkspersistence.medical.GregoriusDrugworksMedicalApplicators;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketCancelApplicatorAnimation;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketStartApplicatorAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class GregoriusDrugworksApplicatorClientHooks {

    private static final Map<Integer, ApplicatorApplicationState> ACTIVE = new HashMap<>();
    private static final ModelHyposprayApplicator MODEL = new ModelHyposprayApplicator();
    private static boolean initialised = false;

    private GregoriusDrugworksApplicatorClientHooks() {
    }

    public static void preInit() {
        if (initialised) {
            return;
        }
        initialised = true;

        GregoriusDrugworksMedicalApplicators.MEDICAL_APPLICATOR.setTileEntityItemStackRenderer(ApplicatorItemStackRenderer.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new GregoriusDrugworksApplicatorClientHooks());
    }

    public static void start(PacketStartApplicatorAnimation message) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world == null) {
            return;
        }

        ApplicatorApplicationState existing = ACTIVE.get(message.getPlayerEntityId());
        if (existing != null && existing.getSequenceId() > message.getSequenceId()) {
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
            ACTIVE.remove(message.getPlayerEntityId());
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
            Vec3d pos = getApplicatorPosition(player, state, worldTime, partialTicks);

            GlStateManager.pushMatrix();
            GlStateManager.translate(
                    pos.x - renderManager.viewerPosX,
                    pos.y - renderManager.viewerPosY,
                    pos.z - renderManager.viewerPosZ
            );

            GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(0.36F, 0.36F, 0.36F);

            minecraft.getTextureManager().bindTexture(
                    new net.minecraft.util.ResourceLocation("gregoriusdrugworkspersistence", "textures/item/medical_applicator_loaded.png")
            );
            MODEL.render(0.0625F);

            GlStateManager.popMatrix();
        }
    }

    private static Vec3d getApplicatorPosition(EntityPlayer player, ApplicatorApplicationState state, long worldTime, float partialTicks) {
        Vec3d eye = player.getPositionEyes(partialTicks);
        Vec3d look = player.getLook(partialTicks).normalize();
        Vec3d right = look.crossProduct(new Vec3d(0.0D, 1.0D, 0.0D)).normalize();

        double handSign = state.getHand() == EnumHand.MAIN_HAND ? 1.0D : -1.0D;
        float progress = state.progress(worldTime, partialTicks);

        double towardArm = 0.10D + progress * 0.10D;
        return eye
                .add(right.scale(0.22D * handSign))
                .add(0.0D, -0.28D, 0.0D)
                .add(look.scale(towardArm));
    }

    private static void applyLocalCameraPolish(EntityPlayerSP player, ApplicatorApplicationState state, long worldTime) {
        float progress = state.progress(worldTime, 1.0F);
        float targetPitch = -8.0F + progress * 3.0F;
        player.rotationPitch = MathHelper.clamp(player.rotationPitch + (targetPitch - player.rotationPitch) * 0.18F, -89.9F, 89.9F);
        player.prevRotationPitch = player.rotationPitch;
    }
}