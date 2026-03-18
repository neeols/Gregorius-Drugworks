package com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance;

import gregtech.api.recipes.chance.BaseChanceEntry;
import gregtech.api.recipes.chance.boost.BoostableChanceEntry;
import net.minecraftforge.fluids.FluidStack;

public class ChancedFluidInputEntry extends BaseChanceEntry<FluidStack> implements BoostableChanceEntry<FluidStack> {

    private final int chanceBoost;

    public ChancedFluidInputEntry(FluidStack ingredient, int chance, int chanceBoost) {
        super(ingredient.copy(), chance);
        this.chanceBoost = chanceBoost;
    }

    @Override
    public FluidStack getIngredient() {
        return super.getIngredient().copy();
    }

    @Override
    public int getChanceBoost() {
        return chanceBoost;
    }

    public ChancedFluidInputEntry copy() {
        return new ChancedFluidInputEntry(super.getIngredient().copy(), getChance(), chanceBoost);
    }
}
