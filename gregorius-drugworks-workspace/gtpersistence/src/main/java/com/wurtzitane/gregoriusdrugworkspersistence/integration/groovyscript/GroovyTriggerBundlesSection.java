package com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript;

import com.wurtzitane.gregoriusdrugworks.common.trigger.TriggerActionDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trigger.TriggerActionType;
import com.wurtzitane.gregoriusdrugworks.common.trigger.TriggerBundleDefinition;

import java.util.ArrayList;
import java.util.List;

public final class GroovyTriggerBundlesSection extends AbstractGroovySection {

    public GroovyTriggerBundlesSection() {
        super("triggerBundles", "triggerBundle", "triggers");
    }

    public Builder builder(String id) {
        return new Builder(id);
    }

    public TriggerActionDefinition action(TriggerActionType type, String primaryId, int intValue) {
        return new TriggerActionDefinition(type, primaryId, intValue);
    }

    public TriggerBundleDefinition register(TriggerBundleDefinition definition) {
        GregoriusDrugworksGroovyScriptBridge.registerTriggerBundle(definition);
        return definition;
    }

    public static final class Builder {
        private final String id;
        private final List<TriggerActionDefinition> actions = new ArrayList<>();

        private Builder(String id) {
            this.id = id;
        }

        public Builder action(TriggerActionDefinition definition) {
            actions.add(definition);
            return this;
        }

        public Builder forwardTripItemUse(String itemId) {
            return action(new TriggerActionDefinition(TriggerActionType.FORWARD_TRIP_ITEM_USE, itemId, 0));
        }

        public Builder startVisualProfile(String profileId, int durationTicks) {
            return action(new TriggerActionDefinition(TriggerActionType.START_VISUAL_PROFILE, profileId, durationTicks));
        }

        public Builder playWorldSound(String soundId) {
            return action(new TriggerActionDefinition(TriggerActionType.PLAY_WORLD_SOUND, soundId, 0));
        }

        public TriggerBundleDefinition build() {
            return new TriggerBundleDefinition(id, actions);
        }

        public TriggerBundleDefinition register() {
            TriggerBundleDefinition definition = build();
            GregoriusDrugworksGroovyScriptBridge.registerTriggerBundle(definition);
            return definition;
        }
    }
}
