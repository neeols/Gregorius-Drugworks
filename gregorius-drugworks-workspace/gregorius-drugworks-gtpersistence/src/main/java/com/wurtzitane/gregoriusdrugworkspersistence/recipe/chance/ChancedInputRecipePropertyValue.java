package com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ChancedInputRecipePropertyValue {

    private static final ChancedInputRecipePropertyValue EMPTY = new ChancedInputRecipePropertyValue(
            Collections.<ChancedItemInputEntry>emptyList(),
            Collections.<ChancedFluidInputEntry>emptyList());

    private final List<ChancedItemInputEntry> itemInputs;
    private final List<ChancedFluidInputEntry> fluidInputs;

    private ChancedInputRecipePropertyValue(List<ChancedItemInputEntry> itemInputs,
                                            List<ChancedFluidInputEntry> fluidInputs) {
        this.itemInputs = Collections.unmodifiableList(itemInputs);
        this.fluidInputs = Collections.unmodifiableList(fluidInputs);
    }

    public static ChancedInputRecipePropertyValue empty() {
        return EMPTY;
    }

    public static ChancedInputRecipePropertyValue ofItem(ChancedItemInputEntry entry) {
        List<ChancedItemInputEntry> items = new ArrayList<ChancedItemInputEntry>(1);
        items.add(entry.copy());
        return new ChancedInputRecipePropertyValue(items, Collections.<ChancedFluidInputEntry>emptyList());
    }

    public static ChancedInputRecipePropertyValue ofFluid(ChancedFluidInputEntry entry) {
        List<ChancedFluidInputEntry> fluids = new ArrayList<ChancedFluidInputEntry>(1);
        fluids.add(entry.copy());
        return new ChancedInputRecipePropertyValue(Collections.<ChancedItemInputEntry>emptyList(), fluids);
    }

    public static ChancedInputRecipePropertyValue merge(ChancedInputRecipePropertyValue first,
                                                        ChancedInputRecipePropertyValue second) {
        if (first == null || first.isEmpty()) {
            return second == null ? empty() : second.copy();
        }
        if (second == null || second.isEmpty()) {
            return first.copy();
        }
        List<ChancedItemInputEntry> items = copyItems(first.itemInputs.size() + second.itemInputs.size(), first.itemInputs);
        appendItems(items, second.itemInputs);
        List<ChancedFluidInputEntry> fluids = copyFluids(first.fluidInputs.size() + second.fluidInputs.size(), first.fluidInputs);
        appendFluids(fluids, second.fluidInputs);
        return new ChancedInputRecipePropertyValue(items, fluids);
    }

    public ChancedInputRecipePropertyValue copy() {
        if (isEmpty()) {
            return empty();
        }
        return new ChancedInputRecipePropertyValue(
                copyItems(itemInputs.size(), itemInputs),
                copyFluids(fluidInputs.size(), fluidInputs));
    }

    public ChancedInputRecipePropertyValue multiplied(int multiplier) {
        if (multiplier <= 0 || isEmpty()) {
            return empty();
        }
        if (multiplier == 1) {
            return copy();
        }
        List<ChancedItemInputEntry> items = new ArrayList<ChancedItemInputEntry>(itemInputs.size() * multiplier);
        for (int i = 0; i < multiplier; i++) {
            for (ChancedItemInputEntry entry : itemInputs) {
                items.add(entry.copy());
            }
        }
        List<ChancedFluidInputEntry> fluids = new ArrayList<ChancedFluidInputEntry>(fluidInputs.size() * multiplier);
        for (int i = 0; i < multiplier; i++) {
            for (ChancedFluidInputEntry entry : fluidInputs) {
                fluids.add(entry.copy());
            }
        }
        return new ChancedInputRecipePropertyValue(items, fluids);
    }

    public boolean isEmpty() {
        return itemInputs.isEmpty() && fluidInputs.isEmpty();
    }

    public List<ChancedItemInputEntry> getItemInputs() {
        return itemInputs;
    }

    public List<ChancedFluidInputEntry> getFluidInputs() {
        return fluidInputs;
    }

    private static List<ChancedItemInputEntry> copyItems(int capacity, List<ChancedItemInputEntry> source) {
        List<ChancedItemInputEntry> items = new ArrayList<ChancedItemInputEntry>(capacity);
        appendItems(items, source);
        return items;
    }

    private static List<ChancedFluidInputEntry> copyFluids(int capacity, List<ChancedFluidInputEntry> source) {
        List<ChancedFluidInputEntry> fluids = new ArrayList<ChancedFluidInputEntry>(capacity);
        appendFluids(fluids, source);
        return fluids;
    }

    private static void appendItems(List<ChancedItemInputEntry> target, List<ChancedItemInputEntry> source) {
        for (ChancedItemInputEntry entry : source) {
            target.add(entry.copy());
        }
    }

    private static void appendFluids(List<ChancedFluidInputEntry> target, List<ChancedFluidInputEntry> source) {
        for (ChancedFluidInputEntry entry : source) {
            target.add(entry.copy());
        }
    }
}
