package com.wurtzitane.gregoriusdrugworks.common.trip.api;

import com.wurtzitane.gregoriusdrugworks.common.trip.model.AntidoteDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.registry.TripRegistry;

public final class TripApiFacade {

    private TripApiFacade() {
    }

    public static TripRegistry registry() {
        return TripRegistrationApi.registry();
    }

    public static void clearAll() {
        TripRegistrationApi.clearAll();
    }

    public static void registerTrip(TripDefinition definition) {
        TripRegistrationApi.registerTrip(definition);
    }

    public static void registerAntidote(AntidoteDefinition definition) {
        TripRegistrationApi.registerAntidote(definition);
    }

    public static void allowAntidoteForTrip(String antidoteItemId, String tripItemId) {
        TripRegistrationApi.allowAntidoteForTrip(antidoteItemId, tripItemId);
    }
}