package com.wurtzitane.gregoriusdrugworkspersistence.payload;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCompatibility;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public interface PayloadCarrierAdapter {

    boolean supports(ItemStack stack);

    PayloadCompatibility getCompatibility();

    boolean hasPayload(ItemStack stack);

    boolean loadPayload(ItemStack stack, String payloadId, int chargesOverride, @Nullable NBTTagCompound extraData);

    @Nullable
    GregoriusDrugworksPayloadRegistry.ResolvedPayload resolve(ItemStack stack);

    void clear(ItemStack stack);

    void decrementOrClear(ItemStack stack, boolean consumeCarrierWhenEmpty);

    String describe(ItemStack stack);
}