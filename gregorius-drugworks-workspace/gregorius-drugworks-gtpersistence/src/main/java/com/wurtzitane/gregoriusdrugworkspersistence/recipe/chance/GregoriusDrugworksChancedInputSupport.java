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
import gregtech.api.recipes.properties.RecipePropertyStorage;
import gregtech.api.recipes.properties.RecipePropertyStorageImpl;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GregoriusDrugworksChancedInputSupport {

    public static final int DEFAULT_CATALYST_TIER_REDUCTION_RATE = 1000;

    private static final Field RECIPE_PROPERTY_STORAGE_FIELD = findField("recipePropertyStorage");
    private static final Field RECIPE_PROPERTY_STORAGE_ERRORED_FIELD = findField("recipePropertyStorageErrored");

    private GregoriusDrugworksChancedInputSupport() {
    }

    public static <R extends RecipeBuilder<R>> R chancedItemInput(R builder, ItemStack stack, int chance, int chanceBoost) {
        return chancedItemInput(builder, stack, chance, chanceBoost, 0);
    }

    public static <R extends RecipeBuilder<R>> R chancedItemInput(R builder, ItemStack stack, int chance, int chanceBoost,
                                                                  int tierReductionRate) {
        validateChance(chance);
        validateTierReductionRate(tierReductionRate);
        if (stack == null || stack.isEmpty()) {
            return builder;
        }
        ItemStack copy = stack.copy();
        builder.input(new ChancedGTRecipeItemInput(copy, chance, chanceBoost, tierReductionRate));
        mergePropertyIntoBuilder(builder, ChancedInputRecipePropertyValue.ofItem(
                new ChancedItemInputEntry(copy, chance, chanceBoost, tierReductionRate)));
        return builder;
    }

    public static <R extends RecipeBuilder<R>> R chancedFluidInput(R builder, FluidStack stack, int chance, int chanceBoost) {
        return chancedFluidInput(builder, stack, chance, chanceBoost, 0);
    }

    public static <R extends RecipeBuilder<R>> R chancedFluidInput(R builder, FluidStack stack, int chance, int chanceBoost,
                                                                   int tierReductionRate) {
        validateChance(chance);
        validateTierReductionRate(tierReductionRate);
        if (stack == null || stack.amount <= 0) {
            return builder;
        }
        FluidStack copy = stack.copy();
        builder.fluidInputs(new ChancedGTRecipeFluidInput(copy, chance, chanceBoost, tierReductionRate));
        mergePropertyIntoBuilder(builder, ChancedInputRecipePropertyValue.ofFluid(
                new ChancedFluidInputEntry(copy, chance, chanceBoost, tierReductionRate)));
        return builder;
    }

    public static <R extends RecipeBuilder<R>> R chancedCatalystItemInput(R builder, ItemStack stack, int chance,
                                                                           int chanceBoost) {
        return chancedItemInput(builder, stack, chance, chanceBoost, DEFAULT_CATALYST_TIER_REDUCTION_RATE);
    }

    public static void mergePropertyIntoBuilder(RecipeBuilder<?> builder, ChancedInputRecipePropertyValue addition) {
        if (builder == null || addition == null || addition.isEmpty()) {
            return;
        }
        ChancedInputRecipePropertyValue merged = ChancedInputRecipePropertyValue.merge(getBuilderProperty(builder), addition);
        setBuilderProperty(builder, merged);
    }

    public static ChancedInputRecipePropertyValue getBuilderProperty(RecipeBuilder<?> builder) {
        RecipePropertyStorage storage = getRecipePropertyStorage(builder);
        if (storage == null) {
            return null;
        }
        return storage.get(ChancedInputRecipeProperty.getInstance(), null);
    }

    public static void setBuilderProperty(RecipeBuilder<?> builder, ChancedInputRecipePropertyValue value) {
        RecipePropertyStorage storage = getRecipePropertyStorage(builder);
        RecipePropertyStorage rebuiltStorage = RecipePropertyStorage.EMPTY;
        if (storage != null && storage != RecipePropertyStorage.EMPTY) {
            RecipePropertyStorageImpl mutableStorage = new RecipePropertyStorageImpl();
            for (Map.Entry<?, ?> rawEntry : storage.entrySet()) {
                @SuppressWarnings("unchecked")
                Map.Entry<gregtech.api.recipes.properties.RecipeProperty<?>, Object> entry =
                        (Map.Entry<gregtech.api.recipes.properties.RecipeProperty<?>, Object>) rawEntry;
                if (ChancedInputRecipeProperty.getInstance().equals(entry.getKey())) {
                    continue;
                }
                mutableStorage.store(entry.getKey(), entry.getValue());
            }
            if (mutableStorage.size() > 0) {
                rebuiltStorage = mutableStorage;
            }
        }
        setRecipePropertyStorage(builder, rebuiltStorage);
        if (value == null || value.isEmpty()) {
            return;
        }
        if (!builder.applyProperty(ChancedInputRecipeProperty.getInstance(), value.copy())) {
            setRecipePropertyStorageErrored(builder, true);
        }
    }

    public static boolean matchesAndConsumeRecipeInputs(Recipe recipe,
                                                        RecipeMap<?> recipeMap,
                                                        int machineTier,
                                                        boolean consumeIfSuccessful,
                                                        IItemHandlerModifiable inputInventory,
                                                        IMultipleTankHandler inputFluids) {
        ChancedInputRecipePropertyValue propertyValue = recipe.getProperty(ChancedInputRecipeProperty.getInstance(), null);
        if (propertyValue == null || propertyValue.isEmpty()) {
            return recipe.matches(consumeIfSuccessful, inputInventory, inputFluids);
        }
        if (!consumeIfSuccessful) {
            return recipe.matches(false, inputInventory, inputFluids);
        }

        List<ItemStack> simulatedItems = copyItemInputs(inputInventory);
        List<FluidStack> simulatedFluids = copyFluidInputs(inputFluids);
        if (!recipe.matches(true, simulatedItems, simulatedFluids)) {
            return false;
        }

        int recipeTier = GTUtility.getTierByVoltage(recipe.getEUt());
        ChancedInputConsumptionPlan consumptionPlan = createConsumptionPlan(
                propertyValue,
                recipeMap,
                recipeTier,
                machineTier,
                simulatedItems,
                simulatedFluids);
        if (consumptionPlan == null) {
            return false;
        }

        if (!recipe.matches(true, inputInventory, inputFluids)) {
            return false;
        }

        applyConsumptionPlan(consumptionPlan, inputInventory, inputFluids);
        return true;
    }

    public static ChancedItemInputEntry getChancedItemInputEntry(Recipe recipe, List<GTRecipeInput> sortedInputs,
                                                                 int slotIndex) {
        if (slotIndex >= 0 && slotIndex < sortedInputs.size()) {
            GTRecipeInput input = sortedInputs.get(slotIndex);
            if (input instanceof ChancedGTRecipeItemInput) {
                return ((ChancedGTRecipeItemInput) input).getChancedEntry();
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
            if (input instanceof ChancedGTRecipeFluidInput) {
                return ((ChancedGTRecipeFluidInput) input).getChancedEntry();
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

    public static int getTierReducedChance(int baseChance, int recipeTier, int machineTier, int tierReductionRate) {
        int tierDiff = machineTier - recipeTier;
        if (tierDiff <= 0 || tierReductionRate <= 0) {
            return baseChance;
        }

        int chance = baseChance;
        for (int i = 0; i < tierDiff; i++) {
            chance = (int) Math.round(chance * (10000.0D - tierReductionRate) / 10000.0D);
        }
        return Math.max(0, chance);
    }

    public static double getTierReductionPerTierDisplay(int baseChance, int tierReductionRate) {
        return (baseChance / 100.0D) * (tierReductionRate / 10000.0D);
    }

    private static void validateChance(int chance) {
        if (chance < 0 || chance > ChancedOutputLogic.getMaxChancedValue()) {
            throw new IllegalArgumentException("Chance must be between 0 and " +
                    ChancedOutputLogic.getMaxChancedValue() + ": " + chance);
        }
    }

    private static void validateTierReductionRate(int tierReductionRate) {
        if (tierReductionRate < 0 || tierReductionRate > 10000) {
            throw new IllegalArgumentException("Tier reduction rate must be between 0 and 10000: " +
                    tierReductionRate);
        }
    }

    private static ChancedInputConsumptionPlan createConsumptionPlan(ChancedInputRecipePropertyValue propertyValue,
                                                                     RecipeMap<?> recipeMap,
                                                                     int recipeTier,
                                                                     int machineTier,
                                                                     List<ItemStack> simulatedItems,
                                                                     List<FluidStack> simulatedFluids) {
        ChanceBoostFunction boostFunction = recipeMap == null ? ChanceBoostFunction.NONE : recipeMap.getChanceFunction();
        int[] itemExtraction = new int[simulatedItems.size()];
        int[] fluidDrain = new int[simulatedFluids.size()];

        for (ChancedItemInputEntry entry : propertyValue.getItemInputs()) {
            if (!passesInputChance(entry, boostFunction, recipeTier, machineTier)) {
                continue;
            }
            if (!reserveItemConsumption(entry, simulatedItems, itemExtraction)) {
                return null;
            }
        }

        for (ChancedFluidInputEntry entry : propertyValue.getFluidInputs()) {
            if (!passesInputChance(entry, boostFunction, recipeTier, machineTier)) {
                continue;
            }
            if (!reserveFluidConsumption(entry, simulatedFluids, fluidDrain)) {
                return null;
            }
        }

        return new ChancedInputConsumptionPlan(itemExtraction, fluidDrain);
    }

    private static void applyConsumptionPlan(ChancedInputConsumptionPlan consumptionPlan,
                                             IItemHandlerModifiable inputInventory,
                                             IMultipleTankHandler inputFluids) {
        for (int slot = 0; slot < consumptionPlan.itemExtraction.length; slot++) {
            int amount = consumptionPlan.itemExtraction[slot];
            if (amount <= 0) {
                continue;
            }
            ItemStack extracted = inputInventory.extractItem(slot, amount, false);
            if (extracted.isEmpty() || extracted.getCount() != amount) {
                GTLog.logger.error("Failed to consume chanced item input from slot {}. Expected {}, extracted {}",
                        slot, amount, extracted.isEmpty() ? 0 : extracted.getCount());
            }
        }

        List<IMultipleTankHandler.ITankEntry> tanks = inputFluids.getFluidTanks();
        for (int tankIndex = 0; tankIndex < consumptionPlan.fluidDrain.length && tankIndex < tanks.size(); tankIndex++) {
            int amount = consumptionPlan.fluidDrain[tankIndex];
            if (amount <= 0) {
                continue;
            }
            FluidStack drained = tanks.get(tankIndex).drain(amount, true);
            if (drained == null || drained.amount != amount) {
                GTLog.logger.error("Failed to consume chanced fluid input from tank {}. Expected {}, drained {}",
                        tankIndex, amount, drained == null ? 0 : drained.amount);
            }
        }
    }

    private static boolean reserveItemConsumption(ChancedItemInputEntry entry,
                                                  List<ItemStack> simulatedItems,
                                                  int[] itemExtraction) {
        ItemStack ingredient = entry.getIngredient();
        int remaining = ingredient.getCount();
        for (int slot = 0; slot < simulatedItems.size(); slot++) {
            ItemStack stackInSlot = simulatedItems.get(slot);
            if (!matchesItemIngredient(stackInSlot, ingredient)) {
                continue;
            }
            int extracted = Math.min(stackInSlot.getCount(), remaining);
            if (extracted <= 0) {
                continue;
            }
            itemExtraction[slot] += extracted;
            remaining -= extracted;
            stackInSlot.shrink(extracted);
            if (stackInSlot.isEmpty()) {
                simulatedItems.set(slot, ItemStack.EMPTY);
            }
            if (remaining == 0) {
                return true;
            }
        }
        return false;
    }

    private static boolean reserveFluidConsumption(ChancedFluidInputEntry entry,
                                                   List<FluidStack> simulatedFluids,
                                                   int[] fluidDrain) {
        FluidStack ingredient = entry.getIngredient();
        int remaining = ingredient.amount;
        for (int tankIndex = 0; tankIndex < simulatedFluids.size(); tankIndex++) {
            FluidStack tankFluid = simulatedFluids.get(tankIndex);
            if (!matchesFluidIngredient(tankFluid, ingredient)) {
                continue;
            }
            int drained = Math.min(tankFluid.amount, remaining);
            if (drained <= 0) {
                continue;
            }
            fluidDrain[tankIndex] += drained;
            remaining -= drained;
            tankFluid.amount -= drained;
            if (tankFluid.amount <= 0) {
                simulatedFluids.set(tankIndex, null);
            }
            if (remaining == 0) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchesItemIngredient(ItemStack stackInSlot, ItemStack ingredient) {
        return stackInSlot != null &&
                !stackInSlot.isEmpty() &&
                stackInSlot.getCount() > 0 &&
                ItemStack.areItemsEqual(stackInSlot, ingredient) &&
                ItemStack.areItemStackTagsEqual(stackInSlot, ingredient);
    }

    private static boolean matchesFluidIngredient(FluidStack tankFluid, FluidStack ingredient) {
        return tankFluid != null &&
                tankFluid.amount > 0 &&
                tankFluid.isFluidEqual(ingredient) &&
                FluidStack.areFluidStackTagsEqual(tankFluid, ingredient);
    }

    private static boolean passesInputChance(ChancedItemInputEntry entry,
                                             ChanceBoostFunction boostFunction,
                                             int recipeTier,
                                             int machineTier) {
        return ChancedOutputLogic.passesChance(getEffectiveChance(entry, boostFunction, recipeTier, machineTier));
    }

    private static boolean passesInputChance(ChancedFluidInputEntry entry,
                                             ChanceBoostFunction boostFunction,
                                             int recipeTier,
                                             int machineTier) {
        return ChancedOutputLogic.passesChance(getEffectiveChance(entry, boostFunction, recipeTier, machineTier));
    }

    private static int getEffectiveChance(ChancedItemInputEntry entry,
                                          ChanceBoostFunction boostFunction,
                                          int recipeTier,
                                          int machineTier) {
        if (entry.getTierReductionRate() > 0) {
            return getTierReducedChance(entry.getChance(), recipeTier, machineTier, entry.getTierReductionRate());
        }
        return boostFunction.getBoostedChance(entry, recipeTier, machineTier);
    }

    private static int getEffectiveChance(ChancedFluidInputEntry entry,
                                          ChanceBoostFunction boostFunction,
                                          int recipeTier,
                                          int machineTier) {
        if (entry.getTierReductionRate() > 0) {
            return getTierReducedChance(entry.getChance(), recipeTier, machineTier, entry.getTierReductionRate());
        }
        return boostFunction.getBoostedChance(entry, recipeTier, machineTier);
    }

    private static List<ItemStack> copyItemInputs(IItemHandlerModifiable inputInventory) {
        List<ItemStack> copiedInputs = new ArrayList<ItemStack>(inputInventory.getSlots());
        for (int slot = 0; slot < inputInventory.getSlots(); slot++) {
            copiedInputs.add(GTUtility.copy(inputInventory.getStackInSlot(slot)));
        }
        return copiedInputs;
    }

    private static List<FluidStack> copyFluidInputs(IMultipleTankHandler inputFluids) {
        List<IMultipleTankHandler.ITankEntry> tanks = inputFluids.getFluidTanks();
        List<FluidStack> copiedInputs = new ArrayList<FluidStack>(tanks.size());
        for (IMultipleTankHandler.ITankEntry tank : tanks) {
            FluidStack stack = tank.getFluid();
            copiedInputs.add(stack == null ? null : stack.copy());
        }
        return copiedInputs;
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
            if (input.getAmount() >= ingredient.getCount() && input.equalIgnoreAmount(ingredientInput)) {
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
            if (input.getAmount() >= ingredient.amount && input.equalIgnoreAmount(ingredientInput)) {
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

    private static RecipePropertyStorage getRecipePropertyStorage(RecipeBuilder<?> builder) {
        try {
            return (RecipePropertyStorage) RECIPE_PROPERTY_STORAGE_FIELD.get(builder);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to access RecipeBuilder.recipePropertyStorage", e);
        }
    }

    private static void setRecipePropertyStorage(RecipeBuilder<?> builder, RecipePropertyStorage storage) {
        try {
            RECIPE_PROPERTY_STORAGE_FIELD.set(builder, storage);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to mutate RecipeBuilder.recipePropertyStorage", e);
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

    private static final class ChancedInputConsumptionPlan {

        private final int[] itemExtraction;
        private final int[] fluidDrain;

        private ChancedInputConsumptionPlan(int[] itemExtraction, int[] fluidDrain) {
            this.itemExtraction = itemExtraction;
            this.fluidDrain = fluidDrain;
        }
    }
}
