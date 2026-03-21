package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import com.wurtzitane.gregoriusdrugworkspersistence.recipe.recipes.LSDRecipes;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.recipes.SalvinorinARecipes;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.recipes.MethamphetamineRecipes;

public final class GregoriusDrugworksRecipeHandler {
    private GregoriusDrugworksRecipeHandler() {}

    public static void init() {
        MultiblockCasingsRecipes.init();
        BlotterRecipes.init();
        SalvinorinARecipes.init();
        MethamphetamineRecipes.init();
        LSDRecipes.init();
    }
}
