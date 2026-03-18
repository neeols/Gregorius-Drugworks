package com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance;

import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.ingredients.GTRecipeItemInput;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class ChancedGTRecipeItemInput extends GTRecipeItemInput {

    private final ChancedItemInputEntry chancedEntry;

    public ChancedGTRecipeItemInput(ItemStack stack, int chance, int chanceBoost) {
        this(new ChancedItemInputEntry(stack, chance, chanceBoost), stack.getCount());
    }

    private ChancedGTRecipeItemInput(ChancedItemInputEntry chancedEntry, int amount) {
        super(withAmount(chancedEntry.getIngredient(), amount), amount);
        this.chancedEntry = new ChancedItemInputEntry(withAmount(chancedEntry.getIngredient(), amount),
                chancedEntry.getChance(), chancedEntry.getChanceBoost());
    }

    public ChancedItemInputEntry getChancedEntry() {
        return chancedEntry.copy();
    }

    @Override
    protected ChancedGTRecipeItemInput copy() {
        ChancedGTRecipeItemInput copy = new ChancedGTRecipeItemInput(this.chancedEntry, this.amount);
        copy.isConsumable = this.isConsumable;
        copy.nbtMatcher = this.nbtMatcher;
        copy.nbtCondition = this.nbtCondition;
        return copy;
    }

    @Override
    public GTRecipeInput copyWithAmount(int amount) {
        ChancedGTRecipeItemInput copy = new ChancedGTRecipeItemInput(this.chancedEntry, amount);
        copy.isConsumable = this.isConsumable;
        copy.nbtMatcher = this.nbtMatcher;
        copy.nbtCondition = this.nbtCondition;
        return copy;
    }

    @Override
    protected int computeHash() {
        return Objects.hash(super.computeHash(), chancedEntry.getChance(), chancedEntry.getChanceBoost());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChancedGTRecipeItemInput other)) {
            return false;
        }
        return super.equals(other) &&
                chancedEntry.getChance() == other.chancedEntry.getChance() &&
                chancedEntry.getChanceBoost() == other.chancedEntry.getChanceBoost();
    }

    @Override
    public boolean equalIgnoreAmount(GTRecipeInput input) {
        if (!(input instanceof ChancedGTRecipeItemInput other)) {
            return false;
        }
        return super.equalIgnoreAmount(other) &&
                chancedEntry.getChance() == other.chancedEntry.getChance() &&
                chancedEntry.getChanceBoost() == other.chancedEntry.getChanceBoost();
    }

    private static ItemStack withAmount(ItemStack stack, int amount) {
        ItemStack copy = stack.copy();
        copy.setCount(amount);
        return copy;
    }
}
