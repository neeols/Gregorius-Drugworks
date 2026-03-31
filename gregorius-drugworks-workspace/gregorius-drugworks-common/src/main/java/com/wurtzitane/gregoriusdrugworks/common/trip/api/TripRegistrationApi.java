package com.wurtzitane.gregoriusdrugworks.common.trip.api;

import com.wurtzitane.gregoriusdrugworks.common.trip.model.AntidoteDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.registry.TripRegistry;

public final class TripRegistrationApi {

    private static final TripRegistry REGISTRY = new TripRegistry();

    private TripRegistrationApi() {
    }

    public static TripRegistry registry() {
        return REGISTRY;
    }

    public static void clearAll() {
        REGISTRY.clear();
    }

    public static void registerTrip(TripDefinition definition) {
        REGISTRY.registerTrip(definition);
    }

    public static void registerAntidote(AntidoteDefinition definition) {
        REGISTRY.registerAntidote(definition);
    }

    public static void allowAntidoteForTrip(String antidoteItemId, String tripItemId) {
        REGISTRY.allowAntidoteForTrip(antidoteItemId, tripItemId);
    }
}