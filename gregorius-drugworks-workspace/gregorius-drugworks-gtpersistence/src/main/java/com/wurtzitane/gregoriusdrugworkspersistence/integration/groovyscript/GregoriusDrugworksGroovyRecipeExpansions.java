package com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript;

import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.GregoriusDrugworksChancedInputSupport;
import gregtech.api.recipes.RecipeBuilder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public final class GregoriusDrugworksGroovyRecipeExpansions {

    public GregoriusDrugworksGroovyRecipeExpansions() {
    }

    public static <R extends RecipeBuilder<R>> R chancedItemInput(R builder, ItemStack stack, int chance,
                                                                  int chanceBoost) {
        return GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, stack, chance, chanceBoost);
    }

    public static <R extends RecipeBuilder<R>> R chancedItemInput(R builder, ItemStack stack, int chance,
                                                                  int chanceBoost, int tierReductionRate) {
        return GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, stack, chance, chanceBoost,
                tierReductionRate);
    }

    public static <R extends RecipeBuilder<R>> R chancedFluidInput(R builder, FluidStack stack, int chance,
                                                                   int chanceBoost) {
        return GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, stack, chance, chanceBoost);
    }

    public static <R extends RecipeBuilder<R>> R chancedFluidInput(R builder, FluidStack stack, int chance,
                                                                   int chanceBoost, int tierReductionRate) {
        return GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, stack, chance, chanceBoost,
                tierReductionRate);
    }

    public static <R extends RecipeBuilder<R>> R chancedCatalystItemInput(R builder, ItemStack stack, int chance,
                                                                          int chanceBoost) {
        return GregoriusDrugworksChancedInputSupport.chancedCatalystItemInput(builder, stack, chance, chanceBoost);
    }
}
