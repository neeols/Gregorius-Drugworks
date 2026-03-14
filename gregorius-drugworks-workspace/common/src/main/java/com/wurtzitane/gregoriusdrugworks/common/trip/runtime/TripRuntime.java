package com.wurtzitane.gregoriusdrugworks.common.trip.runtime;

import com.wurtzitane.gregoriusdrugworks.common.trip.model.EffectSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.ParticleSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.SoundSpec;

import java.util.UUID;

public interface TripRuntime {

    interface TripPlayer {
        UUID getUuid();
        String getUsername();
    }

    long getCurrentTick();

    TripPlayer resolvePlayer(UUID uuid, String username);

    long getPersistentLong(TripPlayer player, String key);

    void setPersistentLong(TripPlayer player, String key, long value);

    boolean getPersistentBoolean(TripPlayer player, String key);

    void setPersistentBoolean(TripPlayer player, String key, boolean value);

    void removePersistentKey(TripPlayer player, String key);

    void sendMessage(TripPlayer player, String message, String color);

    void applyEffect(TripPlayer player, EffectSpec effect);

    void clearEffect(TripPlayer player, String effectId);

    void spawnParticles(TripPlayer player, ParticleSpec particle);

    void playSound(TripPlayer player, SoundSpec sound);

    void consumeHeldItem(TripPlayer player, int amount);

    void log(String message);
}