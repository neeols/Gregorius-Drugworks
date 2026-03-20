package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksMetaItems;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoaderUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoadingService;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.GregoriusDrugworksPillColors;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.GregoriusDrugworksPayloadPills;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.PillColorDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Crafts and recolors payload pills from the declarative pill crafting registry.
 *
 * @author wurtzitane
 */
public final class RecipeLoadPayloadPill implements IRecipe {

    private enum MatchAction {
        COLOR_SHELL,
        LOAD_FROM_ENTRY,
        RECOLOR_EXISTING
    }

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

        boolean revealed = resolveResultRevealed(match.pill, match.revealed);

        if (match.action == MatchAction.RECOLOR_EXISTING) {
            ItemStack result = match.pill.copy();
            result.setCount(1);
            GregoriusDrugworksPayloadPills.setColors(result, match.leftColorId, match.rightColorId);
            GregoriusDrugworksPayloadPills.setRevealed(result, revealed);
            return result;
        }

        if (match.action == MatchAction.COLOR_SHELL) {
            ItemStack shell = createShellResult(match.pill);
            GregoriusDrugworksPayloadPills.setColors(shell, match.leftColorId, match.rightColorId);
            GregoriusDrugworksPayloadPills.setRevealed(shell, revealed);
            return shell;
        }

        ItemStack loaded = PayloadLoadingService.createLoadedResult(
                new ItemStack(GregoriusDrugworksMetaItems.PILL),
                match.entry.getPayloadId(),
                1,
                PayloadFoodLacingRegistry.createFoodPayloadData(match.entry.getModeId(), match.revealed)
        );
        if (loaded.isEmpty()) {
            return ItemStack.EMPTY;
        }

        GregoriusDrugworksPayloadPills.setColors(loaded, match.leftColorId, match.rightColorId);
        GregoriusDrugworksPayloadPills.setRevealed(loaded, revealed);
        return loaded;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(GregoriusDrugworksMetaItems.PILL);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        Match match = findMatch(inv);
        if (match != null && match.additiveSlot >= 0 && match.entry != null) {
            remaining.set(match.additiveSlot, match.entry.getRemainder());
        }
        return remaining;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public String getGroup() {
        return "gregoriusdrugworkspersistence:payload_pill_loading";
    }

    @Override
    public IRecipe setRegistryName(ResourceLocation name) {
        throw new UnsupportedOperationException("Use default constructor and register with explicit registry name externally.");
    }

    @Override
    public ResourceLocation getRegistryName() {
        return GregoriusDrugworksUtil.makeName("load_payload_pill");
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        return IRecipe.class;
    }

    @Nullable
    private Match findMatch(InventoryCrafting inv) {
        Match match = findDyedCompositeMatch(inv);
        if (match != null) {
            return match;
        }

        return findShapelessFillMatch(inv);
    }

    @Nullable
    private Match findDyedCompositeMatch(InventoryCrafting inv) {
        for (int originY = 0; originY <= inv.getHeight() - 2; originY++) {
            for (int originX = 0; originX <= inv.getWidth() - 2; originX++) {
                Match horizontal = tryDyedLoadMatch(inv, originX, originY, originX + 1, originY);
                if (horizontal != null) {
                    return horizontal;
                }

                Match vertical = tryDyedLoadMatch(inv, originX, originY, originX, originY + 1);
                if (vertical != null) {
                    return vertical;
                }
            }
        }

        return null;
    }

    @Nullable
    private Match tryDyedLoadMatch(InventoryCrafting inv, int firstX, int firstY, int secondX, int secondY) {
        if (secondX < 0 || secondX >= inv.getWidth() || secondY < 0 || secondY >= inv.getHeight()) {
            return null;
        }

        int firstSlot = firstX + (firstY * inv.getWidth());
        int secondSlot = secondX + (secondY * inv.getWidth());
        PillColorDefinition leftColor = GregoriusDrugworksPillColors.matchDye(inv.getStackInSlot(firstSlot));
        PillColorDefinition rightColor = GregoriusDrugworksPillColors.matchDye(inv.getStackInSlot(secondSlot));
        if (leftColor == null || rightColor == null) {
            return null;
        }

        ItemStack pillCandidate = ItemStack.EMPTY;
        PayloadPillCraftingRegistry.Entry entry = null;
        int additiveSlot = -1;
        boolean revealed = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            if (i == firstSlot || i == secondSlot) {
                continue;
            }

            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }

            if (!revealed && isRevealGlowstone(stack)) {
                revealed = true;
                continue;
            }

            PayloadPillCraftingRegistry.Entry candidateEntry = PayloadPillCraftingRegistry.find(stack);
            if (candidateEntry != null) {
                if (entry != null) {
                    return null;
                }
                entry = candidateEntry;
                additiveSlot = i;
                continue;
            }

