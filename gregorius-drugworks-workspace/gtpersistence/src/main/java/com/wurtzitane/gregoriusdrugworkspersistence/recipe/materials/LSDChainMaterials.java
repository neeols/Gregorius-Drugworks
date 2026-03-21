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
                .liquid().dust()
                .color(0xF0EADA)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 20, Materials.Hydrogen, 25, Materials.Nitrogen, 3, Materials.Oxygen, 1)
                .build();

        // bromine
        GregoriusDrugworksMaterials.AcidifiedSaltWater = new Material.Builder(32244, resLoc("acidified_salt_water"))
                .liquid()
                .color(0x495A80)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.SulfuricAcid, 1, Materials.SaltWater, 1)
                .build();
        GregoriusDrugworksMaterials.TreatedSaltWater = new Material.Builder(32245, resLoc("treated_salt_water"))
                .liquid()
                .color(0x466B87)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Salt, 1, Materials.Water, 1, Materials.Sulfur, 1)
                .build();
        GregoriusDrugworksMaterials.BromineVapor = new Material.Builder(32246, resLoc("bromine_vapor"))
                .gas()
                .color(0x466B87)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Bromine, 1)
                .build();
        GregoriusDrugworksMaterials.HydrobromicAcid = new Material.Builder(32247, resLoc("hydrobromic_acid"))
                .liquid()
                .color(0xD6D6D6)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Hydrogen, 1, Materials.Bromine, 1)
                .build();
        GregoriusDrugworksMaterials.Material4Nitrotoluene = new Material.Builder(32248, resLoc("nitrotoluene"))
                .liquid()
                .color(0xE1FF00)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 7, Materials.Hydrogen, 7, Materials.Nitrogen, 1, Materials.Oxygen, 2)
                .build();
        GregoriusDrugworksMaterials.Material2Bromo6Nitrotoluene = new Material.Builder(32249, resLoc("nitrotoluene"))
                .liquid()
                .color(0xE1FF00)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 7, Materials.Hydrogen, 7, Materials.Nitrogen, 1, Materials.Oxygen, 2)
                .build();
    }
}
