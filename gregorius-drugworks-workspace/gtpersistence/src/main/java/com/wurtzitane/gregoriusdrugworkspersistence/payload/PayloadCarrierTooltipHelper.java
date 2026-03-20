package com.wurtzitane.gregoriusdrugworkspersistence.payload;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.GregoriusDrugworksPayloadPills;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.GregoriusDrugworksPillColors;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * Client-facing tooltip descriptions for hidden/revealed payload carriers.
 *
 * @author wurtzitane
 */
public final class PayloadCarrierTooltipHelper {

    private PayloadCarrierTooltipHelper() {
    }

    public static void appendTooltip(ItemStack stack, List<String> tooltip) {
        if (stack.isEmpty() || stack.getItem() instanceof com.wurtzitane.gregoriusdrugworkspersistence.medical.ItemMedicalApplicator) {
            return;
        }

        GregoriusDrugworksPayloadRegistry.ResolvedPayload payload = PayloadLoaderUtil.resolve(stack);
        if (payload == null) {
            return;
        }

        boolean revealed = PayloadLoaderUtil.getBooleanExtra(stack, PayloadCarrierDataKeys.REVEALED_KEY, false);
        if (stack.getItem() instanceof com.wurtzitane.gregoriusdrugworkspersistence.pill.ItemPayloadPill) {
            tooltip.add(TextFormatting.LIGHT_PURPLE + localize("tooltip.gregoriusdrugworkspersistence.payload.loaded_pill", "Loaded pill"));
        } else if (stack.getItem() instanceof ItemFood) {
            tooltip.add(TextFormatting.LIGHT_PURPLE + localize("tooltip.gregoriusdrugworkspersistence.payload.laced_food", "Laced food"));
        } else {
            tooltip.add(TextFormatting.LIGHT_PURPLE + localize("tooltip.gregoriusdrugworkspersistence.payload.infused_item", "Payload-infused item"));
        }

        if (!revealed) {
            tooltip.add(TextFormatting.DARK_GRAY + localize("tooltip.gregoriusdrugworkspersistence.payload.hidden_hint",
                    "Craft with Glowstone Dust to inspect contents."));
            return;
        }

        PayloadDefinition definition = payload.getDefinition();
        String payloadName = I18n.hasKey(definition.getDisplayNameKey())
                ? I18n.format(definition.getDisplayNameKey())
                : definition.getId();
        tooltip.add(TextFormatting.GRAY + localize("tooltip.gregoriusdrugworkspersistence.payload.name", "Payload")
                + ": " + TextFormatting.WHITE + payloadName);

        if (!payload.getModeId().isEmpty()) {
            tooltip.add(TextFormatting.GRAY + localize("tooltip.gregoriusdrugworkspersistence.payload.delivery", "Delivery")
                    + ": " + TextFormatting.WHITE + capitalize(payload.getModeId()));
        }

        if (stack.getItem() instanceof com.wurtzitane.gregoriusdrugworkspersistence.pill.ItemPayloadPill) {
            tooltip.add(TextFormatting.GRAY + localize("tooltip.gregoriusdrugworkspersistence.payload.pill.left", "Left Shell")
                    + ": " + TextFormatting.WHITE
                    + GregoriusDrugworksPillColors.resolveDisplayName(GregoriusDrugworksPayloadPills.getLeftColorId(stack), "White"));
            tooltip.add(TextFormatting.GRAY + localize("tooltip.gregoriusdrugworkspersistence.payload.pill.right", "Right Shell")
                    + ": " + TextFormatting.WHITE
                    + GregoriusDrugworksPillColors.resolveDisplayName(GregoriusDrugworksPayloadPills.getRightColorId(stack), "White"));
        }
    }

    private static String localize(String key, String fallback) {
        return I18n.hasKey(key) ? I18n.format(key) : fallback;
    }

    private static String capitalize(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        String normalized = value.replace('_', ' ');
        return Character.toUpperCase(normalized.charAt(0)) + normalized.substring(1);
    }
}
