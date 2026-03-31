package com.wurtzitane.gregoriusdrugworkspersistence.integration.jei;

import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.ChancedFluidInputEntry;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.ChancedItemInputEntry;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Exposes GDW chanced-input metadata from the mixed-in GT recipe wrapper without
 * placing normal runtime types inside the mixin-owned package.
 *
 * @author wurtzitane
 */
public interface GregoriusDrugworksJeiRecipeWrapperAccess {
    void gdw$prepareChancedInputMappings(IIngredients ingredients);

    void gdw$setDisplayInputMappings(List<Integer> itemDisplaySlots, List<Integer> fluidDisplaySlots);

    ChancedItemInputEntry gdw$getChancedItemInput(int slotIndex);

    ChancedFluidInputEntry gdw$getChancedFluidInput(int slotIndex);

    boolean gdw$isNotConsumedItemInput(int slotIndex);

    boolean gdw$isNotConsumedFluidInput(int slotIndex);

    List<List<FluidStack>> gdw$getDisplayedFluidInputs();

    FluidStack gdw$getDisplayedAtmosphereFluid();

    boolean gdw$hasExplicitAtmosphere();
}
