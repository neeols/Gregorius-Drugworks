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

    private static final int[][] DYE_LAYOUTS = new int[][]{
            {0, 1},
            {2, 3},
            {0, 2},
            {1, 3}
    };

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

        if (match.action == MatchAction.RECOLOR_EXISTING) {
            ItemStack result = match.pill.copy();
            result.setCount(1);
            GregoriusDrugworksPayloadPills.setColors(result, match.leftColorId, match.rightColorId);
            GregoriusDrugworksPayloadPills.setRevealed(result, match.revealed);
            return result;
        }

        if (match.action == MatchAction.COLOR_SHELL) {
            ItemStack shell = createShellResult(match.pill);
            GregoriusDrugworksPayloadPills.setColors(shell, match.leftColorId, match.rightColorId);
            GregoriusDrugworksPayloadPills.setRevealed(shell, match.revealed);
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
        GregoriusDrugworksPayloadPills.setRevealed(loaded, match.revealed);
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
        Match match = findDyedLoadMatch(inv);
        if (match != null) {
            return match;
        }

        match = findColorShellMatch(inv);
        if (match != null) {
            return match;
        }

        return findShapelessFillMatch(inv);
    }

    @Nullable
    private Match findDyedLoadMatch(InventoryCrafting inv) {
        if (inv.getWidth() < 2 || inv.getHeight() < 2) {
            return null;
        }

        for (int originY = 0; originY <= inv.getHeight() - 2; originY++) {
            for (int originX = 0; originX <= inv.getWidth() - 2; originX++) {
                OutsideSubgridAnalysis outside = analyzeOutsideSubgrid(inv, originX, originY, inv.getWidth() >= 3 && inv.getHeight() >= 3);
                if (outside == null) {
                    continue;
                }

                ItemStack[] cells = collectSubgrid(inv, originX, originY);
                for (int[] dyeLayout : DYE_LAYOUTS) {
                    PillColorDefinition leftColor = GregoriusDrugworksPillColors.matchDye(cells[dyeLayout[0]]);
                    PillColorDefinition rightColor = GregoriusDrugworksPillColors.matchDye(cells[dyeLayout[1]]);
                    if (leftColor == null || rightColor == null) {
                        continue;
                    }

                    int[] remaining = remainingIndices(dyeLayout[0], dyeLayout[1]);
                    ItemStack first = cells[remaining[0]];
                    ItemStack second = cells[remaining[1]];
                    if (first.isEmpty() || second.isEmpty()) {
                        continue;
                    }

                    Match match = buildDyedLoadMatch(
                            inv,
                            originX,
                            originY,
                            first,
                            second,
                            remaining[0],
                            remaining[1],
                            leftColor.getId(),
                            rightColor.getId(),
                            outside.foundGlowstone
                    );
                    if (match != null) {
                        return match;
                    }
                }
            }
        }

        return null;
    }

    @Nullable
    private Match buildDyedLoadMatch(
            InventoryCrafting inv,
            int originX,
            int originY,
            ItemStack first,
            ItemStack second,
            int firstLocalIndex,
            int secondLocalIndex,
            String leftColorId,
            String rightColorId,
            boolean revealed
    ) {
        PayloadPillCraftingRegistry.Entry firstEntry = PayloadPillCraftingRegistry.find(first);
        PayloadPillCraftingRegistry.Entry secondEntry = PayloadPillCraftingRegistry.find(second);

        if (firstEntry != null && isLoadablePillCandidate(second)) {
            return createDyedLoadMatch(inv, originX, originY, second, secondLocalIndex, firstEntry, firstLocalIndex,
                    leftColorId, rightColorId, revealed);
        }
        if (secondEntry != null && isLoadablePillCandidate(first)) {
            return createDyedLoadMatch(inv, originX, originY, first, firstLocalIndex, secondEntry, secondLocalIndex,
                    leftColorId, rightColorId, revealed);
        }

        return null;
    }

    @Nullable
    private Match createDyedLoadMatch(
            InventoryCrafting inv,
            int originX,
            int originY,
            ItemStack pillCandidate,
            int pillLocalIndex,
            PayloadPillCraftingRegistry.Entry entry,
            int additiveLocalIndex,
            String leftColorId,
            String rightColorId,
            boolean revealed
    ) {
        if (pillCandidate.getItem() == GregoriusDrugworksMetaItems.PILL && PayloadLoaderUtil.hasPayload(pillCandidate)) {
            return null;
        }
        if (!isLoadablePillCandidate(pillCandidate)) {
            return null;
        }

        return new Match(
                pillCandidate,
                entry,
                subgridSlotToInventorySlot(inv, originX, originY, additiveLocalIndex),
                leftColorId,
                rightColorId,
                MatchAction.LOAD_FROM_ENTRY,
                revealed
        );
    }

    @Nullable
    private Match findColorShellMatch(InventoryCrafting inv) {
        for (int y = 0; y < inv.getHeight(); y++) {
            for (int x = 0; x < inv.getWidth(); x++) {
                Match horizontal = tryAdjacentDyePair(inv, x, y, x + 1, y);
                if (horizontal != null) {
                    return horizontal;
                }

                Match vertical = tryAdjacentDyePair(inv, x, y, x, y + 1);
                if (vertical != null) {
                    return vertical;
                }
            }
        }

        return null;
    }

    @Nullable
    private Match tryAdjacentDyePair(InventoryCrafting inv, int firstX, int firstY, int secondX, int secondY) {
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
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            if (i == firstSlot || i == secondSlot) {
                continue;
            }

            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }

            if (!isColorablePillCandidate(stack)) {
                return null;
            }
            if (!pillCandidate.isEmpty()) {
                return null;
            }
            pillCandidate = stack;
        }

        if (pillCandidate.isEmpty()) {
            return null;
        }

        boolean hasPayload = pillCandidate.getItem() == GregoriusDrugworksMetaItems.PILL
                && PayloadLoaderUtil.hasPayload(pillCandidate);
        MatchAction action = hasPayload ? MatchAction.RECOLOR_EXISTING : MatchAction.COLOR_SHELL;

        return new Match(
                pillCandidate,
                null,
                -1,
                leftColor.getId(),
                rightColor.getId(),
                action,
                false
        );
    }

    @Nullable
    private Match findShapelessFillMatch(InventoryCrafting inv) {
        ItemStack pillCandidate = ItemStack.EMPTY;
        ItemStack additive = ItemStack.EMPTY;
        int additiveSlot = -1;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) {
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
                false
        );
    }

    private boolean isOutsideSubgridEmpty(InventoryCrafting inv, int originX, int originY) {
        return analyzeOutsideSubgrid(inv, originX, originY, false) != null;
    }

    @Nullable
    private OutsideSubgridAnalysis analyzeOutsideSubgrid(InventoryCrafting inv, int originX, int originY, boolean allowGlowstone) {
        boolean foundGlowstone = false;
        for (int y = 0; y < inv.getHeight(); y++) {
            for (int x = 0; x < inv.getWidth(); x++) {
                boolean inside = x >= originX && x < originX + 2 && y >= originY && y < originY + 2;
                if (inside) {
                    continue;
                }

                ItemStack stack = inv.getStackInRowAndColumn(x, y);
                if (stack.isEmpty()) {
                    continue;
                }

                if (allowGlowstone && !foundGlowstone && isRevealGlowstone(stack)) {
                    foundGlowstone = true;
                    continue;
                }
                return null;
            }
        }
        return new OutsideSubgridAnalysis(foundGlowstone);
    }

    private ItemStack[] collectSubgrid(InventoryCrafting inv, int originX, int originY) {
        return new ItemStack[]{
                inv.getStackInRowAndColumn(originX, originY),
                inv.getStackInRowAndColumn(originX + 1, originY),
                inv.getStackInRowAndColumn(originX, originY + 1),
                inv.getStackInRowAndColumn(originX + 1, originY + 1)
        };
    }

    private int[] remainingIndices(int first, int second) {
        int[] remaining = new int[2];
        int index = 0;
        for (int i = 0; i < 4; i++) {
            if (i != first && i != second) {
                remaining[index++] = i;
            }
        }
        return remaining;
    }

    private int subgridSlotToInventorySlot(InventoryCrafting inv, int originX, int originY, int localIndex) {
        int x = originX + (localIndex % 2);
        int y = originY + (localIndex / 2);
        return x + (y * inv.getWidth());
    }

    private boolean isLoadablePillCandidate(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() == GregoriusDrugworksMetaItems.EMPTY_CAPSULE_PILL
                || stack.getItem() == GregoriusDrugworksMetaItems.PILL);
    }

    private boolean isColorablePillCandidate(ItemStack stack) {
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

    private static final class OutsideSubgridAnalysis {
        private final boolean foundGlowstone;

        private OutsideSubgridAnalysis(boolean foundGlowstone) {
            this.foundGlowstone = foundGlowstone;
        }
    }
}
