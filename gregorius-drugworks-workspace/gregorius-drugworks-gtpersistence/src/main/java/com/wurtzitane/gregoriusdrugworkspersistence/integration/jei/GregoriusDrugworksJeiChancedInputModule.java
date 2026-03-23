package com.wurtzitane.gregoriusdrugworkspersistence.integration.jei;

import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.ChancedFluidInputEntry;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.ChancedItemInputEntry;
import gregtech.api.recipes.chance.output.ChancedOutputLogic;
import gregtech.client.utils.TooltipHelper;
import gregtech.integration.jei.recipe.GTRecipeWrapper;
import gregtech.integration.jei.utils.render.FluidStackTextRenderer;
import gregtech.integration.jei.utils.render.ItemStackTextRenderer;
import mezz.jei.api.gui.IDrawable;
import net.minecraft.client.resources.I18n;

import java.util.List;

public final class GregoriusDrugworksJeiChancedInputModule {

    private static final ChancedOutputLogic CHANCED_INPUT_LOGIC = ChancedOutputLogic.OR;

    private GregoriusDrugworksJeiChancedInputModule() {
    }

    public static boolean hasChancedItemInput(GTRecipeWrapper recipeWrapper, int slotIndex) {
        return getChancedItemInput(recipeWrapper, slotIndex) != null;
    }

    public static boolean hasChancedFluidInput(GTRecipeWrapper recipeWrapper, int slotIndex) {
        return getChancedFluidInput(recipeWrapper, slotIndex) != null;
    }

    public static ItemStackTextRenderer createItemInputRenderer(GTRecipeWrapper recipeWrapper, int slotIndex) {
        ChancedItemInputEntry chance = getChancedItemInput(recipeWrapper, slotIndex);
        if (chance != null) {
            return new ItemStackTextRenderer(chance, CHANCED_INPUT_LOGIC);
        }
        return new ItemStackTextRenderer(isNotConsumedItem(recipeWrapper, slotIndex));
    }

    public static FluidStackTextRenderer createFluidInputRenderer(GTRecipeWrapper recipeWrapper,
                                                                  int slotIndex,
                                                                  int fluidAmount,
                                                                  int width,
                                                                  int height,
                                                                  IDrawable overlay) {
        ChancedFluidInputEntry chance = getChancedFluidInput(recipeWrapper, slotIndex);
        if (chance != null) {
            return new FluidStackTextRenderer(fluidAmount, false, width, height, overlay, chance,
                    CHANCED_INPUT_LOGIC);
        }
        return new FluidStackTextRenderer(fluidAmount, false, width, height, overlay)
                .setNotConsumed(isNotConsumedFluid(recipeWrapper, slotIndex));
    }

    public static void addItemTooltip(GTRecipeWrapper recipeWrapper, int slotIndex, boolean input,
                                      List<String> tooltip) {
        if (!input) {
            return;
        }
        ChancedItemInputEntry entry = getChancedItemInput(recipeWrapper, slotIndex);
        if (entry == null) {
            return;
        }
        tooltip.add(TooltipHelper.BLINKING_CYAN + I18n.format("gregtech.recipe.chance",
                entry.getChance() / 100.0, -entry.getTierReductionPerTierDisplay()));
        tooltip.add(TooltipHelper.BLINKING_CYAN +
                I18n.format("tooltip.gregoriusdrugworkspersistence.jei.chanced_input"));
    }

    public static void addFluidTooltip(GTRecipeWrapper recipeWrapper, int slotIndex, boolean input,
                                       List<String> tooltip) {
        if (!input) {
            return;
        }
        ChancedFluidInputEntry entry = getChancedFluidInput(recipeWrapper, slotIndex);
        if (entry == null) {
            return;
        }
        tooltip.add(TooltipHelper.BLINKING_CYAN + I18n.format("gregtech.recipe.chance",
                entry.getChance() / 100.0, -entry.getTierReductionPerTierDisplay()));
        tooltip.add(TooltipHelper.BLINKING_CYAN +
                I18n.format("tooltip.gregoriusdrugworkspersistence.jei.chanced_input_fluid"));
    }

    private static ChancedItemInputEntry getChancedItemInput(GTRecipeWrapper recipeWrapper, int slotIndex) {
        if (recipeWrapper instanceof GregoriusDrugworksJeiRecipeWrapperAccess) {
            return ((GregoriusDrugworksJeiRecipeWrapperAccess) recipeWrapper).gdw$getChancedItemInput(slotIndex);
        }
        return null;
    }

    private static ChancedFluidInputEntry getChancedFluidInput(GTRecipeWrapper recipeWrapper, int slotIndex) {
        if (recipeWrapper instanceof GregoriusDrugworksJeiRecipeWrapperAccess) {
            return ((GregoriusDrugworksJeiRecipeWrapperAccess) recipeWrapper).gdw$getChancedFluidInput(slotIndex);
        }
        return null;
    }

    private static boolean isNotConsumedItem(GTRecipeWrapper recipeWrapper, int slotIndex) {
        if (recipeWrapper instanceof GregoriusDrugworksJeiRecipeWrapperAccess) {
            return ((GregoriusDrugworksJeiRecipeWrapperAccess) recipeWrapper).gdw$isNotConsumedItemInput(slotIndex);
        }
        return recipeWrapper.isNotConsumedItem(slotIndex);
    }

    private static boolean isNotConsumedFluid(GTRecipeWrapper recipeWrapper, int slotIndex) {
        if (recipeWrapper instanceof GregoriusDrugworksJeiRecipeWrapperAccess) {
            return ((GregoriusDrugworksJeiRecipeWrapperAccess) recipeWrapper).gdw$isNotConsumedFluidInput(slotIndex);
        }
        return recipeWrapper.isNotConsumedFluid(slotIndex);
    }
}
