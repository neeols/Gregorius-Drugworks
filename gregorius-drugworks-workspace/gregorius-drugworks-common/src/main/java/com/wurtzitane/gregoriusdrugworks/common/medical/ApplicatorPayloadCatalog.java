package com.wurtzitane.gregoriusdrugworks.common.medical;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ApplicatorPayloadCatalog {

    private final Map<String, ApplicatorPayloadDefinition> definitions = new LinkedHashMap<>();

    public void clear() {
        definitions.clear();
    }

    public void register(ApplicatorPayloadDefinition definition) {
        if (definitions.containsKey(definition.getId())) {
            throw new IllegalStateException("Duplicate applicator payload id: " + definition.getId());
        }
        definitions.put(definition.getId(), definition);
    }

    public ApplicatorPayloadDefinition get(String id) {
        return definitions.get(id);
    }

    public Collection<ApplicatorPayloadDefinition> all() {
        return Collections.unmodifiableCollection(definitions.values());
    }
}