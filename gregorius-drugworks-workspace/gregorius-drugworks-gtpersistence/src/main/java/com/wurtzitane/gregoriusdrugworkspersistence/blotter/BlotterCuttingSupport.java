package com.wurtzitane.gregoriusdrugworkspersistence.blotter;

import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksItems;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoaderUtil;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;

/**
 * Preserves printed blotter data when the GT cutter processes printed sheets into single tabs.
 *
 * @author wurtzitane
 */
public final class BlotterCuttingSupport {

    private static final ThreadLocal<ItemStack> PENDING_PRINTED_CUT_INPUT = new ThreadLocal<>();

    private BlotterCuttingSupport() {
    }

    public static void capturePotentialPrintedCutInput(Recipe recipe, RecipeMap<?> recipeMap,
                                                       IItemHandlerModifiable inputInventory) {
        clearPendingPrintedCutInput();
        if (!isBlotterCuttingRecipe(recipe, recipeMap)) {
            return;
        }
        for (int slot = 0; slot < inputInventory.getSlots(); slot++) {
            ItemStack candidate = inputInventory.getStackInSlot(slot);
            if (BlotterPrintStacks.isCarrier(candidate, PrintableCarrierKind.BLOTTER_PAPER) &&
                    (BlotterPrintData.hasPrint(candidate) || PayloadLoaderUtil.hasPayload(candidate))) {
                ItemStack snapshot = candidate.copy();
                snapshot.setCount(1);
                PENDING_PRINTED_CUT_INPUT.set(snapshot);
                return;
            }
        }
    }

    public static void applyPendingPrintedCutOutput(Recipe recipe, RecipeMap<?> recipeMap, List<ItemStack> itemOutputs) {
        try {
            if (!isBlotterCuttingRecipe(recipe, recipeMap) || itemOutputs == null || itemOutputs.isEmpty()) {
                return;
            }
            ItemStack printedInput = PENDING_PRINTED_CUT_INPUT.get();
            if (printedInput == null || printedInput.isEmpty()) {
                return;
            }

            for (int i = 0; i < itemOutputs.size(); i++) {
                ItemStack originalOutput = itemOutputs.get(i);
                if (originalOutput.isEmpty() || originalOutput.getItem() != GregoriusDrugworksItems.SINGLE_TAB) {
                    continue;
                }
                ItemStack printedOutput = BlotterPrintStacks.copyPrintedSingleTab(printedInput);
                printedOutput.setCount(originalOutput.getCount());
                itemOutputs.set(i, printedOutput);
            }
        } finally {
            clearPendingPrintedCutInput();
        }
    }

    public static void clearPendingPrintedCutInput() {
        PENDING_PRINTED_CUT_INPUT.remove();
    }

    private static boolean isBlotterCuttingRecipe(Recipe recipe, RecipeMap<?> recipeMap) {
        if (recipe == null || recipeMap != RecipeMaps.CUTTER_RECIPES) {
            return false;
        }
        if (recipe.getInputs().isEmpty()) {
            return false;
        }
        boolean hasBlotterInput = false;
        for (GTRecipeInput input : recipe.getInputs()) {
            if (input.acceptsStack(new ItemStack(GregoriusDrugworksItems.BLOTTER_PAPER))) {
                hasBlotterInput = true;
                break;
            }
        }
        if (!hasBlotterInput) {
            return false;
        }
        for (ItemStack output : recipe.getAllItemOutputs()) {
            if (!output.isEmpty() && output.getItem() == GregoriusDrugworksItems.SINGLE_TAB) {
                return true;
            }
        }
        return false;
    }
}
