package com.wurtzitane.gregoriusdrugworkspersistence.blotter;

import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksItems;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Shared printed-stack creation helpers for blotter carriers.
 *
 * @author wurtzitane
 */
public final class BlotterPrintStacks {

    private BlotterPrintStacks() {
    }

    public static boolean isBlankBlotterPaper(ItemStack stack) {
        return isCarrier(stack, PrintableCarrierKind.BLOTTER_PAPER) && !BlotterPrintData.hasPrint(stack);
    }

    public static boolean isCarrier(ItemStack stack, PrintableCarrierKind carrierKind) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemPrintableCarrier)) {
            return false;
        }
        ItemPrintableCarrier carrier = (ItemPrintableCarrier) stack.getItem();
        return carrier.getCarrierKind() == carrierKind;
    }

    public static ItemStack createPrintedBlotter(BlotterPrintableRegistry.Entry entry, int opacityPercent) {
        return createPrintedCarrier(new ItemStack(GregoriusDrugworksItems.BLOTTER_PAPER), entry, opacityPercent);
    }

    public static ItemStack createPrintedSingleTab(BlotterPrintableRegistry.Entry entry, int opacityPercent) {
        return createPrintedCarrier(new ItemStack(GregoriusDrugworksItems.SINGLE_TAB), entry, opacityPercent);
    }

    public static ItemStack createPrintedCarrier(ItemStack baseCarrier, BlotterPrintableRegistry.Entry entry,
                                                 int opacityPercent) {
        if (baseCarrier.isEmpty() || entry == null) {
            return ItemStack.EMPTY;
        }
        ItemStack output = baseCarrier.copy();
        output.setCount(1);
        BlotterPrintData.apply(output, entry.getVariantId(), entry.getSourcePath(), opacityPercent);
        return output;
    }

    public static ItemStack copyPrintedSingleTab(ItemStack sourceBlotter) {
        if (!isCarrier(sourceBlotter, PrintableCarrierKind.BLOTTER_PAPER)) {
            return new ItemStack(GregoriusDrugworksItems.SINGLE_TAB);
        }
        ItemStack output = new ItemStack(GregoriusDrugworksItems.SINGLE_TAB);
        if (sourceBlotter.hasTagCompound()) {
            output.setTagCompound(sourceBlotter.getTagCompound().copy());
        }
        if (BlotterPrintData.hasPrint(sourceBlotter)) {
            BlotterPrintData.copy(sourceBlotter, output);
        } else {
            BlotterPrintData.clear(output);
        }
        return output;
    }

    @Nullable
    public static BlotterPrintableRegistry.Entry resolvePrintedEntry(ItemStack stack) {
        if (!BlotterPrintData.hasPrint(stack)) {
            return null;
        }
        return BlotterPrintableRegistry.findByVariantId(BlotterPrintData.getVariantId(stack));
    }
}
