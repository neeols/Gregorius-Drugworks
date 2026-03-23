package com.wurtzitane.gregoriusdrugworks.common.trip.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class TripDefinition {

    private final String itemId;
    private final boolean consumeOnUse;
    private final int consumeAmount;
    private final List<TripStage> stages;
    private final Set<String> effectIds;

    private TripDefinition(Builder builder) {
        this.itemId = builder.itemId;
        this.consumeOnUse = builder.consumeOnUse;
        this.consumeAmount = builder.consumeAmount;
        this.stages = Collections.unmodifiableList(new ArrayList<TripStage>(builder.stages));
        this.effectIds = Collections.unmodifiableSet(collectEffectIds(this.stages));
    }

    public String getItemId() {
        return itemId;
    }

    public boolean isConsumeOnUse() {
        return consumeOnUse;
    }

    public int getConsumeAmount() {
        return consumeAmount;
    }

    public List<TripStage> getStages() {
        return stages;
    }

    public Set<String> getEffectIds() {
        return effectIds;
    }

    private static Set<String> collectEffectIds(List<TripStage> stages) {
        LinkedHashSet<String> ids = new LinkedHashSet<String>();
        for (TripStage stage : stages) {
            if (stage == null) {
                continue;
            }
            for (EffectSpec effect : stage.getEffects()) {
                if (effect != null && effect.getId() != null) {
                    ids.add(effect.getId());
                }
            }
        }
        return ids;
    }

    public static Builder builder(String itemId) {
        return new Builder(itemId);
    }

    public static final class Builder {
        private final String itemId;
        private boolean consumeOnUse = true;
        private int consumeAmount = 1;
        private final List<TripStage> stages = new ArrayList<TripStage>();

        private Builder(String itemId) {
            this.itemId = itemId;
        }

        public Builder consumeOnUse(boolean consumeOnUse) {
            this.consumeOnUse = consumeOnUse;
            return this;
        }

        public Builder consumeAmount(int consumeAmount) {
            this.consumeAmount = consumeAmount;
            return this;
        }

        public Builder stage(TripStage stage) {
            this.stages.add(stage);
            return this;
        }

        public TripDefinition build() {
            return new TripDefinition(this);
        }
    }
}