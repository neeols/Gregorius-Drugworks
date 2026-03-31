package com.wurtzitane.gregoriusdrugworks.common.catalog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ContentCatalog<T> {

    private final Map<String, ContentCatalogEntry<T>> entries = new LinkedHashMap<>();

    public void clear() {
        entries.clear();
    }

    public void register(ContentCatalogEntry<T> entry) {
        if (entries.containsKey(entry.getId())) {
            throw new IllegalStateException("Duplicate catalog entry id: " + entry.getId());
        }
        entries.put(entry.getId(), entry);
    }

    public ContentCatalogEntry<T> get(String id) {
        return entries.get(id);
    }

    public Collection<ContentCatalogEntry<T>> all() {
        return Collections.unmodifiableCollection(entries.values());
    }

    public List<ContentCatalogEntry<T>> byFamily(ContentFamily family) {
        List<ContentCatalogEntry<T>> result = new ArrayList<>();
        for (ContentCatalogEntry<T> entry : entries.values()) {
            if (entry.getFamily() == family) {
                result.add(entry);
            }
        }
        return Collections.unmodifiableList(result);
    }
}