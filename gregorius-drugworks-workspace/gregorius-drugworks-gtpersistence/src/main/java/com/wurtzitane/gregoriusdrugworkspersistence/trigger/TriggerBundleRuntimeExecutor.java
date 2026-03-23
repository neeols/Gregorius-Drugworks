package com.wurtzitane.gregoriusdrugworkspersistence.trigger;

import com.wurtzitane.gregoriusdrugworks.common.trigger.TriggerActionDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trigger.TriggerActionType;
import com.wurtzitane.gregoriusdrugworks.common.trigger.TriggerBundleDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.visual.TripVisualBridge;
import com.wurtzitane.gregoriusdrugworkspersistence.trip.TripHooks;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public final class TriggerBundleRuntimeExecutor {

    private TriggerBundleRuntimeExecutor() {
    }

    public static boolean executeById(EntityPlayerMP player, String triggerBundleId) {
        TriggerBundleDefinition bundle = GregoriusDrugworksTriggerBundles.get(triggerBundleId);
        if (bundle == null) {
            return false;
        }

        for (TriggerActionDefinition action : bundle.getActions()) {
            executeAction(player, action);
        }

        return true;
    }

    private static void executeAction(EntityPlayerMP player, TriggerActionDefinition action) {
        TriggerActionType type = action.getType();

        switch (type) {
            case FORWARD_TRIP_ITEM_USE:
                if (action.getPrimaryId() != null && !action.getPrimaryId().isEmpty()) {
                    TripHooks.onItemUse(player, action.getPrimaryId());
                }
                return;

            case START_VISUAL_PROFILE:
                if (action.getPrimaryId() != null && !action.getPrimaryId().isEmpty()) {
                    TripVisualBridge.activate(
                            player,
                            action.getPrimaryId(),
                            action.getIntValue() > 0 ? action.getIntValue() : 200
                    );
                }
                return;

            case PLAY_WORLD_SOUND:
                if (action.getPrimaryId() != null && !action.getPrimaryId().isEmpty()) {
                    SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(new ResourceLocation(action.getPrimaryId()));
                    if (soundEvent != null) {
                        player.world.playSound(
                                null,
                                player.posX,
                                player.posY,
                                player.posZ,
                                soundEvent,
                                SoundCategory.PLAYERS,
                                1.0F,
                                1.0F
                        );
                    }
                }
                return;

            default:
                return;
        }
    }
}