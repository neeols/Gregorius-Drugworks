package com.wurtzitane.gregoriusdrugworkspersistence.payload;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadModeDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.api.TripRegistrationApi;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.EffectSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.ParticleSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripStage;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripStageTriggerSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.registry.TripRegistry;
import com.wurtzitane.gregoriusdrugworkspersistence.trip.TripRegistrar;

import java.util.List;
import java.util.Locale;

final class PayloadModeTripSupport {

    private PayloadModeTripSupport() {
    }

    public static String resolveTripItemId(String baseItemId, PayloadModeDefinition mode) {
        if (baseItemId == null || baseItemId.isEmpty() || mode == null || !mode.hasKineticsOverrides()) {
            return baseItemId;
        }

        TripRegistry registry = TripRegistrationApi.registry();
        TripDefinition base = registry.getTrip(baseItemId);
        if (base == null) {
            return baseItemId;
        }

        String syntheticItemId = syntheticTripId(baseItemId, mode.getId());
        if (registry.getTrip(syntheticItemId) != null) {
            return syntheticItemId;
        }

        TripRegistrar.registerTrip(scaleTrip(base, syntheticItemId, mode));
        for (String antidoteId : registry.getAntidoteIds()) {
            if (registry.getAllowedTripsForAntidote(antidoteId).contains(baseItemId)) {
                TripRegistrar.allowAntidoteForTrip(antidoteId, syntheticItemId);
            }
        }
        return syntheticItemId;
    }

    private static TripDefinition scaleTrip(TripDefinition base, String syntheticItemId, PayloadModeDefinition mode) {
        TripDefinition.Builder builder = TripDefinition.builder(syntheticItemId)
                .consumeOnUse(base.isConsumeOnUse())
                .consumeAmount(base.getConsumeAmount());

        List<TripStage> stages = base.getStages();
        int maxAtSeconds = stages.isEmpty() ? 0 : stages.get(stages.size() - 1).getAtSeconds();
        int previousScaledAt = Integer.MIN_VALUE;
        int previousBaseAt = Integer.MIN_VALUE;

        for (TripStage stage : stages) {
            int scaledAt = scaleStageTime(stage.getAtSeconds(), maxAtSeconds, mode);
            if (previousScaledAt != Integer.MIN_VALUE
                    && stage.getAtSeconds() > previousBaseAt
                    && scaledAt <= previousScaledAt) {
                scaledAt = previousScaledAt + 1;
            }

            TripStage.Builder stageBuilder = TripStage.builder(scaledAt)
                    .periodTicks(stage.getPeriodTicks())
                    .particlesPeriodTicks(stage.getParticlesPeriodTicks())
                    .particlesMinGapTicks(stage.getParticlesMinGapTicks())
                    .triggerSpec(copyTrigger(stage.getTriggerSpec()));

            if (stage.getMessage() != null && !stage.getMessage().isEmpty()) {
                stageBuilder.message(stage.getMessage(), stage.getMessageColor());
            }

            for (EffectSpec effect : stage.getEffects()) {
                stageBuilder.effect(scaleEffect(effect, mode));
            }

            if (stage.getParticles() != null) {
                stageBuilder.particle(scaleParticle(stage.getParticles(), mode));
            }

            builder.stage(stageBuilder.build());
            previousScaledAt = scaledAt;
            previousBaseAt = stage.getAtSeconds();
        }

        return builder.build();
    }

    private static int scaleStageTime(int atSeconds, int maxAtSeconds, PayloadModeDefinition mode) {
        if (atSeconds <= 0 || maxAtSeconds <= 0) {
            return atSeconds;
        }

        double progress = Math.min(1.0D, Math.max(0.0D, atSeconds / (double) maxAtSeconds));
        double timeScale = mode.getOnsetScale() + ((mode.getDurationScale() - mode.getOnsetScale()) * progress);
        return Math.max(0, (int) Math.round(atSeconds * timeScale));
    }

    private static EffectSpec scaleEffect(EffectSpec effect, PayloadModeDefinition mode) {
        int seconds = Math.max(1, (int) Math.round(effect.getSeconds() * mode.getDurationScale()));
        int amplifier = scaleAmplifier(effect.getAmplifier(), mode.getPeakScale());
        return new EffectSpec(effect.getId(), seconds, amplifier, effect.isHideParticles());
    }

    private static int scaleAmplifier(int amplifier, double peakScale) {
        int scaled = (int) Math.round((amplifier + 1) * peakScale) - 1;
        return Math.max(0, scaled);
    }

    private static ParticleSpec scaleParticle(ParticleSpec particle, PayloadModeDefinition mode) {
        int count = Math.max(1, (int) Math.round(particle.getCount() * mode.getPeakScale()));
        return new ParticleSpec(particle.getId(), count, particle.getSpread(), particle.getSpeed());
    }

    private static TripStageTriggerSpec copyTrigger(TripStageTriggerSpec trigger) {
        if (trigger == null || trigger.isEmpty()) {
            return TripStageTriggerSpec.NONE;
        }
        return TripStageTriggerSpec.builder()
                .onEnter(trigger.getOnEnterTriggerBundleId())
                .onTick(trigger.getOnTickTriggerBundleId(), trigger.getOnTickIntervalTicks())
                .onExit(trigger.getOnExitTriggerBundleId())
                .build();
    }

    private static String syntheticTripId(String baseItemId, String modeId) {
        return baseItemId + "#gdw_mode_" + normalizeModeId(modeId);
    }

    private static String normalizeModeId(String modeId) {
        if (modeId == null || modeId.trim().isEmpty()) {
            return "default";
        }
        return modeId.trim().toLowerCase(Locale.ROOT).replace(' ', '_');
    }
}
