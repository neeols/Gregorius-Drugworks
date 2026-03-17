package com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript;

import com.wurtzitane.gregoriusdrugworks.common.trip.model.AntidoteDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.EffectSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.ParticleSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.SoundSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripStage;

public final class GroovyTripsSection extends AbstractGroovySection {

    public GroovyTripsSection() {
        super("trips", "trip");
    }

    public TripDefinition.Builder trip(String itemId) {
        return TripDefinition.builder(itemId);
    }

    public TripStage.Builder stage(int atSeconds) {
        return TripStage.builder(atSeconds);
    }

    public AntidoteDefinition.Builder antidote(String itemId) {
        return AntidoteDefinition.builder(itemId);
    }

    public EffectSpec effect(String effectId, int seconds, int amplifier, boolean hideParticles) {
        return new EffectSpec(effectId, seconds, amplifier, hideParticles);
    }

    public ParticleSpec particle(String particleId, int count, double radius, double speed) {
        return new ParticleSpec(particleId, count, radius, speed);
    }

    public SoundSpec sound(String soundId, float volume, float pitch) {
        return new SoundSpec(soundId, volume, pitch);
    }

    public TripDefinition registerTrip(TripDefinition definition) {
        GregoriusDrugworksGroovyScriptBridge.registerTrip(definition);
        return definition;
    }

    public AntidoteDefinition registerAntidote(AntidoteDefinition definition) {
        GregoriusDrugworksGroovyScriptBridge.registerAntidote(definition);
        return definition;
    }

    public void allowAntidoteForTrip(String antidoteItemId, String tripItemId) {
        GregoriusDrugworksGroovyScriptBridge.allowAntidoteForTrip(antidoteItemId, tripItemId);
    }
}
