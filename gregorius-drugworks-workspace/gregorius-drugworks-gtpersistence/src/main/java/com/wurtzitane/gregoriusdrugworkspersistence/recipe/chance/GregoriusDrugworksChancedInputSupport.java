package com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance;

import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.chance.input.ChancedInputSupport;
import gregtech.api.recipes.ingredients.GTRecipeInput;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;

public final class GregoriusDrugworksChancedInputSupport {

    public static final int DEFAULT_CATALYST_TIER_REDUCTION_RATE =
            ChancedInputSupport.DEFAULT_CATALYST_TIER_REDUCTION_RATE;

    private GregoriusDrugworksChancedInputSupport() {}

    public static <R extends RecipeBuilder<R>> R chancedItemInput(R builder, ItemStack stack, int chance,
                                                                  int chanceBoost) {
        return builder.chancedItemInput(stack, chance, chanceBoost);
    }

    public static <R extends RecipeBuilder<R>> R chancedItemInput(R builder, ItemStack stack, int chance,
                                                                  int chanceBoost, int tierReductionRate) {
        return builder.chancedItemInput(stack, chance, chanceBoost, tierReductionRate);
    }

    public static <R extends RecipeBuilder<R>> R chancedFluidInput(R builder, FluidStack stack, int chance,
                                                                   int chanceBoost) {
        return builder.chancedFluidInput(stack, chance, chanceBoost);
    }

    public static <R extends RecipeBuilder<R>> R chancedFluidInput(R builder, FluidStack stack, int chance,
                                                                   int chanceBoost, int tierReductionRate) {
        return builder.chancedFluidInput(stack, chance, chanceBoost, tierReductionRate);
    }

    public static <R extends RecipeBuilder<R>> R chancedCatalystItemInput(R builder, ItemStack stack, int chance,
                                                                           int chanceBoost) {
        return builder.chancedCatalystItemInput(stack, chance, chanceBoost);
    }

    public static boolean matchesAndConsumeRecipeInputs(Recipe recipe, RecipeMap<?> recipeMap, int machineTier,
                                                        boolean consumeIfSuccessful,
                                                        IItemHandlerModifiable inputInventory,
                                                        IMultipleTankHandler inputFluids) {
        return ChancedInputSupport.matchesAndConsumeRecipeInputs(recipe, recipeMap, machineTier,
                consumeIfSuccessful, inputInventory, inputFluids);
    }

    public static gregtech.api.recipes.chance.input.ChancedItemInputEntry getChancedItemInputEntry(Recipe recipe,
                                                                                                     List<GTRecipeInput> sortedInputs,
                                                                                                     int slotIndex) {
        return ChancedInputSupport.getChancedItemInputEntry(recipe, sortedInputs, slotIndex);
    }

    public static gregtech.api.recipes.chance.input.ChancedFluidInputEntry getChancedFluidInputEntry(Recipe recipe,
                                                                                                       List<GTRecipeInput> sortedFluidInputs,
                                                                                                       int slotIndex) {
        return ChancedInputSupport.getChancedFluidInputEntry(recipe, sortedFluidInputs, slotIndex);
    }

    public static int getTierReducedChance(int baseChance, int recipeTier, int machineTier, int tierReductionRate) {
        return ChancedInputSupport.getTierReducedChance(baseChance, recipeTier, machineTier, tierReductionRate);
    }

    public static double getTierReductionPerTierDisplay(int baseChance, int tierReductionRate) {
        return ChancedInputSupport.getTierReductionPerTierDisplay(baseChance, tierReductionRate);
    }
}
