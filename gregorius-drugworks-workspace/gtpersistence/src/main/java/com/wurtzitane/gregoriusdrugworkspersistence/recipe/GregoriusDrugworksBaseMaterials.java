package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import com.wurtzitane.gregoriusdrugworkspersistence.worldgen.GregoriusDrugworksOreBuilder;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;

import static com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials.resLoc;
import static gregtech.api.unification.material.info.MaterialFlags.DISABLE_DECOMPOSITION;
import static gregtech.api.unification.material.info.MaterialIconSet.DULL;

public final class GregoriusDrugworksBaseMaterials {
    private GregoriusDrugworksBaseMaterials() {}

    public static void init() {
        GregoriusDrugworksMaterials.ActivatedCarbon = new Material.Builder(32227, resLoc("activated_carbon"))
                .dust()
                .color(0x202020)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 1)
                .build();

        GregoriusDrugworksMaterials.AluminiumTrichloride = new Material.Builder(32228, resLoc("aluminium_trichloride"))
                .dust()
                .color(0xF2F5E9)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Aluminium, 1, Materials.Chlorine, 3)
                .build();

        GregoriusDrugworksMaterials.CalciumHydroxide = new Material.Builder(32229, resLoc("calcium_hydroxide"))
                .dust()
                .color(0xF2F2F2)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Calcium, 1, Materials.Oxygen, 2, Materials.Hydrogen, 2)
                .build();

        GregoriusDrugworksMaterials.Fluorite = GregoriusDrugworksOreBuilder.create(32230, "fluorite")
                .dust()
                .ore(2, 1)
                .color(0xB8F0F2)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Calcium, 1, Materials.Fluorine, 2)
                .formula("CaF2", true)
                .overworldLayeredVein("gregoriusdrugworkspersistence.veins.ore.fluorite", 28, 0.22f, 18, 68, 14, 18,
                        Materials.Calcite, Materials.Apatite)
                .build();

        GregoriusDrugworksMaterials.Formaldehyde = new Material.Builder(32231, resLoc("formaldehyde"))
                .liquid()
                .color(0xEAF7FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 1, Materials.Hydrogen, 2, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.HydrogenCyanide = new Material.Builder(32232, resLoc("hydrogen_cyanide"))
                .gas()
                .color(0xD8F0FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Hydrogen, 1, Materials.Carbon, 1, Materials.Nitrogen, 1)
                .build();

        GregoriusDrugworksMaterials.HydrogenPeroxide = new Material.Builder(32233, resLoc("hydrogen_peroxide"))
                .liquid()
                .color(0xEAF8FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Hydrogen, 2, Materials.Oxygen, 2)
                .build();

        GregoriusDrugworksMaterials.PotassiumChloride = new Material.Builder(32234, resLoc("potassium_chloride"))
                .dust()
                .color(0xF3F1EC)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Potassium, 1, Materials.Chlorine, 1)
                .build();

        GregoriusDrugworksMaterials.PotassiumHydroxide = new Material.Builder(32235, resLoc("potassium_hydroxide"))
                .dust()
                .color(0xF7F7F7)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Potassium, 1, Materials.Oxygen, 1, Materials.Hydrogen, 1)
                .build();

        GregoriusDrugworksMaterials.PotassiumIodide = new Material.Builder(32236, resLoc("potassium_iodide"))
                .dust()
                .color(0xE8D6B6)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Potassium, 1, Materials.Iodine, 1)
                .build();

        GregoriusDrugworksMaterials.PotassiumCarbonate = new Material.Builder(32237, resLoc("potassium_carbonate"))
                .dust()
                .color(0xEDEDED)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Potassium, 2, Materials.Carbon, 1, Materials.Oxygen, 3)
                .build();
    }
}
