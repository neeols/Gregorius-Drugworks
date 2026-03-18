package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import gregtech.api.GTValues;
import gregtech.api.recipes.recipeproperties.RecipeProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.util.List;

public final class ChemicalPlantTierProperty extends RecipeProperty<Integer> {
    public static final String KEY = "chemical_plant_tier";

    private static final ChemicalPlantTierProperty INSTANCE = new ChemicalPlantTierProperty();

    private ChemicalPlantTierProperty() {
        super(KEY, Integer.class);
    }

    public static ChemicalPlantTierProperty getInstance() {
        return INSTANCE;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int x, int y, int color, Object value) {
        minecraft.fontRenderer.drawString(
                I18n.format("gregoriusdrugworkspersistence.recipe.chemical_plant_tier",
                        getTierName(castValue(value))),
                x,
                y,
                color);
    }

    @Override
    public void getTooltipStrings(List<String> tooltip, int mouseX, int mouseY, Object value) {
        tooltip.add(I18n.format("gregoriusdrugworkspersistence.recipe.chemical_plant_tier.tooltip"));
    }

    private static String getTierName(int tier) {
        return tier >= 0 && tier < GTValues.VN.length ? GTValues.VN[tier] : Integer.toString(tier);
    }
}
