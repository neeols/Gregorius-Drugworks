package com.wurtzitane.gregoriusdrugworkspersistence.pill;

import net.minecraft.item.ItemStack;

/**
 * Dual-tone pill shell color entry with its crafting dye source.
 *
 * @author wurtzitane
 */
public final class PillColorDefinition {

    private final String id;
    private final String displayName;
    private final int rgb;
    private final ItemStack dyeStack;

    private PillColorDefinition(Builder builder) {
        this.id = builder.id;
        this.displayName = builder.displayName;
        this.rgb = builder.rgb;
        this.dyeStack = builder.dyeStack.copy();
        this.dyeStack.setCount(1);
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
        return dyeStack.copy();
    }

    public boolean matchesDye(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        return stack.getItem() == dyeStack.getItem()
                && stack.getMetadata() == dyeStack.getMetadata();
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static final class Builder {
        private final String id;
        private String displayName;
        private int rgb = 0xFFFFFF;
        private ItemStack dyeStack = ItemStack.EMPTY;

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
            this.dyeStack = dyeStack.copy();
            return this;
        }

        public PillColorDefinition build() {
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalStateException("Pill color id must be set.");
            }
            if (displayName == null || displayName.trim().isEmpty()) {
                throw new IllegalStateException("Pill color display name must be set for " + id);
            }
            if (dyeStack.isEmpty()) {
                throw new IllegalStateException("Pill color dye stack must be set for " + id);
            }
            return new PillColorDefinition(this);
        }
    }
}
