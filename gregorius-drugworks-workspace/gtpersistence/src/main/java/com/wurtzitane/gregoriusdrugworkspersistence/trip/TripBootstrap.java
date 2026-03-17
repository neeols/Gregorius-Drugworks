package com.wurtzitane.gregoriusdrugworkspersistence.trip;

import com.wurtzitane.gregoriusdrugworks.common.trip.model.EffectSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.ParticleSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripStage;

public final class TripBootstrap {

    private static boolean bootstrapped = false;

    private TripBootstrap() {
    }

    public static void registerDefaults() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;

        TripRegistrar.registerTrip(
                TripDefinition.builder("gregoriusdrugworkspersistence:salvinorin_a_pill")
                        .consumeOnUse(true)
                        .consumeAmount(1)
                        .stage(
                                TripStage.builder(0)
                                        .periodTicks(20)
                                        .message("You feel the onset.", "light_purple")
                                        .effect(new EffectSpec("minecraft:nausea", 8, 0, false))
                                        .particle(new ParticleSpec("minecraft:spell_mob", 6, 0.25D, 0.01D))
                                        .build()
                        )
                        .stage(
                                TripStage.builder(8)
                                        .periodTicks(20)
                                        .message("Reality bends.", "dark_purple")
                                        .effect(new EffectSpec("minecraft:blindness", 6, 0, true))
                                        .effect(new EffectSpec("minecraft:slowness", 6, 0, true))
                                        .particle(new ParticleSpec("minecraft:portal", 12, 0.45D, 0.03D))
                                        .build()
                        )
                        .build()
        );
    }
}