package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import gregtech.api.GregTechAPI;
import gregtech.api.recipes.properties.RecipeProperty;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ChemicalPlantAtmosphereProperty extends RecipeProperty<FluidStack> {
    public static final String KEY = "chemical_plant_atmosphere";

    private static ChemicalPlantAtmosphereProperty INSTANCE;

    private ChemicalPlantAtmosphereProperty() {
        super(KEY, FluidStack.class);
    }

    public static ChemicalPlantAtmosphereProperty getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChemicalPlantAtmosphereProperty();
            GregTechAPI.RECIPE_PROPERTIES.register(KEY, INSTANCE);
        }
        return INSTANCE;
    }

    @Override
    public @NotNull NBTBase serialize(@NotNull Object value) {
        return castValue(value).writeToNBT(new NBTTagCompound());
    }

    @Override
    public @NotNull Object deserialize(@NotNull NBTBase nbt) {
        return Objects.requireNonNull(FluidStack.loadFluidStackFromNBT((NBTTagCompound) nbt),
                "Failed to deserialize chemical plant atmosphere");
    }

    @Override
    public void drawInfo(Minecraft minecraft, int x, int y, int color, Object value) {
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
