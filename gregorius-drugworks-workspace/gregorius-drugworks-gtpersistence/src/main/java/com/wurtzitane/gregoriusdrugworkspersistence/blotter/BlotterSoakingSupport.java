package com.wurtzitane.gregoriusdrugworkspersistence.blotter;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadKeys;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksItems;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadCarrierDataKeys;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoaderUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoadingService;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

        if (!applyItemDrain(plan.itemDrain, inputInventory)) {
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

    public static void applyPendingSoakOutput(Recipe recipe, RecipeMap<?> recipeMap, List<ItemStack> itemOutputs) {
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
                    createSoakExtraData(recipe));
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

    private static NBTTagCompound createSoakExtraData(Recipe recipe) {
        NBTTagCompound extraData = createBlotterModeData();
        if (hasRevealCatalyst(recipe)) {
            extraData.setBoolean(PayloadCarrierDataKeys.REVEALED_KEY, true);
        }
        return extraData;
    }

    private static SoakPlan createSoakPlan(Recipe recipe,
                                           IItemHandlerModifiable inputInventory,
                                           IMultipleTankHandler inputFluids) {
        ItemPlan itemPlan = createItemPlan(recipe, inputInventory);
        if (itemPlan == null) {
            return null;
        }

        int[] fluidDrain = createFluidDrainPlan(recipe, inputFluids);
        if (fluidDrain == null) {
            return null;
        }

        return new SoakPlan(itemPlan.snapshot, itemPlan.itemDrain, fluidDrain);
    }

    private static ItemPlan createItemPlan(Recipe recipe, IItemHandlerModifiable inputInventory) {
        int[] itemDrain = new int[inputInventory.getSlots()];
        ItemStack snapshot = ItemStack.EMPTY;

        for (GTRecipeInput input : recipe.getInputs()) {
            int matchedSlot = isCarrierInput(input)
                    ? findCarrierSlot(input, inputInventory, itemDrain)
                    : findSupportItemSlot(input, inputInventory, itemDrain);
            if (matchedSlot < 0) {
                return null;
            }

            itemDrain[matchedSlot] += input.getAmount();
            if (snapshot.isEmpty() && isCarrierInput(input)) {
                snapshot = inputInventory.getStackInSlot(matchedSlot).copy();
                snapshot.setCount(1);
            }
        }
        return snapshot.isEmpty() ? null : new ItemPlan(snapshot, itemDrain);
    }

    private static int findCarrierSlot(GTRecipeInput input, IItemHandlerModifiable inputInventory, int[] reserved) {
        for (int slot = 0; slot < inputInventory.getSlots(); slot++) {
            ItemStack stack = inputInventory.getStackInSlot(slot);
            if (stack.isEmpty() || stack.getCount() - reserved[slot] < input.getAmount()) {
                continue;
            }
            if (!isPrintableCarrier(stack) || PayloadLoaderUtil.hasPayload(stack) || !input.acceptsStack(stack)) {
                continue;
            }
            return slot;
        }
        return -1;
    }

    private static int findSupportItemSlot(GTRecipeInput input, IItemHandlerModifiable inputInventory, int[] reserved) {
        for (int slot = 0; slot < inputInventory.getSlots(); slot++) {
            ItemStack stack = inputInventory.getStackInSlot(slot);
            if (stack.isEmpty() || stack.getCount() - reserved[slot] < input.getAmount()) {
                continue;
            }
            if (input.acceptsStack(stack)) {
                return slot;
            }
        }
        return -1;
    }

    private static int[] createFluidDrainPlan(Recipe recipe, IMultipleTankHandler inputFluids) {
        List<IMultipleTankHandler.ITankEntry> tanks = inputFluids.getFluidTanks();
        List<FluidStack> simulated = new ArrayList<FluidStack>(tanks.size());
        for (IMultipleTankHandler.ITankEntry tank : tanks) {
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
        List<IMultipleTankHandler.ITankEntry> tanks = inputFluids.getFluidTanks();
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

    private static boolean applyItemDrain(int[] drain, IItemHandlerModifiable inputInventory) {
        for (int slot = 0; slot < drain.length && slot < inputInventory.getSlots(); slot++) {
            int amount = drain[slot];
            if (amount <= 0) {
                continue;
            }

            ItemStack extracted = inputInventory.extractItem(slot, amount, false);
            if (extracted.isEmpty() || extracted.getCount() != amount) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPrintableCarrier(ItemStack stack) {
        return BlotterPrintStacks.isCarrier(stack, PrintableCarrierKind.BLOTTER_PAPER)
                || BlotterPrintStacks.isCarrier(stack, PrintableCarrierKind.SINGLE_TAB);
    }

    private static boolean isCarrierInput(GTRecipeInput input) {
        return input.acceptsStack(new ItemStack(GregoriusDrugworksItems.BLOTTER_PAPER))
                || input.acceptsStack(new ItemStack(GregoriusDrugworksItems.SINGLE_TAB));
    }

    private static boolean hasRevealCatalyst(Recipe recipe) {
        ItemStack glowstone = new ItemStack(Items.GLOWSTONE_DUST);
        for (GTRecipeInput input : recipe.getInputs()) {
            if (!isCarrierInput(input) && input.acceptsStack(glowstone)) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack createPreviewSoakOutput(Recipe recipe, RecipeMap<?> recipeMap, ItemStack carrierTemplate) {
        if (!isBlotterSoakRecipe(recipe, recipeMap) || carrierTemplate.isEmpty()) {
            return carrierTemplate.copy();
        }
        if (!isPrintableCarrier(carrierTemplate) || PayloadLoaderUtil.hasPayload(carrierTemplate)) {
            return carrierTemplate.copy();
        }

        ItemStack loaded = PayloadLoadingService.createLoadedResult(
                carrierTemplate,
                LSD_PAYLOAD_ID,
                1,
                createSoakExtraData(recipe));
        return loaded.isEmpty() ? carrierTemplate.copy() : loaded;
    }

    private static boolean isBlotterSoakRecipe(Recipe recipe, RecipeMap<?> recipeMap) {
        if (recipe == null || recipeMap != RecipeMaps.CHEMICAL_BATH_RECIPES || GregoriusDrugworksMaterials.LSD == null) {
            return false;
        }

        FluidStack lsdFluid = GregoriusDrugworksMaterials.LSD.getFluid(1);
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
            if (input.acceptsFluid(lsdFluid) && input.getAmount() >= 1) {
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

    private static final class ItemPlan {
        private final ItemStack snapshot;
        private final int[] itemDrain;

        private ItemPlan(ItemStack snapshot, int[] itemDrain) {
            this.snapshot = snapshot;
            this.itemDrain = itemDrain;
        }
    }

    private static final class SoakPlan {
        private final ItemStack snapshot;
        private final int[] itemDrain;
        private final int[] fluidDrain;

        private SoakPlan(ItemStack snapshot, int[] itemDrain, int[] fluidDrain) {
            this.snapshot = snapshot;
            this.itemDrain = itemDrain;
            this.fluidDrain = fluidDrain;
        }
    }
}
