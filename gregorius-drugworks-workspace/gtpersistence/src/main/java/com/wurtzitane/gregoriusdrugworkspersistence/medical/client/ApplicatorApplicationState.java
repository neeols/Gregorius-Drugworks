package com.wurtzitane.gregoriusdrugworkspersistence.medical.client;

import com.wurtzitane.gregoriusdrugworks.common.medical.ApplicatorUseProfile;
import net.minecraft.util.EnumHand;

public final class ApplicatorApplicationState {

    private final int playerEntityId;
    private final EnumHand hand;
    private final long startTick;
    private final ApplicatorUseProfile profile;
    private final int sequenceId;
    private final boolean localCameraPolish;

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

    public boolean isLocalCameraPolish() {
        return localCameraPolish;
    }

    public boolean isExpired(long worldTime) {
        return worldTime >= startTick + profile.getTotalUseTicks();
    }

    public float progress(long worldTime, float partialTicks) {
        float age = (worldTime - startTick) + partialTicks;
        float progress = age / (float) profile.getTotalUseTicks();
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
}