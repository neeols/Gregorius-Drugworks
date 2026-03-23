package com.wurtzitane.gregoriusdrugworks.common.trip.model;

import java.util.Objects;

public final class EffectSpec {

    private final String id;
    private final int seconds;
    private final int amplifier;
    private final boolean hideParticles;

    public EffectSpec(String id, int seconds, int amplifier, boolean hideParticles) {
        this.id = Objects.requireNonNull(id, "id");
        this.seconds = seconds;
        this.amplifier = amplifier;
        this.hideParticles = hideParticles;
    }

    public String getId() {
        return id;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public boolean isHideParticles() {
        return hideParticles;
    }
}