package com.wurtzitane.gregoriusdrugworks.common.payload;

public final class PayloadModeDefinition {

    private final String id;
    private final String forwardItemId;
    private final String triggerBundleId;
    private final String visualProfileId;
    private final int visualDurationTicks;
    private final double onsetScale;
    private final double peakScale;
    private final double durationScale;

    public PayloadModeDefinition(
            String id,
            String forwardItemId,
            String triggerBundleId,
            String visualProfileId,
            int visualDurationTicks,
            double onsetScale,
            double peakScale,
            double durationScale
    ) {
        this.id = id;
        this.forwardItemId = forwardItemId;
        this.triggerBundleId = triggerBundleId;
        this.visualProfileId = visualProfileId;
        this.visualDurationTicks = visualDurationTicks;
        this.onsetScale = onsetScale;
        this.peakScale = peakScale;
        this.durationScale = durationScale;
    }

    public String getId() {
        return id;
    }

    public String getForwardItemId() {
        return forwardItemId;
    }

    public String getTriggerBundleId() {
        return triggerBundleId;
    }

    public String getVisualProfileId() {
        return visualProfileId;
    }

    public int getVisualDurationTicks() {
        return visualDurationTicks;
    }

    public double getOnsetScale() {
        return onsetScale;
    }

    public double getPeakScale() {
        return peakScale;
    }

    public double getDurationScale() {
        return durationScale;
    }

    public boolean hasKineticsOverrides() {
        return Math.abs(onsetScale - 1.0D) > 0.0001D
                || Math.abs(peakScale - 1.0D) > 0.0001D
                || Math.abs(durationScale - 1.0D) > 0.0001D;
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static final class Builder {
        private final String id;
        private String forwardItemId;
        private String triggerBundleId;
        private String visualProfileId;
        private int visualDurationTicks;
        private double onsetScale = 1.0D;
        private double peakScale = 1.0D;
        private double durationScale = 1.0D;

        private Builder(String id) {
            this.id = id;
        }

        public Builder forwardItemId(String forwardItemId) {
            this.forwardItemId = forwardItemId;
            return this;
        }

        public Builder triggerBundleId(String triggerBundleId) {
            this.triggerBundleId = triggerBundleId;
            return this;
        }

        public Builder visualProfile(String visualProfileId, int visualDurationTicks) {
            this.visualProfileId = visualProfileId;
            this.visualDurationTicks = visualDurationTicks;
            return this;
        }

        public Builder visualProfileId(String visualProfileId) {
            this.visualProfileId = visualProfileId;
            return this;
        }

        public Builder visualDurationTicks(int visualDurationTicks) {
            this.visualDurationTicks = visualDurationTicks;
            return this;
        }

        public Builder onsetScale(double onsetScale) {
            this.onsetScale = onsetScale;
            return this;
        }

        public Builder peakScale(double peakScale) {
            this.peakScale = peakScale;
            return this;
        }

        public Builder durationScale(double durationScale) {
            this.durationScale = durationScale;
            return this;
        }

        public PayloadModeDefinition build() {
            return new PayloadModeDefinition(
                    id,
                    forwardItemId,
                    triggerBundleId,
                    visualProfileId,
                    visualDurationTicks,
                    onsetScale,
                    peakScale,
                    durationScale
            );
        }
    }
}
