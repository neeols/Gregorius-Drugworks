package com.wurtzitane.gregoriusdrugworkspersistence.pill;

import gregtech.api.util.DyeUtil;
import gregtech.common.items.MetaItems;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Central registry for dye-driven pill shell colors.
 *
 * @author wurtzitane
 */
public final class GregoriusDrugworksPillColors {

    private static final Map<String, PillColorDefinition> COLORS = new LinkedHashMap<>();
    private static boolean bootstrapped = false;

    private GregoriusDrugworksPillColors() {
    }

    public static void preInit() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;
        COLORS.clear();

        registerVanilla("white", "White", 0xEDEDED, 15);
        registerVanilla("orange", "Orange", 0xD87F33, 14);
        registerVanilla("magenta", "Magenta", 0xB24CD8, 13);
        registerVanilla("light_blue", "Light Blue", 0x6699D8, 12);
        registerVanilla("yellow", "Yellow", 0xE5E533, 11);
        registerVanilla("lime", "Lime", 0x7FCC19, 10);
        registerVanilla("pink", "Pink", 0xF27FA5, 9);
        registerVanilla("gray", "Gray", 0x4C4C4C, 8);
        registerVanilla("light_gray", "Light Gray", 0x999999, 7);
        registerVanilla("cyan", "Cyan", 0x4C7F99, 6);
        registerVanilla("purple", "Purple", 0x7F3FB2, 5);
        registerVanilla("blue", "Blue", 0x334CB2, 4);
        registerVanilla("brown", "Brown", 0x664C33, 3);
        registerVanilla("green", "Green", 0x667F33, 2);
        registerVanilla("red", "Red", 0x993333, 1);
        registerVanilla("black", "Black", 0x1A1A1A, 0);
    }

    public static void register(PillColorDefinition definition) {
        String key = normalize(definition.getId());
        if (key.isEmpty()) {
            throw new IllegalStateException("Pill color id cannot be empty.");
        }
        COLORS.put(key, definition);
    }

    @Nullable
    public static PillColorDefinition get(String id) {
        return id == null ? null : COLORS.get(normalize(id));
    }

    public static Collection<PillColorDefinition> all() {
        return Collections.unmodifiableCollection(COLORS.values());
    }

    @Nullable
    public static PillColorDefinition matchDye(ItemStack stack) {
        for (PillColorDefinition definition : COLORS.values()) {
            if (definition.matchesDye(stack)) {
                return definition;
            }
        }
        return null;
    }

    public static int resolveRgb(String id, int fallback) {
        PillColorDefinition definition = get(id);
        return definition == null ? fallback : definition.getRgb();
    }

    public static String resolveDisplayName(String id, String fallback) {
        PillColorDefinition definition = get(id);
        return definition == null ? fallback : definition.getDisplayName();
    }

    public static String getDefaultColorId() {
        return "white";
    }

    private static void registerVanilla(String id, String displayName, int rgb, int dyeMeta) {
        EnumDyeColor dyeColor = EnumDyeColor.values()[15 - dyeMeta];
        PillColorDefinition.Builder builder = PillColorDefinition.builder(id)
                .displayName(displayName)
                .rgb(rgb)
                .dye(new ItemStack(Items.DYE, 1, dyeMeta))
                .oreDict(DyeUtil.getOredictColorName(dyeColor));

        ItemStack gtChemicalDye = getGtChemicalDye(dyeColor);
        if (!gtChemicalDye.isEmpty()) {
            builder.addDye(gtChemicalDye);
        }

        register(builder.build());
    }

    private static ItemStack getGtChemicalDye(EnumDyeColor dyeColor) {
        int index = dyeColor.ordinal();
        if (index < 0 || index >= MetaItems.DYE_ONLY_ITEMS.length) {
            return ItemStack.EMPTY;
        }

        if (MetaItems.DYE_ONLY_ITEMS[index] == null) {
            return ItemStack.EMPTY;
        }

        return MetaItems.DYE_ONLY_ITEMS[index].getStackForm();
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(java.util.Locale.ROOT);
    }
}
