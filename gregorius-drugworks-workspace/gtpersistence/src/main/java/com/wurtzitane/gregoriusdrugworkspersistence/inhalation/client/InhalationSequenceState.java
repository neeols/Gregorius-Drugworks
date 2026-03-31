package com.wurtzitane.gregoriusdrugworkspersistence.inhalation.client;

import net.minecraft.util.EnumHand;

public final class InhalationSequenceState {

    private final int playerEntityId;
    private final String itemId;
    private final EnumHand hand;
    private final long startTick;
    private final int durationTicks;
    private final int inhaleStartTick;
    private final int inhaleEndTick;
    private final int exhaleStartTick;
    private final int exhaleEndTick;
    private final boolean localCameraNudge;
    private final int sequenceId;

    public InhalationSequenceState(
            int playerEntityId,
            String itemId,
            EnumHand hand,
            long startTick,
            int durationTicks,
            int inhaleStartTick,
            int inhaleEndTick,
            int exhaleStartTick,
            int exhaleEndTick,
            boolean localCameraNudge,
            int sequenceId
    ) {
        this.playerEntityId = playerEntityId;
        this.itemId = itemId;
        this.hand = hand;
        this.startTick = startTick;
        this.durationTicks = durationTicks;
        this.inhaleStartTick = inhaleStartTick;
        this.inhaleEndTick = inhaleEndTick;
        this.exhaleStartTick = exhaleStartTick;
        this.exhaleEndTick = exhaleEndTick;
        this.localCameraNudge = localCameraNudge;
        this.sequenceId = sequenceId;
    }

    public int getPlayerEntityId() { return playerEntityId; }
    public String getItemId() { return itemId; }
    public EnumHand getHand() { return hand; }
    public long getStartTick() { return startTick; }
    public int getDurationTicks() { return durationTicks; }
    public int getInhaleStartTick() { return inhaleStartTick; }
    public int getInhaleEndTick() { return inhaleEndTick; }
    public int getExhaleStartTick() { return exhaleStartTick; }
    public int getExhaleEndTick() { return exhaleEndTick; }
    public boolean isLocalCameraNudge() { return localCameraNudge; }
    public int getSequenceId() { return sequenceId; }

    public boolean isExpired(long worldTime) {
        return worldTime >= startTick + durationTicks;
    }

    public float getProgress(long worldTime, float partialTicks) {
        return Math.clamp(((worldTime - startTick) + partialTicks) / (float) durationTicks, 0.0F, 1.0F);
    }

    public float getGlow(long worldTime, float partialTicks, float glowMin, float glowMax) {
        float ticks = (worldTime - startTick) + partialTicks;
        if (ticks < inhaleStartTick || ticks > inhaleEndTick) {
            return glowMin;
        }
        float phase = (ticks - inhaleStartTick) / (float) Math.max(1, inhaleEndTick - inhaleStartTick);
        return glowMin + (glowMax - glowMin) * phase;
    }

    public float getSegmentProgress(long worldTime, float partialTicks, int startInclusive, int endInclusive) {
        if (endInclusive <= startInclusive) {
            return 1.0F;
        }

        float ticks = (worldTime - startTick) + partialTicks;
        if (ticks <= startInclusive) {
            return 0.0F;
        }
        if (ticks >= endInclusive) {
            return 1.0F;
        }

        return (ticks - startInclusive) / (float) (endInclusive - startInclusive);
    }
}
