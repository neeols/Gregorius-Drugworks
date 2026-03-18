package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials;
import gregtech.api.recipes.FluidCellInput;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.util.BaseCreativeTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class GregoriusDrugworksCreativeTabs {

    private GregoriusDrugworksCreativeTabs() {
    }

    public static CreativeTabs MAIN;
    public static CreativeTabs MATERIALS;
    private static List<ItemStack> materialStacksCache;

    public static void preInit() {
        if (MAIN != null) {
            return;
        }
        materialStacksCache = null;

        MAIN = new CreativeTabs("gregoriusdrugworkspersistence.main") {
            @Nonnull
            @Override
            public ItemStack createIcon() {
                if (GregoriusDrugworksMetaItems.KAPPA_RESET_AMPOULE != null) {
                    return new ItemStack(GregoriusDrugworksMetaItems.KAPPA_RESET_AMPOULE);
                }
                if (GregoriusDrugworksItems.CARBON_NANOTUBES != null) {
                    return new ItemStack(GregoriusDrugworksItems.CARBON_NANOTUBES);
                }
                if (GregoriusDrugworksBlocks.CARBONIZED_REACTOR_CASING != null) {
                    return new ItemStack(GregoriusDrugworksBlocks.CARBONIZED_REACTOR_CASING);
                }
                return ItemStack.EMPTY;
            }
        };

        MATERIALS = new BaseCreativeTab(Tags.MOD_ID + ".materials",
                GregoriusDrugworksCreativeTabs::createMaterialsIcon, true) {
            @Override
            public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> items) {
                for (ItemStack stack : getMaterialStacks()) {
                    items.add(stack.copy());
                }
            }
        };
    }

    private static ItemStack createMaterialsIcon() {
        if (GregoriusDrugworksMaterials.SalvinorinA != null) {
            ItemStack dust = OreDictUnifier.get(OrePrefix.dust, GregoriusDrugworksMaterials.SalvinorinA);
            if (!dust.isEmpty()) {
                return dust;
            }
        }
        if (GregoriusDrugworksItems.CARBON_NANOTUBES != null) {
            return new ItemStack(GregoriusDrugworksItems.CARBON_NANOTUBES);
        }
        return ItemStack.EMPTY;
    }

    private static List<ItemStack> getMaterialStacks() {
        if (materialStacksCache != null) {
            return materialStacksCache;
        }
        List<Material> materials = getGregoriusDrugworksMaterials();
        if (materials.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        Map<String, ItemStack> orderedStacks = new LinkedHashMap<>();
        for (Material material : materials) {
            for (OrePrefix prefix : OrePrefix.values()) {
                ItemStack stack = OreDictUnifier.get(prefix, material);
                addUniqueStack(orderedStacks, stack);
            }
            if (material.hasFluid()) {
                try {
                    addUniqueStack(orderedStacks, FluidCellInput.getFilledCell(material.getFluid()));
                } catch (IllegalArgumentException ignored) {
                }
            }
        }

        materialStacksCache = new ArrayList<>(orderedStacks.values());
        return materialStacksCache;
    }

    private static List<Material> getGregoriusDrugworksMaterials() {
        List<Material> materials = new ArrayList<>();
        for (Field field : GregoriusDrugworksMaterials.class.getFields()) {
            if (field.getType() != Material.class) {
                continue;
            }
            try {
                Object value = field.get(null);
                if (!(value instanceof Material)) {
                    continue;
                }
                Material material = (Material) value;
                if (Tags.MOD_ID.equals(material.getModid())) {
                    materials.add(material);
                }
            } catch (IllegalAccessException ignored) {
            }
        }
        materials.sort(Comparator.comparingInt(Material::getId));
        return materials;
    }

    private static void addUniqueStack(Map<String, ItemStack> orderedStacks, ItemStack stack) {
        if (stack == null || stack.isEmpty() || stack.getItem().getRegistryName() == null) {
            return;
        }
        String key = stack.getItem().getRegistryName() + "|" + stack.getMetadata() + "|" +
                (stack.hasTagCompound() ? stack.getTagCompound().toString() : "");
        orderedStacks.putIfAbsent(key, stack.copy());
    }
}
