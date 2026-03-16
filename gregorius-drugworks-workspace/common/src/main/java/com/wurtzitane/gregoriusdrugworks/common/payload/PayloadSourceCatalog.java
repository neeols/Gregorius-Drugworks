package com.wurtzitane.gregoriusdrugworks.common.payload;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class PayloadSourceCatalog {

    private final Map<String, PayloadSourceDefinition> definitions = new LinkedHashMap<>();

    public void clear() {
        definitions.clear();
    }

    public void register(PayloadSourceDefinition definition) {
        if (definitions.containsKey(definition.getSourceItemId())) {
            throw new IllegalStateException("Duplicate payload source item id: " + definition.getSourceItemId());
        }
        definitions.put(definition.getSourceItemId(), definition);
    }

    public PayloadSourceDefinition get(String sourceItemId) {
        return definitions.get(sourceItemId);
    }

    public Collection<PayloadSourceDefinition> all() {
        return Collections.unmodifiableCollection(definitions.values());
    }
}