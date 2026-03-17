package com.wurtzitane.gregoriusdrugworkspersistence.payload;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadBehaviorFlag;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCatalog;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCategory;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadChargePolicy;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCompatibility;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadDefinition;
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
                        "gregoriusdrugworkspersistence:naloxone_payload",
                        PayloadCategory.ANTIDOTE,
                        PayloadCompatibility.APPLICATOR,
                        "payload.gregoriusdrugworkspersistence.naloxone",
                        1,
                        PayloadChargePolicy.SINGLE_USE,
                        EnumSet.noneOf(PayloadBehaviorFlag.class),
                        null,
                        null,
                        0,
                        "gregoriusdrugworkspersistence:naloxone_bundle"
                ),
                null
        );

        register(
                new PayloadDefinition(
                        "gregoriusdrugworkspersistence:flumazenil_payload",
                        PayloadCategory.ANTIDOTE,
                        PayloadCompatibility.APPLICATOR,
                        "payload.gregoriusdrugworkspersistence.flumazenil",
                        1,
                        PayloadChargePolicy.SINGLE_USE,
                        EnumSet.noneOf(PayloadBehaviorFlag.class),
                        null,
                        null,
                        0,
                        "gregoriusdrugworkspersistence:flumazenil_bundle"
                ),
                null
        );

        register(
                new PayloadDefinition(
                        "gregoriusdrugworkspersistence:glucagon_payload",
                        PayloadCategory.DELIVERY_ITEM,
                        PayloadCompatibility.APPLICATOR,
                        "payload.gregoriusdrugworkspersistence.glucagon",
                        1,
                        PayloadChargePolicy.SINGLE_USE,
                        EnumSet.noneOf(PayloadBehaviorFlag.class),
                        null,
                        null,
                        0,
                        "gregoriusdrugworkspersistence:glucagon_bundle"
                ),
                null
        );

        register(
                new PayloadDefinition(
                        "gregoriusdrugworkspersistence:anomaly_payload",
                        PayloadCategory.STAGED_EFFECT,
                        PayloadCompatibility.APPLICATOR,
                        "payload.gregoriusdrugworkspersistence.anomaly",
                        1,
                        PayloadChargePolicy.SINGLE_USE,
                        EnumSet.noneOf(PayloadBehaviorFlag.class),
                        null,
                        null,
                        0,
                        "gregoriusdrugworkspersistence:anomaly_bundle"
                ),
                null
        );
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

        if (definition.getTriggerBundleId() != null && !definition.getTriggerBundleId().isEmpty()) {
            boolean executed = com.wurtzitane.gregoriusdrugworkspersistence.trigger.TriggerBundleRuntimeExecutor.executeById(
                    player,
                    definition.getTriggerBundleId()
            );
            if (executed) {
                return;
            }
        }

        if (definition.hasFlag(PayloadBehaviorFlag.FORWARD_ITEM_USE)
                && definition.getForwardItemId() != null
                && !definition.getForwardItemId().isEmpty()) {
            TripHooks.onItemUse(player, definition.getForwardItemId());
        }

        if (definition.hasFlag(PayloadBehaviorFlag.START_VISUAL_PROFILE)
                && definition.getVisualProfileId() != null
                && !definition.getVisualProfileId().isEmpty()) {
            TripVisualBridge.activate(player, definition.getVisualProfileId(), definition.getDefaultVisualDurationTicks());
        }
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
    }
}