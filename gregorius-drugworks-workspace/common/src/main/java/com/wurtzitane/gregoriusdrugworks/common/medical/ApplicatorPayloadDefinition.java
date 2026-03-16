package com.wurtzitane.gregoriusdrugworks.common.medical;

public final class ApplicatorPayloadDefinition {

    private final String id;
    private final ApplicatorPayloadCategory category;
    private final String deliveryItemId;
    private final String displayNameKey;
    private final int defaultCharges;

    public ApplicatorPayloadDefinition(
            String id,
            ApplicatorPayloadCategory category,
            String deliveryItemId,
            String displayNameKey,
            int defaultCharges
    ) {
        this.id = id;
        this.category = category;
        this.deliveryItemId = deliveryItemId;
        this.displayNameKey = displayNameKey;
        this.defaultCharges = defaultCharges;
    }

    public String getId() {
        return id;
    }

    public ApplicatorPayloadCategory getCategory() {
        return category;
    }

    public String getDeliveryItemId() {
        return deliveryItemId;
    }

    public String getDisplayNameKey() {
        return displayNameKey;
    }

    public int getDefaultCharges() {
        return defaultCharges;
    }
}