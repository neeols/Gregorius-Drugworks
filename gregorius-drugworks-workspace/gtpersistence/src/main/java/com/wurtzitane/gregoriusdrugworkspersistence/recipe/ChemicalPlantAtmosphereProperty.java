package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

public final class ChemicalPlantAtmosphereProperty extends gregtech.api.recipes.recipeproperties.RecipeProperty<FluidStack> {
    public static final String KEY = "chemical_plant_atmosphere";

    private static final ChemicalPlantAtmosphereProperty INSTANCE = new ChemicalPlantAtmosphereProperty();

    private ChemicalPlantAtmosphereProperty() {
        super(KEY, FluidStack.class);
    }

    public static ChemicalPlantAtmosphereProperty getInstance() {
        return INSTANCE;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int x, int y, int color, Object value) {
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
