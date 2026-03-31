package com.wurtzitane.gregoriusdrugworks.common.trip.model;

public final class ActiveTripState {

    private final long tripId;
    private final String tripItemId;
    private final int tripItemHash;
    private final long startTick;
    private final long untilTick;

    public ActiveTripState(long tripId, String tripItemId, int tripItemHash, long startTick, long untilTick) {
        this.tripId = tripId;
        this.tripItemId = tripItemId;
        this.tripItemHash = tripItemHash;
        this.startTick = startTick;
        this.untilTick = untilTick;
    }

    public long getTripId() {
        return tripId;
    }

    public String getTripItemId() {
        return tripItemId;
    }

    public int getTripItemHash() {
        return tripItemHash;
    }

    public long getStartTick() {
        return startTick;
    }

    public long getUntilTick() {
        return untilTick;
    }
}