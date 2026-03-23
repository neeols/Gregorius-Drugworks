package com.wurtzitane.gregoriusdrugworks.common.trigger;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class TriggerBundleCatalog {

    private final Map<String, TriggerBundleDefinition> bundles = new LinkedHashMap<>();

    public void clear() {
        bundles.clear();
    }

    public void register(TriggerBundleDefinition definition) {
        if (bundles.containsKey(definition.getId())) {
            throw new IllegalStateException("Duplicate trigger bundle id: " + definition.getId());
        }
        bundles.put(definition.getId(), definition);
    }

    public TriggerBundleDefinition get(String id) {
        return bundles.get(id);
    }

    public Collection<TriggerBundleDefinition> all() {
        return Collections.unmodifiableCollection(bundles.values());
    }
}