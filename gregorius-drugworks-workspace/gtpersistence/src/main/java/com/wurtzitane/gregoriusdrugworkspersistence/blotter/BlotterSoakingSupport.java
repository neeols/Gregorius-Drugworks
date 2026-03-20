package com.wurtzitane.gregoriusdrugworkspersistence.blotter;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadKeys;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksItems;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoaderUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoadingService;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.List;

/**
 * Runtime support for LSD chemical-bath soaking of printable carriers.
 *
 * @author wurtzitane
 */
public final class BlotterSoakingSupport {

    private static final String LSD_PAYLOAD_ID = "gregoriusdrugworkspersistence:lsd_payload";
    private static final String LSD_MODE_ID = "blotter";

    private static final ThreadLocal<ItemStack> PENDING_SOAK_INPUT = new ThreadLocal<ItemStack>();

    private BlotterSoakingSupport() {
    }

    public static Boolean matchesAndConsumeBlotterSoakRecipe(Recipe recipe,
                                                             RecipeMap<?> recipeMap,
                                                             boolean consumeIfSuccessful,
                                                             IItemHandlerModifiable inputInventory,
                                                             IMultipleTankHandler inputFluids) {
        clearPendingSoakInput();
        if (!isBlotterSoakRecipe(recipe, recipeMap)) {
            return null;
        }

        SoakPlan plan = createSoakPlan(recipe, inputInventory, inputFluids);
        if (plan == null) {
            return false;
        }

        if (!consumeIfSuccessful) {
            return true;
        }

        ItemStack extracted = inputInventory.extractItem(plan.itemSlot, plan.itemAmount, false);
        if (extracted.isEmpty() || extracted.getCount() != plan.itemAmount) {
            clearPendingSoakInput();
            return false;
        }

        PENDING_SOAK_INPUT.set(plan.snapshot);
        if (!applyFluidDrain(plan.fluidDrain, inputFluids)) {
            clearPendingSoakInput();
            return false;
        }
        return true;
    }

    public static void applyPendingSoakOutput(Recipe recipe, RecipeMap<?> recipeMap, NonNullList<ItemStack> itemOutputs) {
        try {
            if (!isBlotterSoakRecipe(recipe, recipeMap) || itemOutputs == null || itemOutputs.isEmpty()) {
                return;
            }

            ItemStack soakedInput = PENDING_SOAK_INPUT.get();
            if (soakedInput == null || soakedInput.isEmpty()) {
                return;
            }

            ItemStack loaded = PayloadLoadingService.createLoadedResult(
                    soakedInput,
                    LSD_PAYLOAD_ID,
                    1,
                    createBlotterModeData());
            if (loaded.isEmpty()) {
                return;
            }

            for (int i = 0; i < itemOutputs.size(); i++) {
                ItemStack originalOutput = itemOutputs.get(i);
                if (originalOutput.isEmpty() || originalOutput.getItem() != loaded.getItem()) {
                    continue;
                }
                ItemStack replacement = loaded.copy();
                replacement.setCount(originalOutput.getCount());
                itemOutputs.set(i, replacement);
            }
        } finally {
            clearPendingSoakInput();
        }
    }

    public static void clearPendingSoakInput() {
        PENDING_SOAK_INPUT.remove();
    }

    private static NBTTagCompound createBlotterModeData() {
        NBTTagCompound extraData = new NBTTagCompound();
        extraData.setString(PayloadKeys.MODE_KEY, LSD_MODE_ID);
        return extraData;
    }

    private static SoakPlan createSoakPlan(Recipe recipe,
                                           IItemHandlerModifiable inputInventory,
                                           IMultipleTankHandler inputFluids) {
        CarrierMatch carrierMatch = findEligibleCarrier(recipe, inputInventory);
        if (carrierMatch == null) {
            return null;
        }

        int[] fluidDrain = createFluidDrainPlan(recipe, inputFluids);
        if (fluidDrain == null) {
            return null;
        }

        return new SoakPlan(carrierMatch.slot, carrierMatch.amount, carrierMatch.snapshot, fluidDrain);
    }

    private static CarrierMatch findEligibleCarrier(Recipe recipe, IItemHandlerModifiable inputInventory) {
        for (GTRecipeInput input : recipe.getInputs()) {
            for (int slot = 0; slot < inputInventory.getSlots(); slot++) {
                ItemStack stack = inputInventory.getStackInSlot(slot);
                if (stack.isEmpty() || stack.getCount() < input.getAmount()) {
                    continue;
                }
                if (!isPrintableCarrier(stack) || PayloadLoaderUtil.hasPayload(stack) || !input.acceptsStack(stack)) {
                    continue;
                }

                ItemStack snapshot = stack.copy();
                snapshot.setCount(1);
                return new CarrierMatch(slot, input.getAmount(), snapshot);
            }
        }
        return null;
    }

