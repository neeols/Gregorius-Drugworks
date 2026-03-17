package com.wurtzitane.gregoriusdrugworkspersistence.inhalation;

import com.wurtzitane.gregoriusdrugworkspersistence.trigger.TriggerBundleRuntimeExecutor;
import com.wurtzitane.gregoriusdrugworkspersistence.trip.TripHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.visual.TripVisualBridge;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

@FunctionalInterface
public interface InhalationPhaseAction {

    void execute(EntityPlayerMP player, ItemStack stack, InhalationDefinition definition, InhalationUsePhase phase, boolean exhausted);

    static InhalationPhaseAction applyPotionEffect(
            String potionId,
            int durationTicks,
            int amplifier,
            boolean ambient,
            boolean showParticles
    ) {
        return (player, stack, definition, phase, exhausted) -> {
            if (durationTicks <= 0) {
                return;
            }

            Potion potion = Potion.REGISTRY.getObject(new ResourceLocation(potionId));
            if (potion == null) {
                return;
            }

            player.addPotionEffect(new PotionEffect(
                    potion,
                    durationTicks,
                    Math.max(0, amplifier),
                    ambient,
                    showParticles
            ));
        };
    }

    static InhalationPhaseAction forwardTripItemUse(String itemId) {
        return (player, stack, definition, phase, exhausted) -> {
            if (itemId != null && !itemId.isEmpty()) {
                TripHooks.onItemUse(player, itemId);
            }
        };
    }

    static InhalationPhaseAction executeTriggerBundle(String triggerBundleId) {
        return (player, stack, definition, phase, exhausted) -> {
            if (triggerBundleId != null && !triggerBundleId.isEmpty()) {
                TriggerBundleRuntimeExecutor.executeById(player, triggerBundleId);
            }
        };
    }

    static InhalationPhaseAction startVisualProfile(String profileId, int durationTicks) {
        return (player, stack, definition, phase, exhausted) -> {
            if (profileId != null && !profileId.isEmpty() && durationTicks > 0) {
                TripVisualBridge.activate(player, profileId, durationTicks);
            }
        };
    }
}
