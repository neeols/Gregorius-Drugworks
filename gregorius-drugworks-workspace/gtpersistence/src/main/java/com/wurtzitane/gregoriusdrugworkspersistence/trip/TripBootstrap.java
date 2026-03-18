package com.wurtzitane.gregoriusdrugworkspersistence.trip;

import com.wurtzitane.gregoriusdrugworks.common.trip.model.AntidoteDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.EffectSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.ParticleSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.SoundSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripStage;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripStageTriggerSpec;

public final class TripBootstrap {

    private static boolean bootstrapped = false;

    private TripBootstrap() {
    }

    public static void registerDefaults() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;

        TripRegistrar.registerTrip(buildSalvinorinTrip("gregoriusdrugworkspersistence:salvinorin_a_pill"));
        TripRegistrar.registerTrip(buildSalvinorinTrip("gregoriusdrugworkspersistence:salvinorin_a_payload"));

        TripRegistrar.registerAntidote(
                AntidoteDefinition.builder("gregoriusdrugworkspersistence:salvinorin_antidote_payload")
                        .consumeOnUse(false)
                        .cancelMessage("Counter-item applied - status cleared.", "aqua")
                        .sound(new SoundSpec("gregoriusdrugworkspersistence:antidote_inject", 1.0F, 1.0F))
                        .particles(new ParticleSpec("minecraft:cloud", 12, 0.22D, 0.01D))
                        .build()
        );

