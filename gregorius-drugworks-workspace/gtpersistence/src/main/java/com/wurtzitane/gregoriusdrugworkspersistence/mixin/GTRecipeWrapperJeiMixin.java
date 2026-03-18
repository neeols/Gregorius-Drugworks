package com.wurtzitane.gregoriusdrugworkspersistence.mixin;

import com.wurtzitane.gregoriusdrugworkspersistence.integration.jei.GregoriusDrugworksJeiChancedInputModule;
import com.wurtzitane.gregoriusdrugworkspersistence.integration.jei.GregoriusDrugworksJeiRecipeWrapperAccess;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.ChemicalPlantAtmosphereHelper;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksRecipeMaps;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(value = GTRecipeWrapper.class, remap = false)
public abstract class GTRecipeWrapperJeiMixin implements GregoriusDrugworksJeiRecipeWrapperAccess {

    @Shadow private RecipeMap<?> recipeMap;
    @Shadow private Recipe recipe;
    @Shadow private List<GTRecipeInput> sortedInputs;
    @Shadow private List<GTRecipeInput> sortedFluidInputs;

    @Unique
    private final Map<Integer, Integer> gdw$itemDisplayToSource = new HashMap<>();

    @Unique
    private final Map<Integer, Integer> gdw$fluidDisplayToSource = new HashMap<>();

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
        boolean notConsumed = input && gdw$isNotConsumedItemInput(slotIndex);

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

        if (input && entry instanceof ChancedItemInputEntry chancedEntry) {
            gdw$addChancedItemInputTooltip(tooltip, chancedEntry);
        } else {
            addIngredientTooltips(tooltip, notConsumed, input, entry,
                    input ? ChancedOutputLogic.OR : recipe.getChancedOutputs().getChancedOutputLogic());
        }
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
        if (input && slotIndex == ChemicalPlantAtmosphereHelper.ATMOSPHERE_SLOT_INDEX) {
            FluidStack fluidStack = (FluidStack) ingredient;
            if (fluidStack != null && !ChemicalPlantAtmosphereHelper.isAirPlaceholder(fluidStack)) {
                TankWidget.addIngotMolFluidTooltip(fluidStack, tooltip);
                addIngredientTooltips(tooltip, true, true, ingredient, null);
                tooltip.add(TooltipHelper.BLINKING_CYAN + I18n.format(
                        "tooltip.gregoriusdrugworkspersistence.chemical_plant.atmosphere",
                        fluidStack.getLocalizedName()));
            } else {
                tooltip.add(TooltipHelper.BLINKING_CYAN +
                        I18n.format("tooltip.gregoriusdrugworkspersistence.chemical_plant.atmosphere.air"));
            }
            return;
        }

        FluidStack fluidStack = (FluidStack) ingredient;
        TankWidget.addIngotMolFluidTooltip(fluidStack, tooltip);

        boolean notConsumed = input && gdw$isNotConsumedFluidInput(slotIndex);

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

        if (input && entry instanceof ChancedFluidInputEntry chancedEntry) {
            gdw$addChancedFluidInputTooltip(tooltip, chancedEntry);
        } else {
            addIngredientTooltips(tooltip, notConsumed, input, entry,
                    input ? ChancedOutputLogic.OR : recipe.getChancedFluidOutputs().getChancedOutputLogic());
        }
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
    public void gdw$setDisplayInputMappings(List<Integer> itemDisplaySlots, List<Integer> fluidDisplaySlots) {
        gdw$itemDisplayToSource.clear();
        gdw$fluidDisplayToSource.clear();

        for (int i = 0; i < itemDisplaySlots.size() && i < sortedInputs.size(); i++) {
            gdw$itemDisplayToSource.put(itemDisplaySlots.get(i), i);
        }

        List<Integer> visibleFluidSourceSlots = gdw$getVisibleFluidInputSourceSlots();
        for (int i = 0; i < fluidDisplaySlots.size() && i < visibleFluidSourceSlots.size(); i++) {
            gdw$fluidDisplayToSource.put(fluidDisplaySlots.get(i), visibleFluidSourceSlots.get(i));
        }
    }

    @Override
    @Unique
    public ChancedItemInputEntry gdw$getChancedItemInput(int slotIndex) {
        int mappedSlot = gdw$mapDisplayedItemSlot(slotIndex);
        return mappedSlot < 0 ? null :
                GregoriusDrugworksChancedInputSupport.getChancedItemInputEntry(recipe, sortedInputs, mappedSlot);
    }

    @Override
    @Unique
    public ChancedFluidInputEntry gdw$getChancedFluidInput(int slotIndex) {
        if (slotIndex == ChemicalPlantAtmosphereHelper.ATMOSPHERE_SLOT_INDEX) {
            return null;
        }
        int mappedSlot = gdw$mapDisplayedFluidSlot(slotIndex);
        return mappedSlot < 0 ? null :
                GregoriusDrugworksChancedInputSupport.getChancedFluidInputEntry(recipe, sortedFluidInputs, mappedSlot);
    }

