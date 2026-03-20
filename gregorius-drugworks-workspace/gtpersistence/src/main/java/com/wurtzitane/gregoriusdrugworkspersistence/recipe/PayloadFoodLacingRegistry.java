package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadCarrierDataKeys;
import gregtech.api.GregTechAPI;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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
        laceFoods(OrePrefix.dust, GregoriusDrugworksMaterials.SalvinorinA,
                "gregoriusdrugworkspersistence:salvinorin_a_payload", "food");
    }

    public static void laceFoods(OrePrefix prefix, Material material, String payloadId) {
        laceFoods(prefix, material, payloadId, null);
    }

    public static void laceFoods(OrePrefix prefix, Material material, String payloadId, @Nullable String modeId) {
        ENTRIES.add(new Entry(stack -> ItemStack.areItemsEqual(stack, OreDictUnifier.get(prefix, material, 1)), payloadId, modeId));
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
        ItemStack template = stack.copy();
        template.setCount(1);
        ENTRIES.add(new Entry(candidate -> ItemStack.areItemsEqual(candidate, template), payloadId, modeId));
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
    public static Entry find(ItemStack additive) {
        for (Entry entry : ENTRIES) {
            if (entry.matches(additive)) {
                return entry;
            }
        }
        return null;
    }

    public static boolean isSupportedFood(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemFood;
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

    public static final class Entry {
        private final Matcher matcher;
        private final String payloadId;
        private final String modeId;

        private Entry(Matcher matcher, String payloadId, @Nullable String modeId) {
            this.matcher = matcher;
            this.payloadId = payloadId;
            this.modeId = modeId;
        }

        public boolean matches(ItemStack stack) {
            return matcher.matches(stack);
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
}
