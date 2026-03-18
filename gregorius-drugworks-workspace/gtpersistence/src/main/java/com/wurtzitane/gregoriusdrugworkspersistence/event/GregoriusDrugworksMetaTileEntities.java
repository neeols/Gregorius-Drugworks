package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.machine.MetaTileEntityChemicalPlant;
import com.wurtzitane.gregoriusdrugworkspersistence.machine.MetaTileEntityDistillationUnit;
import com.wurtzitane.gregoriusdrugworkspersistence.machine.MetaTileEntityPyrolysisChamber;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.util.ResourceLocation;

public final class GregoriusDrugworksMetaTileEntities {

    public static MetaTileEntity CHEMICAL_PLANT;
    public static MetaTileEntity DISTILLATION_UNIT;
    public static MetaTileEntity PYROLYSIS_CHAMBER;

    private static boolean bootstrapped;

    private GregoriusDrugworksMetaTileEntities() {
    }

    public static void preInit() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;

        CHEMICAL_PLANT = MetaTileEntities.registerMetaTileEntity(11000,
                new MetaTileEntityChemicalPlant(id("chemical_plant")));
        DISTILLATION_UNIT = MetaTileEntities.registerMetaTileEntity(11001,
                new MetaTileEntityDistillationUnit(id("distillation_unit")));
        PYROLYSIS_CHAMBER = MetaTileEntities.registerMetaTileEntity(11002,
                new MetaTileEntityPyrolysisChamber(id("pyrolysis_chamber")));
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(com.wurtzitane.gregoriusdrugworkspersistence.Tags.MOD_ID, path);
    }
}
