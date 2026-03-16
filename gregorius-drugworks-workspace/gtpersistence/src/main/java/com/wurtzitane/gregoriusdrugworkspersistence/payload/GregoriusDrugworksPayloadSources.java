package com.wurtzitane.gregoriusdrugworkspersistence.payload;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadSourceCatalog;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadSourceDefinition;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;

public final class GregoriusDrugworksPayloadSources {

    private static final PayloadSourceCatalog CATALOG = new PayloadSourceCatalog();
    private static boolean bootstrapped = false;

    private GregoriusDrugworksPayloadSources() {
    }

    public static void preInit() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;

        register(new PayloadSourceDefinition(
                "gregoriusdrugworkspersistence:naloxone_autoinjector",
                "gregoriusdrugworkspersistence:naloxone_payload",
                1,
                true,
                "gregoriusdrugworkspersistence:empty_glass_ampoule"
        ));

        register(new PayloadSourceDefinition(
                "gregoriusdrugworkspersistence:flumazenil_ampoule",
                "gregoriusdrugworkspersistence:flumazenil_payload",
                1,
                true,
                "gregoriusdrugworkspersistence:empty_glass_ampoule"
        ));

        register(new PayloadSourceDefinition(
                "gregoriusdrugworkspersistence:glucagon_injector",
                "gregoriusdrugworkspersistence:glucagon_payload",
                1,
                true,
                "gregoriusdrugworkspersistence:empty_glass_ampoule"
        ));

        register(new PayloadSourceDefinition(
                "gregoriusdrugworkspersistence:kappa_reset_ampoule",
                "gregoriusdrugworkspersistence:anomaly_payload",
                1,
                true,
                "gregoriusdrugworkspersistence:empty_glass_ampoule"
        ));
    }

    public static void register(PayloadSourceDefinition definition) {
        CATALOG.register(definition);
    }

    public static Collection<PayloadSourceDefinition> all() {
        return CATALOG.all();
    }

    @Nullable
    public static PayloadSourceDefinition findBySourceItemId(String sourceItemId) {
        return CATALOG.get(sourceItemId);
    }

    @Nullable
    public static PayloadSourceDefinition findBySourceStack(ItemStack stack) {
        if (stack.isEmpty() || stack.getItem().getRegistryName() == null) {
            return null;
        }
        return findBySourceItemId(stack.getItem().getRegistryName().toString());
    }
}