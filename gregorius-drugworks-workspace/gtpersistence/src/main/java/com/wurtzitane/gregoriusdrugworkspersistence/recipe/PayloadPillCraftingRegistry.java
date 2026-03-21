package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksMetaItems;
import gregtech.api.GregTechAPI;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Declarative registry for pill payload crafting additives.
 *
 * @author wurtzitane
 */
public final class PayloadPillCraftingRegistry {

    private static final List<Entry> ENTRIES = new ArrayList<>();
    private static boolean bootstrapped = false;

    private PayloadPillCraftingRegistry() {
    }

    public static void preInit() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;
        ENTRIES.clear();

        builder("salvinorin_a_dust")
                .additive(OrePrefix.dust, GregoriusDrugworksMaterials.SalvinorinA)
                .payload("gregoriusdrugworkspersistence:salvinorin_a_payload")
                .mode("pill")
                .register();
        builder("lsd_dust")
                .additive(OrePrefix.dust, GregoriusDrugworksMaterials.LSD)
                .payload("gregoriusdrugworkspersistence:lsd_payload")
                .mode("pill")
                .register();
        builder("crystalmeth_item")
                .additive(GregoriusDrugworksMetaItems.CRYSTALMETH)
                .payload("gregoriusdrugworkspersistence:methamphetamine_payload")
                .mode("pill")
                .register();
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static void register(Entry entry) {
        ENTRIES.add(entry);
    }

    public static Collection<Entry> all() {
        return Collections.unmodifiableList(ENTRIES);
    }

    @Nullable
    public static Entry find(ItemStack additive) {
        for (Entry entry : ENTRIES) {
            if (entry.matches(additive)) {
                return entry;
            }
        }
        return null;
    }

    @Nullable
    private static Material findMaterial(String materialName) {
        String needle = materialName == null ? "" : materialName.trim().toLowerCase(Locale.ROOT);
        for (Material material : GregTechAPI.materialManager.getRegisteredMaterials()) {
            if (material == null) {
                continue;
            }
            if (needle.equals(material.getName()) || needle.equals(material.toCamelCaseString().toLowerCase(Locale.ROOT))) {
                return material;
            }
        }
        return null;
    }

    public static final class Entry {
        private final String id;
        private final Matcher matcher;
        private final String payloadId;
        private final String modeId;
        private final ItemStack remainder;

        private Entry(String id, Matcher matcher, String payloadId, @Nullable String modeId, ItemStack remainder) {
            this.id = id;
            this.matcher = matcher;
            this.payloadId = payloadId;
            this.modeId = modeId;
            this.remainder = remainder.copy();
            this.remainder.setCount(this.remainder.isEmpty() ? 0 : 1);
        }

        public String getId() {
            return id;
        }

        public boolean matches(ItemStack stack) {
            return matcher.matches(stack);
        }

        public String getPayloadId() {
            return payloadId;
        }

        @Nullable
        public String getModeId() {
            return modeId;
        }

        public ItemStack getRemainder() {
            return remainder.copy();
        }
    }

    public static final class Builder {
        private final String id;
        private Matcher matcher;
        private String payloadId;
        private String modeId;
        private ItemStack remainder = ItemStack.EMPTY;

        private Builder(String id) {
            this.id = id == null ? "" : id.trim();
        }

        public Builder additive(ItemStack stack) {
            ItemStack template = stack.copy();
            template.setCount(1);
            this.matcher = candidate -> ItemStack.areItemsEqual(candidate, template);
            return this;
        }

        public Builder additive(Item item) {
            return additive(new ItemStack(item));
        }

        public Builder additive(String itemId) {
            Item item = Item.getByNameOrId(itemId);
            if (item == null) {
                throw new IllegalStateException("Unknown pill additive item: " + itemId);
            }
            return additive(new ItemStack(item));
        }

        public Builder additive(OrePrefix prefix, Material material) {
            this.matcher = candidate -> {
                if (candidate.isEmpty()) {
                    return false;
                }
                ItemStack template = OreDictUnifier.get(prefix, material, 1);
                return !template.isEmpty() && ItemStack.areItemsEqual(candidate, template);
            };
            return this;
        }

        public Builder additive(OrePrefix prefix, String materialName) {
            Material material = findMaterial(materialName);
            if (material == null) {
                throw new IllegalStateException("Unknown pill additive material: " + materialName);
            }
            return additive(prefix, material);
        }

        public Builder payload(String payloadId) {
            this.payloadId = payloadId;
            return this;
        }

        public Builder mode(@Nullable String modeId) {
            this.modeId = modeId;
            return this;
        }

        public Builder remainder(ItemStack remainder) {
            this.remainder = remainder.copy();
            return this;
        }

        public Entry build() {
            if (id.isEmpty()) {
                throw new IllegalStateException("Pill crafting id cannot be empty.");
            }
            if (matcher == null) {
                throw new IllegalStateException("Pill crafting additive matcher must be defined for " + id);
            }
            if (payloadId == null || payloadId.trim().isEmpty()) {
                throw new IllegalStateException("Pill crafting payload id must be defined for " + id);
            }
            return new Entry(id, matcher, payloadId.trim(), modeId == null ? null : modeId.trim(), remainder);
        }

        public Entry register() {
            Entry entry = build();
            PayloadPillCraftingRegistry.register(entry);
            return entry;
        }
    }

    private interface Matcher {
        boolean matches(ItemStack stack);
    }
}
