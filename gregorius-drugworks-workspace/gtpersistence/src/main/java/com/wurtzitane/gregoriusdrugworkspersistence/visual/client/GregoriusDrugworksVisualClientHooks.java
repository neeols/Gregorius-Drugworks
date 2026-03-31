package com.wurtzitane.gregoriusdrugworkspersistence.visual.client;

import com.wurtzitane.gregoriusdrugworks.common.visual.VisualColorMode;
import com.wurtzitane.gregoriusdrugworks.common.visual.VisualEffectProfile;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketStartVisualEffect;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketStopVisualEffect;
import com.wurtzitane.gregoriusdrugworkspersistence.visual.GregoriusDrugworksVisualProfiles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.MobEffects;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.EntityViewRenderEvent.FOVModifier;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

public final class GregoriusDrugworksVisualClientHooks {

    private static final int RADIAL_SEGMENTS = 40;
    private static final int RING_SEGMENTS = 48;

    private static ActiveVisualEffect ACTIVE;
    private static boolean initialised = false;

    private static float smoothedYawOffset;
    private static float smoothedPitchOffset;
    private static float smoothedRollOffset;
    private static float smoothedScreenOffsetX;
    private static float smoothedScreenOffsetY;
    private static boolean forcedNightActive;
    private static long savedNaturalWorldTime = -1L;

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

    public static KirinoTripVisualState getLsdKirinoState(float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        if (ACTIVE == null || mc.world == null) {
            return null;
        }

        VisualEffectProfile profile = ACTIVE.getProfile();
        if (!isLsdProfile(profile)) {
            return null;
        }

        float progress = ACTIVE.progress(mc.world.getTotalWorldTime(), partialTicks);
        float envelope = computeEnvelope(progress);
        float intensity = clamp01(envelope * (0.55F + profile.getPrismSeparation() + (profile.getTunnelStrength() * 0.75F)));
        if (intensity <= 0.0001F) {
            return null;
        }

        return new KirinoTripVisualState(
                ACTIVE.age(mc.world.getTotalWorldTime(), partialTicks),
                progress,
                intensity,
                profile.getPrismSeparation(),
                profile.getTunnelStrength(),
                profile.getRibbonIntensity(),
                profile.getWobbleAmplitude()
        );
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null) {
            ACTIVE = null;
            hardResetSmoothing();
            clearNightOverrideState();
            return;
        }

        long worldTime = mc.world.getTotalWorldTime();
        if (ACTIVE != null && ACTIVE.isExpired(worldTime)) {
            ACTIVE = null;
        }

        updateNightOverride(mc);
        updateSmoothing(worldTime);

