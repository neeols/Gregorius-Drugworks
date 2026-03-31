package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.unification.material.Materials;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public final class ChemicalPlantAtmosphereHelper {

    public static final int ATMOSPHERE_SLOT_INDEX = 10_000;
    public static final int ATMOSPHERE_SLOT_X = 79;
    public static final int ATMOSPHERE_SLOT_Y = 4;
    public static final int ATMOSPHERE_LABEL_X = ATMOSPHERE_SLOT_X + 9;
    public static final int ATMOSPHERE_LABEL_Y = 1;

    private static final String PLACEHOLDER_TAG = "GdwAtmospherePlaceholder";

    private ChemicalPlantAtmosphereHelper() {
    }

    public static boolean supportsAtmosphere(@Nullable RecipeMap<?> recipeMap) {
        return recipeMap == GregoriusDrugworksRecipeMaps.CHEMICAL_PLANT_RECIPES;
    }

    public static boolean isAtmosphereGas(@Nullable FluidStack stack) {
        if (stack == null || stack.getFluid() == null) {
            return false;
        }
        return matches(stack, Materials.Air.getFluid(1)) ||
                matches(stack, Materials.Nitrogen.getFluid(1)) ||
                matches(stack, Materials.Oxygen.getFluid(1)) ||
                matches(stack, Materials.Argon.getFluid(1)) ||
                matches(stack, Materials.Helium.getFluid(1)) ||
                matches(stack, Materials.Neon.getFluid(1)) ||
                matches(stack, Materials.Krypton.getFluid(1)) ||
                matches(stack, Materials.Xenon.getFluid(1)) ||
                matches(stack, Materials.Radon.getFluid(1)) ||
                matches(stack, Materials.CarbonDioxide.getFluid(1));
    }

    public static boolean matches(@Nullable FluidStack left, @Nullable FluidStack right) {
        return left != null && right != null && left.isFluidEqual(right);
    }

    @Nullable
    public static FluidStack getAtmosphere(@Nullable Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        FluidStack atmosphere = recipe.getProperty(ChemicalPlantAtmosphereProperty.getInstance(), null);
        return atmosphere == null ? null : atmosphere.copy();
    }

    @Nullable
    public static FluidStack getDisplayedAtmosphere(@Nullable Recipe recipe) {
        FluidStack atmosphere = getAtmosphere(recipe);
        if (atmosphere != null) {
            return atmosphere;
        }
        FluidStack air = Materials.Air.getFluid(250);
        if (air == null) {
            return null;
        }
        FluidStack placeholder = air.copy();
        if (placeholder.tag == null) {
            placeholder.tag = new NBTTagCompound();
        }
        placeholder.tag.setBoolean(PLACEHOLDER_TAG, true);
        return placeholder;
    }

    public static boolean isAirPlaceholder(@Nullable FluidStack stack) {
        return stack != null && stack.tag != null && stack.tag.getBoolean(PLACEHOLDER_TAG);
    }
}
