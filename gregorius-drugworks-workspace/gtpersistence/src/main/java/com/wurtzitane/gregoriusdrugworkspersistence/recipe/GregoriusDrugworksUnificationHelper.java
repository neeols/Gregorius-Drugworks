package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import gregtech.api.GregTechAPI;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.ItemAndMetadata;
import gregtech.api.unification.stack.UnificationEntry;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * Repairs GTCEu unification lookups for materials whose camel-case names begin with a digit.
 * GT registers ore names like {@code dust2DeacetoxysalvinorinA}, but its reverse parser cannot
 * reconstruct {@code OrePrefix.dust + material} from those names, so direct lookups come back empty.
 *
 * @author wurtzitane
 */
public final class GregoriusDrugworksUnificationHelper {

    private static boolean repairedNumericMaterialUnification;

    private GregoriusDrugworksUnificationHelper() {
    }

    public static ItemStack get(OrePrefix prefix, Material material, int stackSize) {
        ItemStack stack = OreDictUnifier.get(prefix, material, stackSize);
        if (!stack.isEmpty()) {
            return stack;
        }

        ItemStack fallback = OreDictUnifier.get(prefix.name() + material.toCamelCaseString());
        if (fallback.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack copy = fallback.copy();
        copy.setCount(stackSize);
        return copy;
    }

    public static ItemStack get(OrePrefix prefix, Material material) {
        return get(prefix, material, 1);
    }

    public static void repairNumericMaterialUnification() {
        if (repairedNumericMaterialUnification) {
            return;
        }

        try {
            Map<UnificationEntry, ArrayList<ItemAndMetadata>> stackUnificationItems =
                    getStaticField("stackUnificationItems");
            Map<ItemAndMetadata, UnificationEntry> stackUnificationInfo =
                    getStaticField("stackUnificationInfo");
            Comparator<ItemAndMetadata> comparator = OreDictUnifier.getSimpleItemStackComparator();

            for (Material material : GregTechAPI.materialManager.getRegisteredMaterials()) {
                if (!Tags.MOD_ID.equals(material.getModid())) {
                    continue;
                }
                String camelName = material.toCamelCaseString();
                if (camelName.isEmpty() || !Character.isDigit(camelName.charAt(0))) {
                    continue;
                }

                for (OrePrefix prefix : OrePrefix.values()) {
                    if (!prefix.doGenerateItem(material) || !OreDictUnifier.get(prefix, material).isEmpty()) {
                        continue;
                    }

                    ItemStack fallback = OreDictUnifier.get(prefix.name() + camelName);
                    if (fallback.isEmpty()) {
                        continue;
                    }

                    ItemAndMetadata key = new ItemAndMetadata(fallback);
                    UnificationEntry entry = new UnificationEntry(prefix, material);
                    ArrayList<ItemAndMetadata> items = stackUnificationItems.computeIfAbsent(entry,
                            ignored -> new ArrayList<>());
                    if (!items.contains(key)) {
                        items.add(key);
                        items.sort(comparator);
                    }
                    if (!entry.orePrefix.isMarkerPrefix()) {
                        stackUnificationInfo.put(key, entry);
                    }
                }
            }

            repairedNumericMaterialUnification = true;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to repair numeric-leading material unification.", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T getStaticField(String fieldName) throws ReflectiveOperationException {
        Field field = OreDictUnifier.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(null);
    }
}
