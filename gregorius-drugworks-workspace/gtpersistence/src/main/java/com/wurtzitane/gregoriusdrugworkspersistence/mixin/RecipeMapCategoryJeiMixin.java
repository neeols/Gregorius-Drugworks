package com.wurtzitane.gregoriusdrugworkspersistence.mixin;

import com.wurtzitane.gregoriusdrugworkspersistence.integration.jei.GregoriusDrugworksJeiChancedInputModule;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.Widget;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.gui.widgets.TankWidget;
import gregtech.integration.jei.recipe.GTRecipeWrapper;
import gregtech.integration.jei.recipe.RecipeMapCategory;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RecipeMapCategory.class, remap = false)
public abstract class RecipeMapCategoryJeiMixin {

    @Shadow private ModularUI modularUI;
    @Shadow private ItemStackHandler importItems;
    @Shadow private FluidTankList importFluids;

    @Inject(method = "setRecipe", at = @At("TAIL"), remap = false)
    private void gdw$markChancedInputs(IRecipeLayout recipeLayout, GTRecipeWrapper recipeWrapper,
                                       IIngredients ingredients, CallbackInfo ci) {
        IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();
        IGuiFluidStackGroup fluidStackGroup = recipeLayout.getFluidStacks();
        boolean touchedItems = false;
        boolean touchedFluids = false;

        for (Widget widget : modularUI.guiWidgets.values()) {
            if (widget instanceof SlotWidget slotWidget) {
                if (!(slotWidget.getHandle() instanceof SlotItemHandler handle) || handle.getItemHandler() != importItems) {
                    continue;
                }
                int slotIndex = handle.getSlotIndex();
                if (!GregoriusDrugworksJeiChancedInputModule.hasChancedItemInput(recipeWrapper, slotIndex)) {
                    continue;
                }
                itemStackGroup.init(slotIndex, true,
                        GregoriusDrugworksJeiChancedInputModule.createItemInputRenderer(recipeWrapper, slotIndex),
                        slotWidget.getPosition().x + 1,
                        slotWidget.getPosition().y + 1,
                        slotWidget.getSize().width - 2,
                        slotWidget.getSize().height - 2, 0, 0);
                touchedItems = true;
            } else if (widget instanceof TankWidget tankWidget) {
                if (!importFluids.getFluidTanks().contains(tankWidget.fluidTank)) {
                    continue;
                }
                int slotIndex = importFluids.getFluidTanks().indexOf(tankWidget.fluidTank);
                if (!GregoriusDrugworksJeiChancedInputModule.hasChancedFluidInput(recipeWrapper, slotIndex)) {
                    continue;
                }
                int width = tankWidget.getSize().width - (2 * tankWidget.fluidRenderOffset);
                int height = tankWidget.getSize().height - (2 * tankWidget.fluidRenderOffset);
                fluidStackGroup.init(slotIndex, true,
                        GregoriusDrugworksJeiChancedInputModule.createFluidInputRenderer(
                                recipeWrapper,
                                slotIndex,
                                tankWidget.fluidTank.getFluidAmount(),
                                width,
                                height,
                                null),
                        tankWidget.getPosition().x + tankWidget.fluidRenderOffset,
                        tankWidget.getPosition().y + tankWidget.fluidRenderOffset,
                        width,
                        height,
                        0,
                        0);
                touchedFluids = true;
            }
        }

        if (touchedItems) {
            itemStackGroup.set(ingredients);
        }
        if (touchedFluids) {
            fluidStackGroup.set(ingredients);
        }
    }
}
