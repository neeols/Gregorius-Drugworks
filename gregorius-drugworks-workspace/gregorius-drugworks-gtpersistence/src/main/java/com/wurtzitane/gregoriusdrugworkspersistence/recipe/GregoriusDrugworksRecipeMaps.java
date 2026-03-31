package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.builders.ChemicalPlantRecipeBuilder;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import gregtech.api.recipes.builders.UniversalDistillationRecipeBuilder;
import gregtech.api.recipes.properties.impl.TemperatureProperty;

public final class GregoriusDrugworksRecipeMaps {
    public static final RecipeMap<ChemicalPlantRecipeBuilder> CHEMICAL_PLANT_RECIPES = RecipeMaps.CHEMICAL_PLANT_RECIPES;
    public static final RecipeMap<UniversalDistillationRecipeBuilder> DISTILLATION_UNIT_RECIPES =
            RecipeMaps.DISTILLATION_UNIT_RECIPES;
    public static final RecipeMap<SimpleRecipeBuilder> PYROLYSIS_CHAMBER_RECIPES = RecipeMaps.PYROLYSIS_CHAMBER_RECIPES;

    private GregoriusDrugworksRecipeMaps() {
    }

    public static void preInit() {
    }

    public static UniversalDistillationRecipeBuilder distillationUnitBuilder() {
        return DISTILLATION_UNIT_RECIPES.recipeBuilder().disableDistilleryRecipes();
    }

    public static UniversalDistillationRecipeBuilder distillationUnitBuilder(int minimumTemperature) {
        UniversalDistillationRecipeBuilder builder = distillationUnitBuilder();
        if (minimumTemperature > 0) {
            builder.applyProperty(TemperatureProperty.getInstance(), minimumTemperature);
        }
        return builder;
    }
}
