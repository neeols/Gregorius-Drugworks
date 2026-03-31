package com.wurtzitane.gregoriusdrugworkspersistence.pill;

import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksMetaItems;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadCarrierDataKeys;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoaderUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoadingService;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * Convenience helpers for payload-backed pill stacks.
 *
 * @author wurtzitane
 */
public final class GregoriusDrugworksPayloadPills {

    private GregoriusDrugworksPayloadPills() {
    }

    public static ItemStack createLoadedPillStack(
            String payloadId,
            @Nullable String modeId,
            String leftColorId,
            String rightColorId,
            boolean revealed
    ) {
        NBTTagCompound extra = new NBTTagCompound();
        extra.setString(PayloadCarrierDataKeys.LEFT_COLOR_KEY, normalizeColor(leftColorId));
        extra.setString(PayloadCarrierDataKeys.RIGHT_COLOR_KEY, normalizeColor(rightColorId));
        extra.setBoolean(PayloadCarrierDataKeys.REVEALED_KEY, revealed);
        if (modeId != null && !modeId.trim().isEmpty()) {
            extra.setString(com.wurtzitane.gregoriusdrugworks.common.payload.PayloadKeys.MODE_KEY, modeId.trim());
        }

        return PayloadLoadingService.createLoadedResult(new ItemStack(GregoriusDrugworksMetaItems.PILL), payloadId, 1, extra);
    }

    public static String getLeftColorId(ItemStack stack) {
        String colorId = PayloadLoaderUtil.getStringExtra(stack, PayloadCarrierDataKeys.LEFT_COLOR_KEY);
        return colorId.isEmpty() ? GregoriusDrugworksPillColors.getDefaultColorId() : colorId;
    }

    public static String getRightColorId(ItemStack stack) {
        String colorId = PayloadLoaderUtil.getStringExtra(stack, PayloadCarrierDataKeys.RIGHT_COLOR_KEY);
        return colorId.isEmpty() ? GregoriusDrugworksPillColors.getDefaultColorId() : colorId;
    }

    public static void setColors(ItemStack stack, String leftColorId, String rightColorId) {
        PayloadLoaderUtil.setStringExtra(stack, PayloadCarrierDataKeys.LEFT_COLOR_KEY, normalizeColor(leftColorId));
        PayloadLoaderUtil.setStringExtra(stack, PayloadCarrierDataKeys.RIGHT_COLOR_KEY, normalizeColor(rightColorId));
    }

    public static boolean isRevealed(ItemStack stack) {
        return PayloadLoaderUtil.getBooleanExtra(stack, PayloadCarrierDataKeys.REVEALED_KEY, false);
    }

    public static void setRevealed(ItemStack stack, boolean revealed) {
        PayloadLoaderUtil.setBooleanExtra(stack, PayloadCarrierDataKeys.REVEALED_KEY, revealed);
    }

    private static String normalizeColor(String colorId) {
        return (colorId == null || colorId.trim().isEmpty())
                ? GregoriusDrugworksPillColors.getDefaultColorId()
                : colorId.trim().toLowerCase(java.util.Locale.ROOT);
    }
}
