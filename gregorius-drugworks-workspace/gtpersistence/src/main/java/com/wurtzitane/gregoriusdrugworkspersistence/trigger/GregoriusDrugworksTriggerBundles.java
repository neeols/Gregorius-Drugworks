package com.wurtzitane.gregoriusdrugworkspersistence.trigger;

import com.wurtzitane.gregoriusdrugworks.common.trigger.TriggerActionDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trigger.TriggerActionType;
import com.wurtzitane.gregoriusdrugworks.common.trigger.TriggerBundleCatalog;
import com.wurtzitane.gregoriusdrugworks.common.trigger.TriggerBundleDefinition;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;

public final class GregoriusDrugworksTriggerBundles {

    private static final TriggerBundleCatalog CATALOG = new TriggerBundleCatalog();
    private static boolean bootstrapped = false;

    private GregoriusDrugworksTriggerBundles() {
    }

    public static void preInit() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;

        CATALOG.register(new TriggerBundleDefinition(
                "gregoriusdrugworkspersistence:naloxone_bundle",
                Arrays.asList(
                        new TriggerActionDefinition(
                                TriggerActionType.FORWARD_TRIP_ITEM_USE,
                                "gregoriusdrugworkspersistence:naloxone_autoinjector",
                                0
                        )
                )
        ));

        CATALOG.register(new TriggerBundleDefinition(
                "gregoriusdrugworkspersistence:flumazenil_bundle",
                Arrays.asList(
                        new TriggerActionDefinition(
                                TriggerActionType.FORWARD_TRIP_ITEM_USE,
                                "gregoriusdrugworkspersistence:flumazenil_ampoule",
                                0
                        )
                )
        ));

        CATALOG.register(new TriggerBundleDefinition(
                "gregoriusdrugworkspersistence:glucagon_bundle",
                Arrays.asList(
                        new TriggerActionDefinition(
                                TriggerActionType.FORWARD_TRIP_ITEM_USE,
                                "gregoriusdrugworkspersistence:glucagon_injector",
                                0
                        )
                )
        ));

        CATALOG.register(new TriggerBundleDefinition(
                "gregoriusdrugworkspersistence:anomaly_bundle",
                Arrays.asList(
                        new TriggerActionDefinition(
                                TriggerActionType.START_VISUAL_PROFILE,
                                "gregoriusdrugworkspersistence:anomaly_rainbow",
                                200
                        )
                )
        ));
    }

    public static void register(TriggerBundleDefinition definition) {
        CATALOG.register(definition);
    }

    @Nullable
    public static TriggerBundleDefinition get(String id) {
        return CATALOG.get(id);
    }

    public static Collection<TriggerBundleDefinition> all() {
        return CATALOG.all();
    }
}
