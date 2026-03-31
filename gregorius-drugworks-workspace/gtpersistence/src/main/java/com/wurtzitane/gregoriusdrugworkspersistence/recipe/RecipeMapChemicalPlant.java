package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import com.wurtzitane.gregoriusdrugworkspersistence.integration.jei.ChemicalPlantAtmosphereTankWidget;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.gui.ModularUI;
import gregtech.api.recipes.RecipeMap;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;

public class RecipeMapChemicalPlant extends RecipeMap<ChemicalPlantRecipeBuilder> {

    public RecipeMapChemicalPlant(String unlocalizedName, int maxInputs, int maxOutputs, int maxFluidInputs,
                                  int maxFluidOutputs, ChemicalPlantRecipeBuilder defaultRecipeBuilder,
                                  boolean isHidden) {
        super(unlocalizedName, maxInputs, maxOutputs, maxFluidInputs, maxFluidOutputs, defaultRecipeBuilder, isHidden);
    }

    @Override
    public ModularUI.Builder createJeiUITemplate(IItemHandlerModifiable importItems, IItemHandlerModifiable exportItems,
                                                 FluidTankList importFluids, FluidTankList exportFluids, int yOffset) {
        ModularUI.Builder builder = super.createJeiUITemplate(importItems, exportItems, importFluids, exportFluids,
                yOffset);
        builder.widget(new ChemicalPlantAtmosphereTankWidget(new FluidTank(1000),
                ChemicalPlantAtmosphereHelper.ATMOSPHERE_SLOT_X,
                ChemicalPlantAtmosphereHelper.ATMOSPHERE_SLOT_Y + yOffset,
                18, 18)
                        .setAlwaysShowFull(true)
                        .setBackgroundTexture(getOverlaysForSlot(false, true, false)));
        return builder;
    }
}
