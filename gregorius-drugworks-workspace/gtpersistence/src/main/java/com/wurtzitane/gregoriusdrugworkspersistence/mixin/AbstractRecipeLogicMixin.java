package com.wurtzitane.gregoriusdrugworkspersistence.mixin;

import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.GregoriusDrugworksChancedInputSupport;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.GTUtility;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractRecipeLogic.class, remap = false)
public abstract class AbstractRecipeLogicMixin {

    @Shadow(remap = false)
    public abstract RecipeMap<?> getRecipeMap();

    @Shadow(remap = false)
    protected abstract long getMaximumOverclockVoltage();

    @Shadow(remap = false)
    protected abstract int getOverclockForTier(long voltage);

    @Inject(
            method = "setupAndConsumeRecipeInputs(Lgregtech/api/recipes/Recipe;Lnet/minecraftforge/items/IItemHandlerModifiable;Lgregtech/api/capability/IMultipleTankHandler;)Z",
            at = @At("RETURN"),
            remap = false
    )
    private void gdw$applyChancedInputRolls(Recipe recipe,
                                            IItemHandlerModifiable importInventory,
                                            IMultipleTankHandler importFluids,
                                            CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) {
            return;
        }
        int recipeTier = GTUtility.getTierByVoltage(recipe.getEUt());
        int machineTier = getOverclockForTier(getMaximumOverclockVoltage());
        GregoriusDrugworksChancedInputSupport.applyChancedInputRolls(
                recipe,
                getRecipeMap(),
                recipeTier,
                machineTier,
                importInventory,
                importFluids);
    }
}
