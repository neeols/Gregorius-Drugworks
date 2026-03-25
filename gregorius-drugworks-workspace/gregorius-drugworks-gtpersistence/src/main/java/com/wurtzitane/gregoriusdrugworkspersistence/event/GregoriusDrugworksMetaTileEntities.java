package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.machine.MetaTileEntityBlotterPrinter;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.block.machines.MachineItemBlock;
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

        GregoriusDrugworksCreativeTabs.preInit();
        ensureRegistry();
        MachineItemBlock.addCreativeTab(GregoriusDrugworksCreativeTabs.MAIN);
        MachineItemBlock.addCreativeTab(GregoriusDrugworksCreativeTabs.INDUSTRIAL);

        CHEMICAL_PLANT = gregtech.common.metatileentities.MetaTileEntities.CHEMICAL_PLANT;
        DISTILLATION_UNIT = gregtech.common.metatileentities.MetaTileEntities.DISTILLATION_UNIT;
        PYROLYSIS_CHAMBER = gregtech.common.metatileentities.MetaTileEntities.PYROLYSIS_CHAMBER;
        addAddonCreativeTabs(CHEMICAL_PLANT);
        addAddonCreativeTabs(DISTILLATION_UNIT);
        addAddonCreativeTabs(PYROLYSIS_CHAMBER);
        BLOTTER_PRINTER = gregtech.common.metatileentities.MetaTileEntities.registerMetaTileEntity(11003,
                new MetaTileEntityBlotterPrinter(id("blotter_printer")));
        addAddonCreativeTabs(BLOTTER_PRINTER);
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(Tags.MOD_ID, path);
    }

    private static void ensureRegistry() {
        try {
            GregTechAPI.mteManager.getRegistry(Tags.MOD_ID);
        } catch (IllegalArgumentException ignored) {
            GregTechAPI.mteManager.createRegistry(Tags.MOD_ID);
        }
    }

    private static void addAddonCreativeTabs(MetaTileEntity metaTileEntity) {
        if (metaTileEntity == null) {
            return;
        }
        metaTileEntity.addAdditionalCreativeTabs(GregoriusDrugworksCreativeTabs.MAIN);
        metaTileEntity.addAdditionalCreativeTabs(GregoriusDrugworksCreativeTabs.INDUSTRIAL);
    }
}
