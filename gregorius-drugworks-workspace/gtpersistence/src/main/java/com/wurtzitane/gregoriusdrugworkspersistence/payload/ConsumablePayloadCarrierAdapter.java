package com.wurtzitane.gregoriusdrugworkspersistence.payload;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCompatibility;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.ItemInhalationConsumable;
import com.wurtzitane.gregoriusdrugworkspersistence.medical.ItemMedicalApplicator;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.ItemPillBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * Generic payload support for normal consumables and other standard use-finish items.
 *
 * @author wurtzitane
 */
public final class ConsumablePayloadCarrierAdapter implements PayloadCarrierAdapter {

    @Override
    public boolean supports(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        Item item = stack.getItem();
        if (item instanceof ItemMedicalApplicator || item instanceof ItemPillBase || item instanceof ItemInhalationConsumable) {
            return false;
        }

        return item instanceof ItemFood || item.getMaxItemUseDuration(stack) > 0;
    }

    @Override
    public PayloadCompatibility getCompatibility() {
        return PayloadCompatibility.UNIVERSAL;
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
