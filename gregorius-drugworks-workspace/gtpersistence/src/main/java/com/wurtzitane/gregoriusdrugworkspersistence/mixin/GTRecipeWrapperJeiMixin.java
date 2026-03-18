package com.wurtzitane.gregoriusdrugworkspersistence.mixin;

import com.wurtzitane.gregoriusdrugworkspersistence.integration.jei.GregoriusDrugworksJeiChancedInputModule;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.GregoriusDrugworksChancedInputSupport;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.integration.jei.recipe.GTRecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = GTRecipeWrapper.class, remap = false)
public abstract class GTRecipeWrapperJeiMixin implements GregoriusDrugworksJeiRecipeWrapperAccess {

    @Shadow private Recipe recipe;
    @Shadow private List<GTRecipeInput> sortedInputs;
    @Shadow private List<GTRecipeInput> sortedFluidInputs;

    @Inject(method = "addItemTooltip", at = @At("TAIL"), remap = false)
    private void gdw$addChancedItemInputTooltip(int slotIndex, boolean input, Object ingredient, List<String> tooltip,
                                                CallbackInfo ci) {
        GregoriusDrugworksJeiChancedInputModule.addItemTooltip((GTRecipeWrapper) (Object) this, slotIndex, input, tooltip);
    }

    @Inject(method = "addFluidTooltip", at = @At("TAIL"), remap = false)
    private void gdw$addChancedFluidInputTooltip(int slotIndex, boolean input, Object ingredient, List<String> tooltip,
                                                 CallbackInfo ci) {
        GregoriusDrugworksJeiChancedInputModule.addFluidTooltip((GTRecipeWrapper) (Object) this, slotIndex, input, tooltip);
    }

    @Override
    @Unique
    public com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.ChancedItemInputEntry gdw$getChancedItemInput(int slotIndex) {
        return GregoriusDrugworksChancedInputSupport.getChancedItemInputEntry(recipe, sortedInputs, slotIndex);
    }

    @Override
    @Unique
    public com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.ChancedFluidInputEntry gdw$getChancedFluidInput(int slotIndex) {
        return GregoriusDrugworksChancedInputSupport.getChancedFluidInputEntry(recipe, sortedFluidInputs, slotIndex);
    }
}
