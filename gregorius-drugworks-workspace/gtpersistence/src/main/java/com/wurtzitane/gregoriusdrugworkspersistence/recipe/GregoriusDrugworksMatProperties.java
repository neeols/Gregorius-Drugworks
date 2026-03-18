package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import gregtech.api.fluids.FluidBuilder;
import gregtech.api.fluids.store.FluidStorageKeys;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.properties.DustProperty;
import gregtech.api.unification.material.properties.FluidProperty;
import gregtech.api.unification.material.properties.PropertyKey;

import static gregtech.api.unification.material.info.MaterialFlags.GENERATE_FOIL;
import static gregtech.api.unification.material.info.MaterialFlags.GENERATE_PLATE;

public final class GregoriusDrugworksMatProperties {
    private GregoriusDrugworksMatProperties() {}

    public static void flagChanges() {
        Materials.Niobium.addFlags(GENERATE_PLATE, GENERATE_FOIL);
    }

    public static void propertyChanges() {
        if (!Materials.Chlorine.hasProperty(PropertyKey.DUST)) {
            Materials.Chlorine.setProperty(PropertyKey.DUST, new DustProperty());
        }
        if (!Materials.Bromine.hasProperty(PropertyKey.FLUID)) {
            Materials.Bromine.setProperty(PropertyKey.FLUID,
                    new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder().temperature(332)));
        } else {
            Materials.Bromine.getProperty(PropertyKey.FLUID)
                    .enqueueRegistration(FluidStorageKeys.LIQUID, new FluidBuilder().temperature(332));
        }
        if (!Materials.SodiumHydroxide.hasProperty(PropertyKey.FLUID)) {
            Materials.SodiumHydroxide.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        } else {
            Materials.SodiumHydroxide.getProperty(PropertyKey.FLUID).enqueueRegistration(FluidStorageKeys.LIQUID, new FluidBuilder());
        }
    }

    public static void miscChanges() {}
}
