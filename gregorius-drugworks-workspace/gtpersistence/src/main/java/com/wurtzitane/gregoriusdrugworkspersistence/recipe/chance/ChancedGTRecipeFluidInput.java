package com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance;

import gregtech.api.recipes.ingredients.GTRecipeFluidInput;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

public class ChancedGTRecipeFluidInput extends GTRecipeFluidInput {

    private final ChancedFluidInputEntry chancedEntry;

    public ChancedGTRecipeFluidInput(FluidStack stack, int chance, int chanceBoost) {
        this(stack, chance, chanceBoost, 0);
    }

    public ChancedGTRecipeFluidInput(FluidStack stack, int chance, int chanceBoost, int tierReductionRate) {
        this(new ChancedFluidInputEntry(stack, chance, chanceBoost, tierReductionRate), stack.amount);
    }

    private ChancedGTRecipeFluidInput(ChancedFluidInputEntry chancedEntry, int amount) {
        super(withAmount(chancedEntry.getIngredient(), amount), amount);
        this.isConsumable = false;
        this.chancedEntry = new ChancedFluidInputEntry(withAmount(chancedEntry.getIngredient(), amount),
                chancedEntry.getChance(), chancedEntry.getChanceBoost(), chancedEntry.getTierReductionRate());
    }

    public ChancedFluidInputEntry getChancedEntry() {
        return chancedEntry.copy();
    }

    @Override
    protected ChancedGTRecipeFluidInput copy() {
        ChancedGTRecipeFluidInput copy = new ChancedGTRecipeFluidInput(this.chancedEntry, this.amount);
        copy.isConsumable = this.isConsumable;
        copy.nbtMatcher = this.nbtMatcher;
        copy.nbtCondition = this.nbtCondition;
        return copy;
    }

    @Override
    public GTRecipeInput copyWithAmount(int amount) {
        ChancedGTRecipeFluidInput copy = new ChancedGTRecipeFluidInput(this.chancedEntry, amount);
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
        if (!(obj instanceof ChancedGTRecipeFluidInput other)) {
            return false;
        }
        return super.equals(other) &&
                chancedEntry.getChance() == other.chancedEntry.getChance() &&
                chancedEntry.getChanceBoost() == other.chancedEntry.getChanceBoost() &&
                chancedEntry.getTierReductionRate() == other.chancedEntry.getTierReductionRate();
    }

    @Override
    public boolean equalIgnoreAmount(GTRecipeInput input) {
        if (!(input instanceof ChancedGTRecipeFluidInput other)) {
            return false;
        }
        return super.equalIgnoreAmount(other) &&
                chancedEntry.getChance() == other.chancedEntry.getChance() &&
                chancedEntry.getChanceBoost() == other.chancedEntry.getChanceBoost() &&
                chancedEntry.getTierReductionRate() == other.chancedEntry.getTierReductionRate();
    }

    private static FluidStack withAmount(FluidStack stack, int amount) {
        FluidStack copy = stack.copy();
        copy.amount = amount;
        return copy;
    }
}
