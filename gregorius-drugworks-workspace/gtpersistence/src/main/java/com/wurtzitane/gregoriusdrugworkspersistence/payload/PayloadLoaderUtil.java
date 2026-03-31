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
                + " | mode=" + resolved.getModeId()
                + " | triggerBundle=" + (definition.getTriggerBundleId() == null ? "<legacy>" : definition.getTriggerBundleId());
    }

    public static NBTTagCompound getExtraData(ItemStack carrierStack, boolean create) {
        if (carrierStack.isEmpty()) {
            return new NBTTagCompound();
        }

        if (!carrierStack.hasTagCompound()) {
            if (!create) {
                return new NBTTagCompound();
            }
            carrierStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound root = create ? getOrCreateRoot(carrierStack) : getRoot(carrierStack);
        if (root == null) {
            return new NBTTagCompound();
        }

        if (!root.hasKey(PayloadKeys.EXTRA_DATA_KEY)) {
            if (!create) {
                return new NBTTagCompound();
            }
            root.setTag(PayloadKeys.EXTRA_DATA_KEY, new NBTTagCompound());
        }

        return root.getCompoundTag(PayloadKeys.EXTRA_DATA_KEY);
    }

    public static String getStringExtra(ItemStack carrierStack, String key) {
        NBTTagCompound extra = getExtraData(carrierStack, false);
        return extra.hasKey(key) ? extra.getString(key) : "";
    }

    public static void setStringExtra(ItemStack carrierStack, String key, String value) {
        if (carrierStack.isEmpty() || key == null || key.isEmpty()) {
            return;
        }

        NBTTagCompound extra = getExtraData(carrierStack, true);
        if (value == null || value.isEmpty()) {
            extra.removeTag(key);
            return;
        }

        extra.setString(key, value);
    }

    public static boolean getBooleanExtra(ItemStack carrierStack, String key, boolean defaultValue) {
        NBTTagCompound extra = getExtraData(carrierStack, false);
        return extra.hasKey(key) ? extra.getBoolean(key) : defaultValue;
    }

    public static void setBooleanExtra(ItemStack carrierStack, String key, boolean value) {
        if (carrierStack.isEmpty() || key == null || key.isEmpty()) {
            return;
        }

        NBTTagCompound extra = getExtraData(carrierStack, true);
        extra.setBoolean(key, value);
    }

    public static void setMode(ItemStack carrierStack, String modeId) {
        if (carrierStack.isEmpty() || modeId == null || modeId.trim().isEmpty()) {
            return;
        }

        setStringExtra(carrierStack, PayloadKeys.MODE_KEY, modeId.trim());
    }

    public static String getMode(ItemStack carrierStack) {
        GregoriusDrugworksPayloadRegistry.ResolvedPayload resolved = resolve(carrierStack);
        return resolved == null ? "" : resolved.getModeId();
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

    private static NBTTagCompound getRoot(ItemStack carrierStack) {
        if (carrierStack.isEmpty() || !carrierStack.hasTagCompound()) {
            return null;
        }

        NBTTagCompound stackTag = carrierStack.getTagCompound();
        if (!stackTag.hasKey(PayloadKeys.ROOT_TAG)) {
            return null;
        }

        return stackTag.getCompoundTag(PayloadKeys.ROOT_TAG);
    }
}
