package com.wurtzitane.gregoriusdrugworks.common.payload;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class PayloadCatalog {

    private final Map<String, PayloadDefinition> definitions = new LinkedHashMap<>();

    public void clear() {
        definitions.clear();
    }

    public void register(PayloadDefinition definition) {
        if (definitions.containsKey(definition.getId())) {
            throw new IllegalStateException("Duplicate payload id: " + definition.getId());
        }
        definitions.put(definition.getId(), definition);
    }

    public PayloadDefinition get(String id) {
        return definitions.get(id);
    }

    public Collection<PayloadDefinition> all() {
        return Collections.unmodifiableCollection(definitions.values());
    }
}