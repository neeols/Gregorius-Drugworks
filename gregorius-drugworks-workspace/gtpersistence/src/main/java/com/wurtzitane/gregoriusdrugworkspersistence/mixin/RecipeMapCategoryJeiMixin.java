package com.wurtzitane.gregoriusdrugworkspersistence.mixin;

import com.wurtzitane.gregoriusdrugworkspersistence.integration.jei.GregoriusDrugworksJeiChancedInputModule;
import com.wurtzitane.gregoriusdrugworkspersistence.integration.jei.GregoriusDrugworksJeiRecipeWrapperAccess;
import gregtech.api.GTValues;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.Widget;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.gui.widgets.TankWidget;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.recipeproperties.ResearchProperty;
import gregtech.api.recipes.recipeproperties.ResearchPropertyData;
import gregtech.api.util.AssemblyLineManager;
import gregtech.api.util.GTUtility;
import gregtech.integration.jei.recipe.GTRecipeWrapper;
import gregtech.integration.jei.recipe.RecipeMapCategory;
import gregtech.integration.jei.utils.render.FluidStackTextRenderer;
import gregtech.integration.jei.utils.render.ItemStackTextRenderer;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = RecipeMapCategory.class, remap = false)
public abstract class RecipeMapCategoryJeiMixin {

    @Shadow private RecipeMap<?> recipeMap;
    @Shadow private ModularUI modularUI;
    @Shadow private ItemStackHandler importItems;
    @Shadow private ItemStackHandler exportItems;
    @Shadow private FluidTankList importFluids;
    @Shadow private FluidTankList exportFluids;

    /**
     * @author wurtzitane
     * @reason Rebuild GT's JEI slot initialization so chanced inputs are handled during
     * the native slot setup phase instead of being retrofitted after the fact.
     */
    @Overwrite
    public void setRecipe(IRecipeLayout recipeLayout, GTRecipeWrapper recipeWrapper,
                          IIngredients ingredients) {
        if (recipeWrapper instanceof GregoriusDrugworksJeiRecipeWrapperAccess access) {
            access.gdw$prepareChancedInputMappings(ingredients);
        }
        IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();
        IGuiFluidStackGroup fluidStackGroup = recipeLayout.getFluidStacks();
        for (Widget uiWidget : modularUI.guiWidgets.values()) {
            if (uiWidget instanceof SlotWidget slotWidget) {
                if (!(slotWidget.getHandle() instanceof SlotItemHandler handle)) {
                    continue;
                }
                if (handle.getItemHandler() == importItems) {
                    int slotIndex = handle.getSlotIndex();
                    itemStackGroup.init(slotIndex, true,
                            GregoriusDrugworksJeiChancedInputModule.createItemInputRenderer(recipeWrapper, slotIndex),
                            slotWidget.getPosition().x + 1,
                            slotWidget.getPosition().y + 1,
                            slotWidget.getSize().width - 2,
                            slotWidget.getSize().height - 2, 0, 0);
                } else if (handle.getItemHandler() == exportItems) {
                    itemStackGroup.init(importItems.getSlots() + handle.getSlotIndex(), false,
                            new ItemStackTextRenderer(
                                    recipeWrapper.getOutputChance(
                                            handle.getSlotIndex() - recipeWrapper.getRecipe().getOutputs().size()),
                                    recipeWrapper.getChancedOutputLogic()),
                            slotWidget.getPosition().x + 1,
                            slotWidget.getPosition().y + 1,
                            slotWidget.getSize().width - 2,
                            slotWidget.getSize().height - 2, 0, 0);
                }
            } else if (uiWidget instanceof TankWidget tankWidget) {
                if (importFluids.getFluidTanks().contains(tankWidget.fluidTank)) {
                    int importIndex = importFluids.getFluidTanks().indexOf(tankWidget.fluidTank);
                    List<List<FluidStack>> inputsList = ingredients.getInputs(VanillaTypes.FLUID);
                    int fluidAmount = 0;
                    if (inputsList.size() > importIndex && !inputsList.get(importIndex).isEmpty()) {
                        fluidAmount = inputsList.get(importIndex).get(0).amount;
                    }
                    int width = tankWidget.getSize().width - (2 * tankWidget.fluidRenderOffset);
                    int height = tankWidget.getSize().height - (2 * tankWidget.fluidRenderOffset);
                    fluidStackGroup.init(importIndex, true,
                            GregoriusDrugworksJeiChancedInputModule.createFluidInputRenderer(
                                    recipeWrapper, importIndex, fluidAmount, width, height, null),
                            tankWidget.getPosition().x + tankWidget.fluidRenderOffset,
                            tankWidget.getPosition().y + tankWidget.fluidRenderOffset,
                            width,
                            height, 0, 0);
                } else if (exportFluids.getFluidTanks().contains(tankWidget.fluidTank)) {
                    int exportIndex = exportFluids.getFluidTanks().indexOf(tankWidget.fluidTank);
                    List<List<FluidStack>> outputsList = ingredients.getOutputs(VanillaTypes.FLUID);
                    int fluidAmount = 0;
                    if (outputsList.size() > exportIndex && !outputsList.get(exportIndex).isEmpty()) {
                        fluidAmount = outputsList.get(exportIndex).get(0).amount;
                    }
                    fluidStackGroup.init(importFluids.getFluidTanks().size() + exportIndex, false,
                            new FluidStackTextRenderer(fluidAmount, false,
                                    tankWidget.getSize().width - (2 * tankWidget.fluidRenderOffset),
                                    tankWidget.getSize().height - (2 * tankWidget.fluidRenderOffset), null,
                                    recipeWrapper.getFluidOutputChance(
                                            exportIndex - recipeWrapper.getRecipe().getFluidOutputs().size()),
                                    recipeWrapper.getChancedFluidOutputLogic()),
                            tankWidget.getPosition().x + tankWidget.fluidRenderOffset,
                            tankWidget.getPosition().y + tankWidget.fluidRenderOffset,
                            tankWidget.getSize().width - (2 * tankWidget.fluidRenderOffset),
                            tankWidget.getSize().height - (2 * tankWidget.fluidRenderOffset), 0, 0);
                }
            }
        }

        if (gregtech.common.ConfigHolder.machines.enableResearch && this.recipeMap == RecipeMaps.ASSEMBLY_LINE_RECIPES) {
            ResearchPropertyData data = recipeWrapper.getRecipe().getProperty(ResearchProperty.getInstance(), null);
            if (data != null) {
                List<ItemStack> dataItems = new ArrayList<>();
                for (ResearchPropertyData.ResearchEntry entry : data) {
                    ItemStack dataStick = entry.getDataItem().copy();
                    AssemblyLineManager.writeResearchToNBT(GTUtility.getOrCreateNbtCompound(dataStick),
                            entry.getResearchId());
                    dataItems.add(dataStick);
                }
                itemStackGroup.set(16, dataItems);
            }
        }

        itemStackGroup.addTooltipCallback(recipeWrapper::addItemTooltip);
        fluidStackGroup.addTooltipCallback(recipeWrapper::addFluidTooltip);
        itemStackGroup.set(ingredients);
        fluidStackGroup.set(ingredients);
    }
}
