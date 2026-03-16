package com.wurtzitane.gregoriusdrugworkspersistence.inhalation;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InhalationDefinition {

    private final String itemId;
    private final EnumRarity rarity;
    private final int totalUseTicks;
    private final int raiseTicks;
    private final int inhaleStartTick;
    private final int inhaleEndTick;
    private final int holdTicks;
    private final int exhaleStartTick;
    private final int exhaleEndTick;
    private final int finishTicks;
    private final int maxUses;
    private final DurabilityLossMode durabilityLossMode;
    private final int fixedLoss;
    private final int randomMinLoss;
    private final int randomMaxLoss;
    private final float minimumCompletionRatio;
    private final boolean consumeOnInterrupt;
    private final int cooldownTicks;
    private final boolean useCustomRenderer;
    private final boolean localCameraNudge;
    private final float glowMin;
    private final float glowMax;
    private final ResourceLocation startSoundId;
    private final ResourceLocation inhaleSoundId;
    private final ResourceLocation exhaleSoundId;
    private final ResourceLocation finishSoundId;
    private final ResourceLocation exhaustedSoundId;
    private final ResourceLocation modelTexture;
    private final List<InhalationParticleSpec> inhaleParticles;
    private final List<InhalationParticleSpec> exhaleParticles;
    private final List<InhalationRemainderSpec> perUseRemainders;
    private final List<InhalationRemainderSpec> exhaustedRemainders;
    private final InhalationEffectHandler effectHandler;
    private final InhalationLingeringSpec lingeringSpec;

    private InhalationDefinition(Builder builder) {
        this.itemId = builder.itemId;
        this.rarity = builder.rarity;
        this.totalUseTicks = builder.totalUseTicks;
        this.raiseTicks = builder.raiseTicks;
        this.inhaleStartTick = builder.inhaleStartTick;
        this.inhaleEndTick = builder.inhaleEndTick;
        this.holdTicks = builder.holdTicks;
        this.exhaleStartTick = builder.exhaleStartTick;
        this.exhaleEndTick = builder.exhaleEndTick;
        this.finishTicks = builder.finishTicks;
        this.maxUses = builder.maxUses;
        this.durabilityLossMode = builder.durabilityLossMode;
        this.fixedLoss = builder.fixedLoss;
        this.randomMinLoss = builder.randomMinLoss;
        this.randomMaxLoss = builder.randomMaxLoss;
        this.minimumCompletionRatio = builder.minimumCompletionRatio;
        this.consumeOnInterrupt = builder.consumeOnInterrupt;
        this.cooldownTicks = builder.cooldownTicks;
        this.useCustomRenderer = builder.useCustomRenderer;
        this.localCameraNudge = builder.localCameraNudge;
        this.glowMin = builder.glowMin;
        this.glowMax = builder.glowMax;
        this.startSoundId = builder.startSoundId;
        this.inhaleSoundId = builder.inhaleSoundId;
        this.exhaleSoundId = builder.exhaleSoundId;
        this.finishSoundId = builder.finishSoundId;
        this.exhaustedSoundId = builder.exhaustedSoundId;
        this.modelTexture = builder.modelTexture;
        this.inhaleParticles = Collections.unmodifiableList(new ArrayList<>(builder.inhaleParticles));
        this.exhaleParticles = Collections.unmodifiableList(new ArrayList<>(builder.exhaleParticles));
        this.perUseRemainders = Collections.unmodifiableList(new ArrayList<>(builder.perUseRemainders));
        this.exhaustedRemainders = Collections.unmodifiableList(new ArrayList<>(builder.exhaustedRemainders));
        this.effectHandler = builder.effectHandler;
        this.lingeringSpec = builder.lingeringSpec;
    }

    public static Builder builder(String itemId) {
        return new Builder(itemId);
    }

    public String getItemId() {
        return itemId;
    }

    public EnumRarity getRarity() {
        return rarity;
    }

    public int getTotalUseTicks() {
        return totalUseTicks;
    }

    public int getRaiseTicks() {
        return raiseTicks;
    }

    public int getInhaleStartTick() {
        return inhaleStartTick;
    }

    public int getInhaleEndTick() {
        return inhaleEndTick;
    }

    public int getHoldTicks() {
        return holdTicks;
    }

    public int getExhaleStartTick() {
        return exhaleStartTick;
    }

    public int getExhaleEndTick() {
        return exhaleEndTick;
    }

    public int getFinishTicks() {
        return finishTicks;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public DurabilityLossMode getDurabilityLossMode() {
        return durabilityLossMode;
    }

    public int getFixedLoss() {
        return fixedLoss;
    }

    public int getRandomMinLoss() {
        return randomMinLoss;
    }

    public int getRandomMaxLoss() {
        return randomMaxLoss;
    }

    public float getMinimumCompletionRatio() {
        return minimumCompletionRatio;
    }

    public boolean isConsumeOnInterrupt() {
        return consumeOnInterrupt;
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }

    public boolean isUseCustomRenderer() {
        return useCustomRenderer;
    }

    public boolean isLocalCameraNudge() {
        return localCameraNudge;
    }

    public float getGlowMin() {
        return glowMin;
    }

    public float getGlowMax() {
        return glowMax;
    }

    public ResourceLocation getStartSoundId() {
        return startSoundId;
    }

    public ResourceLocation getInhaleSoundId() {
        return inhaleSoundId;
    }

    public ResourceLocation getExhaleSoundId() {
        return exhaleSoundId;
    }

    public ResourceLocation getFinishSoundId() {
        return finishSoundId;
    }

    public ResourceLocation getExhaustedSoundId() {
        return exhaustedSoundId;
    }

    public ResourceLocation getModelTexture() {
        return modelTexture;
    }

    public List<InhalationParticleSpec> getInhaleParticles() {
        return inhaleParticles;
    }

    public List<InhalationParticleSpec> getExhaleParticles() {
        return exhaleParticles;
    }

    public List<InhalationRemainderSpec> getPerUseRemainders() {
        return perUseRemainders;
    }

    public List<InhalationRemainderSpec> getExhaustedRemainders() {
        return exhaustedRemainders;
    }

    public InhalationEffectHandler getEffectHandler() {
        return effectHandler;
    }

    public InhalationLingeringSpec getLingeringSpec() {
        return lingeringSpec;
    }

    public static final class Builder {
        private final String itemId;
        private EnumRarity rarity = EnumRarity.UNCOMMON;
        private int totalUseTicks = 24;
        private int raiseTicks = 4;
        private int inhaleStartTick = 4;
        private int inhaleEndTick = 12;
        private int holdTicks = 2;
        private int exhaleStartTick = 14;
        private int exhaleEndTick = 22;
        private int finishTicks = 24;
        private int maxUses = 8;
        private DurabilityLossMode durabilityLossMode = DurabilityLossMode.FIXED;
        private int fixedLoss = 1;
        private int randomMinLoss = 1;
        private int randomMaxLoss = 2;
        private float minimumCompletionRatio = 0.55F;
        private boolean consumeOnInterrupt = false;
        private int cooldownTicks = 4;
        private boolean useCustomRenderer = true;
        private boolean localCameraNudge = true;
        private float glowMin = 0.15F;
        private float glowMax = 1.0F;
        private ResourceLocation startSoundId;
        private ResourceLocation inhaleSoundId;
        private ResourceLocation exhaleSoundId;
        private ResourceLocation finishSoundId;
        private ResourceLocation exhaustedSoundId;
        private ResourceLocation modelTexture;
        private final List<InhalationParticleSpec> inhaleParticles = new ArrayList<>();
        private final List<InhalationParticleSpec> exhaleParticles = new ArrayList<>();
        private final List<InhalationRemainderSpec> perUseRemainders = new ArrayList<>();
        private final List<InhalationRemainderSpec> exhaustedRemainders = new ArrayList<>();
        private InhalationEffectHandler effectHandler = InhalationEffectHandler.NO_OP;
        private InhalationLingeringSpec lingeringSpec;

        private Builder(String itemId) {
            this.itemId = itemId;
        }

        public Builder rarity(EnumRarity value) { this.rarity = value; return this; }
        public Builder totalUseTicks(int value) { this.totalUseTicks = value; return this; }
        public Builder raiseTicks(int value) { this.raiseTicks = value; return this; }
        public Builder inhaleStartTick(int value) { this.inhaleStartTick = value; return this; }
        public Builder inhaleEndTick(int value) { this.inhaleEndTick = value; return this; }
        public Builder holdTicks(int value) { this.holdTicks = value; return this; }
        public Builder exhaleStartTick(int value) { this.exhaleStartTick = value; return this; }
        public Builder exhaleEndTick(int value) { this.exhaleEndTick = value; return this; }
        public Builder finishTicks(int value) { this.finishTicks = value; return this; }
        public Builder maxUses(int value) { this.maxUses = value; return this; }
        public Builder durabilityLossMode(DurabilityLossMode value) { this.durabilityLossMode = value; return this; }
        public Builder fixedLoss(int value) { this.fixedLoss = value; return this; }
        public Builder randomLossRange(int min, int max) { this.randomMinLoss = min; this.randomMaxLoss = max; return this; }
        public Builder minimumCompletionRatio(float value) { this.minimumCompletionRatio = value; return this; }
        public Builder consumeOnInterrupt(boolean value) { this.consumeOnInterrupt = value; return this; }
        public Builder cooldownTicks(int value) { this.cooldownTicks = value; return this; }
        public Builder useCustomRenderer(boolean value) { this.useCustomRenderer = value; return this; }
        public Builder localCameraNudge(boolean value) { this.localCameraNudge = value; return this; }
        public Builder glowRange(float min, float max) { this.glowMin = min; this.glowMax = max; return this; }
        public Builder startSoundId(ResourceLocation value) { this.startSoundId = value; return this; }
        public Builder inhaleSoundId(ResourceLocation value) { this.inhaleSoundId = value; return this; }
        public Builder exhaleSoundId(ResourceLocation value) { this.exhaleSoundId = value; return this; }
        public Builder finishSoundId(ResourceLocation value) { this.finishSoundId = value; return this; }
        public Builder exhaustedSoundId(ResourceLocation value) { this.exhaustedSoundId = value; return this; }
        public Builder modelTexture(ResourceLocation value) { this.modelTexture = value; return this; }
        public Builder addInhaleParticle(InhalationParticleSpec value) { this.inhaleParticles.add(value); return this; }
        public Builder addExhaleParticle(InhalationParticleSpec value) { this.exhaleParticles.add(value); return this; }
        public Builder addPerUseRemainder(InhalationRemainderSpec value) { this.perUseRemainders.add(value); return this; }
        public Builder addExhaustedRemainder(InhalationRemainderSpec value) { this.exhaustedRemainders.add(value); return this; }
        public Builder effectHandler(InhalationEffectHandler value) { this.effectHandler = value; return this; }
        public Builder lingeringSpec(InhalationLingeringSpec value) { this.lingeringSpec = value; return this; }

        public InhalationDefinition build() {
            if (finishTicks < exhaleEndTick) {
                throw new IllegalStateException("finishTicks must be >= exhaleEndTick for " + itemId);
            }
            if (modelTexture == null) {
                throw new IllegalStateException("modelTexture must be set for " + itemId);
            }
            return new InhalationDefinition(this);
        }
    }
}