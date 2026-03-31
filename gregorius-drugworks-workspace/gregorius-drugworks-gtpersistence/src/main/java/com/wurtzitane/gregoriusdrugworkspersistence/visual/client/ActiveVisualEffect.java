package com.wurtzitane.gregoriusdrugworkspersistence.visual.client;

import com.wurtzitane.gregoriusdrugworks.common.visual.VisualEffectProfile;

public final class ActiveVisualEffect {

    private final VisualEffectProfile profile;
    private final long startTick;
    private final int durationTicks;
    private final int sequenceId;

    public ActiveVisualEffect(VisualEffectProfile profile, long startTick, int durationTicks, int sequenceId) {
        this.profile = profile;
        this.startTick = startTick;
        this.durationTicks = durationTicks;
        this.sequenceId = sequenceId;
    }

    public VisualEffectProfile getProfile() {
        return profile;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public boolean isExpired(long worldTime) {
        return worldTime >= startTick + durationTicks;
    }

    public float progress(long worldTime, float partialTicks) {
        float age = (worldTime - startTick) + partialTicks;
        float progress = age / (float) durationTicks;
        if (progress < 0.0F) {
            return 0.0F;
        }
        if (progress > 1.0F) {
            return 1.0F;
        }
        return progress;
    }

    public float age(long worldTime, float partialTicks) {
        return (worldTime - startTick) + partialTicks;
    }
}