package com.wurtzitane.gregoriusdrugworkspersistence.medical.client;

import com.wurtzitane.gregoriusdrugworks.common.medical.ApplicatorUseProfile;
import net.minecraft.util.EnumHand;

public final class ApplicatorApplicationState {

    private static final int COMPLETION_LINGER_TICKS = 5;

    private final int playerEntityId;
    private final EnumHand hand;
    private final long startTick;
    private final ApplicatorUseProfile profile;
    private final int sequenceId;
    private final boolean localCameraPolish;
    private long completionTick = -1L;

    public ApplicatorApplicationState(
            int playerEntityId,
            EnumHand hand,
            long startTick,
            ApplicatorUseProfile profile,
            int sequenceId,
            boolean localCameraPolish
    ) {
        this.playerEntityId = playerEntityId;
        this.hand = hand;
        this.startTick = startTick;
        this.profile = profile;
        this.sequenceId = sequenceId;
        this.localCameraPolish = localCameraPolish;
    }

    public int getPlayerEntityId() {
        return playerEntityId;
    }

    public EnumHand getHand() {
        return hand;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public long getStartTick() {
        return startTick;
    }

    public boolean isLocalCameraPolish() {
        return localCameraPolish;
    }

    public boolean isExpired(long worldTime) {
        if (completionTick >= 0L) {
            return worldTime >= completionTick + COMPLETION_LINGER_TICKS;
        }
        return worldTime >= startTick + profile.getTotalUseTicks();
    }

    public float progress(long worldTime, float partialTicks) {
        float progress = getElapsedTicks(worldTime, partialTicks) / (float) profile.getTotalUseTicks();
        if (progress < 0.0F) {
            return 0.0F;
        }
        if (progress > 1.0F) {
            return 1.0F;
        }
        return progress;
    }

    public ApplicatorUseProfile getProfile() {
        return profile;
    }

    public float getElapsedTicks(long worldTime, float partialTicks) {
        return (worldTime - startTick) + partialTicks;
    }

    public float getSegmentProgress(long worldTime, float partialTicks, int startInclusive, int endInclusive) {
        if (endInclusive <= startInclusive) {
            return 1.0F;
        }

        float elapsedTicks = getElapsedTicks(worldTime, partialTicks);
        if (elapsedTicks <= startInclusive) {
            return 0.0F;
        }
        if (elapsedTicks >= endInclusive) {
            return 1.0F;
        }

        return (elapsedTicks - startInclusive) / (float) (endInclusive - startInclusive);
    }

    public void markCompleted(long worldTime) {
        if (completionTick < 0L) {
            completionTick = worldTime;
        }
    }

    public boolean isCompleted() {
        return completionTick >= 0L;
    }

    public float getCompletionLingerProgress(long worldTime, float partialTicks) {
        if (completionTick < 0L) {
            return 0.0F;
        }

        float age = (worldTime - completionTick) + partialTicks;
        if (age <= 0.0F) {
            return 0.0F;
        }
        if (age >= COMPLETION_LINGER_TICKS) {
            return 1.0F;
        }
        return age / COMPLETION_LINGER_TICKS;
    }
}
