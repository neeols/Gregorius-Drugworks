package com.wurtzitane.gregoriusdrugworkspersistence.mixin;

import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.ChancedFluidInputEntry;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.ChancedItemInputEntry;

public interface GregoriusDrugworksJeiRecipeWrapperAccess {
    ChancedItemInputEntry gdw$getChancedItemInput(int slotIndex);

    ChancedFluidInputEntry gdw$getChancedFluidInput(int slotIndex);
}
