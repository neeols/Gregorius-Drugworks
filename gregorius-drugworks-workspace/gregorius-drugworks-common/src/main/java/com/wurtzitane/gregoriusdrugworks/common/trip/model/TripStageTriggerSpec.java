package com.wurtzitane.gregoriusdrugworks.common.trip.model;

public final class TripStageTriggerSpec {

    public static final TripStageTriggerSpec NONE = new TripStageTriggerSpec(
            null,
            null,
            null,
            0
    );

    private final String onEnterTriggerBundleId;
    private final String onTickTriggerBundleId;
    private final String onExitTriggerBundleId;
    private final int onTickIntervalTicks;

    public TripStageTriggerSpec(
            String onEnterTriggerBundleId,
            String onTickTriggerBundleId,
            String onExitTriggerBundleId,
            int onTickIntervalTicks
    ) {
        this.onEnterTriggerBundleId = onEnterTriggerBundleId;
        this.onTickTriggerBundleId = onTickTriggerBundleId;
        this.onExitTriggerBundleId = onExitTriggerBundleId;
        this.onTickIntervalTicks = onTickIntervalTicks;
    }

    public String getOnEnterTriggerBundleId() {
        return onEnterTriggerBundleId;
    }

    public String getOnTickTriggerBundleId() {
        return onTickTriggerBundleId;
    }

    public String getOnExitTriggerBundleId() {
        return onExitTriggerBundleId;
    }

    public int getOnTickIntervalTicks() {
        return onTickIntervalTicks;
    }

    public boolean hasOnEnter() {
        return onEnterTriggerBundleId != null && !onEnterTriggerBundleId.isEmpty();
    }

    public boolean hasOnTick() {
        return onTickTriggerBundleId != null && !onTickTriggerBundleId.isEmpty() && onTickIntervalTicks > 0;
    }

    public boolean hasOnExit() {
        return onExitTriggerBundleId != null && !onExitTriggerBundleId.isEmpty();
    }

    public boolean isEmpty() {
        return !hasOnEnter() && !hasOnTick() && !hasOnExit();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String onEnterTriggerBundleId;
        private String onTickTriggerBundleId;
        private String onExitTriggerBundleId;
        private int onTickIntervalTicks = 0;

        private Builder() {
        }

        public Builder onEnter(String id) {
            this.onEnterTriggerBundleId = id;
            return this;
        }

        public Builder onTick(String id, int intervalTicks) {
            this.onTickTriggerBundleId = id;
            this.onTickIntervalTicks = intervalTicks;
            return this;
        }

        public Builder onExit(String id) {
            this.onExitTriggerBundleId = id;
            return this;
        }

        public TripStageTriggerSpec build() {
            return new TripStageTriggerSpec(
                    onEnterTriggerBundleId,
                    onTickTriggerBundleId,
                    onExitTriggerBundleId,
                    onTickIntervalTicks
            );
        }
    }
}