package com.wurtzitane.gregoriusdrugworkspersistence.inhalation;

import net.minecraft.item.ItemStack;

public final class InhalationRemainderSpec {

    private final ItemStack stack;
    private final float chance;
    private final boolean dropIfInventoryFull;

    public InhalationRemainderSpec(ItemStack stack, float chance, boolean dropIfInventoryFull) {
        this.stack = stack;
        this.chance = chance;
        this.dropIfInventoryFull = dropIfInventoryFull;
    }

    public ItemStack createStackCopy() {
        return stack.copy();
    }

    public float getChance() {
        return chance;
    }

    public boolean isDropIfInventoryFull() {
        return dropIfInventoryFull;
    }
}