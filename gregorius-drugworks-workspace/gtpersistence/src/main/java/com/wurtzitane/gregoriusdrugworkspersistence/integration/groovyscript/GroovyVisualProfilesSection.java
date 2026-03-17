package com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript;

import com.wurtzitane.gregoriusdrugworks.common.visual.VisualColorMode;
import com.wurtzitane.gregoriusdrugworks.common.visual.VisualEffectProfile;

public final class GroovyVisualProfilesSection extends AbstractGroovySection {

    public GroovyVisualProfilesSection() {
        super("visualProfiles", "visualProfile", "visuals");
    }

    public Builder builder(String id, String debugName) {
        return new Builder(id, debugName);
    }

    public VisualEffectProfile register(VisualEffectProfile profile) {
        GregoriusDrugworksGroovyScriptBridge.registerVisualProfile(profile);
        return profile;
    }

    public static final class Builder {
        private final String id;
        private final String debugName;

        private VisualColorMode colorMode = VisualColorMode.STATIC_TINT;
        private int tintArgb = 0x55FFFFFF;
        private float pulseSpeed = 0.10F;
        private float pulseAmplitude = 0.10F;
        private float flashFrequency = 0.0F;
        private float flashIntensity = 0.0F;
        private float yawDrift = 0.0F;
        private float pitchDrift = 0.0F;
        private float wobbleSpeed = 0.0F;
        private float wobbleAmplitude = 0.0F;
        private float fovPulseAmount = 0.0F;
        private float fovPulseSpeed = 0.0F;
        private int particleDensity = 0;
        private float vignetteStrength = 0.0F;
        private float prismSeparation = 0.0F;
        private float tunnelStrength = 0.0F;
        private float ribbonIntensity = 0.0F;
        private int ribbonDensity = 0;
        private float scanlineStrength = 0.0F;
        private float rollDrift = 0.0F;
        private float afterimageStrength = 0.0F;
        private boolean localOnly = true;
        private String startSoundId = "";

        private Builder(String id, String debugName) {
            this.id = id;
            this.debugName = debugName;
        }

        public Builder colorMode(VisualColorMode value) {
            this.colorMode = value;
            return this;
        }

        public Builder colorMode(String value) {
            return colorMode(GroovyScriptUtil.enumValue(VisualColorMode.class, value));
        }

        public Builder tintArgb(int value) {
            this.tintArgb = value;
            return this;
        }

        public Builder pulseSpeed(float value) {
            this.pulseSpeed = value;
            return this;
        }

        public Builder pulseAmplitude(float value) {
            this.pulseAmplitude = value;
            return this;
        }

        public Builder flashFrequency(float value) {
            this.flashFrequency = value;
            return this;
        }

        public Builder flashIntensity(float value) {
            this.flashIntensity = value;
            return this;
        }

        public Builder yawDrift(float value) {
            this.yawDrift = value;
            return this;
        }

        public Builder pitchDrift(float value) {
            this.pitchDrift = value;
            return this;
        }

        public Builder wobbleSpeed(float value) {
            this.wobbleSpeed = value;
            return this;
        }

        public Builder wobbleAmplitude(float value) {
            this.wobbleAmplitude = value;
            return this;
        }

        public Builder fovPulseAmount(float value) {
            this.fovPulseAmount = value;
            return this;
        }

        public Builder fovPulseSpeed(float value) {
            this.fovPulseSpeed = value;
            return this;
        }

        public Builder particleDensity(int value) {
            this.particleDensity = value;
            return this;
        }

        public Builder vignetteStrength(float value) {
            this.vignetteStrength = value;
            return this;
        }

        public Builder prismSeparation(float value) {
            this.prismSeparation = value;
            return this;
        }

        public Builder tunnelStrength(float value) {
            this.tunnelStrength = value;
            return this;
        }

        public Builder ribbonIntensity(float value) {
            this.ribbonIntensity = value;
            return this;
        }

        public Builder ribbonDensity(int value) {
            this.ribbonDensity = value;
            return this;
        }

        public Builder scanlineStrength(float value) {
            this.scanlineStrength = value;
            return this;
        }

        public Builder rollDrift(float value) {
            this.rollDrift = value;
            return this;
        }

        public Builder afterimageStrength(float value) {
            this.afterimageStrength = value;
            return this;
        }

        public Builder localOnly(boolean value) {
            this.localOnly = value;
            return this;
        }

        public Builder startSoundId(String value) {
            this.startSoundId = value == null ? "" : value;
            return this;
        }

        public VisualEffectProfile build() {
            return new VisualEffectProfile(
                    id,
                    debugName,
                    colorMode,
                    tintArgb,
                    pulseSpeed,
                    pulseAmplitude,
                    flashFrequency,
                    flashIntensity,
                    yawDrift,
                    pitchDrift,
                    wobbleSpeed,
                    wobbleAmplitude,
                    fovPulseAmount,
                    fovPulseSpeed,
                    particleDensity,
                    vignetteStrength,
                    prismSeparation,
                    tunnelStrength,
                    ribbonIntensity,
                    ribbonDensity,
                    scanlineStrength,
                    rollDrift,
                    afterimageStrength,
                    localOnly,
                    startSoundId
            );
        }

        public VisualEffectProfile register() {
            VisualEffectProfile profile = build();
            GregoriusDrugworksGroovyScriptBridge.registerVisualProfile(profile);
            return profile;
        }
    }
}
