package com.wurtzitane.gregoriusdrugworks.common.trigger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TriggerBundleDefinition {

    private final String id;
    private final List<TriggerActionDefinition> actions;

    public TriggerBundleDefinition(String id, List<TriggerActionDefinition> actions) {
        this.id = id;
        this.actions = Collections.unmodifiableList(new ArrayList<>(actions));
    }

    public String getId() {
        return id;
    }

    public List<TriggerActionDefinition> getActions() {
        return actions;
    }
}