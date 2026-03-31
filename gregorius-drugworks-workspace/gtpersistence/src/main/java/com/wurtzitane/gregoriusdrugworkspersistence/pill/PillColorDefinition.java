package com.wurtzitane.gregoriusdrugworkspersistence.pill;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dual-tone pill shell color entry with its crafting dye source.
 *
 * @author wurtzitane
 */
public final class PillColorDefinition {

    private final String id;
    private final String displayName;
    private final int rgb;
    private final List<ItemStack> dyeStacks;
    private final List<String> oreDictNames;

    private PillColorDefinition(Builder builder) {
        this.id = builder.id;
        this.displayName = builder.displayName;
        this.rgb = builder.rgb;
        this.dyeStacks = new ArrayList<>(builder.dyeStacks.size());
        for (ItemStack dyeStack : builder.dyeStacks) {
            if (dyeStack.isEmpty()) {
                continue;
            }
            ItemStack copy = dyeStack.copy();
            copy.setCount(1);
            this.dyeStacks.add(copy);
        }
        this.oreDictNames = Collections.unmodifiableList(new ArrayList<>(builder.oreDictNames));
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getRgb() {
        return rgb;
    }

    public ItemStack getDyeStack() {
        return dyeStacks.isEmpty() ? ItemStack.EMPTY : dyeStacks.get(0).copy();
    }

    public boolean matchesDye(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        for (ItemStack dyeStack : dyeStacks) {
            if (stack.getItem() == dyeStack.getItem() && stack.getMetadata() == dyeStack.getMetadata()) {
                return true;
            }
        }

        if (oreDictNames.isEmpty()) {
            return false;
        }

        int[] oreIds = OreDictionary.getOreIDs(stack);
        for (int oreId : oreIds) {
            String oreName = OreDictionary.getOreName(oreId);
            if (oreDictNames.contains(oreName)) {
                return true;
            }
        }
        return false;
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static final class Builder {
        private final String id;
        private String displayName;
        private int rgb = 0xFFFFFF;
        private final List<ItemStack> dyeStacks = new ArrayList<>();
        private final List<String> oreDictNames = new ArrayList<>();

        private Builder(String id) {
            this.id = id;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder rgb(int rgb) {
            this.rgb = rgb;
            return this;
        }

        public Builder dye(ItemStack dyeStack) {
            this.dyeStacks.clear();
            return addDye(dyeStack);
        }

        public Builder addDye(ItemStack dyeStack) {
            if (!dyeStack.isEmpty()) {
                this.dyeStacks.add(dyeStack.copy());
            }
            return this;
        }

        public Builder oreDict(String oreDictName) {
            if (oreDictName != null && !oreDictName.trim().isEmpty()) {
                this.oreDictNames.add(oreDictName.trim());
            }
            return this;
        }

        public PillColorDefinition build() {
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalStateException("Pill color id must be set.");
            }
            if (displayName == null || displayName.trim().isEmpty()) {
                throw new IllegalStateException("Pill color display name must be set for " + id);
            }
            if (dyeStacks.isEmpty() && oreDictNames.isEmpty()) {
                throw new IllegalStateException("Pill color dye source must be set for " + id);
            }
            return new PillColorDefinition(this);
        }
    }
}