        if (ACTIVE != null) {
            spawnLocalParticles(mc, ACTIVE, worldTime);
        }
    }

    @SubscribeEvent
    public void onCameraSetup(CameraSetup event) {
        if (!hasResidualCameraOffsets()) {
            return;
        }

        event.setYaw(event.getYaw() + smoothedYawOffset);
        event.setPitch(event.getPitch() + smoothedPitchOffset);
        event.setRoll(event.getRoll() + smoothedRollOffset);
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
        float envelope = computeEnvelope(ACTIVE.progress(mc.world.getTotalWorldTime(), 1.0F));
        float wobble = (float) Math.sin(age * (0.025F + profile.getFovPulseSpeed())) * profile.getFovPulseAmount() * 9.0F * envelope;
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
        float progress = ACTIVE.progress(mc.world.getTotalWorldTime(), event.getPartialTicks());
        float envelope = computeEnvelope(progress);

        ScaledResolution res = new ScaledResolution(mc);
        float width = res.getScaledWidth();
        float height = res.getScaledHeight();
        float centerX = width * 0.5F + (smoothedScreenOffsetX * width * 0.08F);
        float centerY = height * 0.5F + (smoothedScreenOffsetY * height * 0.08F);
        int baseColor = computeTint(profile, age, envelope);
        float[] baseRgb = computeRgb(profile, age);

        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableTexture2D();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        drawFullscreenQuad(0.0F, 0.0F, width, height, baseColor);
        drawAfterimageEchoes(centerX, centerY, width, height, profile, age, envelope, baseRgb);
        drawTunnelRings(centerX, centerY, width, height, profile, age, envelope, baseRgb);
        drawVignette(centerX, centerY, width, height, profile, envelope, baseRgb);

        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        drawPrismGhosts(centerX, centerY, width, height, profile, age, envelope);
        drawRibbonBands(width, height, profile, age, envelope);
        drawScanlines(width, height, profile, age, envelope, baseRgb);
        if (isLsdProfile(profile)) {
            drawKaleidoscopeSpokes(centerX, centerY, width, height, profile, age, envelope);
            drawWaveVeil(width, height, profile, age, envelope);
        }
        if (isMethProfile(profile)) {
            drawRushLines(width, height, profile, age, envelope);
        }

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    private static void updateSmoothing(long worldTime) {
        if (ACTIVE == null) {
            smoothedYawOffset = approach(smoothedYawOffset, 0.0F, 0.18F);
            smoothedPitchOffset = approach(smoothedPitchOffset, 0.0F, 0.18F);
            smoothedRollOffset = approach(smoothedRollOffset, 0.0F, 0.18F);
            smoothedScreenOffsetX = approach(smoothedScreenOffsetX, 0.0F, 0.18F);
            smoothedScreenOffsetY = approach(smoothedScreenOffsetY, 0.0F, 0.18F);
            return;
        }

        VisualEffectProfile profile = ACTIVE.getProfile();
        float age = ACTIVE.age(worldTime, 1.0F);
        float envelope = computeEnvelope(ACTIVE.progress(worldTime, 1.0F));

        float yawWave = (float) (Math.sin(age * (0.045F + profile.getWobbleSpeed() * 0.45F))
                + (Math.sin(age * (0.023F + profile.getPulseSpeed() * 0.35F) + 1.2F) * 0.45F));
        float pitchWave = (float) (Math.cos(age * (0.041F + profile.getWobbleSpeed() * 0.42F) + 0.8F)
                + (Math.sin(age * (0.017F + profile.getFlashFrequency() * 0.50F) + 2.3F) * 0.35F));
        float rollWave = (float) (Math.sin(age * (0.032F + profile.getWobbleSpeed() * 0.36F) + 0.6F)
                + (Math.cos(age * (0.011F + profile.getPulseSpeed() * 0.28F)) * 0.50F));

        float targetYaw = yawWave * profile.getYawDrift() * 7.5F * envelope;
        float targetPitch = pitchWave * profile.getPitchDrift() * 6.0F * envelope;
        float targetRoll = rollWave * (profile.getRollDrift() * 10.0F + profile.getPrismSeparation() * 1.6F) * envelope;

        float targetScreenOffsetX = (float) Math.sin(age * (0.018F + profile.getWobbleSpeed() * 0.28F)) * profile.getWobbleAmplitude() * 4.0F * envelope;
        float targetScreenOffsetY = (float) Math.cos(age * (0.015F + profile.getWobbleSpeed() * 0.25F) + 0.7F) * profile.getWobbleAmplitude() * 3.0F * envelope;

        smoothedYawOffset = approach(smoothedYawOffset, targetYaw, 0.16F);
        smoothedPitchOffset = approach(smoothedPitchOffset, targetPitch, 0.16F);
        smoothedRollOffset = approach(smoothedRollOffset, targetRoll, 0.14F);
        smoothedScreenOffsetX = approach(smoothedScreenOffsetX, targetScreenOffsetX, 0.16F);
        smoothedScreenOffsetY = approach(smoothedScreenOffsetY, targetScreenOffsetY, 0.16F);
    }

    private static void drawPrismGhosts(
            float centerX,
            float centerY,
            float width,
            float height,
            VisualEffectProfile profile,
            float age,
            float envelope
    ) {
        if (profile.getPrismSeparation() <= 0.0F) {
            return;
        }

        float separation = profile.getPrismSeparation() * Math.min(width, height) * 0.20F;
        int ghostCount = 3 + Math.min(2, Math.max(0, profile.getRibbonDensity() / 3));

        for (int i = 0; i < ghostCount; i++) {
            float lane = ghostCount == 1 ? 0.0F : ((i / (float) (ghostCount - 1)) * 2.0F) - 1.0F;
            float orbit = age * 0.020F + (i * 0.95F);
            float offsetX = lane * separation * 0.85F + (float) Math.sin(orbit) * separation * 0.35F;
            float offsetY = (float) Math.cos(orbit * 0.8F) * separation * 0.18F;
            float[] rgb = profile.getColorMode() == VisualColorMode.RAINBOW
                    ? rainbowRgb(age * 0.60F + (i * 1.8F))
                    : shiftRgb(computeRgb(profile, age), 0.12F * i);

            int inner = colorArgb((int) (255.0F * envelope * (0.035F + profile.getPrismSeparation() * 0.14F)), rgb[0], rgb[1], rgb[2]);
            drawRadialGlow(centerX + offsetX, centerY + offsetY, width * 0.44F, height * 0.34F, inner, 0x00000000, RADIAL_SEGMENTS);
        }
    }

    private static void drawTunnelRings(
            float centerX,
            float centerY,
            float width,
            float height,
            VisualEffectProfile profile,
            float age,
            float envelope,
            float[] baseRgb
    ) {
        if (profile.getTunnelStrength() <= 0.0F) {
            return;
        }

        float maxRadius = (float) Math.hypot(width, height) * 0.58F;
        int ringCount = clampInt(2 + Math.round(profile.getTunnelStrength() * 7.0F), 2, 8);

        for (int i = 0; i < ringCount; i++) {
            float cycle = fract(age * (0.010F + profile.getPulseSpeed() * 0.030F) + (i * (1.0F / ringCount)));
            float radius = 34.0F + cycle * (maxRadius - 34.0F);
            float thickness = 8.0F + profile.getTunnelStrength() * 26.0F;
            float ringAlpha = envelope * (0.04F + profile.getTunnelStrength() * 0.14F) * (1.0F - cycle);
            float[] rgb = profile.getColorMode() == VisualColorMode.RAINBOW
                    ? rainbowRgb(age * 0.55F + (i * 0.75F))
                    : shiftRgb(baseRgb, i * 0.05F);

            int inner = colorArgb((int) (ringAlpha * 255.0F), rgb[0], rgb[1], rgb[2]);
            int outer = colorArgb(0, rgb[0], rgb[1], rgb[2]);
            drawRing(centerX, centerY, radius, thickness, inner, outer, RING_SEGMENTS);
        }
    }

    private static void drawRibbonBands(float width, float height, VisualEffectProfile profile, float age, float envelope) {
        if (profile.getRibbonIntensity() <= 0.0F || profile.getRibbonDensity() <= 0) {
            return;
        }

        int ribbonCount = clampInt(profile.getRibbonDensity(), 1, 10);
        for (int i = 0; i < ribbonCount; i++) {
            float yProgress = fract(age * (0.004F + profile.getPulseSpeed() * 0.010F) + (i * 0.173F));
            float bandHeight = height * (0.05F + (profile.getRibbonIntensity() * 0.05F) + ((i % 3) * 0.012F));
            float y = yProgress * (height + (bandHeight * 2.0F)) - bandHeight;
            float skew = width * (0.08F + profile.getRibbonIntensity() * 0.12F) * (float) Math.sin(age * 0.022F + i);
            float alpha = envelope * (0.02F + profile.getRibbonIntensity() * 0.09F) * (1.0F - Math.abs(0.5F - yProgress) * 1.35F);
            float[] rgb = profile.getColorMode() == VisualColorMode.RAINBOW
                    ? rainbowRgb(age * 0.80F + (i * 0.90F))
                    : shiftRgb(computeRgb(profile, age), i * 0.04F);

            int color = colorArgb((int) (clamp01(alpha) * 255.0F), rgb[0], rgb[1], rgb[2]);
            drawQuad(
                    -skew,
                    y,
                    width + skew * 0.25F,
                    y,
                    width + skew,
                    y + bandHeight,
                    skew * 0.25F,
                    y + bandHeight,
                    color,
                    color,
                    color,
                    color
            );
        }
    }

    private static void drawScanlines(
            float width,
            float height,
            VisualEffectProfile profile,
            float age,
            float envelope,
            float[] baseRgb
    ) {
        if (profile.getScanlineStrength() <= 0.0F) {
            return;
        }

        int step = clampInt(12 - Math.round(profile.getScanlineStrength() * 32.0F), 2, 12);
        float travel = fract(age * (0.012F + profile.getFlashFrequency() * 0.04F));
        float sweepY = travel * height;

        for (int y = 0; y < height; y += step) {
            float linePhase = fract((y / Math.max(1.0F, height)) + (age * 0.003F));
            float alpha = envelope * (0.01F + profile.getScanlineStrength() * 0.06F) * (0.55F + 0.45F * (float) Math.sin(linePhase * Math.PI * 2.0F));
            int color = colorArgb((int) (clamp01(alpha) * 255.0F), baseRgb[0], baseRgb[1], baseRgb[2]);
            drawFullscreenQuad(0.0F, y, width, y + 1.0F, color);
        }

        int sweepColor = colorArgb((int) (envelope * profile.getScanlineStrength() * 62.0F), baseRgb[0], baseRgb[1], baseRgb[2]);
        drawFullscreenQuad(0.0F, sweepY - 2.0F, width, sweepY + 2.0F, sweepColor);
    }

    private static void drawAfterimageEchoes(
            float centerX,
            float centerY,
            float width,
            float height,
            VisualEffectProfile profile,
            float age,
            float envelope,
            float[] baseRgb
    ) {
        if (profile.getAfterimageStrength() <= 0.0F) {
            return;
        }

        int echoCount = clampInt(1 + Math.round(profile.getAfterimageStrength() * 10.0F), 1, 5);
        float reach = profile.getAfterimageStrength() * Math.min(width, height) * 0.10F;

        for (int i = 0; i < echoCount; i++) {
            float orbit = age * 0.018F + (i * 1.4F);
            float offsetX = (float) Math.sin(orbit) * reach * (0.6F + i * 0.18F);
            float offsetY = (float) Math.cos(orbit * 0.86F) * reach * (0.4F + i * 0.14F);
            float[] rgb = profile.getColorMode() == VisualColorMode.RAINBOW
                    ? rainbowRgb(age * 0.45F + i)
                    : shiftRgb(baseRgb, i * 0.03F);
            int inner = colorArgb((int) (255.0F * envelope * (0.02F + profile.getAfterimageStrength() * 0.10F) * (1.0F - (i / (float) echoCount))), rgb[0], rgb[1], rgb[2]);
            drawRadialGlow(centerX + offsetX, centerY + offsetY, width * 0.34F, height * 0.26F, inner, 0x00000000, RADIAL_SEGMENTS);
        }
    }

    private static void drawKaleidoscopeSpokes(
            float centerX,
            float centerY,
            float width,
            float height,
            VisualEffectProfile profile,
            float age,
            float envelope
    ) {
        int spokes = clampInt(10 + profile.getRibbonDensity(), 10, 24);
        float innerRadius = 26.0F + profile.getTunnelStrength() * 84.0F;
        float outerRadius = Math.max(width, height) * 0.82F;

        for (int i = 0; i < spokes; i++) {
            float baseAngle = ((float) (Math.PI * 2.0D * i) / spokes) + (age * 0.010F);
            float spread = 0.070F + profile.getPrismSeparation() * 0.11F
                    + ((float) Math.sin(age * 0.032F + i) * 0.018F);
            float[] rgb = rainbowRgb(age * 0.45F + (i * 0.85F));
            int innerColor = colorArgb(
                    (int) (255.0F * envelope * (0.035F + profile.getPrismSeparation() * 0.10F)),
                    rgb[0], rgb[1], rgb[2]
            );
            int outerColor = colorArgb(0, rgb[0], rgb[1], rgb[2]);

            drawQuad(
                    centerX + ((float) Math.cos(baseAngle - spread) * innerRadius),
                    centerY + ((float) Math.sin(baseAngle - spread) * innerRadius),
                    centerX + ((float) Math.cos(baseAngle + spread) * innerRadius),
                    centerY + ((float) Math.sin(baseAngle + spread) * innerRadius),
                    centerX + ((float) Math.cos(baseAngle + spread * 1.7F) * outerRadius),
                    centerY + ((float) Math.sin(baseAngle + spread * 1.7F) * outerRadius),
                    centerX + ((float) Math.cos(baseAngle - spread * 1.7F) * outerRadius),
                    centerY + ((float) Math.sin(baseAngle - spread * 1.7F) * outerRadius),
                    innerColor,
                    innerColor,
                    outerColor,
                    outerColor
            );
        }
    }

    private static void drawWaveVeil(float width, float height, VisualEffectProfile profile, float age, float envelope) {
        int bands = clampInt(4 + profile.getRibbonDensity(), 6, 14);
        float amplitude = width * (0.020F + profile.getWobbleAmplitude() * 0.32F);
        float bandHeight = 10.0F + profile.getRibbonIntensity() * 38.0F;

        for (int i = 0; i < bands; i++) {
            float lane = (bands == 1) ? 0.5F : i / (float) (bands - 1);
            float y = (lane * height) + ((float) Math.sin(age * 0.022F + i * 0.9F) * height * 0.05F);
            float leftShift = (float) Math.sin(age * 0.030F + i) * amplitude;
            float rightShift = (float) Math.cos(age * 0.026F + (i * 1.3F)) * amplitude;
            float[] rgb = rainbowRgb(age * 0.30F + (i * 0.55F));
            int color = colorArgb(
                    (int) (255.0F * envelope * (0.018F + profile.getRibbonIntensity() * 0.08F)),
                    rgb[0], rgb[1], rgb[2]
            );

            drawQuad(
                    -bandHeight + leftShift,
                    y - bandHeight,
                    width + rightShift,
                    y - bandHeight * 0.35F,
                    width - leftShift,
                    y + bandHeight,
                    bandHeight - rightShift,
                    y + bandHeight * 0.35F,
                    color,
                    color,
                    color,
                    color
            );
        }
    }

    private static void drawRushLines(float width, float height, VisualEffectProfile profile, float age, float envelope) {
        int lanes = clampInt(8 + profile.getRibbonDensity(), 10, 20);
        float[] rgb = computeRgb(profile, age);
        for (int i = 0; i < lanes; i++) {
            float lane = i / (float) Math.max(1, lanes - 1);
            float travel = fract((age * (0.030F + profile.getPulseSpeed() * 0.16F)) + lane);
            float lineWidth = 2.0F + profile.getScanlineStrength() * 8.0F;
            float length = height * (0.08F + profile.getRibbonIntensity() * 0.16F);
            float x = lane * width + ((float) Math.sin(age * 0.015F + i) * width * 0.04F);
            float y = (travel * (height + length)) - length;
            int color = colorArgb(
                    (int) (255.0F * envelope * (0.025F + profile.getScanlineStrength() * 0.10F)),
                    rgb[0], rgb[1], rgb[2]
            );

            drawQuad(
                    x - lineWidth,
                    y,
                    x + lineWidth,
                    y,
                    x + (lineWidth * 2.4F),
                    y + length,
                    x - (lineWidth * 2.4F),
                    y + length,
                    color,
                    color,
                    0x00000000,
                    0x00000000
            );
        }
    }

    private static void drawVignette(
            float centerX,
            float centerY,
            float width,
            float height,
            VisualEffectProfile profile,
            float envelope,
            float[] baseRgb
    ) {
        if (profile.getVignetteStrength() <= 0.0F) {
            return;
        }

        int outerColor = colorArgb(
                (int) (255.0F * envelope * (0.10F + profile.getVignetteStrength() * 0.28F)),
                baseRgb[0] * 0.28F,
                baseRgb[1] * 0.22F,
                baseRgb[2] * 0.30F
        );
        drawRadialGlow(centerX, centerY, width * 0.82F, height * 0.82F, 0x00000000, outerColor, RADIAL_SEGMENTS);
    }

    private static void spawnLocalParticles(Minecraft mc, ActiveVisualEffect active, long worldTime) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        VisualEffectProfile profile = active.getProfile();
        if (profile.getParticleDensity() <= 0) {
            return;
        }

        long cadence = profile.getParticleDensity() >= 5 ? 1L : 2L;
        if ((worldTime % cadence) != 0L) {
            return;
        }

        EnumParticleTypes particleType = chooseLocalParticle(profile, worldTime);
        float age = active.age(worldTime, 1.0F);
        float[] rgb = computeRgb(profile, age);
        int bursts = Math.min(8, profile.getParticleDensity() + Math.max(0, profile.getRibbonDensity() / 2));
        double orbitScale = 0.18D + profile.getTunnelStrength() * 0.35D;

        for (int i = 0; i < bursts; i++) {
            double angle = age * 0.08D + (i * (Math.PI * 0.65D));
            double ox = Math.sin(angle) * orbitScale + ((mc.world.rand.nextDouble() - 0.5D) * 0.35D);
            double oy = (mc.world.rand.nextDouble() - 0.5D) * 0.45D;
            double oz = Math.cos(angle) * orbitScale + ((mc.world.rand.nextDouble() - 0.5D) * 0.35D);

            if (particleType == EnumParticleTypes.SPELL_MOB_AMBIENT) {
                mc.world.spawnParticle(
                        particleType,
                        mc.player.posX + ox,
                        mc.player.posY + mc.player.getEyeHeight() - 0.08D + oy,
                        mc.player.posZ + oz,
                        rgb[0],
                        rgb[1],
                        rgb[2]
                );
                continue;
            }

            mc.world.spawnParticle(
                    particleType,
                    mc.player.posX + ox,
                    mc.player.posY + mc.player.getEyeHeight() - 0.08D + oy,
                    mc.player.posZ + oz,
                    ox * 0.04D,
                    0.008D + profile.getPulseAmplitude() * 0.02D,
                    oz * 0.04D
            );
        }
    }

    private static EnumParticleTypes chooseLocalParticle(VisualEffectProfile profile, long worldTime) {
        if (isLsdProfile(profile)) {
            return EnumParticleTypes.SPELL_MOB_AMBIENT;
        }
        if (profile.getTunnelStrength() > 0.60F) {
            return (worldTime & 1L) == 0L ? EnumParticleTypes.PORTAL : EnumParticleTypes.END_ROD;
        }
        if (profile.getAfterimageStrength() > 0.16F) {
            return EnumParticleTypes.END_ROD;
        }
        if (profile.getColorMode() == VisualColorMode.RAINBOW) {
            return EnumParticleTypes.SPELL_MOB_AMBIENT;
        }
        return EnumParticleTypes.SPELL_MOB_AMBIENT;
    }

    private static int computeTint(VisualEffectProfile profile, float age, float envelope) {
        int base = profile.getTintArgb();
        int alpha = (base >>> 24) & 0xFF;
        float[] rgb = computeRgb(profile, age);
        float pulse = (float) ((Math.sin(age * profile.getPulseSpeed()) + 1.0D) * 0.5D);
        float flash = (float) ((Math.sin(age * (0.5F + profile.getFlashFrequency())) + 1.0D) * 0.5D);
        float intensity = envelope * (0.25F + (pulse * profile.getPulseAmplitude()) + (flash * profile.getFlashIntensity()));

        return colorArgb(
                clampInt((int) (alpha * intensity), 0, 255),
                rgb[0],
                rgb[1],
                rgb[2]
        );
    }

    private static float[] computeRgb(VisualEffectProfile profile, float age) {
        int base = profile.getTintArgb();
        float[] rgb = new float[] {
                ((base >>> 16) & 0xFF) / 255.0F,
                ((base >>> 8) & 0xFF) / 255.0F,
                (base & 0xFF) / 255.0F
        };

        if (profile.getColorMode() == VisualColorMode.RAINBOW) {
            return rainbowRgb(age * rainbowPhaseSpeed(profile));
        }

        if (profile.getColorMode() == VisualColorMode.PULSE) {
            float mix = 0.20F + (float) ((Math.sin(age * (0.06F + profile.getPulseSpeed() * 0.50F)) + 1.0D) * 0.15D);
            return new float[] {
                    clamp01(rgb[0] + ((1.0F - rgb[0]) * mix)),
                    clamp01(rgb[1] + ((1.0F - rgb[1]) * (mix * 0.75F))),
                    clamp01(rgb[2] + ((1.0F - rgb[2]) * (mix * 0.55F)))
            };
        }

        return rgb;
    }

    private static float rainbowPhaseSpeed(VisualEffectProfile profile) {
        return (0.0125F + (profile.getPulseSpeed() * 0.12F)) * rainbowAccelerationMultiplier();
    }

    private static float rainbowAccelerationMultiplier() {
        Minecraft mc = Minecraft.getMinecraft();
        if (ACTIVE == null || mc.world == null) {
            return 1.0F;
        }

        float progress = ACTIVE.progress(mc.world.getTotalWorldTime(), 1.0F);
        return 1.0F + (progress * 2.25F);
    }

    private static void updateNightOverride(Minecraft mc) {
        if (mc.world == null || mc.player == null) {
            clearNightOverrideState();
            return;
        }

        boolean shouldForceNight = shouldForceSalvinorinNight(mc);
        if (!shouldForceNight) {
            if (forcedNightActive && savedNaturalWorldTime >= 0L) {
                mc.world.setWorldTime(savedNaturalWorldTime);
            }
            forcedNightActive = false;
            savedNaturalWorldTime = mc.world.getWorldTime();
            return;
        }

        if (!forcedNightActive) {
            savedNaturalWorldTime = mc.world.getWorldTime();
            forcedNightActive = true;
        } else if (savedNaturalWorldTime >= 0L) {
            savedNaturalWorldTime++;
        }

        mc.world.setWorldTime(18000L);
    }

    private static boolean shouldForceSalvinorinNight(Minecraft mc) {
        return ACTIVE != null
                && mc.player != null
                && mc.player.isPotionActive(MobEffects.BLINDNESS)
                && ACTIVE.getProfile().getId().startsWith("gregoriusdrugworkspersistence:salvinorin_a_");
    }

    private static boolean isLsdProfile(VisualEffectProfile profile) {
        return profile.getId().startsWith("gregoriusdrugworkspersistence:lsd_");
    }

    private static boolean isMethProfile(VisualEffectProfile profile) {
        return profile.getId().startsWith("gregoriusdrugworkspersistence:methamphetamine_");
    }

    private static void clearNightOverrideState() {
        forcedNightActive = false;
        savedNaturalWorldTime = -1L;
    }

    private static float[] rainbowRgb(float phase) {
        float r = (float) ((Math.sin(phase) + 1.0D) * 0.5D);
        float g = (float) ((Math.sin(phase + 2.094D) + 1.0D) * 0.5D);
        float b = (float) ((Math.sin(phase + 4.188D) + 1.0D) * 0.5D);
        return new float[] { r, g, b };
    }

    private static float[] shiftRgb(float[] rgb, float amount) {
        return new float[] {
                clamp01(rgb[0] + amount),
                clamp01(rgb[1] + (amount * 0.7F)),
                clamp01(rgb[2] + (amount * 0.9F))
        };
    }

    private static float computeEnvelope(float progress) {
        float fadeIn = smoothStep(clamp01(progress * 3.5F));
        float fadeOut = smoothStep(clamp01((1.0F - progress) * 4.0F));
        return fadeIn * fadeOut;
    }

    private static float smoothStep(float value) {
        return value * value * (3.0F - (2.0F * value));
    }

    private static boolean hasResidualCameraOffsets() {
        return Math.abs(smoothedYawOffset) > 0.001F
                || Math.abs(smoothedPitchOffset) > 0.001F
                || Math.abs(smoothedRollOffset) > 0.001F;
    }

    private static void hardResetSmoothing() {
        smoothedYawOffset = 0.0F;
        smoothedPitchOffset = 0.0F;
        smoothedRollOffset = 0.0F;
        smoothedScreenOffsetX = 0.0F;
        smoothedScreenOffsetY = 0.0F;
    }

    private static float approach(float current, float target, float factor) {
        return current + ((target - current) * factor);
    }

    private static float fract(float value) {
        return value - (float) Math.floor(value);
    }

    private static float clamp01(float value) {
        if (value < 0.0F) {
            return 0.0F;
        }
        if (value > 1.0F) {
            return 1.0F;
        }
        return value;
    }

    private static int clampInt(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    private static int colorArgb(int alpha, float red, float green, float blue) {
        return ((alpha & 0xFF) << 24)
                | ((int) (clamp01(red) * 255.0F) << 16)
                | ((int) (clamp01(green) * 255.0F) << 8)
                | (int) (clamp01(blue) * 255.0F);
    }

    private static void drawFullscreenQuad(float left, float top, float right, float bottom, int color) {
        drawQuad(left, top, right, top, right, bottom, left, bottom, color, color, color, color);
    }

    private static void drawQuad(
            float x1,
            float y1,
            float x2,
            float y2,
            float x3,
            float y3,
            float x4,
            float y4,
            int c1,
            int c2,
            int c3,
            int c4
    ) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        putVertex(buffer, x1, y1, c1);
        putVertex(buffer, x2, y2, c2);
        putVertex(buffer, x3, y3, c3);
        putVertex(buffer, x4, y4, c4);
        tessellator.draw();
    }

    private static void drawRadialGlow(float centerX, float centerY, float radiusX, float radiusY, int innerColor, int outerColor, int segments) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        putVertex(buffer, centerX, centerY, innerColor);

        for (int i = 0; i <= segments; i++) {
            double angle = (Math.PI * 2.0D * i) / segments;
            float x = centerX + (float) Math.cos(angle) * radiusX;
            float y = centerY + (float) Math.sin(angle) * radiusY;
            putVertex(buffer, x, y, outerColor);
        }

        tessellator.draw();
    }

    private static void drawRing(float centerX, float centerY, float radius, float thickness, int innerColor, int outerColor, int segments) {
        float innerRadius = Math.max(0.0F, radius - thickness);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);

        for (int i = 0; i <= segments; i++) {
            double angle = (Math.PI * 2.0D * i) / segments;
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);
            putVertex(buffer, centerX + cos * innerRadius, centerY + sin * innerRadius, innerColor);
            putVertex(buffer, centerX + cos * radius, centerY + sin * radius, outerColor);
        }

        tessellator.draw();
    }

    private static void putVertex(BufferBuilder buffer, float x, float y, int color) {
        buffer.pos(x, y, 0.0D)
                .color(
                        (color >>> 16) & 0xFF,
                        (color >>> 8) & 0xFF,
                        color & 0xFF,
                        (color >>> 24) & 0xFF
                )
                .endVertex();
    }
}
