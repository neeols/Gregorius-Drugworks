package com.wurtzitane.gregoriusdrugworkspersistence.recipe.materials;

import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;

import static com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials.resLoc;
import static gregtech.api.unification.material.info.MaterialFlags.DISABLE_DECOMPOSITION;
import static gregtech.api.unification.material.info.MaterialIconSet.DULL;

public final class LSDChainMaterials {
    private LSDChainMaterials() {
    }

    public static void init() {
        GregoriusDrugworksMaterials.LSD = new Material.Builder(32243, resLoc("lsd"))
                .liquid()
                .color(0xDDE6FF)
                .flags(DISABLE_DECOMPOSITION)
                //.components(Materials.Hydrogen, 1, Materials.Bromine, 1)
                .build();
    }
}
