package com.wurtzitane.gregoriusdrugworkspersistence.mixin;

import com.wurtzitane.gregoriusdrugworkspersistence.integration.jei.GregoriusDrugworksJeiChancedInputModule;
import com.wurtzitane.gregoriusdrugworkspersistence.integration.jei.GregoriusDrugworksJeiRecipeWrapperAccess;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.ChancedFluidInputEntry;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.ChancedItemInputEntry;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.GregoriusDrugworksChancedInputSupport;
import gregtech.api.gui.widgets.TankWidget;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.chance.boost.BoostableChanceEntry;
import gregtech.api.recipes.chance.output.ChancedOutputLogic;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.client.utils.TooltipHelper;
import gregtech.integration.jei.recipe.GTRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(value = GTRecipeWrapper.class, remap = false)
public abstract class GTRecipeWrapperJeiMixin implements GregoriusDrugworksJeiRecipeWrapperAccess {

    @Shadow private RecipeMap<?> recipeMap;
    @Shadow private Recipe recipe;
    @Shadow private List<GTRecipeInput> sortedInputs;
    @Shadow private List<GTRecipeInput> sortedFluidInputs;

    @Shadow(remap = false)
    public abstract boolean isNotConsumedItem(int slot);

    @Shadow(remap = false)
    public abstract boolean isNotConsumedFluid(int slot);

    @Shadow(remap = false)
    public abstract void addIngredientTooltips(java.util.Collection<String> tooltip, boolean notConsumed, boolean input,
                                               Object ingredient, Object ingredient2);

    /**
     * @author wurtzitane
     * @reason Extend GT's native tooltip path so chanced inputs use the same chance tooltip
     * plumbing as chanced outputs.
     */
    @Overwrite
    public void addItemTooltip(int slotIndex, boolean input, Object ingredient, List<String> tooltip) {
        boolean notConsumed = input && isNotConsumedItem(slotIndex);

        BoostableChanceEntry<?> entry = null;
        if (input) {
            entry = gdw$getChancedItemInput(slotIndex);
        } else if (!recipe.getChancedOutputs().getChancedEntries().isEmpty()) {
            int outputIndex = slotIndex - recipeMap.getMaxInputs();
            if (outputIndex >= recipe.getOutputs().size()) {
                entry = recipe.getChancedOutputs().getChancedEntries()
                        .get(outputIndex - recipe.getOutputs().size());
            }
        }

        addIngredientTooltips(tooltip, notConsumed, input, entry,
                input ? ChancedOutputLogic.OR : recipe.getChancedOutputs().getChancedOutputLogic());
        addIngredientTooltips(tooltip, notConsumed, input, ingredient, null);

        if (input && entry instanceof ChancedItemInputEntry) {
            tooltip.add(TooltipHelper.BLINKING_CYAN +
                    I18n.format("tooltip.gregoriusdrugworkspersistence.jei.chanced_input"));
        }
    }

    /**
     * @author wurtzitane
     * @reason Extend GT's native fluid tooltip path so chanced input fluids display the same
     * chance tooltip metadata as chanced output fluids.
     */
    @Overwrite
    public void addFluidTooltip(int slotIndex, boolean input, Object ingredient, List<String> tooltip) {
        FluidStack fluidStack = (FluidStack) ingredient;
        TankWidget.addIngotMolFluidTooltip(fluidStack, tooltip);

        boolean notConsumed = input && isNotConsumedFluid(slotIndex);

        BoostableChanceEntry<?> entry = null;
        if (input) {
            entry = gdw$getChancedFluidInput(slotIndex);
        } else if (!recipe.getChancedFluidOutputs().getChancedEntries().isEmpty()) {
            int outputIndex = slotIndex - recipeMap.getMaxFluidInputs();
            if (outputIndex >= recipe.getFluidOutputs().size()) {
                entry = recipe.getChancedFluidOutputs().getChancedEntries()
                        .get(outputIndex - recipe.getFluidOutputs().size());
            }
        }

        addIngredientTooltips(tooltip, notConsumed, input, entry,
                input ? ChancedOutputLogic.OR : recipe.getChancedFluidOutputs().getChancedOutputLogic());
        addIngredientTooltips(tooltip, notConsumed, input, ingredient, null);

        if (input && entry instanceof ChancedFluidInputEntry) {
            tooltip.add(TooltipHelper.BLINKING_CYAN +
                    I18n.format("tooltip.gregoriusdrugworkspersistence.jei.chanced_input_fluid"));
        }
    }

    @Override
    @Unique
    public void gdw$prepareChancedInputMappings(mezz.jei.api.ingredients.IIngredients ingredients) {
    }

    @Override
    @Unique
    public ChancedItemInputEntry gdw$getChancedItemInput(int slotIndex) {
        return GregoriusDrugworksChancedInputSupport.getChancedItemInputEntry(recipe, sortedInputs, slotIndex);
    }

    @Override
    @Unique
    public ChancedFluidInputEntry gdw$getChancedFluidInput(int slotIndex) {
        return GregoriusDrugworksChancedInputSupport.getChancedFluidInputEntry(recipe, sortedFluidInputs, slotIndex);
    }
}
