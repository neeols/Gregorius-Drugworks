package com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript;

import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.ConfigurableInhalationEffectHandler;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.DurabilityLossMode;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationEffectHandler;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationLingeringSpec;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationParticleSpec;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationPhaseAction;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationRemainderSpec;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public final class GroovyVapesSection extends AbstractGroovySection {

    public GroovyVapesSection() {
        super("vapes", "vape", "inhalations", "inhalation");
    }

    public Builder builder(String itemId) {
        return new Builder(itemId);
    }

    public Item register(InhalationDefinition definition) {
        return GregoriusDrugworksGroovyScriptBridge.registerVape(definition, true);
    }

    public ConfigurableInhalationEffectHandler.Builder effectHandler() {
        return ConfigurableInhalationEffectHandler.builder();
    }

    public InhalationLingeringSpec.Builder lingering() {
        return InhalationLingeringSpec.builder();
    }

    public InhalationParticleSpec particle(
            String particleId,
            int count,
            double spreadX,
            double spreadY,
            double spreadZ,
            double speedX,
            double speedY,
            double speedZ,
            int cadenceTicks
    ) {
        return new InhalationParticleSpec(
                GroovyScriptUtil.particle(particleId),
                count,
                spreadX,
                spreadY,
                spreadZ,
                speedX,
                speedY,
                speedZ,
                cadenceTicks
        );
    }

    public InhalationRemainderSpec remainder(String itemId, float chance, boolean replaceSourceStack) {
        return new InhalationRemainderSpec(
                GroovyScriptUtil.itemStack(itemId),
                chance,
                replaceSourceStack
        );
    }

    public InhalationPhaseAction applyPotionEffect(
            String potionId,
            int durationTicks,
            int amplifier,
            boolean ambient,
            boolean showParticles
    ) {
        return InhalationPhaseAction.applyPotionEffect(potionId, durationTicks, amplifier, ambient, showParticles);
    }

    public InhalationPhaseAction forwardTripItemUse(String itemId) {
        return InhalationPhaseAction.forwardTripItemUse(itemId);
    }

    public InhalationPhaseAction executeTriggerBundle(String triggerBundleId) {
        return InhalationPhaseAction.executeTriggerBundle(triggerBundleId);
    }

    public InhalationPhaseAction startVisualProfile(String profileId, int durationTicks) {
        return InhalationPhaseAction.startVisualProfile(profileId, durationTicks);
    }

    public static final class Builder {
        private final InhalationDefinition.Builder delegate;
        private boolean sample = true;

        private Builder(String itemId) {
            this.delegate = InhalationDefinition.builder(itemId);
        }

        public Builder sample(boolean value) {
            this.sample = value;
            return this;
        }

        public Builder rarity(EnumRarity value) {
            delegate.rarity(value);
            return this;
        }

        public Builder rarity(String value) {
            return rarity(GroovyScriptUtil.enumValue(EnumRarity.class, value));
        }

        public Builder totalUseTicks(int value) {
            delegate.totalUseTicks(value);
            return this;
        }

        public Builder raiseTicks(int value) {
            delegate.raiseTicks(value);
            return this;
        }

        public Builder inhaleStartTick(int value) {
            delegate.inhaleStartTick(value);
            return this;
        }

        public Builder inhaleEndTick(int value) {
            delegate.inhaleEndTick(value);
            return this;
        }

        public Builder holdTicks(int value) {
            delegate.holdTicks(value);
            return this;
        }

        public Builder exhaleStartTick(int value) {
            delegate.exhaleStartTick(value);
            return this;
        }

        public Builder exhaleEndTick(int value) {
            delegate.exhaleEndTick(value);
            return this;
        }

        public Builder finishTicks(int value) {
            delegate.finishTicks(value);
            return this;
        }

        public Builder maxUses(int value) {
            delegate.maxUses(value);
            return this;
        }

        public Builder durabilityLossMode(DurabilityLossMode value) {
            delegate.durabilityLossMode(value);
            return this;
        }

        public Builder durabilityLossMode(String value) {
            return durabilityLossMode(GroovyScriptUtil.enumValue(DurabilityLossMode.class, value));
        }

        public Builder fixedLoss(int value) {
            delegate.fixedLoss(value);
            return this;
        }

        public Builder randomLossRange(int min, int max) {
            delegate.randomLossRange(min, max);
            return this;
        }

        public Builder minimumCompletionRatio(float value) {
            delegate.minimumCompletionRatio(value);
            return this;
        }

        public Builder consumeOnInterrupt(boolean value) {
            delegate.consumeOnInterrupt(value);
            return this;
        }

        public Builder cooldownTicks(int value) {
            delegate.cooldownTicks(value);
            return this;
        }

        public Builder useCustomRenderer(boolean value) {
            delegate.useCustomRenderer(value);
            return this;
        }

        public Builder localCameraNudge(boolean value) {
            delegate.localCameraNudge(value);
            return this;
        }

        public Builder glowRange(float min, float max) {
            delegate.glowRange(min, max);
            return this;
        }

        public Builder startSound(ResourceLocation value) {
            delegate.startSoundId(value);
            return this;
        }

        public Builder startSound(String value) {
            return startSound(GroovyScriptUtil.resourceLocation(value));
        }

        public Builder inhaleSound(ResourceLocation value) {
            delegate.inhaleSoundId(value);
            return this;
        }

        public Builder inhaleSound(String value) {
            return inhaleSound(GroovyScriptUtil.resourceLocation(value));
        }

        public Builder exhaleSound(ResourceLocation value) {
            delegate.exhaleSoundId(value);
            return this;
        }

        public Builder exhaleSound(String value) {
            return exhaleSound(GroovyScriptUtil.resourceLocation(value));
        }

        public Builder finishSound(ResourceLocation value) {
            delegate.finishSoundId(value);
            return this;
        }

        public Builder finishSound(String value) {
            return finishSound(GroovyScriptUtil.resourceLocation(value));
        }

        public Builder exhaustedSound(ResourceLocation value) {
            delegate.exhaustedSoundId(value);
            return this;
        }

        public Builder exhaustedSound(String value) {
            return exhaustedSound(GroovyScriptUtil.resourceLocation(value));
        }

        public Builder modelTexture(ResourceLocation value) {
            delegate.modelTexture(value);
            return this;
        }

        public Builder modelTexture(String value) {
            return modelTexture(GroovyScriptUtil.resourceLocation(value));
        }

        public Builder inhaleParticle(InhalationParticleSpec value) {
            delegate.addInhaleParticle(value);
            return this;
        }

        public Builder exhaleParticle(InhalationParticleSpec value) {
            delegate.addExhaleParticle(value);
            return this;
        }

        public Builder perUseRemainder(InhalationRemainderSpec value) {
            delegate.addPerUseRemainder(value);
            return this;
        }

        public Builder exhaustedRemainder(InhalationRemainderSpec value) {
            delegate.addExhaustedRemainder(value);
            return this;
        }

        public Builder effectHandler(InhalationEffectHandler value) {
            delegate.effectHandler(value);
            return this;
        }

        public Builder lingering(InhalationLingeringSpec value) {
            delegate.lingeringSpec(value);
            return this;
        }

        public InhalationDefinition build() {
            return delegate.build();
        }

        public Item register() {
            return GregoriusDrugworksGroovyScriptBridge.registerVape(build(), sample);
        }
    }
}
