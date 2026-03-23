package com.wurtzitane.gregoriusdrugworkspersistence.inhalation;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class ConfigurableInhalationEffectHandler implements InhalationEffectHandler {

    private final Map<InhalationUsePhase, List<InhalationPhaseAction>> actionsByPhase;

    private ConfigurableInhalationEffectHandler(Builder builder) {
        EnumMap<InhalationUsePhase, List<InhalationPhaseAction>> builtActions = new EnumMap<>(InhalationUsePhase.class);
        for (Map.Entry<InhalationUsePhase, List<InhalationPhaseAction>> entry : builder.actionsByPhase.entrySet()) {
            builtActions.put(entry.getKey(), Collections.unmodifiableList(new ArrayList<>(entry.getValue())));
        }
        this.actionsByPhase = Collections.unmodifiableMap(builtActions);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void onPhase(EntityPlayerMP player, ItemStack stack, InhalationDefinition definition, InhalationUsePhase phase, boolean exhausted) {
        List<InhalationPhaseAction> actions = actionsByPhase.get(phase);
        if (actions == null || actions.isEmpty()) {
            return;
        }

        for (InhalationPhaseAction action : actions) {
            action.execute(player, stack, definition, phase, exhausted);
        }
    }

    public static final class Builder {
        private final EnumMap<InhalationUsePhase, List<InhalationPhaseAction>> actionsByPhase = new EnumMap<>(InhalationUsePhase.class);

        private Builder() {
        }

        public Builder onPhase(InhalationUsePhase phase, InhalationPhaseAction action) {
            if (phase == null || action == null) {
                return this;
            }

            actionsByPhase.computeIfAbsent(phase, ignored -> new ArrayList<>()).add(action);
            return this;
        }

        public ConfigurableInhalationEffectHandler build() {
            return new ConfigurableInhalationEffectHandler(this);
        }
    }
}
