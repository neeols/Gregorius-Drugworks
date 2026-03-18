package com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance;

import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.chance.boost.ChanceBoostFunction;
import gregtech.api.recipes.chance.output.ChancedOutputLogic;
import gregtech.api.recipes.ingredients.GTRecipeFluidInput;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.ingredients.GTRecipeItemInput;
import gregtech.api.recipes.recipeproperties.IRecipePropertyStorage;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTTransferUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class GregoriusDrugworksChancedInputSupport {

    private static final Field RECIPE_PROPERTY_STORAGE_FIELD = findField("recipePropertyStorage");
    private static final Field RECIPE_PROPERTY_STORAGE_ERRORED_FIELD = findField("recipePropertyStorageErrored");

    private GregoriusDrugworksChancedInputSupport() {
    }

    public static <R extends RecipeBuilder<R>> R chancedItemInput(R builder, ItemStack stack, int chance, int chanceBoost) {
        validateChance(chance);
        if (stack == null || stack.isEmpty()) {
            return builder;
        }
        ItemStack copy = stack.copy();
        builder.input(new ChancedGTRecipeItemInput(copy, chance, chanceBoost));
        mergePropertyIntoBuilder(builder, ChancedInputRecipePropertyValue.ofItem(
                new ChancedItemInputEntry(copy, chance, chanceBoost)));
        return builder;
    }

    public static <R extends RecipeBuilder<R>> R chancedFluidInput(R builder, FluidStack stack, int chance, int chanceBoost) {
        validateChance(chance);
        if (stack == null || stack.amount <= 0) {
            return builder;
        }
        FluidStack copy = stack.copy();
        builder.fluidInputs(new ChancedGTRecipeFluidInput(copy, chance, chanceBoost));
        mergePropertyIntoBuilder(builder, ChancedInputRecipePropertyValue.ofFluid(
                new ChancedFluidInputEntry(copy, chance, chanceBoost)));
        return builder;
    }

    public static void mergePropertyIntoBuilder(RecipeBuilder<?> builder, ChancedInputRecipePropertyValue addition) {
        if (builder == null || addition == null || addition.isEmpty()) {
            return;
        }
        ChancedInputRecipePropertyValue merged = ChancedInputRecipePropertyValue.merge(getBuilderProperty(builder), addition);
        setBuilderProperty(builder, merged);
    }

    public static ChancedInputRecipePropertyValue getBuilderProperty(RecipeBuilder<?> builder) {
        IRecipePropertyStorage storage = getRecipePropertyStorage(builder);
        if (storage == null) {
            return null;
        }
        return storage.getRecipePropertyValue(ChancedInputRecipeProperty.getInstance(), null);
    }

    public static void setBuilderProperty(RecipeBuilder<?> builder, ChancedInputRecipePropertyValue value) {
        IRecipePropertyStorage storage = getRecipePropertyStorage(builder);
        if (storage != null) {
            storage.freeze(false);
            storage.remove(ChancedInputRecipeProperty.getInstance());
        }
        if (value == null || value.isEmpty()) {
            return;
        }
        if (!builder.applyProperty(ChancedInputRecipeProperty.getInstance(), value.copy())) {
            setRecipePropertyStorageErrored(builder, true);
        }
    }

    public static void applyChancedInputRolls(Recipe recipe,
                                              RecipeMap<?> recipeMap,
                                              int recipeTier,
                                              int machineTier,
                                              IItemHandlerModifiable inputInventory,
                                              IMultipleTankHandler inputFluids) {
        ChancedInputRecipePropertyValue propertyValue = recipe.getProperty(ChancedInputRecipeProperty.getInstance(), null);
        if (propertyValue == null || propertyValue.isEmpty()) {
            return;
        }
        ChanceBoostFunction boostFunction = recipeMap == null ? ChanceBoostFunction.NONE : recipeMap.getChanceFunction();

        for (ChancedItemInputEntry entry : propertyValue.getItemInputs()) {
            if (shouldRefund(entry, boostFunction, recipeTier, machineTier)) {
                ItemStack refund = entry.getIngredient();
                if (!GTTransferUtils.addItemsToItemHandler(inputInventory, false, Collections.singletonList(refund))) {
                    GTLog.logger.warn("Failed to refund chanced recipe item input {}", refund);
                }
            }
        }

        for (ChancedFluidInputEntry entry : propertyValue.getFluidInputs()) {
            if (shouldRefund(entry, boostFunction, recipeTier, machineTier)) {
                FluidStack refund = entry.getIngredient();
                if (!GTTransferUtils.addFluidsToFluidHandler(inputFluids, false, Collections.singletonList(refund))) {
                    GTLog.logger.warn("Failed to refund chanced recipe fluid input {}", refund.getLocalizedName());
                }
            }
        }
    }

    public static ChancedItemInputEntry getChancedItemInputEntry(Recipe recipe, List<GTRecipeInput> sortedInputs,
                                                                 int slotIndex) {
        if (slotIndex >= 0 && slotIndex < sortedInputs.size()) {
            GTRecipeInput input = sortedInputs.get(slotIndex);
            if (input instanceof ChancedGTRecipeItemInput chancedInput) {
                return chancedInput.getChancedEntry();
            }
        }
        ChancedInputRecipePropertyValue propertyValue = recipe.getProperty(ChancedInputRecipeProperty.getInstance(), null);
        if (propertyValue == null || propertyValue.isEmpty() || slotIndex < 0 || slotIndex >= sortedInputs.size()) {
            return null;
        }
        List<ChancedItemInputEntry> remaining = getSortedItemEntries(propertyValue);
        for (int i = 0; i <= slotIndex && i < sortedInputs.size(); i++) {
            GTRecipeInput input = sortedInputs.get(i);
            int matchIndex = findMatchingItemEntryIndex(remaining, input);
            if (matchIndex < 0) {
                continue;
            }
            ChancedItemInputEntry match = remaining.remove(matchIndex);
            if (i == slotIndex) {
                return match;
            }
        }
        return null;
    }

    public static ChancedFluidInputEntry getChancedFluidInputEntry(Recipe recipe, List<GTRecipeInput> sortedFluidInputs,
                                                                   int slotIndex) {
        if (slotIndex >= 0 && slotIndex < sortedFluidInputs.size()) {
            GTRecipeInput input = sortedFluidInputs.get(slotIndex);
            if (input instanceof ChancedGTRecipeFluidInput chancedInput) {
                return chancedInput.getChancedEntry();
            }
        }
        ChancedInputRecipePropertyValue propertyValue = recipe.getProperty(ChancedInputRecipeProperty.getInstance(), null);
        if (propertyValue == null || propertyValue.isEmpty() || slotIndex < 0 || slotIndex >= sortedFluidInputs.size()) {
            return null;
        }
        List<ChancedFluidInputEntry> remaining = getSortedFluidEntries(propertyValue);
        for (int i = 0; i <= slotIndex && i < sortedFluidInputs.size(); i++) {
            GTRecipeInput input = sortedFluidInputs.get(i);
            int matchIndex = findMatchingFluidEntryIndex(remaining, input);
            if (matchIndex < 0) {
                continue;
            }
            ChancedFluidInputEntry match = remaining.remove(matchIndex);
            if (i == slotIndex) {
                return match;
            }
        }
        return null;
    }

    public static Map<Integer, ChancedItemInputEntry> buildDisplayedItemInputMap(Recipe recipe,
                                                                                 List<List<ItemStack>> displayedInputs) {
        Map<Integer, ChancedItemInputEntry> result = new HashMap<Integer, ChancedItemInputEntry>();
        ChancedInputRecipePropertyValue propertyValue = recipe.getProperty(ChancedInputRecipeProperty.getInstance(), null);
        if (propertyValue == null || propertyValue.isEmpty() || displayedInputs == null || displayedInputs.isEmpty()) {
            return result;
        }
        List<ChancedItemInputEntry> remaining = getSortedItemEntries(propertyValue);
        for (int slotIndex = 0; slotIndex < displayedInputs.size(); slotIndex++) {
            int matchIndex = findDisplayedItemEntryIndex(remaining, displayedInputs.get(slotIndex));
            if (matchIndex < 0) {
                continue;
            }
            result.put(slotIndex, remaining.remove(matchIndex));
        }
        return result;
    }

    public static Map<Integer, ChancedFluidInputEntry> buildDisplayedFluidInputMap(Recipe recipe,
                                                                                   List<List<FluidStack>> displayedInputs) {
        Map<Integer, ChancedFluidInputEntry> result = new HashMap<Integer, ChancedFluidInputEntry>();
        ChancedInputRecipePropertyValue propertyValue = recipe.getProperty(ChancedInputRecipeProperty.getInstance(), null);
        if (propertyValue == null || propertyValue.isEmpty() || displayedInputs == null || displayedInputs.isEmpty()) {
            return result;
        }
        List<ChancedFluidInputEntry> remaining = getSortedFluidEntries(propertyValue);
        for (int slotIndex = 0; slotIndex < displayedInputs.size(); slotIndex++) {
            int matchIndex = findDisplayedFluidEntryIndex(remaining, displayedInputs.get(slotIndex));
            if (matchIndex < 0) {
                continue;
            }
            result.put(slotIndex, remaining.remove(matchIndex));
        }
        return result;
    }

    private static boolean shouldRefund(ChancedItemInputEntry entry,
                                        ChanceBoostFunction boostFunction,
                                        int recipeTier,
                                        int machineTier) {
        int chance = boostFunction.getBoostedChance(entry, recipeTier, machineTier);
        return !ChancedOutputLogic.passesChance(chance);
    }

    private static boolean shouldRefund(ChancedFluidInputEntry entry,
                                        ChanceBoostFunction boostFunction,
                                        int recipeTier,
                                        int machineTier) {
        int chance = boostFunction.getBoostedChance(entry, recipeTier, machineTier);
        return !ChancedOutputLogic.passesChance(chance);
    }

    private static void validateChance(int chance) {
        if (chance < 0 || chance > ChancedOutputLogic.getMaxChancedValue()) {
            throw new IllegalArgumentException("Chance must be between 0 and " +
                    ChancedOutputLogic.getMaxChancedValue() + ": " + chance);
        }
    }

    private static List<ChancedItemInputEntry> getSortedItemEntries(ChancedInputRecipePropertyValue propertyValue) {
        List<ChancedItemInputEntry> entries = new ArrayList<ChancedItemInputEntry>(propertyValue.getItemInputs().size());
        for (ChancedItemInputEntry entry : propertyValue.getItemInputs()) {
            entries.add(entry.copy());
        }
        entries.sort(Comparator.comparing(
                entry -> new GTRecipeItemInput(entry.getIngredient(), entry.getIngredient().getCount()),
                GTRecipeInput.RECIPE_INPUT_COMPARATOR));
        return entries;
    }

    private static List<ChancedFluidInputEntry> getSortedFluidEntries(ChancedInputRecipePropertyValue propertyValue) {
        List<ChancedFluidInputEntry> entries = new ArrayList<ChancedFluidInputEntry>(propertyValue.getFluidInputs().size());
        for (ChancedFluidInputEntry entry : propertyValue.getFluidInputs()) {
            entries.add(entry.copy());
        }
        entries.sort(Comparator.comparing(
                entry -> new GTRecipeFluidInput(entry.getIngredient(), entry.getIngredient().amount),
                GTRecipeInput.RECIPE_INPUT_COMPARATOR));
        return entries;
    }

    private static int findMatchingItemEntryIndex(List<ChancedItemInputEntry> entries, GTRecipeInput input) {
        for (int i = 0; i < entries.size(); i++) {
            ItemStack ingredient = entries.get(i).getIngredient();
            GTRecipeItemInput ingredientInput = new GTRecipeItemInput(ingredient, ingredient.getCount());
            if (!input.isNonConsumable() &&
                    input.getAmount() >= ingredient.getCount() &&
                    input.equalIgnoreAmount(ingredientInput)) {
                return i;
            }
        }
        return -1;
    }

    private static int findDisplayedItemEntryIndex(List<ChancedItemInputEntry> entries, List<ItemStack> displayedStacks) {
        if (displayedStacks == null || displayedStacks.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < entries.size(); i++) {
            ItemStack ingredient = entries.get(i).getIngredient();
            for (ItemStack displayed : displayedStacks) {
                if (displayed == null || displayed.isEmpty()) {
                    continue;
                }
                if (displayed.getCount() >= ingredient.getCount() &&
                        ItemStack.areItemsEqual(displayed, ingredient) &&
                        ItemStack.areItemStackTagsEqual(displayed, ingredient)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static int findMatchingFluidEntryIndex(List<ChancedFluidInputEntry> entries, GTRecipeInput input) {
        for (int i = 0; i < entries.size(); i++) {
            FluidStack ingredient = entries.get(i).getIngredient();
            GTRecipeFluidInput ingredientInput = new GTRecipeFluidInput(ingredient, ingredient.amount);
            if (!input.isNonConsumable() &&
                    input.getAmount() >= ingredient.amount &&
                    input.equalIgnoreAmount(ingredientInput)) {
                return i;
            }
        }
        return -1;
    }

    private static int findDisplayedFluidEntryIndex(List<ChancedFluidInputEntry> entries,
                                                    List<FluidStack> displayedFluids) {
        if (displayedFluids == null || displayedFluids.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < entries.size(); i++) {
            FluidStack ingredient = entries.get(i).getIngredient();
            for (FluidStack displayed : displayedFluids) {
                if (displayed == null || displayed.amount <= 0) {
                    continue;
                }
                if (displayed.amount >= ingredient.amount &&
                        displayed.getFluid().getName().equals(ingredient.getFluid().getName()) &&
                        FluidStack.areFluidStackTagsEqual(displayed, ingredient)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static IRecipePropertyStorage getRecipePropertyStorage(RecipeBuilder<?> builder) {
        try {
            return (IRecipePropertyStorage) RECIPE_PROPERTY_STORAGE_FIELD.get(builder);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to access RecipeBuilder.recipePropertyStorage", e);
        }
    }

    private static void setRecipePropertyStorageErrored(RecipeBuilder<?> builder, boolean errored) {
        try {
            RECIPE_PROPERTY_STORAGE_ERRORED_FIELD.setBoolean(builder, errored);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to access RecipeBuilder.recipePropertyStorageErrored", e);
        }
    }

    private static Field findField(String name) {
        try {
            Field field = RecipeBuilder.class.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to resolve RecipeBuilder field " + name, e);
        }
    }
}
