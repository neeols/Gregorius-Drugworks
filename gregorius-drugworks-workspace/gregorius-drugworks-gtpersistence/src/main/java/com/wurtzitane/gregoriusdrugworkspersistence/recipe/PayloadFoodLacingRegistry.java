package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksMetaItems;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadCarrierDataKeys;
import gregtech.api.GregTechAPI;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Declarative registry for food-lacing crafting inputs.
 *
 * @author wurtzitane
 */
public final class PayloadFoodLacingRegistry {

    private static final List<Entry> ENTRIES = new ArrayList<>();
    private static boolean bootstrapped = false;

    private PayloadFoodLacingRegistry() {
    }

    public static void preInit() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;
        ENTRIES.clear();
        builder("salvinorin_a_food")
                .additive(OrePrefix.dust, GregoriusDrugworksMaterials.SalvinorinA)
                .payload("gregoriusdrugworkspersistence:salvinorin_a_payload")
                .mode("food")
                .allVanillaFoods()
                .register();
        builder("lsd_food")
                .additive(OrePrefix.dust, GregoriusDrugworksMaterials.LSD)
                .payload("gregoriusdrugworkspersistence:lsd_payload")
                .mode("food")
                .allVanillaFoods()
                .register();
        builder("crystalmeth_food")
                .additive(GregoriusDrugworksMetaItems.CRYSTALMETH)
                .payload("gregoriusdrugworkspersistence:methamphetamine_payload")
                .mode("food")
                .allVanillaFoods()
                .register();
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static void register(Entry entry) {
        ENTRIES.add(entry);
    }

    public static void laceFoods(OrePrefix prefix, Material material, String payloadId) {
        laceFoods(prefix, material, payloadId, null);
    }

    public static void laceFoods(OrePrefix prefix, Material material, String payloadId, @Nullable String modeId) {
        builder(autoId(payloadId))
                .additive(prefix, material)
                .payload(payloadId)
                .mode(modeId)
                .anyFood()
                .register();
    }

    public static void laceFoods(OrePrefix prefix, String materialName, String payloadId) {
        laceFoods(prefix, materialName, payloadId, null);
    }

    public static void laceFoods(OrePrefix prefix, String materialName, String payloadId, @Nullable String modeId) {
        Material material = findMaterial(materialName);
        if (material == null) {
            throw new IllegalStateException("Unknown material for food lacing: " + materialName);
        }
        laceFoods(prefix, material, payloadId, modeId);
    }

    public static void laceFoods(Item item, String payloadId) {
        laceFoods(new ItemStack(item), payloadId, null);
    }

    public static void laceFoods(Item item, String payloadId, @Nullable String modeId) {
        laceFoods(new ItemStack(item), payloadId, modeId);
    }

    public static void laceFoods(ItemStack stack, String payloadId) {
        laceFoods(stack, payloadId, null);
    }

    public static void laceFoods(ItemStack stack, String payloadId, @Nullable String modeId) {
        builder(autoId(payloadId))
                .additive(stack)
                .payload(payloadId)
                .mode(modeId)
                .anyFood()
                .register();
    }

    public static void laceFoods(String itemId, String payloadId) {
        laceFoods(itemId, payloadId, null);
    }

    public static void laceFoods(String itemId, String payloadId, @Nullable String modeId) {
        Item item = Item.getByNameOrId(itemId);
        if (item == null) {
            throw new IllegalStateException("Unknown item for food lacing: " + itemId);
        }
        laceFoods(new ItemStack(item), payloadId, modeId);
    }

    public static List<Entry> all() {
        return Collections.unmodifiableList(ENTRIES);
    }

    @Nullable
    public static Entry find(ItemStack food, ItemStack additive) {
        for (Entry entry : ENTRIES) {
            if (entry.matches(food, additive)) {
                return entry;
            }
        }
        return null;
    }

    public static boolean isSupportedFood(ItemStack stack) {
        for (Entry entry : ENTRIES) {
            if (entry.matchesFood(stack)) {
                return true;
            }
        }
        return false;
    }

