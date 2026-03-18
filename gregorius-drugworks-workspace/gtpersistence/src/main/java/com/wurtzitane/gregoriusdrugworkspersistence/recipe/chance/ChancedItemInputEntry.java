package com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance;

import gregtech.api.recipes.chance.BaseChanceEntry;
import gregtech.api.recipes.chance.boost.BoostableChanceEntry;
import net.minecraft.item.ItemStack;

public class ChancedItemInputEntry extends BaseChanceEntry<ItemStack> implements BoostableChanceEntry<ItemStack> {

    private final int chanceBoost;
    private final int tierReductionRate;

    public ChancedItemInputEntry(ItemStack ingredient, int chance, int chanceBoost) {
        this(ingredient, chance, chanceBoost, 0);
    }

    public ChancedItemInputEntry(ItemStack ingredient, int chance, int chanceBoost, int tierReductionRate) {
        super(ingredient.copy(), chance);
        this.chanceBoost = chanceBoost;
        this.tierReductionRate = tierReductionRate;
    }

    @Override
    public ItemStack getIngredient() {
        return super.getIngredient().copy();
    }

    @Override
    public int getChanceBoost() {
        return chanceBoost;
    }

    public int getTierReductionRate() {
        return tierReductionRate;
    }

    public double getTierReductionPerTierDisplay() {
        return GregoriusDrugworksChancedInputSupport.getTierReductionPerTierDisplay(getChance(), tierReductionRate);
    }

    public ChancedItemInputEntry copy() {
        return new ChancedItemInputEntry(super.getIngredient().copy(), getChance(), chanceBoost, tierReductionRate);
    }
}
