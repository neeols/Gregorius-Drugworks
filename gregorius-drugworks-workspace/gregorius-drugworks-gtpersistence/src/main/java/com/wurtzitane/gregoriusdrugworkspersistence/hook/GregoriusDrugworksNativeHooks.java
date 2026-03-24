package com.wurtzitane.gregoriusdrugworkspersistence.hook;

import com.wurtzitane.gregoriusdrugworkspersistence.blotter.BlotterCuttingSupport;
import com.wurtzitane.gregoriusdrugworkspersistence.blotter.BlotterSoakingSupport;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.integration.jei.recipe.GTRecipeWrapper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class GregoriusDrugworksNativeHooks {

    private static boolean registered;

    private static final AbstractRecipeLogic.RecipeLifecycleListener BLOTTTER_RECIPE_LISTENER =
            new AbstractRecipeLogic.RecipeLifecycleListener() {

                @Override
                public @NotNull AbstractRecipeLogic.SetupInterceptResult beforeSetupAndConsume(
                        @NotNull AbstractRecipeLogic logic, @NotNull Recipe recipe, @Nullable RecipeMap<?> recipeMap,
                        @NotNull IItemHandlerModifiable importInventory,
                        @NotNull IMultipleTankHandler importFluids) {
                    BlotterCuttingSupport.capturePotentialPrintedCutInput(recipe, recipeMap, importInventory);
                    Boolean blotterSoakMatch = BlotterSoakingSupport.matchesAndConsumeBlotterSoakRecipe(
                            recipe,
                            recipeMap,
                            true,
                            importInventory,
                            importFluids);
                    if (blotterSoakMatch == null) {
                        return AbstractRecipeLogic.SetupInterceptResult.passThrough();
                    }
                    if (!blotterSoakMatch.booleanValue()) {
                        return AbstractRecipeLogic.SetupInterceptResult.fail();
                    }
                    return AbstractRecipeLogic.SetupInterceptResult.success(recipe);
                }

                @Override
                public void onSetupFailed(@NotNull AbstractRecipeLogic logic, @NotNull Recipe recipe,
                                          @Nullable RecipeMap<?> recipeMap,
                                          @NotNull IItemHandlerModifiable importInventory,
                                          @NotNull IMultipleTankHandler importFluids) {
                    BlotterCuttingSupport.clearPendingPrintedCutInput();
                    BlotterSoakingSupport.clearPendingSoakInput();
                }

                @Override
                public void onSetupRecipe(@NotNull AbstractRecipeLogic logic, @NotNull Recipe recipe,
                                          @Nullable RecipeMap<?> recipeMap, @NotNull List<ItemStack> itemOutputs,
                                          @NotNull List<FluidStack> fluidOutputs) {
                    BlotterCuttingSupport.applyPendingPrintedCutOutput(recipe, recipeMap, itemOutputs);
                    BlotterSoakingSupport.applyPendingSoakOutput(recipe, recipeMap, itemOutputs);
                }
            };

    private static final GTRecipeWrapper.JeiIngredientRewriteHook BLOTTER_JEI_REWRITE_HOOK =
            new GTRecipeWrapper.JeiIngredientRewriteHook() {

                @Override
                public void rewriteIngredients(@NotNull Recipe recipe, @NotNull RecipeMap<?> recipeMap,
                                               @NotNull IIngredients ingredients) {
                    List<List<ItemStack>> itemOutputs = ingredients.getOutputs(VanillaTypes.ITEM);
                    if (itemOutputs == null || itemOutputs.isEmpty()) {
                        return;
                    }

                    boolean changed = false;
                    List<List<ItemStack>> rewrittenOutputs = new ArrayList<>(itemOutputs.size());
                    for (List<ItemStack> outputOptions : itemOutputs) {
                        List<ItemStack> rewrittenOptions = new ArrayList<>(outputOptions.size());
                        for (ItemStack output : outputOptions) {
                            ItemStack rewritten = BlotterSoakingSupport.createPreviewSoakOutput(recipe, recipeMap,
                                    output);
                            rewrittenOptions.add(rewritten);
                            if (!ItemStack.areItemsEqual(output, rewritten) ||
                                    !ItemStack.areItemStackTagsEqual(output, rewritten)) {
                                changed = true;
                            }
                        }
                        rewrittenOutputs.add(rewrittenOptions);
                    }

                    if (changed) {
                        ingredients.setOutputLists(VanillaTypes.ITEM, rewrittenOutputs);
                    }
                }
            };

    private GregoriusDrugworksNativeHooks() {}

    public static void register() {
        if (registered) {
            return;
        }
        registered = true;
        AbstractRecipeLogic.registerRecipeLifecycleListener(BLOTTTER_RECIPE_LISTENER);
        GTRecipeWrapper.registerJeiIngredientRewriteHook(BLOTTER_JEI_REWRITE_HOOK);
    }
}
