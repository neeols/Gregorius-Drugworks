package com.wurtzitane.gregoriusdrugworks.common.trip.model;

import java.util.Objects;

public final class ParticleSpec {

    private final String id;
    private final int count;
    private final double spread;
    private final double speed;

    public ParticleSpec(String id, int count, double spread, double speed) {
        this.id = Objects.requireNonNull(id, "id");
        this.count = count;
        this.spread = spread;
        this.speed = speed;
    }

    public String getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public double getSpread() {
        return spread;
    }

    public double getSpeed() {
        return speed;
    }
}