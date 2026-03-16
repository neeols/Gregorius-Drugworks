package com.wurtzitane.gregoriusdrugworkspersistence.debug;

import com.wurtzitane.gregoriusdrugworks.common.debug.GdwDebugCategory;

import java.util.EnumSet;

public final class GregoriusDrugworksDebug {

    private static boolean enabled = false;
    private static final EnumSet<GdwDebugCategory> ENABLED_CATEGORIES = EnumSet.allOf(GdwDebugCategory.class);

    private GregoriusDrugworksDebug() {
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }

    public static boolean isEnabled(GdwDebugCategory category) {
        return enabled && ENABLED_CATEGORIES.contains(category);
    }

    public static void log(GdwDebugCategory category, String message) {
        if (!isEnabled(category)) {
            return;
        }
        System.out.println("[GDW][" + category.name() + "] " + message);
    }
}
