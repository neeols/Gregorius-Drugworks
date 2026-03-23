package com.wurtzitane.gregoriusdrugworks.common.trip.model;

import java.util.Objects;

public final class SoundSpec {

    private final String id;
    private final float volume;
    private final float pitch;

    public SoundSpec(String id, float volume, float pitch) {
        this.id = Objects.requireNonNull(id, "id");
        this.volume = volume;
        this.pitch = pitch;
    }

    public String getId() {
        return id;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}