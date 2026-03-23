package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadCarriers;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadCarrierAdapter;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadCarrierDataKeys;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoaderUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Reveals hidden payload tooltip data with glowstone dust.
 *
 * @author wurtzitane
 */
public final class RecipeRevealPayloadCarrier implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return findCarrier(inv) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack carrier = findCarrier(inv);
        if (carrier.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = carrier.copy();
        result.setCount(1);
        PayloadLoaderUtil.setBooleanExtra(result, PayloadCarrierDataKeys.REVEALED_KEY, true);
        return result;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(Items.BREAD);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public String getGroup() {
        return "gregoriusdrugworkspersistence:payload_reveal";
    }

    @Override
    public IRecipe setRegistryName(ResourceLocation name) {
        throw new UnsupportedOperationException("Use default constructor and register with explicit registry name externally.");
    }

    @Override
    public ResourceLocation getRegistryName() {
        return GregoriusDrugworksUtil.makeName("reveal_payload_carrier");
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        return IRecipe.class;
    }

    private ItemStack findCarrier(InventoryCrafting inv) {
        ItemStack carrier = ItemStack.EMPTY;
        boolean foundGlowstone = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.getItem() == Items.GLOWSTONE_DUST) {
                if (foundGlowstone) {
                    return ItemStack.EMPTY;
                }
                foundGlowstone = true;
                continue;
            }

            if (!carrier.isEmpty()) {
                return ItemStack.EMPTY;
            }

            PayloadCarrierAdapter adapter = GregoriusDrugworksPayloadCarriers.find(stack);
            if (adapter == null || !adapter.hasPayload(stack)) {
                return ItemStack.EMPTY;
            }
            if (stack.getItem() instanceof com.wurtzitane.gregoriusdrugworkspersistence.medical.ItemMedicalApplicator) {
                return ItemStack.EMPTY;
            }
            if (PayloadLoaderUtil.getBooleanExtra(stack, PayloadCarrierDataKeys.REVEALED_KEY, false)) {
                return ItemStack.EMPTY;
            }
            carrier = stack;
        }

        return foundGlowstone ? carrier : ItemStack.EMPTY;
    }
}
