package com.wurtzitane.gregoriusdrugworkspersistence.pill;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;

public final class PillItemDefinition {

    private final String itemId;
    private final int maxStackSize;
    private final int useDurationTicks;
    private final float arcHeight;
    private final float launchForward;
    private final float mouthOffsetY;
    private final float spinXPerTick;
    private final float spinYPerTick;
    private final float spinZPerTick;
    private final boolean lockCamera;
    private final boolean tripHookEnabled;
    private final EnumRarity rarity;
    private final ResourceLocation finishSoundId;
    private final ResourceLocation modelTexture;

    private PillItemDefinition(Builder builder) {
        this.itemId = builder.itemId;
        this.maxStackSize = builder.maxStackSize;
        this.useDurationTicks = builder.useDurationTicks;
        this.arcHeight = builder.arcHeight;
        this.launchForward = builder.launchForward;
        this.mouthOffsetY = builder.mouthOffsetY;
        this.spinXPerTick = builder.spinXPerTick;
        this.spinYPerTick = builder.spinYPerTick;
        this.spinZPerTick = builder.spinZPerTick;
        this.lockCamera = builder.lockCamera;
        this.tripHookEnabled = builder.tripHookEnabled;
        this.rarity = builder.rarity;
        this.finishSoundId = builder.finishSoundId;
        this.modelTexture = builder.modelTexture;
    }

    public String getItemId() {
        return itemId;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public int getUseDurationTicks() {
        return useDurationTicks;
    }

    public float getArcHeight() {
        return arcHeight;
    }

    public float getLaunchForward() {
        return launchForward;
    }

    public float getMouthOffsetY() {
        return mouthOffsetY;
    }

    public float getSpinXPerTick() {
        return spinXPerTick;
    }

    public float getSpinYPerTick() {
        return spinYPerTick;
    }

    public float getSpinZPerTick() {
        return spinZPerTick;
    }

    public boolean isLockCamera() {
        return lockCamera;
    }

    public boolean isTripHookEnabled() {
        return tripHookEnabled;
    }

    public EnumRarity getRarity() {
        return rarity;
    }

    public ResourceLocation getFinishSoundId() {
        return finishSoundId;
    }

    public ResourceLocation getModelTexture() {
        return modelTexture;
    }

    public static Builder builder(String itemId) {
        return new Builder(itemId);
    }

    public static final class Builder {
        private final String itemId;
        private int maxStackSize = 16;
        private int useDurationTicks = 18;
        private float arcHeight = 1.15F;
        private float launchForward = 0.35F;
        private float mouthOffsetY = -0.10F;
        private float spinXPerTick = 26.0F;
        private float spinYPerTick = 38.0F;
        private float spinZPerTick = 19.0F;
        private boolean lockCamera = true;
        private boolean tripHookEnabled = true;
        private EnumRarity rarity = EnumRarity.UNCOMMON;
        private ResourceLocation finishSoundId;
        private ResourceLocation modelTexture;

        private Builder(String itemId) {
            this.itemId = itemId;
        }

        public Builder maxStackSize(int value) {
            this.maxStackSize = value;
            return this;
        }

        public Builder useDurationTicks(int value) {
            this.useDurationTicks = value;
            return this;
        }

        public Builder arcHeight(float value) {
            this.arcHeight = value;
            return this;
        }

        public Builder launchForward(float value) {
            this.launchForward = value;
            return this;
        }

        public Builder mouthOffsetY(float value) {
            this.mouthOffsetY = value;
            return this;
        }

        public Builder spinXPerTick(float value) {
            this.spinXPerTick = value;
            return this;
        }

        public Builder spinYPerTick(float value) {
            this.spinYPerTick = value;
            return this;
        }

        public Builder spinZPerTick(float value) {
            this.spinZPerTick = value;
            return this;
        }

        public Builder lockCamera(boolean value) {
            this.lockCamera = value;
            return this;
        }

        public Builder tripHookEnabled(boolean value) {
            this.tripHookEnabled = value;
            return this;
        }

        public Builder rarity(EnumRarity value) {
            this.rarity = value;
            return this;
        }

        public Builder finishSoundId(ResourceLocation value) {
            this.finishSoundId = value;
            return this;
        }

        public Builder modelTexture(ResourceLocation value) {
            this.modelTexture = value;
            return this;
        }

        public PillItemDefinition build() {
            if (finishSoundId == null) {
                throw new IllegalStateException("finishSoundId must be set for pill " + itemId);
            }
            if (modelTexture == null) {
                throw new IllegalStateException("modelTexture must be set for pill " + itemId);
            }
            return new PillItemDefinition(this);
        }
    }
}