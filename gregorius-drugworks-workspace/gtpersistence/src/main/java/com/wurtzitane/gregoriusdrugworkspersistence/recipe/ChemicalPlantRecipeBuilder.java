package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import gregtech.api.GTValues;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.recipeproperties.TemperatureProperty;
import gregtech.api.util.EnumValidationResult;
import gregtech.api.util.GTLog;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nonnull;

public class ChemicalPlantRecipeBuilder extends RecipeBuilder<ChemicalPlantRecipeBuilder> {

    public ChemicalPlantRecipeBuilder() {}

    public ChemicalPlantRecipeBuilder(Recipe recipe, RecipeMap<ChemicalPlantRecipeBuilder> recipeMap) {
        super(recipe, recipeMap);
    }

    public ChemicalPlantRecipeBuilder(RecipeBuilder<ChemicalPlantRecipeBuilder> recipeBuilder) {
        super(recipeBuilder);
    }

    @Override
    public ChemicalPlantRecipeBuilder copy() {
        return new ChemicalPlantRecipeBuilder(this);
    }

    @Override
    public boolean applyProperty(@Nonnull String key, Object value) {
        if (ChemicalPlantTierProperty.KEY.equals(key)) {
            if (!(value instanceof Number)) {
                return false;
            }
            chemicalPlantTier(((Number) value).intValue());
            return true;
        }
        if (TemperatureProperty.KEY.equals(key)) {
            if (!(value instanceof Number)) {
                return false;
            }
            coilTemperature(((Number) value).intValue());
            return true;
        }
        return super.applyProperty(key, value);
    }

    public ChemicalPlantRecipeBuilder chemicalPlantTier(int tier) {
        if (tier < GTValues.MV || tier > GTValues.UV) {
            GTLog.logger.error("Chemical Plant tier must be between MV and UV. Actual: {}", tier,
                    new IllegalArgumentException());
            recipeStatus = EnumValidationResult.INVALID;
        }
        applyProperty(ChemicalPlantTierProperty.getInstance(), tier);
        return this;
    }

    public ChemicalPlantRecipeBuilder coilTemperature(int temperature) {
        if (temperature <= 0) {
            GTLog.logger.error("Chemical Plant coil temperature must be greater than 0. Actual: {}", temperature,
                    new IllegalArgumentException());
            recipeStatus = EnumValidationResult.INVALID;
        }
        applyProperty(TemperatureProperty.getInstance(), temperature);
        return this;
    }

    public int getChemicalPlantTier() {
        return recipePropertyStorage == null ? GTValues.MV :
                recipePropertyStorage.getRecipePropertyValue(ChemicalPlantTierProperty.getInstance(), GTValues.MV);
    }

    public int getCoilTemperature() {
        return recipePropertyStorage == null ? 0 :
                recipePropertyStorage.getRecipePropertyValue(TemperatureProperty.getInstance(), 0);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append(ChemicalPlantTierProperty.getInstance().getKey(), getChemicalPlantTier())
                .append(TemperatureProperty.getInstance().getKey(), getCoilTemperature())
                .toString();
    }
}
