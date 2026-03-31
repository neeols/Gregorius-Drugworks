package com.wurtzitane.gregoriusdrugworks.common.trip.runtime;

import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripStageTriggerSpec;

public final class TripStageTriggerSupport {

    private static final String KEY_ROOT = "gdw_stage_triggers";

    private TripStageTriggerSupport() {
    }

    public static void onStageEnter(
            TripRuntime runtime,
            TripRuntime.TripPlayer player,
            String tripId,
            int stageIndex,
            TripStageTriggerSpec spec
    ) {
        if (spec == null || spec.isEmpty() || !spec.hasOnEnter()) {
            return;
        }

        runtime.executeTriggerBundle(player, spec.getOnEnterTriggerBundleId());
        runtime.removePersistentKey(player, lastTickKey(tripId, stageIndex));
    }

    public static void onStageTick(
            TripRuntime runtime,
            TripRuntime.TripPlayer player,
            String tripId,
            int stageIndex,
            TripStageTriggerSpec spec
    ) {
        if (spec == null || spec.isEmpty() || !spec.hasOnTick()) {
            return;
        }

        long now = runtime.getCurrentTick();
        String key = lastTickKey(tripId, stageIndex);
        long last = runtime.getPersistentLong(player, key);

        if (last == 0L || now - last >= spec.getOnTickIntervalTicks()) {
            runtime.executeTriggerBundle(player, spec.getOnTickTriggerBundleId());
            runtime.setPersistentLong(player, key, now);
        }
    }

    public static void onStageExit(
            TripRuntime runtime,
            TripRuntime.TripPlayer player,
            String tripId,
            int stageIndex,
            TripStageTriggerSpec spec
    ) {
        if (spec == null || spec.isEmpty()) {
            return;
        }

        if (spec.hasOnExit()) {
            runtime.executeTriggerBundle(player, spec.getOnExitTriggerBundleId());
        }

        runtime.removePersistentKey(player, lastTickKey(tripId, stageIndex));
    }

    public static void clearStageState(
            TripRuntime runtime,
            TripRuntime.TripPlayer player,
            String tripId,
            int stageIndex
    ) {
        runtime.removePersistentKey(player, lastTickKey(tripId, stageIndex));
    }

    private static String lastTickKey(String tripId, int stageIndex) {
        return KEY_ROOT + "." + tripId + "." + stageIndex + ".last_tick";
    }
}