            if (isLoadablePillCandidate(stack)) {
                if (!pillCandidate.isEmpty()) {
                    return null;
                }
                pillCandidate = stack;
                continue;
            }

            return null;
        }

        if (pillCandidate.isEmpty()) {
            return null;
        }

        boolean hasPayload = pillCandidate.getItem() == GregoriusDrugworksMetaItems.PILL
                && PayloadLoaderUtil.hasPayload(pillCandidate);

        if (entry != null) {
            if (hasPayload) {
                return null;
            }

            return new Match(
                    pillCandidate,
                    entry,
                    additiveSlot,
                    leftColor.getId(),
                    rightColor.getId(),
                    MatchAction.LOAD_FROM_ENTRY,
                    revealed
            );
        }

        MatchAction action = hasPayload ? MatchAction.RECOLOR_EXISTING : MatchAction.COLOR_SHELL;
        return new Match(
                pillCandidate,
                null,
                -1,
                leftColor.getId(),
                rightColor.getId(),
                action,
                revealed
        );
    }

    @Nullable
    private Match findShapelessFillMatch(InventoryCrafting inv) {
        ItemStack pillCandidate = ItemStack.EMPTY;
        ItemStack additive = ItemStack.EMPTY;
        int additiveSlot = -1;
        boolean revealed = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }

            if (!revealed && isRevealGlowstone(stack)) {
                revealed = true;
                continue;
            }

            if (PayloadPillCraftingRegistry.find(stack) != null) {
                if (!additive.isEmpty()) {
                    return null;
                }
                additive = stack;
                additiveSlot = i;
                continue;
            }

            if (isLoadablePillCandidate(stack)) {
                if (!pillCandidate.isEmpty()) {
                    return null;
                }
                pillCandidate = stack;
                continue;
            }

            return null;
        }

        if (pillCandidate.isEmpty() || additive.isEmpty()) {
            return null;
        }

        if (pillCandidate.getItem() == GregoriusDrugworksMetaItems.PILL && PayloadLoaderUtil.hasPayload(pillCandidate)) {
            return null;
        }

        PayloadPillCraftingRegistry.Entry entry = PayloadPillCraftingRegistry.find(additive);
        if (entry == null) {
            return null;
        }

        return new Match(
                pillCandidate,
                entry,
                additiveSlot,
                resolveExistingLeftColor(pillCandidate),
                resolveExistingRightColor(pillCandidate),
                MatchAction.LOAD_FROM_ENTRY,
                revealed
        );
    }

    private boolean isLoadablePillCandidate(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() == GregoriusDrugworksMetaItems.EMPTY_CAPSULE_PILL
                || stack.getItem() == GregoriusDrugworksMetaItems.PILL);
    }

    private boolean isRevealGlowstone(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == Items.GLOWSTONE_DUST;
    }

    private String resolveExistingLeftColor(ItemStack stack) {
        if (stack.getItem() != GregoriusDrugworksMetaItems.PILL) {
            return GregoriusDrugworksPillColors.getDefaultColorId();
        }
        return GregoriusDrugworksPayloadPills.getLeftColorId(stack);
    }

    private String resolveExistingRightColor(ItemStack stack) {
        if (stack.getItem() != GregoriusDrugworksMetaItems.PILL) {
            return GregoriusDrugworksPillColors.getDefaultColorId();
        }
        return GregoriusDrugworksPayloadPills.getRightColorId(stack);
    }

    private ItemStack createShellResult(ItemStack sourcePill) {
        if (sourcePill.getItem() == GregoriusDrugworksMetaItems.PILL) {
            ItemStack result = sourcePill.copy();
            result.setCount(1);
            if (PayloadLoaderUtil.hasPayload(result)) {
                PayloadLoaderUtil.clear(result);
            }
            return result;
        }
        return new ItemStack(GregoriusDrugworksMetaItems.PILL);
    }

    private boolean resolveResultRevealed(ItemStack sourcePill, boolean explicitReveal) {
        return explicitReveal || GregoriusDrugworksPayloadPills.isRevealed(sourcePill);
    }

    private static final class Match {
        private final ItemStack pill;
        private final PayloadPillCraftingRegistry.Entry entry;
        private final int additiveSlot;
        private final String leftColorId;
        private final String rightColorId;
        private final MatchAction action;
        private final boolean revealed;

        private Match(
                ItemStack pill,
                @Nullable PayloadPillCraftingRegistry.Entry entry,
                int additiveSlot,
                String leftColorId,
                String rightColorId,
                MatchAction action,
                boolean revealed
        ) {
            this.pill = pill;
            this.entry = entry;
            this.additiveSlot = additiveSlot;
            this.leftColorId = leftColorId;
            this.rightColorId = rightColorId;
            this.action = action;
            this.revealed = revealed;
        }
    }
}
