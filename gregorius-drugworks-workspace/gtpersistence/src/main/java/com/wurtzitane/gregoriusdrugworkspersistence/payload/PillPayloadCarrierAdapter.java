package com.wurtzitane.gregoriusdrugworkspersistence.payload;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCompatibility;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.ItemPayloadPill;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * Payload carrier bridge for the generic pill item.
 *
 * @author wurtzitane
 */
public final class PillPayloadCarrierAdapter implements PayloadCarrierAdapter {

    @Override
    public boolean supports(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemPayloadPill;
    }

    @Override
    public PayloadCompatibility getCompatibility() {
        return PayloadCompatibility.PILL;
    }

    @Override
    public boolean hasPayload(ItemStack stack) {
        return PayloadLoaderUtil.hasPayload(stack);
    }

    @Override
    public boolean loadPayload(ItemStack stack, String payloadId, int chargesOverride, @Nullable NBTTagCompound extraData) {
        return PayloadLoaderUtil.loadIntoCarrier(
                stack,
                getCompatibility(),
                payloadId,
                1,
                extraData
        );
    }

    @Nullable
    @Override
    public GregoriusDrugworksPayloadRegistry.ResolvedPayload resolve(ItemStack stack) {
        return PayloadLoaderUtil.resolve(stack);
    }

    @Override
    public void clear(ItemStack stack) {
        PayloadLoaderUtil.clear(stack);
    }

    @Override
    public void decrementOrClear(ItemStack stack, boolean consumeCarrierWhenEmpty) {
        PayloadLoaderUtil.decrementChargeOrClear(stack, consumeCarrierWhenEmpty);
    }

    @Override
    public String describe(ItemStack stack) {
        return PayloadLoaderUtil.describe(stack);
    }
}
