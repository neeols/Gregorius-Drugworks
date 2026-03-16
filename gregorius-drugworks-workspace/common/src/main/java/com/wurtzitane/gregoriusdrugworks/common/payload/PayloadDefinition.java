package com.wurtzitane.gregoriusdrugworks.common.payload;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public final class PayloadDefinition {

    private final String id;
    private final PayloadCategory category;
    private final PayloadCompatibility compatibility;
    private final String displayNameKey;
    private final int defaultCharges;
    private final PayloadChargePolicy chargePolicy;
    private final Set<PayloadBehaviorFlag> behaviorFlags;
    private final String forwardItemId;
    private final String visualProfileId;
    private final int defaultVisualDurationTicks;
    private final String triggerBundleId;

    public PayloadDefinition(
            String id,
            PayloadCategory category,
            PayloadCompatibility compatibility,
            String displayNameKey,
            int defaultCharges,
            PayloadChargePolicy chargePolicy,
            Set<PayloadBehaviorFlag> behaviorFlags,
            String forwardItemId,
            String visualProfileId,
            int defaultVisualDurationTicks
    ) {
        this(
                id,
                category,
                compatibility,
                displayNameKey,
                defaultCharges,
                chargePolicy,
                behaviorFlags,
                forwardItemId,
                visualProfileId,
                defaultVisualDurationTicks,
                null
        );
    }

    public PayloadDefinition(
            String id,
            PayloadCategory category,
            PayloadCompatibility compatibility,
            String displayNameKey,
            int defaultCharges,
            PayloadChargePolicy chargePolicy,
            Set<PayloadBehaviorFlag> behaviorFlags,
            String forwardItemId,
            String visualProfileId,
            int defaultVisualDurationTicks,
            String triggerBundleId
    ) {
        this.id = id;
        this.category = category;
        this.compatibility = compatibility;
        this.displayNameKey = displayNameKey;
        this.defaultCharges = defaultCharges;
        this.chargePolicy = chargePolicy;
        this.behaviorFlags = behaviorFlags == null
                ? Collections.<PayloadBehaviorFlag>emptySet()
                : Collections.unmodifiableSet(EnumSet.copyOf(behaviorFlags));
        this.forwardItemId = forwardItemId;
        this.visualProfileId = visualProfileId;
        this.defaultVisualDurationTicks = defaultVisualDurationTicks;
        this.triggerBundleId = triggerBundleId;
    }

    public String getId() {
        return id;
    }

    public PayloadCategory getCategory() {
        return category;
    }

    public PayloadCompatibility getCompatibility() {
        return compatibility;
    }

    public String getDisplayNameKey() {
        return displayNameKey;
    }

    public int getDefaultCharges() {
        return defaultCharges;
    }

    public PayloadChargePolicy getChargePolicy() {
        return chargePolicy;
    }

    public Set<PayloadBehaviorFlag> getBehaviorFlags() {
        return behaviorFlags;
    }

    public String getForwardItemId() {
        return forwardItemId;
    }

    public String getVisualProfileId() {
        return visualProfileId;
    }

    public int getDefaultVisualDurationTicks() {
        return defaultVisualDurationTicks;
    }

    public String getTriggerBundleId() {
        return triggerBundleId;
    }

    public boolean hasFlag(PayloadBehaviorFlag flag) {
        return behaviorFlags.contains(flag);
    }
}