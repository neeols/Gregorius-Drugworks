package com.wurtzitane.gregoriusdrugworkspersistence.inhalation;

import com.wurtzitane.gregoriusdrugworks.common.inhalation.LingeringOriginMode;
import com.wurtzitane.gregoriusdrugworks.common.inhalation.LingeringPhaseProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InhalationLingeringSpec extends LingeringPhaseProfile {

    private final List<InhalationParticleSpec> particles;

    private InhalationLingeringSpec(Builder builder) {
        super(
                builder.startDelayTicks,
                builder.durationTicks,
                builder.cadenceTicks,
                builder.attachedTicks,
                builder.initialCount,
                builder.finalCount,
                builder.initialSpread,
                builder.finalSpread,
                builder.initialSpeed,
                builder.finalSpeed,
                builder.forwardDrift,
                builder.upwardDrift,
                builder.originMode
        );
        this.particles = Collections.unmodifiableList(new ArrayList<>(builder.particles));
    }

    public List<InhalationParticleSpec> getParticles() {
        return particles;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private int startDelayTicks = 0;
        private int durationTicks = 20;
        private int cadenceTicks = 2;
        private int attachedTicks = 4;
        private int initialCount = 4;
        private int finalCount = 1;
        private double initialSpread = 0.03D;
        private double finalSpread = 0.12D;
        private double initialSpeed = 0.01D;
        private double finalSpeed = 0.005D;
        private double forwardDrift = 0.01D;
        private double upwardDrift = 0.02D;
        private LingeringOriginMode originMode = LingeringOriginMode.FRONT_OF_FACE;
        private final List<InhalationParticleSpec> particles = new ArrayList<>();

        private Builder() {
        }

        public Builder startDelayTicks(int value) {
            this.startDelayTicks = value;
            return this;
        }

        public Builder durationTicks(int value) {
            this.durationTicks = value;
            return this;
        }

        public Builder cadenceTicks(int value) {
            this.cadenceTicks = value;
            return this;
        }

        public Builder attachedTicks(int value) {
            this.attachedTicks = value;
            return this;
        }

        public Builder initialCount(int value) {
            this.initialCount = value;
            return this;
        }

        public Builder finalCount(int value) {
            this.finalCount = value;
            return this;
        }

        public Builder initialSpread(double value) {
            this.initialSpread = value;
            return this;
        }

        public Builder finalSpread(double value) {
            this.finalSpread = value;
            return this;
        }

        public Builder initialSpeed(double value) {
            this.initialSpeed = value;
            return this;
        }

        public Builder finalSpeed(double value) {
            this.finalSpeed = value;
            return this;
        }

        public Builder forwardDrift(double value) {
            this.forwardDrift = value;
            return this;
        }

        public Builder upwardDrift(double value) {
            this.upwardDrift = value;
            return this;
        }

        public Builder originMode(LingeringOriginMode value) {
            this.originMode = value;
            return this;
        }

        public Builder addParticle(InhalationParticleSpec value) {
            this.particles.add(value);
            return this;
        }

        public InhalationLingeringSpec build() {
            return new InhalationLingeringSpec(this);
        }
    }
}
