package com.wurtzitane.gregoriusdrugworkspersistence.mixin;

import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.GregoriusDrugworksChancedInputSupport;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = AbstractRecipeLogic.class, remap = false)
public abstract class AbstractRecipeLogicMixin {

    @Shadow(remap = false)
    public abstract RecipeMap<?> getRecipeMap();

    @Shadow(remap = false)
    protected abstract long getMaximumOverclockVoltage();

    @Shadow(remap = false)
    protected abstract int getOverclockForTier(long voltage);

    @Redirect(
            method = "setupAndConsumeRecipeInputs(Lgregtech/api/recipes/Recipe;Lnet/minecraftforge/items/IItemHandlerModifiable;Lgregtech/api/capability/IMultipleTankHandler;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lgregtech/api/recipes/Recipe;matches(ZLnet/minecraftforge/items/IItemHandlerModifiable;Lgregtech/api/capability/IMultipleTankHandler;)Z"
            ),
            remap = false
    )
    private boolean gdw$consumeChancedInputs(Recipe recipe,
                                             boolean consumeIfSuccessful,
                                             IItemHandlerModifiable importInventory,
                                             IMultipleTankHandler importFluids) {
        return GregoriusDrugworksChancedInputSupport.matchesAndConsumeRecipeInputs(
                recipe,
                getRecipeMap(),
                getOverclockForTier(getMaximumOverclockVoltage()),
                consumeIfSuccessful,
                importInventory,
                importFluids);
    }
}