    private static int[] createFluidDrainPlan(Recipe recipe, IMultipleTankHandler inputFluids) {
        List<IMultipleTankHandler.MultiFluidTankEntry> tanks = inputFluids.getFluidTanks();
        List<FluidStack> simulated = new ArrayList<FluidStack>(tanks.size());
        for (IMultipleTankHandler.MultiFluidTankEntry tank : tanks) {
            FluidStack fluid = tank.getFluid();
            simulated.add(fluid == null ? null : fluid.copy());
        }

        int[] drain = new int[tanks.size()];
        for (GTRecipeInput fluidInput : recipe.getFluidInputs()) {
            int remaining = fluidInput.getAmount();
            for (int tankIndex = 0; tankIndex < simulated.size(); tankIndex++) {
                FluidStack tankFluid = simulated.get(tankIndex);
                if (tankFluid == null || !fluidInput.acceptsFluid(tankFluid)) {
                    continue;
                }

                int drained = Math.min(remaining, tankFluid.amount);
                if (drained <= 0) {
                    continue;
                }

                drain[tankIndex] += drained;
                remaining -= drained;
                tankFluid.amount -= drained;
                if (tankFluid.amount <= 0) {
                    simulated.set(tankIndex, null);
                }
                if (remaining <= 0) {
                    break;
                }
            }

            if (remaining > 0) {
                return null;
            }
        }

        return drain;
    }

    private static boolean applyFluidDrain(int[] drain, IMultipleTankHandler inputFluids) {
        List<IMultipleTankHandler.MultiFluidTankEntry> tanks = inputFluids.getFluidTanks();
        for (int tankIndex = 0; tankIndex < drain.length && tankIndex < tanks.size(); tankIndex++) {
            int amount = drain[tankIndex];
            if (amount <= 0) {
                continue;
            }

            FluidStack drained = tanks.get(tankIndex).drain(amount, true);
            if (drained == null || drained.amount != amount) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPrintableCarrier(ItemStack stack) {
        return BlotterPrintStacks.isCarrier(stack, PrintableCarrierKind.BLOTTER_PAPER)
                || BlotterPrintStacks.isCarrier(stack, PrintableCarrierKind.SINGLE_TAB);
    }

    private static boolean isBlotterSoakRecipe(Recipe recipe, RecipeMap<?> recipeMap) {
        if (recipe == null || recipeMap != RecipeMaps.CHEMICAL_BATH_RECIPES || GregoriusDrugworksMaterials.LSD == null) {
            return false;
        }

        FluidStack lsdFluid = GregoriusDrugworksMaterials.LSD.getFluid(64);
        if (lsdFluid == null) {
            return false;
        }

        boolean hasCarrierInput = false;
        for (GTRecipeInput input : recipe.getInputs()) {
            if (input.acceptsStack(new ItemStack(GregoriusDrugworksItems.BLOTTER_PAPER))
                    || input.acceptsStack(new ItemStack(GregoriusDrugworksItems.SINGLE_TAB))) {
                hasCarrierInput = true;
                break;
            }
        }
        if (!hasCarrierInput) {
            return false;
        }

        boolean hasLsdFluid = false;
        for (GTRecipeInput input : recipe.getFluidInputs()) {
            if (input.acceptsFluid(lsdFluid) && input.getAmount() <= lsdFluid.amount) {
                hasLsdFluid = true;
                break;
            }
        }
        if (!hasLsdFluid) {
            return false;
        }

        for (ItemStack output : recipe.getAllItemOutputs()) {
            if (!output.isEmpty() && (output.getItem() == GregoriusDrugworksItems.BLOTTER_PAPER
                    || output.getItem() == GregoriusDrugworksItems.SINGLE_TAB)) {
                return true;
            }
        }
        return false;
    }

    private static final class CarrierMatch {
        private final int slot;
        private final int amount;
        private final ItemStack snapshot;

        private CarrierMatch(int slot, int amount, ItemStack snapshot) {
            this.slot = slot;
            this.amount = amount;
            this.snapshot = snapshot;
        }
    }

    private static final class SoakPlan {
        private final int itemSlot;
        private final int itemAmount;
        private final ItemStack snapshot;
        private final int[] fluidDrain;

        private SoakPlan(int itemSlot, int itemAmount, ItemStack snapshot, int[] fluidDrain) {
            this.itemSlot = itemSlot;
            this.itemAmount = itemAmount;
            this.snapshot = snapshot;
            this.fluidDrain = fluidDrain;
        }
    }
}