    @Override
    @Unique
    public boolean gdw$isNotConsumedItemInput(int slotIndex) {
        int mappedSlot = gdw$mapDisplayedItemSlot(slotIndex);
        return mappedSlot >= 0 ? isNotConsumedItem(mappedSlot) : false;
    }

    @Override
    @Unique
    public boolean gdw$isNotConsumedFluidInput(int slotIndex) {
        if (slotIndex == ChemicalPlantAtmosphereHelper.ATMOSPHERE_SLOT_INDEX) {
            return gdw$hasExplicitAtmosphere();
        }
        int mappedSlot = gdw$mapDisplayedFluidSlot(slotIndex);
        return mappedSlot >= 0 && mappedSlot < sortedFluidInputs.size() && sortedFluidInputs.get(mappedSlot).isNonConsumable();
    }

    @Override
    @Unique
    public List<List<FluidStack>> gdw$getDisplayedFluidInputs() {
        if (!ChemicalPlantAtmosphereHelper.supportsAtmosphere(recipeMap)) {
            return Collections.emptyList();
        }
        List<List<FluidStack>> result = new ArrayList<>();
        for (int mappedSlot : gdw$getVisibleFluidInputSourceSlots()) {
            FluidStack fluid = sortedFluidInputs.get(mappedSlot).getInputFluidStack();
            if (fluid != null) {
                result.add(Collections.singletonList(fluid.copy()));
            }
        }
        return result;
    }

    @Override
    @Unique
    public FluidStack gdw$getDisplayedAtmosphereFluid() {
        if (!ChemicalPlantAtmosphereHelper.supportsAtmosphere(recipeMap)) {
            return null;
        }
        return ChemicalPlantAtmosphereHelper.getDisplayedAtmosphere(recipe);
    }

    @Override
    @Unique
    public boolean gdw$hasExplicitAtmosphere() {
        return ChemicalPlantAtmosphereHelper.getAtmosphere(recipe) != null;
    }

    @Unique
    private static void gdw$addChancedItemInputTooltip(List<String> tooltip, ChancedItemInputEntry entry) {
        double chance = entry.getChance() / 100.0;
        double boost = -entry.getTierReductionPerTierDisplay();
        tooltip.add(TooltipHelper.BLINKING_CYAN + I18n.format("gregtech.recipe.chance", chance, boost));
    }

    @Unique
    private static void gdw$addChancedFluidInputTooltip(List<String> tooltip, ChancedFluidInputEntry entry) {
        double chance = entry.getChance() / 100.0;
        double boost = -entry.getTierReductionPerTierDisplay();
        tooltip.add(TooltipHelper.BLINKING_CYAN + I18n.format("gregtech.recipe.chance", chance, boost));
    }

    @Unique
    private int gdw$mapDisplayedFluidSlot(int displayedSlot) {
        Integer mappedSlot = gdw$fluidDisplayToSource.get(displayedSlot);
        if (mappedSlot != null) {
            return mappedSlot;
        }
        if (!ChemicalPlantAtmosphereHelper.supportsAtmosphere(recipeMap)) {
            return displayedSlot;
        }
        List<Integer> visibleSlots = gdw$getVisibleFluidInputSourceSlots();
        return displayedSlot >= 0 && displayedSlot < visibleSlots.size() ? visibleSlots.get(displayedSlot) : -1;
    }

    @Unique
    private int gdw$mapDisplayedItemSlot(int displayedSlot) {
        Integer mappedSlot = gdw$itemDisplayToSource.get(displayedSlot);
        if (mappedSlot != null) {
            return mappedSlot;
        }
        if (!ChemicalPlantAtmosphereHelper.supportsAtmosphere(recipeMap)) {
            return displayedSlot;
        }
        return displayedSlot >= 0 && displayedSlot < sortedInputs.size() ? displayedSlot : -1;
    }

    @Unique
    private List<Integer> gdw$getVisibleFluidInputSourceSlots() {
        List<Integer> visibleSlots = new ArrayList<>();
        int atmosphereSlot = gdw$findAtmosphereSourceSlot();
        for (int i = 0; i < sortedFluidInputs.size(); i++) {
            if (i != atmosphereSlot) {
                visibleSlots.add(i);
            }
        }
        return visibleSlots;
    }

    @Unique
    private int gdw$findAtmosphereSourceSlot() {
        if (recipeMap != GregoriusDrugworksRecipeMaps.CHEMICAL_PLANT_RECIPES) {
            return -1;
        }
        FluidStack atmosphere = ChemicalPlantAtmosphereHelper.getAtmosphere(recipe);
        if (atmosphere == null) {
            return -1;
        }
        for (int i = 0; i < sortedFluidInputs.size(); i++) {
            GTRecipeInput input = sortedFluidInputs.get(i);
            FluidStack inputFluid = input.getInputFluidStack();
            if (input.isNonConsumable() &&
                    inputFluid != null &&
                    input.getAmount() == atmosphere.amount &&
                    ChemicalPlantAtmosphereHelper.matches(inputFluid, atmosphere)) {
                return i;
            }
        }
        return -1;
    }
}
