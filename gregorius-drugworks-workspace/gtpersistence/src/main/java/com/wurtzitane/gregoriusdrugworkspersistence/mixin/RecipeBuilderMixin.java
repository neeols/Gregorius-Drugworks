package com.wurtzitane.gregoriusdrugworkspersistence.mixin;

import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.ChancedInputRecipeProperty;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.ChancedInputRecipePropertyValue;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.GregoriusDrugworksChancedInputSupport;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RecipeBuilder.class, remap = false)
public class RecipeBuilderMixin {

    @Unique
    private ChancedInputRecipePropertyValue gdw$appendExistingProfile;

    @Inject(method = "applyProperty(Ljava/lang/String;Ljava/lang/Object;)Z", at = @At("HEAD"), cancellable = true, remap = false)
    private void gdw$handleChancedInputProperty(String key, Object value, CallbackInfoReturnable<Boolean> cir) {
        if (!ChancedInputRecipeProperty.KEY.equals(key)) {
            return;
        }
        if (value == null) {
            GregoriusDrugworksChancedInputSupport.setBuilderProperty((RecipeBuilder<?>) (Object) this, null);
            cir.setReturnValue(true);
            return;
        }
        if (!(value instanceof ChancedInputRecipePropertyValue)) {
            cir.setReturnValue(false);
            return;
        }
        GregoriusDrugworksChancedInputSupport.mergePropertyIntoBuilder(
                (RecipeBuilder<?>) (Object) this,
                (ChancedInputRecipePropertyValue) value);
        cir.setReturnValue(true);
    }

    @Inject(method = "append", at = @At("HEAD"), remap = false)
    private void gdw$captureExistingProfile(Recipe recipe, int multiplier, boolean multiplyDuration,
                                            CallbackInfoReturnable<?> cir) {
        gdw$appendExistingProfile = GregoriusDrugworksChancedInputSupport.getBuilderProperty(
                (RecipeBuilder<?>) (Object) this);
    }

    @Inject(method = "append", at = @At("TAIL"), remap = false)
    private void gdw$scaleChancedInputsOnAppend(Recipe recipe, int multiplier, boolean multiplyDuration,
                                                CallbackInfoReturnable<?> cir) {
        ChancedInputRecipePropertyValue fromRecipe = recipe.getProperty(ChancedInputRecipeProperty.getInstance(), null);
        ChancedInputRecipePropertyValue merged = ChancedInputRecipePropertyValue.merge(
                gdw$appendExistingProfile,
                fromRecipe == null ? null : fromRecipe.multiplied(multiplier));
        GregoriusDrugworksChancedInputSupport.setBuilderProperty((RecipeBuilder<?>) (Object) this, merged);
        gdw$appendExistingProfile = null;
    }
}
