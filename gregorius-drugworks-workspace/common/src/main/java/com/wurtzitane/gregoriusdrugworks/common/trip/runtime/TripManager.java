package com.wurtzitane.gregoriusdrugworks.common.trip.runtime;

import com.wurtzitane.gregoriusdrugworks.common.trip.api.TripRuntimeApi;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.AntidoteDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.EffectSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.ParticleSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripStage;
import com.wurtzitane.gregoriusdrugworks.common.trip.registry.TripRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class TripManager implements TripRuntimeApi {

    public static final String KEY_ACTIVE_TRIP_ID = "trip_active_id";
    public static final String KEY_RUNNING_START = "trip_running_start_tick";
    public static final String KEY_RUNNING_UNTIL = "trip_running_until_tick";
    public static final String KEY_ACTIVE_SUBSTANCE_HASH = "trip_active_substance_hash";
    public static final String KEY_ACTIVE_STAGE_INDEX = "trip_active_stage_index";
    public static final String KEY_STAGE_MSG_PREFIX = "trip_stage_msg_";
    public static final String KEY_STAGE_PART_PREFIX = "trip_stage_part_";

    private static final List<String> DEFAULT_CLEANUP_EFFECTS = Arrays.asList(
            "minecraft:nausea",
            "minecraft:night_vision",
            "minecraft:blindness",
            "minecraft:slowness",
            "minecraft:mining_fatigue"
    );

    private final TripRuntime runtime;
    private final TripRegistry registry;
    private final List<ScheduledJob> jobs = new ArrayList<ScheduledJob>();
    private final Map<String, Long> debugLast = new HashMap<String, Long>();

    private boolean debug = true;
    private long debugTickThrottle = 20L;
    private long nowTick = 0L;

    public TripManager(TripRuntime runtime, TripRegistry registry) {
        this.runtime = runtime;
        this.registry = registry;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setDebugTickThrottle(long debugTickThrottle) {
        this.debugTickThrottle = debugTickThrottle;
    }

    public long getNowTick() {
        return nowTick;
    }

    @Override
    public void tick() {
        nowTick = runtime.getCurrentTick();

        int index = 0;
        while (index < jobs.size()) {
            ScheduledJob job = jobs.get(index);
            if (job.at <= nowTick) {
                jobs.remove(index);
                try {
                    job.task.run();
                } catch (Exception ex) {
                    runtime.log("[TRIP][JOBERR] " + ex);
                }
            } else {
                index++;
            }
        }
    }

    @Override
    public void onPlayerLogin(TripRuntime.TripPlayer player) {
        hardResetPlayerTripState(player);
    }

    @Override
    public void onPlayerLogout(TripRuntime.TripPlayer player) {
        hardResetPlayerTripState(player);
    }

    @Override
    public boolean handleItemUse(TripRuntime.TripPlayer player, String itemId) {
        if (player == null || itemId == null || itemId.isEmpty()) {
            return false;
        }

        dlog("[TRIP][CLICK] player=" + player.getUsername() + " item=" + itemId + " now=" + nowTick);

        AntidoteDefinition antidote = registry.getAntidote(itemId);
        if (antidote != null) {
            if (!isTripRunning(player)) {
                runtime.sendMessage(player, antidote.getNoTripMessage(), antidote.getNoTripMessageColor());
                return true;
            }

            if (!registry.canAntidoteCancel(itemId, getActiveSubstanceHash(player))) {
                runtime.sendMessage(player, antidote.getWrongTripMessage(), antidote.getWrongTripMessageColor());
                return true;
            }

            if (antidote.isConsumeOnUse()) {
                runtime.consumeHeldItem(player, clampInt(antidote.getConsumeAmount(), 1, 64, 1));
            }

            if (antidote.getSound() != null) {
                runtime.playSound(player, antidote.getSound());
            }

            if (antidote.getParticles() != null) {
                runtime.spawnParticles(player, antidote.getParticles());
            }

            cancelTripNow(player);
            runtime.sendMessage(player, antidote.getCancelMessage(), antidote.getCancelMessageColor());
            return true;
        }

        TripDefinition definition = registry.getTrip(itemId);
        if (definition == null) {
            return false;
        }

        if (isTripRunning(player)) {
            runtime.sendMessage(player, "You are already in an active staged state. (" + formatTripLeft(player) + ")", "gray");
            return true;
        }

        if (definition.isConsumeOnUse()) {
            runtime.consumeHeldItem(player, clampInt(definition.getConsumeAmount(), 1, 64, 1));
        }

        runTrip(player, definition);
        return true;
    }

    @Override
    public void cancelTripNow(TripRuntime.TripPlayer player) {
        long activeHash = getActiveSubstanceHash(player);
        long activeTripId = getActiveTripId(player);
        TripDefinition definition = registry.getTripByHash(activeHash);

        fireCurrentStageExitIfNeeded(player, definition, activeTripId);

        clearTripWindow(player);
        clearActiveTripId(player);
        clearActiveStageIndex(player);
        runtime.removePersistentKey(player, KEY_ACTIVE_SUBSTANCE_HASH);

        if (definition != null) {
            clearEffectsList(player, definition.getEffectIds());
        }

        clearEffectsList(player, DEFAULT_CLEANUP_EFFECTS);
    }

    public void hardResetPlayerTripState(TripRuntime.TripPlayer player) {
        if (player == null) {
            return;
        }
        cancelTripNow(player);
    }

    @Override
    public boolean isTripRunning(TripRuntime.TripPlayer player) {
        return getTripUntilTick(player) > nowTick;
    }

    @Override
    public String formatTripLeft(TripRuntime.TripPlayer player) {
        long start = getTripStartTick(player);
        long until = getTripUntilTick(player);

        long totalTicks = until - start;
        long leftTicks = until - nowTick;

        if (totalTicks < 1L) {
            totalTicks = 1L;
        }
        if (leftTicks < 0L) {
            leftTicks = 0L;
        }

        long totalSec = (long) Math.ceil(totalTicks / 20.0D);
        long leftSec = (long) Math.ceil(leftTicks / 20.0D);

        if (totalSec < 1L) {
            totalSec = 1L;
        }
        if (leftSec < 0L) {
            leftSec = 0L;
        }

        return leftSec + "s / " + totalSec + "s";
    }

    public void runTrip(TripRuntime.TripPlayer player, TripDefinition definition) {
        if (player == null) {
            return;
        }

        if (isTripRunning(player)) {
            runtime.sendMessage(player, "You are already in an active staged state. (" + formatTripLeft(player) + ")", "gray");
            return;
        }

        long tripId = nowTick + ThreadLocalRandom.current().nextInt(1_000_000);
        setActiveTripId(player, tripId);
        setActiveSubstance(player, definition.getItemId());
        clearActiveStageIndex(player);

        clearEffectsList(player, definition.getEffectIds());

        List<TripStage> stages = definition.getStages();
        if (stages.isEmpty()) {
            dlog("[TRIP][RUN] no stages for item=" + definition.getItemId());
            clearTripWindow(player);
            clearActiveTripId(player);
            clearActiveStageIndex(player);
            runtime.removePersistentKey(player, KEY_ACTIVE_SUBSTANCE_HASH);
            return;
        }

        String username = player.getUsername();
        UUID uuid = player.getUuid();
        String tripItemId = definition.getItemId();

        long startBase = nowTick;
        List<Long> starts = new ArrayList<Long>(stages.size());
        for (TripStage stage : stages) {
            long startTick = startBase + ((long) stage.getAtSeconds() * 20L);
            starts.add(Long.valueOf(startTick));
        }

        long tripEndTick = starts.get(starts.size() - 1).longValue() + 200L;
        setTripWindow(player, startBase, tripEndTick);

        dlog("[TRIP][RUN] start player=" + username + " uuid=" + uuid + " item=" + definition.getItemId() +
                " tripId=" + tripId + " startTick=" + startBase + " endTick=" + tripEndTick + " now=" + nowTick);

        for (int i = 0; i < stages.size(); i++) {
            dlog("[TRIP][RUN] stage[" + i + "] atSeconds=" + stages.get(i).getAtSeconds() + " startTick=" + starts.get(i));
        }

        for (int i = 0; i < stages.size(); i++) {
            long stageStart = starts.get(i).longValue();
            long stageEnd = (i + 1 < stages.size()) ? starts.get(i + 1).longValue() : tripEndTick;
            runStage(uuid, username, tripId, tripItemId, stages.get(i), i, stageStart, stageEnd);
        }

        scheduleAbs(tripEndTick, () -> {
            TripRuntime.TripPlayer resolved = runtime.resolvePlayer(uuid, username);
            if (resolved == null) {
                dlog("[TRIP][END] resolvePlayer failed user=" + username);
                return;
            }
            if (getActiveTripId(resolved) == tripId) {
                dlog("[TRIP][END] hard reset user=" + username + " tripId=" + tripId);
                hardResetPlayerTripState(resolved);
            }
        });
    }

    private void runStage(
            UUID playerUuid,
            String username,
            long tripId,
            String tripItemId,
            TripStage stage,
            int stageIndex,
            long stageStartTick,
            long stageEndTick
    ) {
        final int effectPeriod = clampInt(stage.getPeriodTicks(), 1, 1_000_000, 10);
        final int particlePeriod = clampInt(stage.getParticlesPeriodTicks(), 1, 1_000_000, 20);
        final int particleGap = stage.getParticlesMinGapTicks() > 0
                ? clampInt(stage.getParticlesMinGapTicks(), 1, 1_000_000, particlePeriod + 4)
                : (particlePeriod + 4);

        dlog("[TRIP][STAGE] schedule user=" + username + " stage=" + stageIndex +
                " start=" + stageStartTick + " end=" + stageEndTick +
                " effPeriod=" + effectPeriod + " partPeriod=" + particlePeriod);

        scheduleAbs(stageStartTick, () -> enterStage(playerUuid, username, tripId, tripItemId, stage, stageIndex));
        scheduleAbs(stageStartTick, () -> stepEffects(playerUuid, username, tripId, tripItemId, stage, stageIndex, stageStartTick, stageEndTick, effectPeriod));

        if (stage.getParticles() != null) {
            scheduleAbs(stageStartTick, () -> stepParticles(playerUuid, username, tripId, stage, stageIndex, stageStartTick, stageEndTick, particlePeriod, particleGap));
        }

        if (stage.getTriggerSpec() != null && !stage.getTriggerSpec().isEmpty() && stage.getTriggerSpec().hasOnTick()) {
            scheduleAbs(stageStartTick, () -> stepStageTriggers(playerUuid, username, tripId, tripItemId, stage, stageIndex, stageEndTick));
        }

        scheduleAbs(stageEndTick, () -> exitStage(playerUuid, username, tripId, tripItemId, stage, stageIndex));
    }

    private void enterStage(
            UUID playerUuid,
            String username,
            long tripId,
            String tripItemId,
            TripStage stage,
            int stageIndex
    ) {
        TripRuntime.TripPlayer player = runtime.resolvePlayer(playerUuid, username);
        if (player == null) {
            dlogThrottled("enter_no_player_" + username, "[TRIP][STAGE][ENTER] resolvePlayer FAILED user=" + username + " uuid=" + playerUuid + " tick=" + nowTick);
            return;
        }

        long currentTripId = getActiveTripId(player);
        if (currentTripId != tripId) {
            dlogThrottled("enter_token_mismatch_" + username, "[TRIP][STAGE][ENTER] token mismatch user=" + username + " have=" + currentTripId + " need=" + tripId + " tick=" + nowTick);
            return;
        }

        setActiveStageIndex(player, stageIndex);
        TripStageTriggerSupport.onStageEnter(runtime, player, tripItemId, stageIndex, stage.getTriggerSpec());

        dlog("[TRIP][STAGE][ENTER] user=" + username + " stage=" + stageIndex + " tick=" + nowTick);
    }

    private void exitStage(
            UUID playerUuid,
            String username,
            long tripId,
            String tripItemId,
            TripStage stage,
            int stageIndex
    ) {
        TripRuntime.TripPlayer player = runtime.resolvePlayer(playerUuid, username);
        if (player == null) {
            dlogThrottled("exit_no_player_" + username, "[TRIP][STAGE][EXIT] resolvePlayer FAILED user=" + username + " uuid=" + playerUuid + " tick=" + nowTick);
            return;
        }

        long currentTripId = getActiveTripId(player);
        if (currentTripId != tripId) {
            dlogThrottled("exit_token_mismatch_" + username, "[TRIP][STAGE][EXIT] token mismatch user=" + username + " have=" + currentTripId + " need=" + tripId + " tick=" + nowTick);
            return;
        }

        long activeStageIndex = getActiveStageIndex(player);
        if (activeStageIndex != (long) stageIndex) {
            return;
        }

        TripStageTriggerSupport.onStageExit(runtime, player, tripItemId, stageIndex, stage.getTriggerSpec());
        clearStageTransientState(player, tripId, tripItemId, stageIndex);
        clearActiveStageIndex(player);

        dlog("[TRIP][STAGE][EXIT] user=" + username + " stage=" + stageIndex + " tick=" + nowTick);
    }

    private void stepStageTriggers(
            UUID playerUuid,
            String username,
            long tripId,
            String tripItemId,
            TripStage stage,
            int stageIndex,
            long stageEndTick
    ) {
        if (nowTick >= stageEndTick) {
            return;
        }

        TripRuntime.TripPlayer player = runtime.resolvePlayer(playerUuid, username);
        if (player == null) {
            dlogThrottled("stage_trigger_no_player_" + username, "[TRIP][STAGE][TRIGGER] resolvePlayer FAILED user=" + username + " uuid=" + playerUuid + " tick=" + nowTick);
            scheduleAbs(nowTick + 1L, () -> stepStageTriggers(playerUuid, username, tripId, tripItemId, stage, stageIndex, stageEndTick));
            return;
        }

        long currentTripId = getActiveTripId(player);
        if (currentTripId != tripId) {
            dlogThrottled("stage_trigger_token_mismatch_" + username, "[TRIP][STAGE][TRIGGER] token mismatch user=" + username + " have=" + currentTripId + " need=" + tripId + " tick=" + nowTick);
            return;
        }

        long activeStageIndex = getActiveStageIndex(player);
        if (activeStageIndex == (long) stageIndex) {
            TripStageTriggerSupport.onStageTick(runtime, player, tripItemId, stageIndex, stage.getTriggerSpec());
        }

        scheduleAbs(nowTick + 1L, () -> stepStageTriggers(playerUuid, username, tripId, tripItemId, stage, stageIndex, stageEndTick));
    }

    private void stepEffects(
            UUID playerUuid,
            String username,
            long tripId,
            String tripItemId,
            TripStage stage,
            int stageIndex,
            long stageStartTick,
            long stageEndTick,
            int effectPeriod
    ) {
        if (nowTick >= stageEndTick) {
            dlog("[TRIP][STAGE] effects done user=" + username + " stage=" + stageIndex + " tick=" + nowTick);
            return;
        }

        TripRuntime.TripPlayer player = runtime.resolvePlayer(playerUuid, username);
        if (player == null) {
            dlogThrottled("no_player_eff_" + username, "[TRIP][STAGE][EFFECTS] resolvePlayer FAILED user=" + username + " uuid=" + playerUuid + " tick=" + nowTick);
            scheduleAbs(nowTick + effectPeriod, () -> stepEffects(playerUuid, username, tripId, tripItemId, stage, stageIndex, stageStartTick, stageEndTick, effectPeriod));
            return;
        }

        long currentTripId = getActiveTripId(player);
        if (currentTripId != tripId) {
            dlogThrottled("token_mismatch_eff_" + username, "[TRIP][STAGE][EFFECTS] token mismatch user=" + username + " have=" + currentTripId + " need=" + tripId + " tick=" + nowTick);
            return;
        }

        if (stage.getMessage() != null && !stage.getMessage().isEmpty()) {
            String msgKey = KEY_STAGE_MSG_PREFIX + tripId + "_" + stageIndex;
            if (!runtime.getPersistentBoolean(player, msgKey)) {
                runtime.setPersistentBoolean(player, msgKey, true);
                dlog("[TRIP][MSG] user=" + username + " stage=" + stageIndex + " msg=" + stage.getMessage());
                runtime.sendMessage(player, stage.getMessage(), stage.getMessageColor() != null ? stage.getMessageColor() : "light_purple");
            }
        }

        for (EffectSpec effect : stage.getEffects()) {
            if (effect == null || effect.getId() == null) {
                continue;
            }

            dlogThrottled(
                    "give_" + username + "_" + stageIndex + "_" + effect.getId(),
                    "[TRIP][EFFECT] user=" + username + " stage=" + stageIndex + " id=" + effect.getId() +
                            " sec=" + effect.getSeconds() +
                            " amp=" + effect.getAmplifier() +
                            " hide=" + effect.isHideParticles()
            );

            runtime.applyEffect(player, effect);
        }

        scheduleAbs(nowTick + effectPeriod, () -> stepEffects(playerUuid, username, tripId, tripItemId, stage, stageIndex, stageStartTick, stageEndTick, effectPeriod));
    }

    private void stepParticles(UUID playerUuid, String username, long tripId, TripStage stage, int stageIndex, long stageStartTick, long stageEndTick, int particlePeriod, int particleGap) {
        if (nowTick >= stageEndTick) {
            dlog("[TRIP][STAGE] particles done user=" + username + " stage=" + stageIndex + " tick=" + nowTick);
            return;
        }

        TripRuntime.TripPlayer player = runtime.resolvePlayer(playerUuid, username);
        if (player == null) {
            dlogThrottled("no_player_part_" + username, "[TRIP][STAGE][PART] resolvePlayer FAILED user=" + username + " uuid=" + playerUuid + " tick=" + nowTick);
            scheduleAbs(nowTick + 1L, () -> stepParticles(playerUuid, username, tripId, stage, stageIndex, stageStartTick, stageEndTick, particlePeriod, particleGap));
            return;
        }

        long currentTripId = getActiveTripId(player);
        if (currentTripId != tripId) {
            dlogThrottled("token_mismatch_part_" + username, "[TRIP][STAGE][PART] token mismatch user=" + username + " have=" + currentTripId + " need=" + tripId + " tick=" + nowTick);
            return;
        }

        if (stage.getParticles() != null && shouldSpawnParticles(player, tripId, stageIndex, stageStartTick, particlePeriod, particleGap)) {
            dlogThrottled("spawn_part_" + username + "_" + stageIndex, "[TRIP][STAGE][PART] spawn user=" + username + " stage=" + stageIndex + " tick=" + nowTick);
            runtime.spawnParticles(player, stage.getParticles());
        }

        scheduleAbs(nowTick + 1L, () -> stepParticles(playerUuid, username, tripId, stage, stageIndex, stageStartTick, stageEndTick, particlePeriod, particleGap));
    }

    private boolean shouldSpawnParticles(TripRuntime.TripPlayer player, long tripId, int stageIndex, long stageStartTick, int particlesPeriodTicks, int particlesMinGapTicks) {
        int period = clampInt(particlesPeriodTicks, 1, 1_000_000, 20);
        int gap = clampInt(particlesMinGapTicks, 1, 1_000_000, period + 4);

        long elapsed = nowTick - stageStartTick;
        if (elapsed < 0L) {
            return false;
        }

        if ((elapsed % period) != 0L) {
            return false;
        }

        String lastKey = KEY_STAGE_PART_PREFIX + tripId + "_" + stageIndex;
        long last = runtime.getPersistentLong(player, lastKey);

        if (last > 0L && (nowTick - last) < gap) {
            return false;
        }

        runtime.setPersistentLong(player, lastKey, nowTick);
        return true;
    }

    private void clearEffectsList(TripRuntime.TripPlayer player, Iterable<String> effectIds) {
        if (player == null || effectIds == null) {
            return;
        }

        for (String effectId : effectIds) {
            if (effectId == null || effectId.isEmpty()) {
                continue;
            }
            try {
                runtime.clearEffect(player, effectId);
                dlogThrottled("clear_" + player.getUsername() + "_" + effectId, "[TRIP][EFFECT][CLEAR] " + player.getUsername() + " id=" + effectId);
            } catch (Exception ex) {
                runtime.log("[TRIP][EFFECT][CLEARERR] " + effectId + " -> " + ex);
            }
        }
    }

    private void fireCurrentStageExitIfNeeded(TripRuntime.TripPlayer player, TripDefinition definition, long activeTripId) {
        if (player == null || definition == null || activeTripId <= 0L) {
            return;
        }

        long activeStageIndexLong = getActiveStageIndex(player);
        if (activeStageIndexLong < 0L || activeStageIndexLong > Integer.MAX_VALUE) {
            return;
        }

        int activeStageIndex = (int) activeStageIndexLong;
        List<TripStage> stages = definition.getStages();
        if (activeStageIndex < 0 || activeStageIndex >= stages.size()) {
            return;
        }

        TripStage stage = stages.get(activeStageIndex);
        TripStageTriggerSupport.onStageExit(runtime, player, definition.getItemId(), activeStageIndex, stage.getTriggerSpec());
        clearStageTransientState(player, activeTripId, definition.getItemId(), activeStageIndex);
    }

    private void clearStageTransientState(TripRuntime.TripPlayer player, long tripId, String tripItemId, int stageIndex) {
        runtime.removePersistentKey(player, KEY_STAGE_MSG_PREFIX + tripId + "_" + stageIndex);
        runtime.removePersistentKey(player, KEY_STAGE_PART_PREFIX + tripId + "_" + stageIndex);
        TripStageTriggerSupport.clearStageState(runtime, player, tripItemId, stageIndex);
    }

    private void scheduleAbs(long tick, Runnable task) {
        jobs.add(new ScheduledJob(tick, task));
    }

    private long getActiveTripId(TripRuntime.TripPlayer player) {
        return runtime.getPersistentLong(player, KEY_ACTIVE_TRIP_ID);
    }

    private void setActiveTripId(TripRuntime.TripPlayer player, long tripId) {
        runtime.setPersistentLong(player, KEY_ACTIVE_TRIP_ID, tripId);
    }

    private void clearActiveTripId(TripRuntime.TripPlayer player) {
        runtime.removePersistentKey(player, KEY_ACTIVE_TRIP_ID);
        runtime.setPersistentLong(player, KEY_ACTIVE_TRIP_ID, 0L);
    }

    private long getTripStartTick(TripRuntime.TripPlayer player) {
        return runtime.getPersistentLong(player, KEY_RUNNING_START);
    }

    private long getTripUntilTick(TripRuntime.TripPlayer player) {
        return runtime.getPersistentLong(player, KEY_RUNNING_UNTIL);
    }

    private void setTripWindow(TripRuntime.TripPlayer player, long startTick, long untilTick) {
        runtime.setPersistentLong(player, KEY_RUNNING_START, startTick);
        runtime.setPersistentLong(player, KEY_RUNNING_UNTIL, untilTick);
    }

    private void clearTripWindow(TripRuntime.TripPlayer player) {
        runtime.removePersistentKey(player, KEY_RUNNING_START);
        runtime.removePersistentKey(player, KEY_RUNNING_UNTIL);
        runtime.setPersistentLong(player, KEY_RUNNING_START, 0L);
        runtime.setPersistentLong(player, KEY_RUNNING_UNTIL, 0L);
    }

    private void setActiveSubstance(TripRuntime.TripPlayer player, String itemId) {
        runtime.setPersistentLong(player, KEY_ACTIVE_SUBSTANCE_HASH, TripRegistry.hashId(itemId));
    }

    private long getActiveSubstanceHash(TripRuntime.TripPlayer player) {
        return runtime.getPersistentLong(player, KEY_ACTIVE_SUBSTANCE_HASH);
    }

    private long getActiveStageIndex(TripRuntime.TripPlayer player) {
        return runtime.getPersistentLong(player, KEY_ACTIVE_STAGE_INDEX);
    }

    private void setActiveStageIndex(TripRuntime.TripPlayer player, int stageIndex) {
        runtime.setPersistentLong(player, KEY_ACTIVE_STAGE_INDEX, (long) stageIndex);
    }

    private void clearActiveStageIndex(TripRuntime.TripPlayer player) {
        runtime.removePersistentKey(player, KEY_ACTIVE_STAGE_INDEX);
        runtime.setPersistentLong(player, KEY_ACTIVE_STAGE_INDEX, -1L);
    }

    private int clampInt(int value, int min, int max, int fallback) {
        int out = value;
        if (out < min || out > max) {
            out = fallback;
        }
        if (out < min) {
            out = min;
        }
        if (out > max) {
            out = max;
        }
        return out;
    }

    private void dlog(String message) {
        if (debug) {
            runtime.log(message);
        }
    }

    private void dlogThrottled(String key, String message) {
        if (!debug) {
            return;
        }

        Long last = debugLast.get(key);
        if (last == null || (nowTick - last.longValue()) >= debugTickThrottle) {
            debugLast.put(key, Long.valueOf(nowTick));
            runtime.log(message);
        }
    }

    private static final class ScheduledJob {
        private final long at;
        private final Runnable task;

        private ScheduledJob(long at, Runnable task) {
            this.at = at;
            this.task = task;
        }
    }
}