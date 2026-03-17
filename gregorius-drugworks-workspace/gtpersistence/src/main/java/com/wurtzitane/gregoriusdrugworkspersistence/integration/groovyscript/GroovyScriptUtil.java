package com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript;

import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

final class GroovyScriptUtil {

    private GroovyScriptUtil() {
    }

    static ResourceLocation resourceLocation(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Expected a non-empty resource location");
        }

        String trimmed = value.trim();
        return trimmed.indexOf(':') >= 0 ? new ResourceLocation(trimmed) : GregoriusDrugworksUtil.makeName(trimmed);
    }

    static Item resolveItem(String itemId) {
        Item item = Item.REGISTRY.getObject(resourceLocation(itemId));
        if (item == null) {
            throw new IllegalArgumentException("Unknown item id: " + itemId);
        }
        return item;
    }

    static ItemStack itemStack(String itemId) {
        return new ItemStack(resolveItem(itemId));
    }

    static EnumParticleTypes particle(String particleId) {
        String normalized = normalizeEnumName(particleId);
        for (EnumParticleTypes particleType : EnumParticleTypes.values()) {
            if (particleType.name().equals(normalized) || particleType.getParticleName().equalsIgnoreCase(particleId)) {
                return particleType;
            }
        }
        throw new IllegalArgumentException("Unknown particle id: " + particleId);
    }

    static <E extends Enum<E>> E enumValue(Class<E> enumType, String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Expected a non-empty value for " + enumType.getSimpleName());
        }
        return Enum.valueOf(enumType, normalizeEnumName(value));
    }

    private static String normalizeEnumName(String value) {
        return value.trim()
                .replace('-', '_')
                .replace(' ', '_')
                .toUpperCase(Locale.ROOT);
    }
}
