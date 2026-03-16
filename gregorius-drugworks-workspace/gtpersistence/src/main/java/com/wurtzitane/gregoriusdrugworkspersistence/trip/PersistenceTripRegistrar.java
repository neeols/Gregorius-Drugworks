package com.wurtzitane.gregoriusdrugworkspersistence.trip;

import com.wurtzitane.gregoriusdrugworks.common.trip.api.TripApiFacade;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.AntidoteDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripDefinition;

public final class PersistenceTripRegistrar {

    private PersistenceTripRegistrar() {
    }

    public static void clearAll() {
        TripApiFacade.clearAll();
    }

    public static void registerTrip(TripDefinition definition) {
        TripApiFacade.registerTrip(definition);
    }

    public static void registerAntidote(AntidoteDefinition definition) {
        TripApiFacade.registerAntidote(definition);
    }

    public static void allowAntidoteForTrip(String antidoteItemId, String tripItemId) {
        TripApiFacade.allowAntidoteForTrip(antidoteItemId, tripItemId);
    }
}