        TripRegistrar.allowAntidoteForTrip(
                "gregoriusdrugworkspersistence:salvinorin_antidote_payload",
                "gregoriusdrugworkspersistence:salvinorin_a_pill"
        );
        TripRegistrar.allowAntidoteForTrip(
                "gregoriusdrugworkspersistence:salvinorin_antidote_payload",
                "gregoriusdrugworkspersistence:salvinorin_a_payload"
        );
    }

    private static TripDefinition buildSalvinorinTrip(String itemId) {
        return TripDefinition.builder(itemId)
                .consumeOnUse(false)
                .consumeAmount(1)
                .stage(stage(
                        0,
                        10,
                        20,
                        26,
                        "Come-up... the world starts bending.",
                        "light_purple",
                        particle("minecraft:portal", 90, 0.72D, 0.05D),
                        onEnter("gregoriusdrugworkspersistence:salvinorin_a_onset_bundle"),
                        effect("minecraft:nausea", 14, 0, true),
                        effect("minecraft:night_vision", 18, 0, true)
                ))
                .stage(stage(
                        10,
                        10,
                        16,
                        22,
                        "Veil lift. The edges of everything peel into stacked copies.",
                        "dark_purple",
                        particle("minecraft:enchant", 120, 0.85D, 0.02D),
                        onEnter("gregoriusdrugworkspersistence:salvinorin_a_veil_lift_bundle"),
                        effect("minecraft:nausea", 16, 0, true),
                        effect("minecraft:night_vision", 20, 0, true),
                        effect("minecraft:blindness", 10, 0, true)
                ))
                .stage(stage(
                        24,
                        10,
                        14,
                        20,
                        "Fracture... surfaces split into rotating panes.",
                        "dark_aqua",
                        particle("minecraft:spell_mob_ambient", 130, 0.90D, 0.03D),
                        onEnter("gregoriusdrugworkspersistence:salvinorin_a_fracture_bundle"),
                        effect("minecraft:nausea", 18, 1, true),
                        effect("minecraft:night_vision", 4, 0, true),
                        effect("minecraft:slowness", 12, 0, true),
                        effect("minecraft:glowing", 10, 0, true)
                ))
                .stage(stage(
                        42,
                        10,
                        14,
                        20,
                        "Chrysanthemum... concentric petals bloom behind your eyes.",
                        "dark_aqua",
                        particle("minecraft:fireworks_spark", 140, 0.96D, 0.015D),
                        onEnter("gregoriusdrugworkspersistence:salvinorin_a_chrysanthemum_bundle"),
                        effect("minecraft:nausea", 20, 1, true),
                        effect("minecraft:night_vision", 24, 0, true),
                        effect("minecraft:mining_fatigue", 14, 0, true),
                        effect("minecraft:weakness", 8, 0, true)
                ))
                .stage(stage(
                        66,
                        12,
                        18,
                        26,
                        "Machine corridor... distance stretches into impossible architecture.",
                        "aqua",
                        particle("minecraft:end_rod", 125, 0.88D, 0.01D),
                        onEnter("gregoriusdrugworkspersistence:salvinorin_a_machine_corridor_bundle"),
                        effect("minecraft:nausea", 20, 1, true),
                        effect("minecraft:night_vision", 24, 0, true),
                        effect("minecraft:slowness", 14, 0, true),
                        effect("minecraft:mining_fatigue", 18, 0, true),
                        effect("minecraft:weakness", 12, 0, true)
                ))
                .stage(stage(
                        96,
                        10,
                        12,
                        18,
                        "Apex... colour shears into a living prism.",
                        "light_purple",
                        particle("minecraft:dragon_breath", 150, 1.05D, 0.02D),
                        onEnter("gregoriusdrugworkspersistence:salvinorin_a_apex_prism_bundle"),
                        effect("minecraft:nausea", 22, 1, true),
                        effect("minecraft:night_vision", 28, 0, true),
                        effect("minecraft:blindness", 5, 0, true),
                        effect("minecraft:slowness", 16, 0, true),
                        effect("minecraft:glowing", 16, 0, true)
                ))
                .stage(stage(
                        132,
                        12,
                        16,
                        24,
                        "Recursion... every movement trails a second version of itself.",
                        "aqua",
                        particle("minecraft:spell_mob", 145, 0.92D, 0.03D),
                        onEnter("gregoriusdrugworkspersistence:salvinorin_a_recursion_bundle"),
                        effect("minecraft:nausea", 18, 1, true),
                        effect("minecraft:night_vision", 24, 0, true),
                        effect("minecraft:mining_fatigue", 18, 0, true),
                        effect("minecraft:weakness", 14, 0, true)
                ))
                .stage(stage(
                        168,
                        12,
                        22,
                        30,
                        "After-image... the trip hangs behind reality in translucent frames.",
                        "green",
                        particle("minecraft:cloud", 110, 0.82D, 0.02D),
                        onEnter("gregoriusdrugworkspersistence:salvinorin_a_afterimage_bundle"),
                        effect("minecraft:nausea", 12, 0, true),
                        effect("minecraft:night_vision", 18, 0, true),
                        effect("minecraft:slowness", 10, 0, true)
                ))
                .stage(stage(
                        204,
                        16,
                        28,
                        36,
                        "Comedown....",
                        "gray",
                        particle("minecraft:happy_villager", 85, 0.74D, 0.02D),
                        onEnter("gregoriusdrugworkspersistence:salvinorin_a_comedown_bundle"),
                        effect("minecraft:nausea", 8, 0, true),
                        effect("minecraft:weakness", 6, 0, true)
                ))
                .stage(stage(
                        232,
                        20,
                        34,
                        40,
                        "Afterglow... slow waves settle back into the body.",
                        "green",
                        particle("minecraft:town_aura", 70, 0.68D, 0.01D),
                        onEnter("gregoriusdrugworkspersistence:salvinorin_a_afterglow_bundle"),
                        effect("minecraft:night_vision", 10, 0, true)
                ))
                .build();
    }

    private static TripStage stage(
            int atSeconds,
            int periodTicks,
            int particlesPeriodTicks,
            int particlesMinGapTicks,
            String message,
            String messageColor,
            ParticleSpec particle,
            TripStageTriggerSpec triggerSpec,
            EffectSpec... effects
    ) {
        TripStage.Builder builder = TripStage.builder(atSeconds)
                .periodTicks(periodTicks)
                .particlesPeriodTicks(particlesPeriodTicks)
                .particlesMinGapTicks(particlesMinGapTicks)
                .message(message, messageColor)
                .particle(particle)
                .triggerSpec(triggerSpec);

        for (EffectSpec effect : effects) {
            builder.effect(effect);
        }

        return builder.build();
    }

    private static EffectSpec effect(String id, int seconds, int amplifier, boolean hideParticles) {
        return new EffectSpec(id, seconds, amplifier, hideParticles);
    }

    private static ParticleSpec particle(String id, int count, double spread, double speed) {
        return new ParticleSpec(id, count, spread, speed);
    }

    private static TripStageTriggerSpec onEnter(String triggerBundleId) {
        return TripStageTriggerSpec.builder()
                .onEnter(triggerBundleId)
                .build();
    }
}
