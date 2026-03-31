package com.wurtzitane.gregoriusdrugworkspersistence.medical;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadDefinition;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCompatibility;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadRegistry;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoaderUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class GregoriusDrugworksApplicatorPayloads {

    private GregoriusDrugworksApplicatorPayloads() {
    }

    public static void preInit() {
        GregoriusDrugworksPayloadRegistry.preInit();
    }

    public static Collection<PayloadDefinition> allDefinitions() {
        return GregoriusDrugworksPayloadRegistry.all();
    }

    public static List<String> allPayloadIds() {
        List<String> ids = new ArrayList<>();
        for (PayloadDefinition definition : GregoriusDrugworksPayloadRegistry.all()) {
            ids.add(definition.getId());
        }
        return ids;
    }

    public static boolean loadPayloadIntoApplicator(ItemStack applicatorStack, String payloadId, int chargesOverride, @Nullable NBTTagCompound payloadData) {
        return PayloadLoaderUtil.loadIntoCarrier(
                applicatorStack,
                PayloadCompatibility.APPLICATOR,
                payloadId,
                chargesOverride,
                payloadData
        );
    }

    public static ItemStack createLoadedApplicatorStack(Item applicatorItem, String payloadId, int chargesOverride, @Nullable NBTTagCompound payloadData) {
        ItemStack stack = new ItemStack(applicatorItem);
        if (!loadPayloadIntoApplicator(stack, payloadId, chargesOverride, payloadData)) {
            throw new IllegalStateException("Unknown or incompatible applicator payload id: " + payloadId);
        }
        return stack;
    }

    public static GregoriusDrugworksPayloadRegistry.ResolvedPayload resolve(ItemStack stack) {
        return PayloadLoaderUtil.resolve(stack);
    }

    public static void decrementChargeOrClear(ItemStack applicatorStack, boolean consumeApplicatorWhenEmpty) {
        PayloadLoaderUtil.decrementChargeOrClear(applicatorStack, consumeApplicatorWhenEmpty);
    }

    public static void clearPayload(ItemStack applicatorStack) {
        PayloadLoaderUtil.clear(applicatorStack);
    }

    public static boolean hasPayload(ItemStack stack) {
        return PayloadLoaderUtil.hasPayload(stack);
    }

    public static String describeResolved(ItemStack stack) {
        return PayloadLoaderUtil.describe(stack);
    }
}