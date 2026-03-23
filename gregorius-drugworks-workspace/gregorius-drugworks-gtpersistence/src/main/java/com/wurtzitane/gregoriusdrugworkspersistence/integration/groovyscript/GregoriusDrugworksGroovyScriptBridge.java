package com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript;

import com.wurtzitane.gregoriusdrugworks.common.catalog.ContentFamily;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadDefinition;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadSourceDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trigger.TriggerBundleDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.AntidoteDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripDefinition;
import com.wurtzitane.gregoriusdrugworks.common.visual.VisualEffectProfile;
import com.wurtzitane.gregoriusdrugworkspersistence.catalog.GregoriusDrugworksContentCatalogs;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.ItemInhalationConsumable;
import com.wurtzitane.gregoriusdrugworkspersistence.medical.GregoriusDrugworksApplicatorPayloads;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadRegistry;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadSources;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.ItemPillBase;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.PillItemDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.trigger.GregoriusDrugworksTriggerBundles;
import com.wurtzitane.gregoriusdrugworkspersistence.trip.TripRegistrar;
import com.wurtzitane.gregoriusdrugworkspersistence.visual.GregoriusDrugworksVisualProfiles;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class GregoriusDrugworksGroovyScriptBridge {

    private static final Map<String, ScriptedItemEntry> SCRIPTED_ITEMS = new LinkedHashMap<>();
    private static boolean itemRegistrationClosed = false;

    private GregoriusDrugworksGroovyScriptBridge() {
    }

    public static void onCommonPreInit() {
        for (ScriptedItemEntry entry : SCRIPTED_ITEMS.values()) {
            GregoriusDrugworksContentCatalogs.registerItem(entry.item, entry.family, entry.sample);
        }
    }

    public static Item registerPill(PillItemDefinition definition) {
        return registerItem(new ItemPillBase(definition), ContentFamily.PILL, false);
    }

    public static Item registerVape(InhalationDefinition definition, boolean sample) {
        return registerItem(new ItemInhalationConsumable(definition), ContentFamily.INHALATION, sample);
    }

    public static void registerPayload(PayloadDefinition definition) {
        GregoriusDrugworksPayloadRegistry.register(definition, null);
    }

    public static void registerPayloadSource(PayloadSourceDefinition definition) {
        GregoriusDrugworksPayloadSources.register(definition);
    }

    public static void registerTriggerBundle(TriggerBundleDefinition definition) {
        GregoriusDrugworksTriggerBundles.register(definition);
    }

    public static void registerVisualProfile(VisualEffectProfile profile) {
        GregoriusDrugworksVisualProfiles.register(profile);
    }

    public static void registerTrip(TripDefinition definition) {
        TripRegistrar.registerTrip(definition);
    }

    public static void registerAntidote(AntidoteDefinition definition) {
        TripRegistrar.registerAntidote(definition);
    }

    public static void allowAntidoteForTrip(String antidoteItemId, String tripItemId) {
        TripRegistrar.allowAntidoteForTrip(antidoteItemId, tripItemId);
    }

    public static boolean loadApplicatorPayload(
            ItemStack applicatorStack,
            String payloadId,
            int chargesOverride,
            NBTTagCompound payloadData
    ) {
        return GregoriusDrugworksApplicatorPayloads.loadPayloadIntoApplicator(applicatorStack, payloadId, chargesOverride, payloadData);
    }

    public static void registerItems(IForgeRegistry<Item> registry) {
        itemRegistrationClosed = true;
        for (ScriptedItemEntry entry : SCRIPTED_ITEMS.values()) {
            registry.register(entry.item);
        }
    }

    static List<Item> getScriptedItems() {
        List<Item> items = new ArrayList<>(SCRIPTED_ITEMS.size());
        for (ScriptedItemEntry entry : SCRIPTED_ITEMS.values()) {
            items.add(entry.item);
        }
        return items;
    }

    private static Item registerItem(Item item, ContentFamily family, boolean sample) {
        if (itemRegistrationClosed) {
            throw new IllegalStateException("Gregorius scripted items must be registered during GroovyScript preInit scripts.");
        }
        if (item.getRegistryName() == null) {
            throw new IllegalStateException("Scripted item is missing a registry name: " + item);
        }

        String itemId = item.getRegistryName().toString();
        if (SCRIPTED_ITEMS.containsKey(itemId)) {
            throw new IllegalStateException("A scripted Gregorius item with id " + itemId + " is already registered.");
        }

        SCRIPTED_ITEMS.put(itemId, new ScriptedItemEntry(item, family, sample));
        GregoriusDrugworksContentCatalogs.registerItem(item, family, sample);
        return item;
    }

    private static final class ScriptedItemEntry {
        private final Item item;
        private final ContentFamily family;
        private final boolean sample;

        private ScriptedItemEntry(Item item, ContentFamily family, boolean sample) {
            this.item = item;
            this.family = family;
            this.sample = sample;
        }
    }
}
