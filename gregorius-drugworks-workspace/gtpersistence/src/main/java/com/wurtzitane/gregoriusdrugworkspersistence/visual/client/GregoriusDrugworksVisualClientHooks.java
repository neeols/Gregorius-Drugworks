package com.wurtzitane.gregoriusdrugworkspersistence.visual.client;

import com.wurtzitane.gregoriusdrugworks.common.visual.VisualColorMode;
import com.wurtzitane.gregoriusdrugworks.common.visual.VisualEffectProfile;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketStartVisualEffect;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketStopVisualEffect;
import com.wurtzitane.gregoriusdrugworkspersistence.visual.GregoriusDrugworksVisualProfiles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.FOVModifier;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nullable;

public final class GregoriusDrugworksVisualClientHooks {

    private static ActiveVisualEffect ACTIVE;
    private static boolean initialised = false;

    private GregoriusDrugworksVisualClientHooks() {
    }

    public static void preInit() {
        if (initialised) {
            return;
        }
        initialised = true;
        MinecraftForge.EVENT_BUS.register(new GregoriusDrugworksVisualClientHooks());
    }

    public static void start(PacketStartVisualEffect message) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) {
            return;
        }

        VisualEffectProfile profile = GregoriusDrugworksVisualProfiles.get(message.getProfileId());
        if (profile == null) {
            return;
        }

        if (ACTIVE != null && ACTIVE.getSequenceId() > message.getSequenceId()) {
            return;
        }

        ACTIVE = new ActiveVisualEffect(profile, mc.world.getTotalWorldTime(), message.getDurationTicks(), message.getSequenceId());

        if (profile.getStartSoundId() != null && !profile.getStartSoundId().isEmpty() && mc.player != null) {
            SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(new ResourceLocation(profile.getStartSoundId()));
            if (soundEvent != null) {
                mc.player.playSound(soundEvent, 1.0F, 1.0F);
            }
        }
    }

    public static void stop(PacketStopVisualEffect message) {
        if (ACTIVE != null && ACTIVE.getSequenceId() <= message.getSequenceId()) {
            ACTIVE = null;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null) {
            ACTIVE = null;
            return;
        }

        if (ACTIVE == null) {
            return;
        }

        long worldTime = mc.world.getTotalWorldTime();
        if (ACTIVE.isExpired(worldTime)) {
            ACTIVE = null;
            return;
        }

        applyCameraDrift(mc.player, ACTIVE, worldTime);
        spawnLocalParticles(mc, ACTIVE, worldTime);
    }

    @SubscribeEvent
    public void onFovUpdate(FOVModifier event) {
        if (ACTIVE == null) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) {
            return;
        }

        VisualEffectProfile profile = ACTIVE.getProfile();
        float age = ACTIVE.age(mc.world.getTotalWorldTime(), 1.0F);
        float wobble = (float) Math.sin(age * profile.getFovPulseSpeed()) * profile.getFovPulseAmount();
        event.setFOV(event.getFOV() + wobble);
    }

    @SubscribeEvent
    public void onOverlay(RenderGameOverlayEvent.Post event) {
        if (ACTIVE == null || event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) {
            return;
        }

        VisualEffectProfile profile = ACTIVE.getProfile();
        float age = ACTIVE.age(mc.world.getTotalWorldTime(), event.getPartialTicks());
        int color = computeTint(profile, age);

        ScaledResolution res = new ScaledResolution(mc);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        Gui.drawRect(0, 0, res.getScaledWidth(), res.getScaledHeight(), color);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private static void applyCameraDrift(EntityPlayerSP player, ActiveVisualEffect active, long worldTime) {
        VisualEffectProfile profile = active.getProfile();
        float age = active.age(worldTime, 1.0F);

        float yaw = (float) Math.sin(age * profile.getWobbleSpeed()) * profile.getYawDrift();
        float pitch = (float) Math.cos(age * profile.getWobbleSpeed()) * profile.getPitchDrift();

        player.rotationYaw += yaw;
        player.rotationPitch += pitch;
        player.prevRotationYaw = player.rotationYaw;
        player.prevRotationPitch = player.rotationPitch;
    }

    private static void spawnLocalParticles(Minecraft mc, ActiveVisualEffect active, long worldTime) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        VisualEffectProfile profile = active.getProfile();
        if (profile.getParticleDensity() <= 0) {
            return;
        }

        if (worldTime % 2L != 0L) {
            return;
        }

        for (int i = 0; i < profile.getParticleDensity(); i++) {
            double ox = (mc.world.rand.nextDouble() - 0.5D) * 1.2D;
            double oy = mc.world.rand.nextDouble() * 0.8D;
            double oz = (mc.world.rand.nextDouble() - 0.5D) * 1.2D;

            float[] rgb = computeRgb(profile, active.age(worldTime, 1.0F));

            mc.world.spawnParticle(
                    EnumParticleTypes.SPELL_MOB_AMBIENT,
                    mc.player.posX + ox,
                    mc.player.posY + mc.player.getEyeHeight() + oy,
                    mc.player.posZ + oz,
                    rgb[0],
                    rgb[1],
                    rgb[2]
            );
        }
    }

    private static int computeTint(VisualEffectProfile profile, float age) {
        int base = profile.getTintArgb();
        int alpha = (base >>> 24) & 0xFF;
        int red = (base >>> 16) & 0xFF;
        int green = (base >>> 8) & 0xFF;
        int blue = base & 0xFF;

        if (profile.getColorMode() == VisualColorMode.RAINBOW) {
            float[] rgb = computeRgb(profile, age);
            red = (int) (rgb[0] * 255.0F);
            green = (int) (rgb[1] * 255.0F);
            blue = (int) (rgb[2] * 255.0F);
        }

        float pulse = (float) ((Math.sin(age * profile.getPulseSpeed()) + 1.0D) * 0.5D);
        float flash = (float) ((Math.sin(age * profile.getFlashFrequency()) + 1.0D) * 0.5D);

        alpha = Math.min(255, Math.max(0, (int) (alpha * (0.25F + pulse * profile.getPulseAmplitude() + flash * profile.getFlashIntensity()))));

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    private static float[] computeRgb(VisualEffectProfile profile, float age) {
        if (profile.getColorMode() != VisualColorMode.RAINBOW) {
            int base = profile.getTintArgb();
            return new float[] {
                    ((base >>> 16) & 0xFF) / 255.0F,
                    ((base >>> 8) & 0xFF) / 255.0F,
                    (base & 0xFF) / 255.0F
            };
        }

        float r = (float) ((Math.sin(age * 0.08F) + 1.0D) * 0.5D);
        float g = (float) ((Math.sin(age * 0.08F + 2.094D) + 1.0D) * 0.5D);
        float b = (float) ((Math.sin(age * 0.08F + 4.188D) + 1.0D) * 0.5D);
        return new float[] { r, g, b };
    }
}