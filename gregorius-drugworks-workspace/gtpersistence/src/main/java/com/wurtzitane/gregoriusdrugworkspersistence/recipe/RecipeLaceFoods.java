package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoaderUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoadingService;
import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Dynamic shapeless crafting recipe for payload-laced food items.
 *
 * @author wurtzitane
 */
public final class RecipeLaceFoods implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return findMatch(inv) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        Match match = findMatch(inv);
        if (match == null) {
            return ItemStack.EMPTY;
        }

        ItemStack result = match.food.copy();
        result.setCount(1);
        return PayloadLoadingService.createLoadedResult(
                result,
                match.entry.getPayloadId(),
                1,
                PayloadFoodLacingRegistry.createFoodPayloadData(match.entry.getModeId(), false)
        );
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(net.minecraft.init.Items.BREAD);
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
        return "gregoriusdrugworkspersistence:lace_foods";
    }

    @Override
    public IRecipe setRegistryName(ResourceLocation name) {
        throw new UnsupportedOperationException("Use default constructor and register with explicit registry name externally.");
    }

    @Override
    public ResourceLocation getRegistryName() {
        return GregoriusDrugworksUtil.makeName("lace_foods");
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        return IRecipe.class;
    }

    private Match findMatch(InventoryCrafting inv) {
        if (inv.getWidth() * inv.getHeight() < 2) {
            return null;
        }

        int firstSlot = -1;
        int secondSlot = -1;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            if (inv.getStackInSlot(i).isEmpty()) {
                continue;
            }
            if (firstSlot < 0) {
                firstSlot = i;
                continue;
            }
            if (secondSlot < 0) {
                secondSlot = i;
                continue;
            }
            return null;
        }

        if (firstSlot < 0 || secondSlot < 0) {
            return null;
        }

        Match match = tryMatch(inv.getStackInSlot(firstSlot), inv.getStackInSlot(secondSlot));
        if (match != null) {
            return match;
        }

        return tryMatch(inv.getStackInSlot(secondSlot), inv.getStackInSlot(firstSlot));
    }

    private Match tryMatch(ItemStack food, ItemStack additive) {
        if (food.isEmpty() || additive.isEmpty()) {
            return null;
        }
        if (PayloadLoaderUtil.hasPayload(food)) {
            return null;
        }

        PayloadFoodLacingRegistry.Entry entry = PayloadFoodLacingRegistry.find(food, additive);
        if (entry == null) {
            return null;
        }

        if (PayloadLoadingService.createLoadedResult(food.copy(), entry.getPayloadId(), 1,
                PayloadFoodLacingRegistry.createFoodPayloadData(entry.getModeId(), false)).isEmpty()) {
            return null;
        }

        return new Match(food, entry);
    }

    private static final class Match {
        private final ItemStack food;
        private final PayloadFoodLacingRegistry.Entry entry;

        private Match(ItemStack food, PayloadFoodLacingRegistry.Entry entry) {
            this.food = food;
            this.entry = entry;
        }
    }
}
