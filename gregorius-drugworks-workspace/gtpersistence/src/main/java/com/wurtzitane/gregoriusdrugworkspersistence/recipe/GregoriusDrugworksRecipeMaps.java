package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.widgets.ProgressWidget;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import gregtech.api.recipes.builders.UniversalDistillationRecipeBuilder;
import gregtech.api.recipes.recipeproperties.TemperatureProperty;
import gregtech.core.sound.GTSoundEvents;

public final class GregoriusDrugworksRecipeMaps {
    public static final RecipeMap<ChemicalPlantRecipeBuilder> CHEMICAL_PLANT_RECIPES = new RecipeMap<>(
            "chemical_plant", 6, 6, 6, 6, new ChemicalPlantRecipeBuilder(), false)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, ProgressWidget.MoveType.HORIZONTAL)
            .setSlotOverlay(false, false, GuiTextures.BEAKER_OVERLAY_1)
            .setSound(GTSoundEvents.CHEMICAL_REACTOR);

    public static final RecipeMap<UniversalDistillationRecipeBuilder> DISTILLATION_UNIT_RECIPES =
            new RecipeMap<>(
                    "distillation_unit",
                    2,
                    6,
                    2,
                    9,
                    new UniversalDistillationRecipeBuilder().disableDistilleryRecipes(),
                    false
            )
                    .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, ProgressWidget.MoveType.HORIZONTAL)
                    .setSlotOverlay(false, false, GuiTextures.MOLECULAR_OVERLAY_1)
                    .setSlotOverlay(false, true, GuiTextures.BEAKER_OVERLAY_1)
                    .setSound(GTSoundEvents.MOTOR);

    public static final RecipeMap<SimpleRecipeBuilder> PYROLYSIS_CHAMBER_RECIPES = new RecipeMap<>(
            "pyrolysis_chamber", 3, 6, 3, 6, new SimpleRecipeBuilder(), false)
            .setProgressBar(GuiTextures.PROGRESS_BAR_CRACKING, ProgressWidget.MoveType.HORIZONTAL)
            .setSlotOverlay(false, false, GuiTextures.HEATING_OVERLAY_1)
            .setSound(GTSoundEvents.COMBUSTION);

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
