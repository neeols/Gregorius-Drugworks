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

        CATALOG.register(bundle(
                "gregoriusdrugworkspersistence:salvinorin_a_bundle",
                action(TriggerActionType.FORWARD_TRIP_ITEM_USE, "gregoriusdrugworkspersistence:salvinorin_a_payload", 0)
        ));

        CATALOG.register(bundle(
                "gregoriusdrugworkspersistence:salvinorin_a_onset_bundle",
                action(TriggerActionType.START_VISUAL_PROFILE, "gregoriusdrugworkspersistence:salvinorin_a_onset", 220),
                action(TriggerActionType.PLAY_WORLD_SOUND, "minecraft:block.portal.trigger", 0)
        ));
        CATALOG.register(bundle(
                "gregoriusdrugworkspersistence:salvinorin_a_veil_lift_bundle",
                action(TriggerActionType.START_VISUAL_PROFILE, "gregoriusdrugworkspersistence:salvinorin_a_veil_lift", 320),
                action(TriggerActionType.PLAY_WORLD_SOUND, "minecraft:block.portal.travel", 0)
        ));
        CATALOG.register(bundle(
                "gregoriusdrugworkspersistence:salvinorin_a_fracture_bundle",
                action(TriggerActionType.START_VISUAL_PROFILE, "gregoriusdrugworkspersistence:salvinorin_a_fracture", 360),
                action(TriggerActionType.PLAY_WORLD_SOUND, "minecraft:block.end_gateway.spawn", 0)
        ));
        CATALOG.register(bundle(
                "gregoriusdrugworkspersistence:salvinorin_a_chrysanthemum_bundle",
                action(TriggerActionType.START_VISUAL_PROFILE, "gregoriusdrugworkspersistence:salvinorin_a_chrysanthemum", 420),
                action(TriggerActionType.PLAY_WORLD_SOUND, "minecraft:block.enchantment_table.use", 0)
        ));
        CATALOG.register(bundle(
                "gregoriusdrugworkspersistence:salvinorin_a_machine_corridor_bundle",
                action(TriggerActionType.START_VISUAL_PROFILE, "gregoriusdrugworkspersistence:salvinorin_a_machine_corridor", 520),
                action(TriggerActionType.PLAY_WORLD_SOUND, "minecraft:entity.evocation_illager.prepare_attack", 0)
        ));
        CATALOG.register(bundle(
                "gregoriusdrugworkspersistence:salvinorin_a_apex_prism_bundle",
                action(TriggerActionType.START_VISUAL_PROFILE, "gregoriusdrugworkspersistence:salvinorin_a_apex_prism", 620),
                action(TriggerActionType.PLAY_WORLD_SOUND, "minecraft:block.beacon.activate", 0)
        ));
        CATALOG.register(bundle(
                "gregoriusdrugworkspersistence:salvinorin_a_recursion_bundle",
                action(TriggerActionType.START_VISUAL_PROFILE, "gregoriusdrugworkspersistence:salvinorin_a_recursion", 620),
                action(TriggerActionType.PLAY_WORLD_SOUND, "minecraft:entity.evocation_illager.cast_spell", 0)
        ));
        CATALOG.register(bundle(
                "gregoriusdrugworkspersistence:salvinorin_a_afterimage_bundle",
                action(TriggerActionType.START_VISUAL_PROFILE, "gregoriusdrugworkspersistence:salvinorin_a_afterimage", 520),
                action(TriggerActionType.PLAY_WORLD_SOUND, "minecraft:entity.experience_orb.pickup", 0)
        ));
        CATALOG.register(bundle(
                "gregoriusdrugworkspersistence:salvinorin_a_comedown_bundle",
                action(TriggerActionType.START_VISUAL_PROFILE, "gregoriusdrugworkspersistence:salvinorin_a_comedown", 360),
                action(TriggerActionType.PLAY_WORLD_SOUND, "minecraft:block.beacon.deactivate", 0)
        ));
        CATALOG.register(bundle(
                "gregoriusdrugworkspersistence:salvinorin_a_afterglow_bundle",
                action(TriggerActionType.START_VISUAL_PROFILE, "gregoriusdrugworkspersistence:salvinorin_a_afterglow", 280),
                action(TriggerActionType.PLAY_WORLD_SOUND, "minecraft:entity.player.levelup", 0)
        ));

        CATALOG.register(bundle(
                "gregoriusdrugworkspersistence:salvinorin_antidote_bundle",
                action(TriggerActionType.FORWARD_TRIP_ITEM_USE, "gregoriusdrugworkspersistence:salvinorin_antidote_payload", 0)
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

    private static TriggerBundleDefinition bundle(String id, TriggerActionDefinition... actions) {
        return new TriggerBundleDefinition(id, Arrays.asList(actions));
    }

    private static TriggerActionDefinition action(TriggerActionType type, String primaryId, int intValue) {
        return new TriggerActionDefinition(type, primaryId, intValue);
    }
}
