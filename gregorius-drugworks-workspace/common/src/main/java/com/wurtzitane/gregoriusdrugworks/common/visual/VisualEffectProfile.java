package com.wurtzitane.gregoriusdrugworks.common.visual;

public final class VisualEffectProfile {

    private final String id;
    private final String debugName;
    private final VisualColorMode colorMode;
    private final int tintArgb;
    private final float pulseSpeed;
    private final float pulseAmplitude;
    private final float flashFrequency;
    private final float flashIntensity;
    private final float yawDrift;
    private final float pitchDrift;
    private final float wobbleSpeed;
    private final float wobbleAmplitude;
    private final float fovPulseAmount;
    private final float fovPulseSpeed;
    private final int particleDensity;
    private final boolean localOnly;
    private final String startSoundId;

    public VisualEffectProfile(
            String id,
            String debugName,
            VisualColorMode colorMode,
            int tintArgb,
            float pulseSpeed,
            float pulseAmplitude,
            float flashFrequency,
            float flashIntensity,
            float yawDrift,
            float pitchDrift,
            float wobbleSpeed,
            float wobbleAmplitude,
            float fovPulseAmount,
            float fovPulseSpeed,
            int particleDensity,
            boolean localOnly,
            String startSoundId
    ) {
        this.id = id;
        this.debugName = debugName;
        this.colorMode = colorMode;
        this.tintArgb = tintArgb;
        this.pulseSpeed = pulseSpeed;
        this.pulseAmplitude = pulseAmplitude;
        this.flashFrequency = flashFrequency;
        this.flashIntensity = flashIntensity;
        this.yawDrift = yawDrift;
        this.pitchDrift = pitchDrift;
        this.wobbleSpeed = wobbleSpeed;
        this.wobbleAmplitude = wobbleAmplitude;
        this.fovPulseAmount = fovPulseAmount;
        this.fovPulseSpeed = fovPulseSpeed;
        this.particleDensity = particleDensity;
        this.localOnly = localOnly;
        this.startSoundId = startSoundId;
    }

    public String getId() {
        return id;
    }

    public String getDebugName() {
        return debugName;
    }

    public VisualColorMode getColorMode() {
        return colorMode;
    }

    public int getTintArgb() {
        return tintArgb;
    }

    public float getPulseSpeed() {
        return pulseSpeed;
    }

    public float getPulseAmplitude() {
        return pulseAmplitude;
    }

    public float getFlashFrequency() {
        return flashFrequency;
    }

    public float getFlashIntensity() {
        return flashIntensity;
    }

    public float getYawDrift() {
        return yawDrift;
    }

    public float getPitchDrift() {
        return pitchDrift;
    }

    public float getWobbleSpeed() {
        return wobbleSpeed;
    }

    public float getWobbleAmplitude() {
        return wobbleAmplitude;
    }

    public float getFovPulseAmount() {
        return fovPulseAmount;
    }

    public float getFovPulseSpeed() {
        return fovPulseSpeed;
    }

    public int getParticleDensity() {
        return particleDensity;
    }

    public boolean isLocalOnly() {
        return localOnly;
    }

    public String getStartSoundId() {
        return startSoundId;
    }
}