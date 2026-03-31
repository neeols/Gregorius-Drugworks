package com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript;

import com.wurtzitane.gregoriusdrugworkspersistence.pill.PillItemDefinition;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public final class GroovyPillsSection extends AbstractGroovySection {

    public GroovyPillsSection() {
        super("pills", "pill");
    }

    public Builder builder(String itemId) {
        return new Builder(itemId);
    }

    public Item register(PillItemDefinition definition) {
        return GregoriusDrugworksGroovyScriptBridge.registerPill(definition);
    }

    public static final class Builder {
        private final PillItemDefinition.Builder delegate;

        private Builder(String itemId) {
            this.delegate = PillItemDefinition.builder(itemId);
        }

        public Builder maxStackSize(int value) {
            delegate.maxStackSize(value);
            return this;
        }

        public Builder useDurationTicks(int value) {
            delegate.useDurationTicks(value);
            return this;
        }

        public Builder arcHeight(float value) {
            delegate.arcHeight(value);
            return this;
        }

        public Builder launchForward(float value) {
            delegate.launchForward(value);
            return this;
        }

        public Builder mouthOffsetY(float value) {
            delegate.mouthOffsetY(value);
            return this;
        }

        public Builder spinXPerTick(float value) {
            delegate.spinXPerTick(value);
            return this;
        }

        public Builder spinYPerTick(float value) {
            delegate.spinYPerTick(value);
            return this;
        }

        public Builder spinZPerTick(float value) {
            delegate.spinZPerTick(value);
            return this;
        }

        public Builder lockCamera(boolean value) {
            delegate.lockCamera(value);
            return this;
        }

        public Builder tripHookEnabled(boolean value) {
            delegate.tripHookEnabled(value);
            return this;
        }

        public Builder rarity(EnumRarity value) {
            delegate.rarity(value);
            return this;
        }

        public Builder rarity(String value) {
            return rarity(GroovyScriptUtil.enumValue(EnumRarity.class, value));
        }

        public Builder finishSound(ResourceLocation value) {
            delegate.finishSoundId(value);
            return this;
        }

        public Builder finishSound(String value) {
            return finishSound(GroovyScriptUtil.resourceLocation(value));
        }

        public Builder modelTexture(ResourceLocation value) {
            delegate.modelTexture(value);
            return this;
        }

        public Builder modelTexture(String value) {
            return modelTexture(GroovyScriptUtil.resourceLocation(value));
        }

        public PillItemDefinition build() {
            return delegate.build();
        }

        public Item register() {
            return GregoriusDrugworksGroovyScriptBridge.registerPill(build());
        }
    }
}
