package com.wurtzitane.gregoriusdrugworks.common.payload;

public final class PayloadSourceDefinition {

    private final String sourceItemId;
    private final String payloadId;
    private final int chargesOverride;
    private final boolean consumed;
    private final String remainderItemId;

    public PayloadSourceDefinition(
            String sourceItemId,
            String payloadId,
            int chargesOverride,
            boolean consumed,
            String remainderItemId
    ) {
        this.sourceItemId = sourceItemId;
        this.payloadId = payloadId;
        this.chargesOverride = chargesOverride;
        this.consumed = consumed;
        this.remainderItemId = remainderItemId;
    }

    public String getSourceItemId() {
        return sourceItemId;
    }

    public String getPayloadId() {
        return payloadId;
    }

    public int getChargesOverride() {
        return chargesOverride;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public String getRemainderItemId() {
        return remainderItemId;
    }
}