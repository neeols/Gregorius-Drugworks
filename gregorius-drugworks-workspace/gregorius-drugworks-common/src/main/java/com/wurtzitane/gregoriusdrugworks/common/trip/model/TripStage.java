package com.wurtzitane.gregoriusdrugworks.common.trip.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TripStage {

    private final int atSeconds;
    private final int periodTicks;
    private final int particlesPeriodTicks;
    private final int particlesMinGapTicks;
    private final String message;
    private final String messageColor;
    private final List<EffectSpec> effects;
    private final ParticleSpec particles;

    private TripStage(Builder builder) {
        this.atSeconds = builder.atSeconds;
        this.periodTicks = builder.periodTicks;
        this.particlesPeriodTicks = builder.particlesPeriodTicks;
        this.particlesMinGapTicks = builder.particlesMinGapTicks;
        this.message = builder.message;
        this.messageColor = builder.messageColor;
        this.effects = Collections.unmodifiableList(new ArrayList<EffectSpec>(builder.effects));
        this.particles = builder.particles;
        this.triggerSpec = builder.triggerSpec == null ? TripStageTriggerSpec.NONE : builder.triggerSpec;
    }

    public int getAtSeconds() {
        return atSeconds;
    }

    public int getPeriodTicks() {
        return periodTicks;
    }

    public int getParticlesPeriodTicks() {
        return particlesPeriodTicks;
    }

    public int getParticlesMinGapTicks() {
        return particlesMinGapTicks;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageColor() {
        return messageColor;
    }

    public List<EffectSpec> getEffects() {
        return effects;
    }

    public ParticleSpec getParticles() {
        return particles;
    }

    private final TripStageTriggerSpec triggerSpec;

    public TripStageTriggerSpec getTriggerSpec() {
        return triggerSpec;
    }

    public static Builder builder(int atSeconds) {
        return new Builder(atSeconds);
    }

    public static final class Builder {
        private final int atSeconds;
        private int periodTicks = 10;
        private int particlesPeriodTicks = 20;
        private int particlesMinGapTicks = -1;
        private String message;
        private String messageColor;
        private final List<EffectSpec> effects = new ArrayList<EffectSpec>();
        private ParticleSpec particles;
        private TripStageTriggerSpec triggerSpec = TripStageTriggerSpec.NONE;

        private Builder(int atSeconds) {
            this.atSeconds = atSeconds;
        }

        public Builder periodTicks(int periodTicks) {
            this.periodTicks = periodTicks;
            return this;
        }

        public Builder particlesPeriodTicks(int particlesPeriodTicks) {
            this.particlesPeriodTicks = particlesPeriodTicks;
            return this;
        }

        public Builder particlesMinGapTicks(int particlesMinGapTicks) {
            this.particlesMinGapTicks = particlesMinGapTicks;
            return this;
        }

        public Builder message(String message, String color) {
            this.message = message;
            this.messageColor = color;
            return this;
        }

        public Builder effect(EffectSpec effect) {
            this.effects.add(effect);
            return this;
        }

        public Builder particle(ParticleSpec particles) {
            this.particles = particles;
            return this;
        }

        public Builder triggerSpec(TripStageTriggerSpec triggerSpec) {
            this.triggerSpec = triggerSpec == null ? TripStageTriggerSpec.NONE : triggerSpec;
            return this;
        }

        public TripStage build() {
            return new TripStage(this);
        }
    }
}