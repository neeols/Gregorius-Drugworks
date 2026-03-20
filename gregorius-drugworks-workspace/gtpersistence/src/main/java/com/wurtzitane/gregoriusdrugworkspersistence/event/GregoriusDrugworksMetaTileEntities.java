package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.machine.MetaTileEntityChemicalPlant;
import com.wurtzitane.gregoriusdrugworkspersistence.machine.MetaTileEntityBlotterPrinter;
import com.wurtzitane.gregoriusdrugworkspersistence.machine.MetaTileEntityDistillationUnit;
import com.wurtzitane.gregoriusdrugworkspersistence.machine.MetaTileEntityPyrolysisChamber;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.block.machines.MachineItemBlock;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.util.ResourceLocation;

public final class GregoriusDrugworksMetaTileEntities {

    public static MetaTileEntity CHEMICAL_PLANT;
    public static MetaTileEntity DISTILLATION_UNIT;
    public static MetaTileEntity PYROLYSIS_CHAMBER;
    public static MetaTileEntity BLOTTER_PRINTER;

    private static boolean bootstrapped;

    private GregoriusDrugworksMetaTileEntities() {
    }

    public static void preInit() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;

        MachineItemBlock.addCreativeTab(GregoriusDrugworksCreativeTabs.MAIN);

        CHEMICAL_PLANT = MetaTileEntities.registerMetaTileEntity(11000,
                new MetaTileEntityChemicalPlant(id("chemical_plant")));
        DISTILLATION_UNIT = MetaTileEntities.registerMetaTileEntity(11001,
                new MetaTileEntityDistillationUnit(id("distillation_unit")));
        PYROLYSIS_CHAMBER = MetaTileEntities.registerMetaTileEntity(11002,
                new MetaTileEntityPyrolysisChamber(id("pyrolysis_chamber")));
        BLOTTER_PRINTER = MetaTileEntities.registerMetaTileEntity(11003,
                new MetaTileEntityBlotterPrinter(id("blotter_printer")));
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(com.wurtzitane.gregoriusdrugworkspersistence.Tags.MOD_ID, path);
    }
}
