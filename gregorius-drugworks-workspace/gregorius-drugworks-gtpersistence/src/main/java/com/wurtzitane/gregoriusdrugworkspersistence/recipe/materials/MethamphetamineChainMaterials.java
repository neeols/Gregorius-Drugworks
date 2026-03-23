package com.wurtzitane.gregoriusdrugworkspersistence.recipe.materials;

import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;

import static com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials.resLoc;
import static gregtech.api.unification.material.info.MaterialFlags.*;

public final class MethamphetamineChainMaterials {
    private MethamphetamineChainMaterials() {
    }

    public static void init() {
        GregoriusDrugworksMaterials.Methamphetamine = new Material.Builder(32238, resLoc("methamphetamine"))
                .liquid().dust()
                .color(0xAFFFF6)
                .flags(DISABLE_DECOMPOSITION, GENERATE_PLATE, FORCE_GENERATE_BLOCK)
                .components(Materials.Carbon, 10, Materials.Hydrogen, 15, Materials.Nitrogen, 1)
                .build().setFormula("C10H15N", true);

        GregoriusDrugworksMaterials.Chloroacetone = new Material.Builder(32239, resLoc("chloroacetone"))
                .liquid()
                .color(0xFFBF00)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 3, Materials.Hydrogen, 5, Materials.Chlorine, 1, Materials.Oxygen, 1)
                .build().setFormula("CH3COCH2Cl", true);

        GregoriusDrugworksMaterials.Methylamine = new Material.Builder(32240, resLoc("methylamine"))
                .gas()
                .color(0xFFFFFF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 1, Materials.Hydrogen, 3, Materials.Nitrogen, 1, Materials.Hydrogen, 2)
                .build().setFormula("CH3NH2", true);

        GregoriusDrugworksMaterials.Phenylacetone = new Material.Builder(32241, resLoc("phenylacetone"))
                .liquid()
                .color(0xFFFFFF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 9, Materials.Hydrogen, 10, Materials.Oxygen, 1)
                .build().setFormula("C6H5CH2COCH3", true);

        GregoriusDrugworksMaterials.LithiumChloride = new Material.Builder(32242, resLoc("lithium_chloride"))
                .dust()
                .color(0x72A38C)
                .flags()
                .components(Materials.Lithium, 1, Materials.Chlorine, 1)
                .build().setFormula("LiCl", true);
    }
}
