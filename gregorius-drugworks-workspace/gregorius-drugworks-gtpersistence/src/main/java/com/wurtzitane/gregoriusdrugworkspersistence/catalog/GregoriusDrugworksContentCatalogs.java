package com.wurtzitane.gregoriusdrugworkspersistence.catalog;

import com.wurtzitane.gregoriusdrugworks.common.catalog.ContentCatalog;
import com.wurtzitane.gregoriusdrugworks.common.catalog.ContentCatalogEntry;
import com.wurtzitane.gregoriusdrugworks.common.catalog.ContentFamily;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.List;

public final class GregoriusDrugworksContentCatalogs {

    private static final ContentCatalog<Item> ITEM_CATALOG = new ContentCatalog<>();

    private GregoriusDrugworksContentCatalogs() {
    }

    public static void clear() {
        ITEM_CATALOG.clear();
    }

    public static void registerItem(Item item, ContentFamily family, boolean sample) {
        ResourceLocation registryName = item.getRegistryName();
        if (registryName == null) {
            throw new IllegalStateException("Cannot catalog item without registry name: " + item);
        }

        ITEM_CATALOG.register(
                new ContentCatalogEntry<>(
                        registryName.toString(),
                        family,
                        item.getTranslationKey(),
                        sample,
                        item
                )
        );
    }

    public static ContentCatalogEntry<Item> getItem(String id) {
        return ITEM_CATALOG.get(id);
    }

    public static Collection<ContentCatalogEntry<Item>> allItems() {
        return ITEM_CATALOG.all();
    }

    public static List<ContentCatalogEntry<Item>> byFamily(ContentFamily family) {
        return ITEM_CATALOG.byFamily(family);
    }
}