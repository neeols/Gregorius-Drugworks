package com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance;

import gregtech.api.recipes.chance.BaseChanceEntry;
import gregtech.api.recipes.chance.boost.BoostableChanceEntry;
import net.minecraft.item.ItemStack;

public class ChancedItemInputEntry extends BaseChanceEntry<ItemStack> implements BoostableChanceEntry<ItemStack> {

    private final int chanceBoost;

    public ChancedItemInputEntry(ItemStack ingredient, int chance, int chanceBoost) {
        super(ingredient.copy(), chance);
        this.chanceBoost = chanceBoost;
    }

    @Override
    public ItemStack getIngredient() {
        return super.getIngredient().copy();
    }

    @Override
    public int getChanceBoost() {
        return chanceBoost;
    }

    public ChancedItemInputEntry copy() {
        return new ChancedItemInputEntry(super.getIngredient().copy(), getChance(), chanceBoost);
    }
}
