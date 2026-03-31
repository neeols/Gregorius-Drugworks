package com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadBehaviorFlag;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCategory;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadChargePolicy;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCompatibility;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadDefinition;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadModeDefinition;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

public final class GroovyPayloadsSection extends AbstractGroovySection {

    public GroovyPayloadsSection() {
        super("payloads", "payload");
    }

    public Builder builder(
            String id,
            PayloadCategory category,
            PayloadCompatibility compatibility,
            String displayNameKey
    ) {
        return new Builder(id, category, compatibility, displayNameKey);
    }

    public PayloadDefinition register(PayloadDefinition definition) {
        GregoriusDrugworksGroovyScriptBridge.registerPayload(definition);
        return definition;
    }

    public static final class Builder {
        private final String id;
        private final PayloadCategory category;
        private final PayloadCompatibility compatibility;
        private final String displayNameKey;

        private int defaultCharges = 1;
        private PayloadChargePolicy chargePolicy = PayloadChargePolicy.SINGLE_USE;
        private final EnumSet<PayloadBehaviorFlag> behaviorFlags = EnumSet.noneOf(PayloadBehaviorFlag.class);
        private String forwardItemId;
        private String visualProfileId;
        private int defaultVisualDurationTicks;
        private String triggerBundleId;
        private final Map<String, PayloadModeDefinition> modes = new LinkedHashMap<String, PayloadModeDefinition>();

        private Builder(
                String id,
                PayloadCategory category,
                PayloadCompatibility compatibility,
                String displayNameKey
        ) {
            this.id = id;
            this.category = category;
            this.compatibility = compatibility;
            this.displayNameKey = displayNameKey;
        }

        public Builder defaultCharges(int value) {
            this.defaultCharges = value;
            return this;
        }

        public Builder chargePolicy(PayloadChargePolicy value) {
            this.chargePolicy = value;
            return this;
        }

        public Builder chargePolicy(String value) {
            return chargePolicy(GroovyScriptUtil.enumValue(PayloadChargePolicy.class, value));
        }

        public Builder flag(PayloadBehaviorFlag value) {
            this.behaviorFlags.add(value);
            return this;
        }

        public Builder flag(String value) {
            return flag(GroovyScriptUtil.enumValue(PayloadBehaviorFlag.class, value));
        }

        public Builder forwardItemId(String value) {
            this.forwardItemId = value;
            return flag(PayloadBehaviorFlag.FORWARD_ITEM_USE);
        }

        public Builder visualProfile(String value, int durationTicks) {
            this.visualProfileId = value;
            this.defaultVisualDurationTicks = durationTicks;
            return flag(PayloadBehaviorFlag.START_VISUAL_PROFILE);
        }

        public Builder visualProfileId(String value) {
            this.visualProfileId = value;
            return this;
        }

        public Builder defaultVisualDurationTicks(int value) {
            this.defaultVisualDurationTicks = value;
            return this;
        }

        public Builder triggerBundleId(String value) {
            this.triggerBundleId = value;
            return this;
        }

        public Builder mode(String id, Map<?, ?> config) {
            this.modes.put(id, parseMode(id, config));
            return this;
        }

        public Builder mode(PayloadModeDefinition value) {
            if (value == null) {
                throw new IllegalArgumentException("Payload mode definition cannot be null");
            }
            this.modes.put(value.getId(), value);
            return this;
        }

        public Builder modes(Map<?, ?> values) {
            if (values == null) {
                return this;
            }

            for (Map.Entry<?, ?> entry : values.entrySet()) {
                String id = entry.getKey() == null ? null : String.valueOf(entry.getKey());
                Object config = entry.getValue();
                if (!(config instanceof Map)) {
                    throw new IllegalArgumentException("Payload mode `" + id + "` must be configured with a map");
                }
                mode(id, (Map<?, ?>) config);
            }
            return this;
        }

        public PayloadDefinition build() {
            return new PayloadDefinition(
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
                    triggerBundleId,
                    modes
            );
        }

        public PayloadDefinition register() {
            PayloadDefinition definition = build();
            GregoriusDrugworksGroovyScriptBridge.registerPayload(definition);
            return definition;
        }

        private static PayloadModeDefinition parseMode(String id, Map<?, ?> config) {
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException("Payload mode id cannot be empty");
            }
            if (config == null) {
                throw new IllegalArgumentException("Payload mode `" + id + "` must have a config map");
            }

            return PayloadModeDefinition.builder(id)
                    .forwardItemId(GroovyScriptUtil.stringValue(config, "forwardItemId"))
                    .triggerBundleId(GroovyScriptUtil.stringValue(config, "triggerBundleId"))
                    .visualProfileId(GroovyScriptUtil.stringValue(config, "visualProfileId"))
                    .visualDurationTicks(GroovyScriptUtil.intValue(config, "visualDurationTicks", 0))
                    .onsetScale(GroovyScriptUtil.doubleValue(config, "onsetScale", 1.0D))
                    .peakScale(GroovyScriptUtil.doubleValue(config, "peakScale", 1.0D))
                    .durationScale(GroovyScriptUtil.doubleValue(config, "durationScale", 1.0D))
                    .build();
        }
    }
}
