package com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance;

import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.ingredients.GTRecipeItemInput;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class ChancedGTRecipeItemInput extends GTRecipeItemInput {

    private final ChancedItemInputEntry chancedEntry;

    public ChancedGTRecipeItemInput(ItemStack stack, int chance, int chanceBoost) {
        this(stack, chance, chanceBoost, 0);
    }

    public ChancedGTRecipeItemInput(ItemStack stack, int chance, int chanceBoost, int tierReductionRate) {
        this(new ChancedItemInputEntry(stack, chance, chanceBoost, tierReductionRate), stack.getCount());
    }

    private ChancedGTRecipeItemInput(ChancedItemInputEntry chancedEntry, int amount) {
        super(withAmount(chancedEntry.getIngredient(), amount), amount);
        this.isConsumable = false;
        this.chancedEntry = new ChancedItemInputEntry(withAmount(chancedEntry.getIngredient(), amount),
                chancedEntry.getChance(), chancedEntry.getChanceBoost(), chancedEntry.getTierReductionRate());
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
        return Objects.hash(super.computeHash(), chancedEntry.getChance(), chancedEntry.getChanceBoost(),
                chancedEntry.getTierReductionRate());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChancedGTRecipeItemInput)) {
            return false;
        }
        ChancedGTRecipeItemInput other = (ChancedGTRecipeItemInput) obj;
        return super.equals(other) &&
                chancedEntry.getChance() == other.chancedEntry.getChance() &&
                chancedEntry.getChanceBoost() == other.chancedEntry.getChanceBoost() &&
                chancedEntry.getTierReductionRate() == other.chancedEntry.getTierReductionRate();
    }

    @Override
    public boolean equalIgnoreAmount(GTRecipeInput input) {
        if (!(input instanceof ChancedGTRecipeItemInput)) {
            return false;
        }
        ChancedGTRecipeItemInput other = (ChancedGTRecipeItemInput) input;
        return super.equalIgnoreAmount(other) &&
                chancedEntry.getChance() == other.chancedEntry.getChance() &&
                chancedEntry.getChanceBoost() == other.chancedEntry.getChanceBoost() &&
                chancedEntry.getTierReductionRate() == other.chancedEntry.getTierReductionRate();
    }

    private static ItemStack withAmount(ItemStack stack, int amount) {
        ItemStack copy = stack.copy();
        copy.setCount(amount);
        return copy;
    }
}
