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
}