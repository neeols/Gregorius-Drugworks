package com.wurtzitane.gregoriusdrugworks.common.visual;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class VisualProfileCatalog {

    private final Map<String, VisualEffectProfile> profiles = new LinkedHashMap<>();

    public void clear() {
        profiles.clear();
    }

    public void register(VisualEffectProfile profile) {
        if (profiles.containsKey(profile.getId())) {
            throw new IllegalStateException("Duplicate visual profile id: " + profile.getId());
        }
        profiles.put(profile.getId(), profile);
    }

    public VisualEffectProfile get(String id) {
        return profiles.get(id);
    }

    public Collection<VisualEffectProfile> all() {
        return Collections.unmodifiableCollection(profiles.values());
    }
}