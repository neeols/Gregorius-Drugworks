package com.wurtzitane.gregoriusdrugworks.common.payload;

public final class PayloadValidation {

    private PayloadValidation() {
    }

    public static String validateDefinition(PayloadDefinition definition) {
        if (definition == null) {
            return "Definition was null";
        }
        if (definition.getId() == null || definition.getId().isEmpty()) {
            return "Missing payload id";
        }
        if (definition.getDefaultCharges() <= 0) {
            return "Default charges must be > 0";
        }
        for (PayloadModeDefinition mode : definition.getModes().values()) {
            String modeError = validateMode(mode);
            if (modeError != null) {
                return "Invalid mode: " + modeError;
            }
        }
        return null;
    }

    public static String validateSerializedData(
            int version,
            String payloadId,
            String category,
            String compatibility,
            int charges,
            int maxCharges,
            PayloadDefinition definition
    ) {
        if (version <= 0) {
            return "Invalid payload version";
        }
        if (payloadId == null || payloadId.isEmpty()) {
            return "Missing payload id";
        }
        if (definition == null) {
            return "Unknown payload id";
        }
        if (category == null || category.isEmpty()) {
            return "Missing payload category";
        }
        if (compatibility == null || compatibility.isEmpty()) {
            return "Missing payload compatibility";
        }
        if (charges <= 0) {
            return "Payload charges must be > 0";
        }
        if (maxCharges <= 0) {
            return "Payload max charges must be > 0";
        }
        if (charges > maxCharges) {
            return "Payload charges exceeded max charges";
        }
        return null;
    }

    public static boolean isCompatible(PayloadCompatibility payloadCompatibility, PayloadCompatibility carrierCompatibility) {
        return payloadCompatibility == PayloadCompatibility.UNIVERSAL || payloadCompatibility == carrierCompatibility;
    }

    public static String validateMode(PayloadModeDefinition mode) {
        if (mode == null) {
            return "Mode was null";
        }
        if (mode.getId() == null || mode.getId().trim().isEmpty()) {
            return "Missing mode id";
        }
        if (mode.getOnsetScale() <= 0.0D) {
            return "Onset scale must be > 0";
        }
        if (mode.getPeakScale() <= 0.0D) {
            return "Peak scale must be > 0";
        }
        if (mode.getDurationScale() <= 0.0D) {
            return "Duration scale must be > 0";
        }
        return null;
    }
}
