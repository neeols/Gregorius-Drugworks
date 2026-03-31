package com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance;

import net.minecraft.client.Minecraft;
import gregtech.api.recipes.recipeproperties.RecipeProperty;

public final class ChancedInputRecipeProperty extends RecipeProperty<ChancedInputRecipePropertyValue> {

    public static final String KEY = "gdw_chanced_inputs";

    private static final ChancedInputRecipeProperty INSTANCE = new ChancedInputRecipeProperty();

    private ChancedInputRecipeProperty() {
        super(KEY, ChancedInputRecipePropertyValue.class);
    }

    public static ChancedInputRecipeProperty getInstance() {
        return INSTANCE;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int x, int y, int color, Object value) {
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
