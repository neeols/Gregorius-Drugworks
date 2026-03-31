package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksItems;
import net.minecraft.item.Item;
import gregtech.api.GTValues;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.ingredients.nbtmatch.NBTCondition;
import gregtech.api.recipes.ingredients.nbtmatch.NBTMatcher;

/**
 * GregTech recipe integration for blotter items.
 *
 * @author wurtzitane
 */
public final class BlotterRecipes {

    private BlotterRecipes() {
    }

    public static void init() {
        RecipeMaps.CUTTER_RECIPES.recipeBuilder()
                .inputNBT(GregoriusDrugworksItems.BLOTTER_PAPER, NBTMatcher.ANY, NBTCondition.ANY)
                .output(GregoriusDrugworksItems.SINGLE_TAB, 64)
                .duration(40)
                .EUt(GTValues.VA[GTValues.LV])
                .buildAndRegister();

        registerLsdSoakRecipe(GregoriusDrugworksItems.BLOTTER_PAPER, 64);
        registerLsdSoakRecipe(GregoriusDrugworksItems.SINGLE_TAB, 1);
    }

    private static void registerLsdSoakRecipe(Item carrierItem, int lsdAmount) {
        RecipeMaps.CHEMICAL_BATH_RECIPES.recipeBuilder()
                .inputNBT(carrierItem, NBTMatcher.ANY, NBTCondition.ANY)
                .fluidInputs(GregoriusDrugworksMaterials.LSD.getFluid(lsdAmount))
                .output(carrierItem)
                .duration(40)
                .EUt(GTValues.VA[GTValues.LV])
                .buildAndRegister();
    }
}
