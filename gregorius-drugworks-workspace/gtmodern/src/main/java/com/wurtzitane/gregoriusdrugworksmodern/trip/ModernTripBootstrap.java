package com.wurtzitane.gregoriusdrugworksmodern.trip;

public final class ModernTripBootstrap {

    private ModernTripBootstrap() {
    }

    public static void registerDefaults() {
        /*
         * Future registration API examples:
         *
         * TripRegistrationApi.registerTrip(
         *     TripDefinition.builder("yourmod:your_trip_item")
         *         .consumeOnUse(true)
         *         .consumeAmount(1)
         *         .stage(
         *             TripStage.builder(0)
         *                 .periodTicks(10)
         *                 .particlesPeriodTicks(20)
         *                 .particlesMinGapTicks(26)
         *                 .message("Stage one.", "light_purple")
         *                 .effect(new EffectSpec("minecraft:nausea", 12, 0, true))
         *                 .particle(new ParticleSpec("minecraft:portal", 90, 0.7D, 0.05D))
         *                 .build()
         *         )
         *         .build()
         * );
         *
         * TripRegistrationApi.registerAntidote(
         *     AntidoteDefinition.builder("yourmod:your_counter_item")
         *         .consumeOnUse(true)
         *         .consumeAmount(1)
         *         .cancelMessage("Status cleared.", "aqua")
         *         .build()
         * );
         *
         * TripRegistrationApi.allowAntidoteForTrip(
         *     "yourmod:your_counter_item",
         *     "yourmod:your_trip_item"
         * );
         *
         * Future KubeJS bridge should call the same Java API instead of duplicating logic.
         */
    }
}