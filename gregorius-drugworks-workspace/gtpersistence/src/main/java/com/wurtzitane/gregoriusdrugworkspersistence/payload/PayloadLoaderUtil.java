package com.wurtzitane.gregoriusdrugworkspersistence.payload;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCompatibility;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadDefinition;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadKeys;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadValidation;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public final class PayloadLoaderUtil {

    private PayloadLoaderUtil() {
    }

    public static boolean loadIntoCarrier(
            ItemStack carrierStack,
            PayloadCompatibility carrierCompatibility,
            String payloadId,
            int chargesOverride,
            @Nullable NBTTagCompound extraData
    ) {
        PayloadDefinition definition = GregoriusDrugworksPayloadRegistry.get(payloadId);
        if (definition == null) {
            return false;
        }

        if (!PayloadValidation.isCompatible(definition.getCompatibility(), carrierCompatibility)) {
            return false;
        }

        int charges = chargesOverride > 0 ? chargesOverride : definition.getDefaultCharges();

        NBTTagCompound root = getOrCreateRoot(carrierStack);
        root.setInteger(PayloadKeys.VERSION_KEY, PayloadKeys.CURRENT_VERSION);
        root.setString(PayloadKeys.PAYLOAD_ID_KEY, definition.getId());
        root.setString(PayloadKeys.PAYLOAD_CATEGORY_KEY, definition.getCategory().name());
        root.setString(PayloadKeys.COMPATIBILITY_KEY, definition.getCompatibility().name());
        root.setInteger(PayloadKeys.CHARGES_KEY, charges);
        root.setInteger(PayloadKeys.MAX_CHARGES_KEY, charges);
        root.setString(PayloadKeys.FORWARD_ITEM_ID_KEY, definition.getForwardItemId() == null ? "" : definition.getForwardItemId());
        root.setString(PayloadKeys.VISUAL_PROFILE_ID_KEY, definition.getVisualProfileId() == null ? "" : definition.getVisualProfileId());
        root.setInteger(PayloadKeys.VISUAL_DURATION_KEY, definition.getDefaultVisualDurationTicks());
        root.setTag(PayloadKeys.EXTRA_DATA_KEY, extraData == null ? new NBTTagCompound() : extraData.copy());

        return true;
    }

    @Nullable
    public static GregoriusDrugworksPayloadRegistry.ResolvedPayload resolve(ItemStack carrierStack) {
        if (carrierStack.isEmpty() || !carrierStack.hasTagCompound()) {
            return null;
        }

        NBTTagCompound stackTag = carrierStack.getTagCompound();
        if (!stackTag.hasKey(PayloadKeys.ROOT_TAG)) {
            return null;
        }

        NBTTagCompound root = stackTag.getCompoundTag(PayloadKeys.ROOT_TAG);
        String payloadId = root.getString(PayloadKeys.PAYLOAD_ID_KEY);
        PayloadDefinition definition = GregoriusDrugworksPayloadRegistry.get(payloadId);

        String error = PayloadValidation.validateSerializedData(
                root.getInteger(PayloadKeys.VERSION_KEY),
                payloadId,
                root.getString(PayloadKeys.PAYLOAD_CATEGORY_KEY),
                root.getString(PayloadKeys.COMPATIBILITY_KEY),
                root.getInteger(PayloadKeys.CHARGES_KEY),
                root.getInteger(PayloadKeys.MAX_CHARGES_KEY),
                definition
        );

        if (error != null || definition == null) {
            return null;
        }

        return new GregoriusDrugworksPayloadRegistry.ResolvedPayload(
                definition,
                root.getInteger(PayloadKeys.CHARGES_KEY),
                root.getInteger(PayloadKeys.MAX_CHARGES_KEY),
                root.hasKey(PayloadKeys.EXTRA_DATA_KEY) ? root.getCompoundTag(PayloadKeys.EXTRA_DATA_KEY).copy() : new NBTTagCompound()
        );
    }

    public static void decrementChargeOrClear(ItemStack carrierStack, boolean consumeCarrierWhenEmpty) {
        if (carrierStack.isEmpty() || !carrierStack.hasTagCompound()) {
            return;
        }

        NBTTagCompound stackTag = carrierStack.getTagCompound();
        if (!stackTag.hasKey(PayloadKeys.ROOT_TAG)) {
            return;
        }

        NBTTagCompound root = stackTag.getCompoundTag(PayloadKeys.ROOT_TAG);
        int charges = root.getInteger(PayloadKeys.CHARGES_KEY) - 1;

        if (charges <= 0) {
            if (consumeCarrierWhenEmpty) {
                carrierStack.shrink(1);
            } else {
                clear(carrierStack);
            }
            return;
        }

        root.setInteger(PayloadKeys.CHARGES_KEY, charges);
    }

    public static boolean hasPayload(ItemStack carrierStack) {
        return resolve(carrierStack) != null;
    }

    public static void clear(ItemStack carrierStack) {
        if (carrierStack.hasTagCompound()) {
            carrierStack.getTagCompound().removeTag(PayloadKeys.ROOT_TAG);
        }
    }

    public static String describe(ItemStack carrierStack) {
        GregoriusDrugworksPayloadRegistry.ResolvedPayload resolved = resolve(carrierStack);
        if (resolved == null) {
            return "empty";
        }

        PayloadDefinition definition = resolved.getDefinition();
        return "payload="
                + definition.getId()
                + " | category=" + describeCategory(definition)
                + " | charges=" + resolved.getCharges() + "/" + resolved.getMaxCharges()
                + " | compatibility=" + definition.getCompatibility().name().toLowerCase()
                + " | triggerBundle=" + (definition.getTriggerBundleId() == null ? "<legacy>" : definition.getTriggerBundleId());
    }

    private static String describeCategory(PayloadDefinition definition) {
        if (definition.getCategory() == com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCategory.STAGED_EFFECT) {
            return "psychedelic";
        }
        if (definition.getCategory() == com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCategory.COSMETIC
                && ((definition.getTriggerBundleId() != null && !definition.getTriggerBundleId().isEmpty())
                || (definition.getVisualProfileId() != null && !definition.getVisualProfileId().isEmpty()))) {
            return "psychedelic";
        }
        return definition.getCategory().name().toLowerCase();
    }

    private static NBTTagCompound getOrCreateRoot(ItemStack carrierStack) {
        if (!carrierStack.hasTagCompound()) {
            carrierStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound stackTag = carrierStack.getTagCompound();
        if (!stackTag.hasKey(PayloadKeys.ROOT_TAG)) {
            stackTag.setTag(PayloadKeys.ROOT_TAG, new NBTTagCompound());
        }

        return stackTag.getCompoundTag(PayloadKeys.ROOT_TAG);
    }
}