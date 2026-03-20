package com.wurtzitane.gregoriusdrugworkspersistence.payload;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GregoriusDrugworksPayloadCarriers {

    private static final List<PayloadCarrierAdapter> ADAPTERS = new ArrayList<>();
    private static boolean bootstrapped = false;

    private GregoriusDrugworksPayloadCarriers() {
    }

    public static void preInit() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;

        ADAPTERS.clear();
        ADAPTERS.add(new ApplicatorPayloadCarrierAdapter());
        ADAPTERS.add(new PillPayloadCarrierAdapter());
        ADAPTERS.add(new ConsumablePayloadCarrierAdapter());
    }

    public static List<PayloadCarrierAdapter> all() {
        return Collections.unmodifiableList(ADAPTERS);
    }

    @Nullable
    public static PayloadCarrierAdapter find(ItemStack stack) {
        for (PayloadCarrierAdapter adapter : ADAPTERS) {
            if (adapter.supports(stack)) {
                return adapter;
            }
        }
        return null;
    }
}
