package com.wurtzitane.gregoriusdrugworks.common.medical;

public final class ApplicatorUseProfile {

    private final int totalUseTicks;
    private final int raiseEndTick;
    private final int applyStartTick;
    private final int applyEndTick;
    private final int holdEndTick;
    private final int finishTick;

    public ApplicatorUseProfile(
            int totalUseTicks,
            int raiseEndTick,
            int applyStartTick,
            int applyEndTick,
            int holdEndTick,
            int finishTick
    ) {
        this.totalUseTicks = totalUseTicks;
        this.raiseEndTick = raiseEndTick;
        this.applyStartTick = applyStartTick;
        this.applyEndTick = applyEndTick;
        this.holdEndTick = holdEndTick;
        this.finishTick = finishTick;
    }

    public int getTotalUseTicks() {
        return totalUseTicks;
    }

    public int getRaiseEndTick() {
        return raiseEndTick;
    }

    public int getApplyStartTick() {
        return applyStartTick;
    }

    public int getApplyEndTick() {
        return applyEndTick;
    }

    public int getHoldEndTick() {
        return holdEndTick;
    }

    public int getFinishTick() {
        return finishTick;
    }
}