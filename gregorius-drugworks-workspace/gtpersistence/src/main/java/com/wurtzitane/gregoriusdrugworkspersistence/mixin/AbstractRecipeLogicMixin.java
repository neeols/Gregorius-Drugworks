package com.wurtzitane.gregoriusdrugworkspersistence.mixin;

import com.wurtzitane.gregoriusdrugworkspersistence.blotter.BlotterCuttingSupport;
import com.wurtzitane.gregoriusdrugworkspersistence.blotter.BlotterSoakingSupport;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.GregoriusDrugworksChancedInputSupport;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractRecipeLogic.class, remap = false)
public abstract class AbstractRecipeLogicMixin {

    @Shadow(remap = false)
    public abstract RecipeMap<?> getRecipeMap();

    @Shadow(remap = false)
    protected abstract long getMaximumOverclockVoltage();

    @Shadow(remap = false)
    protected abstract int getOverclockForTier(long voltage);

    @Shadow(remap = false)
    protected NonNullList<ItemStack> itemOutputs;

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
        BlotterCuttingSupport.capturePotentialPrintedCutInput(recipe, getRecipeMap(), importInventory);
        Boolean blotterSoakMatch = BlotterSoakingSupport.matchesAndConsumeBlotterSoakRecipe(
                recipe,
                getRecipeMap(),
                consumeIfSuccessful,
                importInventory,
                importFluids);
        if (blotterSoakMatch != null) {
            if (!blotterSoakMatch.booleanValue()) {
                BlotterCuttingSupport.clearPendingPrintedCutInput();
                BlotterSoakingSupport.clearPendingSoakInput();
            }
            return blotterSoakMatch.booleanValue();
        }
        boolean matched = GregoriusDrugworksChancedInputSupport.matchesAndConsumeRecipeInputs(
                recipe,
                getRecipeMap(),
                getOverclockForTier(getMaximumOverclockVoltage()),
                consumeIfSuccessful,
                importInventory,
                importFluids);
        if (!matched) {
            BlotterCuttingSupport.clearPendingPrintedCutInput();
        }
        return matched;
    }

    @Inject(method = "setupRecipe(Lgregtech/api/recipes/Recipe;)V", at = @At("TAIL"), remap = false)
    private void gdw$rewritePrintedCutOutputs(Recipe recipe, CallbackInfo callbackInfo) {
        BlotterCuttingSupport.applyPendingPrintedCutOutput(recipe, getRecipeMap(), itemOutputs);
        BlotterSoakingSupport.applyPendingSoakOutput(recipe, getRecipeMap(), itemOutputs);
    }
}
