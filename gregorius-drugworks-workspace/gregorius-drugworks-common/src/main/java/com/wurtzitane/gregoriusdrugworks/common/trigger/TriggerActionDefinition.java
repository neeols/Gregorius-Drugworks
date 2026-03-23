package com.wurtzitane.gregoriusdrugworks.common.trigger;

public final class TriggerActionDefinition {

    private final TriggerActionType type;
    private final String primaryId;
    private final int intValue;

    public TriggerActionDefinition(
            TriggerActionType type,
            String primaryId,
            int intValue
    ) {
        this.type = type;
        this.primaryId = primaryId;
        this.intValue = intValue;
    }

    public TriggerActionType getType() {
        return type;
    }

    public String getPrimaryId() {
        return primaryId;
    }

    public int getIntValue() {
        return intValue;
    }
}