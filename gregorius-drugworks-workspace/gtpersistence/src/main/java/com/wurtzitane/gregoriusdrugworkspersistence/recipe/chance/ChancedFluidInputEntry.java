package com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance;

import gregtech.api.recipes.chance.BaseChanceEntry;
import gregtech.api.recipes.chance.boost.BoostableChanceEntry;
import net.minecraftforge.fluids.FluidStack;

public class ChancedFluidInputEntry extends BaseChanceEntry<FluidStack> implements BoostableChanceEntry<FluidStack> {

    private final int chanceBoost;
    private final int tierReductionRate;

    public ChancedFluidInputEntry(FluidStack ingredient, int chance, int chanceBoost) {
        this(ingredient, chance, chanceBoost, 0);
    }

    public ChancedFluidInputEntry(FluidStack ingredient, int chance, int chanceBoost, int tierReductionRate) {
        super(ingredient.copy(), chance);
        this.chanceBoost = chanceBoost;
        this.tierReductionRate = tierReductionRate;
    }

    @Override
    public FluidStack getIngredient() {
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

    public ChancedFluidInputEntry copy() {
        return new ChancedFluidInputEntry(super.getIngredient().copy(), getChance(), chanceBoost, tierReductionRate);
    }
}