    public static NBTTagCompound createFoodPayloadData(@Nullable String modeId, boolean revealed) {
        NBTTagCompound extra = new NBTTagCompound();
        extra.setBoolean(PayloadCarrierDataKeys.REVEALED_KEY, revealed);
        if (modeId != null && !modeId.trim().isEmpty()) {
            extra.setString(com.wurtzitane.gregoriusdrugworks.common.payload.PayloadKeys.MODE_KEY, modeId.trim());
        }
        return extra;
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

    private static boolean isFoodStack(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemFood;
    }

    private static String autoId(String payloadId) {
        String normalized = payloadId == null ? "payload" : payloadId.replace(':', '_').replace('/', '_');
        return "auto_food_lacing_" + normalized + "_" + (ENTRIES.size() + 1);
    }

    public static final class Entry {
        private final String id;
        private final Matcher additiveMatcher;
        private final Matcher foodMatcher;
        private final String payloadId;
        private final String modeId;

        private Entry(String id, Matcher additiveMatcher, Matcher foodMatcher, String payloadId, @Nullable String modeId) {
            this.id = id;
            this.additiveMatcher = additiveMatcher;
            this.foodMatcher = foodMatcher;
            this.payloadId = payloadId;
            this.modeId = modeId;
        }

        public String getId() {
            return id;
        }

        public boolean matches(ItemStack food, ItemStack additive) {
            return matchesFood(food) && matchesAdditive(additive);
        }

        public boolean matchesAdditive(ItemStack stack) {
            return additiveMatcher.matches(stack);
        }

        public boolean matchesFood(ItemStack stack) {
            return foodMatcher.matches(stack);
        }

        public String getPayloadId() {
            return payloadId;
        }

        public String getModeId() {
            return modeId;
        }
    }

    private interface Matcher {
        boolean matches(ItemStack stack);
    }

    public static final class Builder {
        private final String id;
        private Matcher additiveMatcher;
        private Matcher foodMatcher;
        private String payloadId;
        private String modeId;

        private Builder(String id) {
            this.id = id == null ? "" : id.trim();
        }

        public Builder additive(ItemStack stack) {
            ItemStack template = stack.copy();
            template.setCount(1);
            this.additiveMatcher = candidate -> !candidate.isEmpty() && ItemStack.areItemsEqual(candidate, template);
            return this;
        }

        public Builder additive(Item item) {
            return additive(new ItemStack(item));
        }

        public Builder additive(String itemId) {
            Item item = Item.getByNameOrId(itemId);
            if (item == null) {
                throw new IllegalStateException("Unknown food-lacing additive item: " + itemId);
            }
            return additive(item);
        }

        public Builder additive(OrePrefix prefix, Material material) {
            this.additiveMatcher = candidate -> {
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
                throw new IllegalStateException("Unknown food-lacing additive material: " + materialName);
            }
            return additive(prefix, material);
        }

        public Builder anyFood() {
            this.foodMatcher = PayloadFoodLacingRegistry::isFoodStack;
            return this;
        }

        public Builder allVanillaFoods() {
            this.foodMatcher = candidate -> {
                if (!isFoodStack(candidate)) {
                    return false;
                }
                ResourceLocation name = candidate.getItem().getRegistryName();
                return name != null && "minecraft".equals(name.getNamespace());
            };
            return this;
        }

        public Builder food(ItemStack stack) {
            ItemStack template = stack.copy();
            template.setCount(1);
            this.foodMatcher = candidate -> !candidate.isEmpty() && ItemStack.areItemsEqual(candidate, template);
            return this;
        }

        public Builder food(Item item) {
            return food(new ItemStack(item));
        }

        public Builder food(String itemId) {
            Item item = Item.getByNameOrId(itemId);
            if (item == null) {
                throw new IllegalStateException("Unknown laced food target item: " + itemId);
            }
            return food(item);
        }

        public Builder payload(String payloadId) {
            this.payloadId = payloadId;
            return this;
        }

        public Builder mode(@Nullable String modeId) {
            this.modeId = modeId;
            return this;
        }

        public Entry build() {
            if (id.isEmpty()) {
                throw new IllegalStateException("Food-lacing id cannot be empty.");
            }
            if (additiveMatcher == null) {
                throw new IllegalStateException("Food-lacing additive matcher must be defined for " + id);
            }
            if (payloadId == null || payloadId.trim().isEmpty()) {
                throw new IllegalStateException("Food-lacing payload id must be defined for " + id);
            }
            Matcher resolvedFoodMatcher = foodMatcher == null ? PayloadFoodLacingRegistry::isFoodStack : foodMatcher;
            return new Entry(id, additiveMatcher, resolvedFoodMatcher, payloadId.trim(),
                    modeId == null ? null : modeId.trim());
        }

        public Entry register() {
            Entry entry = build();
            PayloadFoodLacingRegistry.register(entry);
            return entry;
        }
    }
}
