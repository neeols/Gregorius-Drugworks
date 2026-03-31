package com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadSourceDefinition;

public final class GroovyPayloadSourcesSection extends AbstractGroovySection {

    public GroovyPayloadSourcesSection() {
        super("payloadSources", "payloadSource", "sources");
    }

    public Builder builder(String sourceItemId, String payloadId) {
        return new Builder(sourceItemId, payloadId);
    }

    public PayloadSourceDefinition register(PayloadSourceDefinition definition) {
        GregoriusDrugworksGroovyScriptBridge.registerPayloadSource(definition);
        return definition;
    }

    public static final class Builder {
        private final String sourceItemId;
        private final String payloadId;

        private int chargesOverride = 0;
        private boolean consumed = true;
        private String remainderItemId;

        private Builder(String sourceItemId, String payloadId) {
            this.sourceItemId = sourceItemId;
            this.payloadId = payloadId;
        }

        public Builder chargesOverride(int value) {
            this.chargesOverride = value;
            return this;
        }

        public Builder consumed(boolean value) {
            this.consumed = value;
            return this;
        }

        public Builder remainderItemId(String value) {
            this.remainderItemId = value;
            return this;
        }

        public PayloadSourceDefinition build() {
            return new PayloadSourceDefinition(
                    sourceItemId,
                    payloadId,
                    chargesOverride,
                    consumed,
                    remainderItemId
            );
        }

        public PayloadSourceDefinition register() {
            PayloadSourceDefinition definition = build();
            GregoriusDrugworksGroovyScriptBridge.registerPayloadSource(definition);
            return definition;
        }
    }
}
