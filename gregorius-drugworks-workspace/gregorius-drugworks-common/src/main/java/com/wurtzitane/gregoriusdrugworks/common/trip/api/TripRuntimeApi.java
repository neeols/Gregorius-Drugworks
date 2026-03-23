package com.wurtzitane.gregoriusdrugworks.common.trip.api;

import com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime;

public interface TripRuntimeApi {

    void tick();

    void onPlayerLogin(TripRuntime.TripPlayer player);

    void onPlayerLogout(TripRuntime.TripPlayer player);

    /**
     * @return true if the item use was handled by the trip system and should be treated as consumed/cancelled by the platform.
     */
    boolean handleItemUse(TripRuntime.TripPlayer player, String itemId);

    void cancelTripNow(TripRuntime.TripPlayer player);

    boolean isTripRunning(TripRuntime.TripPlayer player);

    String formatTripLeft(TripRuntime.TripPlayer player);
}