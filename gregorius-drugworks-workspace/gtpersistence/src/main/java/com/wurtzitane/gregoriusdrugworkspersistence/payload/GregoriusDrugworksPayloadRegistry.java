package com.wurtzitane.gregoriusdrugworkspersistence.payload;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadBehaviorFlag;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCatalog;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCategory;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadChargePolicy;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCompatibility;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadDefinition;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadKeys;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadModeDefinition;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadValidation;
import com.wurtzitane.gregoriusdrugworkspersistence.trip.TripHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.trigger.PayloadTriggerExecutor;
import com.wurtzitane.gregoriusdrugworkspersistence.visual.TripVisualBridge;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

public final class GregoriusDrugworksPayloadRegistry {

    private static final PayloadCatalog CATALOG = new PayloadCatalog();
    private static final Map<String, PayloadHandler> HANDLERS = new LinkedHashMap<>();
    private static boolean bootstrapped = false;

    private GregoriusDrugworksPayloadRegistry() {
    }

    public static void preInit() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;
        register(
                new PayloadDefinition(
                        "gregoriusdrugworkspersistence:salvinorin_a_payload",
                        PayloadCategory.STAGED_EFFECT,
                        PayloadCompatibility.UNIVERSAL,
                        "payload.gregoriusdrugworkspersistence.salvinorin_a",
                        1,
                        PayloadChargePolicy.SINGLE_USE,
                        EnumSet.of(PayloadBehaviorFlag.FORWARD_ITEM_USE),
                        "gregoriusdrugworkspersistence:salvinorin_a_payload",
                        null,
                        0,
                        null,
                        salvinorinModes()
                ),
                null
        );
        register(
                new PayloadDefinition(
                        "gregoriusdrugworkspersistence:salvinorin_antidote_payload",
                        PayloadCategory.ANTIDOTE,
                        PayloadCompatibility.APPLICATOR,
                        "payload.gregoriusdrugworkspersistence.salvinorin_antidote",
                        1,
                        PayloadChargePolicy.SINGLE_USE,
                        EnumSet.noneOf(PayloadBehaviorFlag.class),
                        null,
                        null,
                        0,
                        "gregoriusdrugworkspersistence:salvinorin_antidote_bundle"
                ),
                null
        );
    }

    private static Map<String, PayloadModeDefinition> salvinorinModes() {
        Map<String, PayloadModeDefinition> modes = new LinkedHashMap<>();
        modes.put("injection", PayloadModeDefinition.builder("injection")
                .onsetScale(0.45D)
                .peakScale(1.20D)
                .durationScale(0.72D)
                .build());
        modes.put("pill", PayloadModeDefinition.builder("pill")
                .onsetScale(1.45D)
                .peakScale(0.92D)
                .durationScale(1.30D)
                .build());
        modes.put("food", PayloadModeDefinition.builder("food")
                .onsetScale(1.80D)
                .peakScale(0.86D)
                .durationScale(1.55D)
                .build());
        return modes;
    }

    public static void register(PayloadDefinition definition, @Nullable PayloadHandler handler) {
        String error = PayloadValidation.validateDefinition(definition);
        if (error != null) {
            throw new IllegalStateException("Invalid payload definition " + definition.getId() + ": " + error);
        }

        CATALOG.register(definition);
        if (handler != null) {
            HANDLERS.put(definition.getId(), handler);
        }
    }

    public static Collection<PayloadDefinition> all() {
        return CATALOG.all();
    }

    @Nullable
    public static PayloadDefinition get(String payloadId) {
        return CATALOG.get(payloadId);
    }

    @Nullable
    public static PayloadHandler getHandler(String payloadId) {
        return HANDLERS.get(payloadId);
    }

    public static void applyResolved(EntityPlayerMP player, ItemStack carrierStack, ResolvedPayload payload) {
        PayloadHandler handler = getHandler(payload.getDefinition().getId());
        if (handler != null) {
            handler.apply(player, carrierStack, payload);
            return;
        }

        PayloadDefinition definition = payload.getDefinition();
        PayloadModeDefinition mode = payload.getMode();
        String triggerBundleId = firstNonEmpty(mode != null ? mode.getTriggerBundleId() : null, definition.getTriggerBundleId());
        String forwardItemId = firstNonEmpty(mode != null ? mode.getForwardItemId() : null, definition.getForwardItemId());
        String visualProfileId = firstNonEmpty(mode != null ? mode.getVisualProfileId() : null, definition.getVisualProfileId());
        int visualDurationTicks = mode != null && mode.getVisualDurationTicks() > 0
                ? mode.getVisualDurationTicks()
                : definition.getDefaultVisualDurationTicks();

        if (triggerBundleId != null && !triggerBundleId.isEmpty()) {
            boolean executed = com.wurtzitane.gregoriusdrugworkspersistence.trigger.TriggerBundleRuntimeExecutor.executeById(
                    player,
                    triggerBundleId
            );
            if (executed) {
                return;
            }
        }

        if ((definition.hasFlag(PayloadBehaviorFlag.FORWARD_ITEM_USE) || (mode != null && forwardItemId != null && !forwardItemId.isEmpty()))
                && forwardItemId != null
                && !forwardItemId.isEmpty()) {
            TripHooks.onItemUse(player, PayloadModeTripSupport.resolveTripItemId(forwardItemId, mode));
        }

        if ((definition.hasFlag(PayloadBehaviorFlag.START_VISUAL_PROFILE) || (mode != null && visualProfileId != null && !visualProfileId.isEmpty()))
                && visualProfileId != null
                && !visualProfileId.isEmpty()) {
            TripVisualBridge.activate(player, visualProfileId, visualDurationTicks);
        }
    }

    private static String firstNonEmpty(String preferred, String fallback) {
        if (preferred != null && !preferred.isEmpty()) {
            return preferred;
        }
        return fallback;
    }

    public interface PayloadHandler {
        void apply(EntityPlayerMP player, ItemStack carrierStack, ResolvedPayload payload);
    }

    public static final class ResolvedPayload {
        private final PayloadDefinition definition;
        private final int charges;
        private final int maxCharges;
        private final net.minecraft.nbt.NBTTagCompound extraData;

        public ResolvedPayload(
                PayloadDefinition definition,
                int charges,
                int maxCharges,
                net.minecraft.nbt.NBTTagCompound extraData
        ) {
            this.definition = definition;
            this.charges = charges;
            this.maxCharges = maxCharges;
            this.extraData = extraData;
        }

        public PayloadDefinition getDefinition() {
            return definition;
        }

        public int getCharges() {
            return charges;
        }

        public int getMaxCharges() {
            return maxCharges;
        }

        public net.minecraft.nbt.NBTTagCompound getExtraData() {
            return extraData;
        }

        public String getModeId() {
            return extraData == null || !extraData.hasKey(PayloadKeys.MODE_KEY)
                    ? ""
                    : extraData.getString(PayloadKeys.MODE_KEY);
        }

        public PayloadModeDefinition getMode() {
            return definition.getMode(getModeId());
        }
    }
}
