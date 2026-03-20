package com.wurtzitane.gregoriusdrugworkspersistence.payload;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadSourceDefinition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public final class PayloadLoadingService {

    private PayloadLoadingService() {
    }

    public static boolean canLoad(ItemStack carrierStack, ItemStack sourceStack) {
        PayloadCarrierAdapter carrier = GregoriusDrugworksPayloadCarriers.find(carrierStack);
        PayloadSourceDefinition source = GregoriusDrugworksPayloadSources.findBySourceStack(sourceStack);

        if (carrier == null || source == null) {
            return false;
        }

        if (carrier.hasPayload(carrierStack)) {
            return false;
        }

        return carrier.loadPayload(carrierStack.copy(), source.getPayloadId(), source.getChargesOverride(), null);
    }

    public static ItemStack createLoadedResult(ItemStack carrierStack, ItemStack sourceStack) {
        return createLoadedResult(carrierStack, sourceStack, null);
    }

    public static ItemStack createLoadedResult(ItemStack carrierStack, ItemStack sourceStack, @Nullable NBTTagCompound extraData) {
        PayloadCarrierAdapter carrier = GregoriusDrugworksPayloadCarriers.find(carrierStack);
        PayloadSourceDefinition source = GregoriusDrugworksPayloadSources.findBySourceStack(sourceStack);

        if (carrier == null || source == null) {
            return ItemStack.EMPTY;
        }

        ItemStack result = carrierStack.copy();
        result.setCount(1);

        boolean ok = carrier.loadPayload(result, source.getPayloadId(), source.getChargesOverride(), extraData);
        return ok ? result : ItemStack.EMPTY;
    }

    public static ItemStack createLoadedResult(
            ItemStack carrierStack,
            String payloadId,
            int chargesOverride,
            @Nullable NBTTagCompound extraData
    ) {
        PayloadCarrierAdapter carrier = GregoriusDrugworksPayloadCarriers.find(carrierStack);
        if (carrier == null) {
            return ItemStack.EMPTY;
        }

        ItemStack result = carrierStack.copy();
        result.setCount(1);
        return carrier.loadPayload(result, payloadId, chargesOverride, extraData) ? result : ItemStack.EMPTY;
    }

    public static ItemStack getSourceRemainder(ItemStack sourceStack) {
        PayloadSourceDefinition source = GregoriusDrugworksPayloadSources.findBySourceStack(sourceStack);
        if (source == null) {
            return ItemStack.EMPTY;
        }

        if (!source.isConsumed()) {
            ItemStack copy = sourceStack.copy();
            copy.setCount(1);
            return copy;
        }

        if (source.getRemainderItemId() == null || source.getRemainderItemId().isEmpty()) {
            return ItemStack.EMPTY;
        }

        Item item = Item.getByNameOrId(source.getRemainderItemId());
        if (item == null) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(item);
    }
}
