package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import com.wurtzitane.gregoriusdrugworkspersistence.medical.GregoriusDrugworksMedicalApplicators;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoadingService;
import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public final class RecipeLoadMedicalApplicator implements IRecipe {

    public RecipeLoadMedicalApplicator() {
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        SlotMatch match = findMatch(inv);
        return match != null && PayloadLoadingService.canLoad(match.carrier, match.source);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        SlotMatch match = findMatch(inv);
        if (match == null) {
            return ItemStack.EMPTY;
        }
        return PayloadLoadingService.createLoadedResult(match.carrier, match.source);
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(GregoriusDrugworksMedicalApplicators.MEDICAL_APPLICATOR);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        SlotMatch match = findMatch(inv);
        if (match == null) {
            return remaining;
        }

        remaining.set(match.sourceSlot, PayloadLoadingService.getSourceRemainder(match.source));
        return remaining;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public String getGroup() {
        return "gregoriusdrugworkspersistence:payload_loading";
    }

    @Override
    public IRecipe setRegistryName(ResourceLocation name) {
        throw new UnsupportedOperationException("Use default constructor and register with explicit registry name externally.");
    }

    @Override
    public ResourceLocation getRegistryName() {
        return GregoriusDrugworksUtil.makeName("load_medical_applicator");
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        return IRecipe.class;
    }

    private SlotMatch findMatch(InventoryCrafting inv) {
        ItemStack carrier = ItemStack.EMPTY;
        ItemStack source = ItemStack.EMPTY;
        int sourceSlot = -1;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.getItem() == GregoriusDrugworksMedicalApplicators.MEDICAL_APPLICATOR) {
                if (!carrier.isEmpty()) {
                    return null;
                }
                carrier = stack;
                continue;
            }

            if (!source.isEmpty()) {
                return null;
            }

            source = stack;
            sourceSlot = i;
        }

        if (carrier.isEmpty() || source.isEmpty()) {
            return null;
        }

        return new SlotMatch(carrier, source, sourceSlot);
    }

    private static final class SlotMatch {
        private final ItemStack carrier;
        private final ItemStack source;
        private final int sourceSlot;

        private SlotMatch(ItemStack carrier, ItemStack source, int sourceSlot) {
            this.carrier = carrier;
            this.source = source;
            this.sourceSlot = sourceSlot;
        }
    }
}