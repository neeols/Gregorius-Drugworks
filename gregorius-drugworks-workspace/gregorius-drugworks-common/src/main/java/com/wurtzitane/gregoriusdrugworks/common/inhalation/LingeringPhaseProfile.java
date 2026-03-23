package com.wurtzitane.gregoriusdrugworks.common.inhalation;

public class LingeringPhaseProfile {

    private final int startDelayTicks;
    private final int durationTicks;
    private final int cadenceTicks;
    private final int attachedTicks;
    private final int initialCount;
    private final int finalCount;
    private final double initialSpread;
    private final double finalSpread;
    private final double initialSpeed;
    private final double finalSpeed;
    private final double forwardDrift;
    private final double upwardDrift;
    private final LingeringOriginMode originMode;

    public LingeringPhaseProfile(
            int startDelayTicks,
            int durationTicks,
            int cadenceTicks,
            int attachedTicks,
            int initialCount,
            int finalCount,
            double initialSpread,
            double finalSpread,
            double initialSpeed,
            double finalSpeed,
            double forwardDrift,
            double upwardDrift,
            LingeringOriginMode originMode
    ) {
        this.startDelayTicks = startDelayTicks;
        this.durationTicks = durationTicks;
        this.cadenceTicks = cadenceTicks;
        this.attachedTicks = attachedTicks;
        this.initialCount = initialCount;
        this.finalCount = finalCount;
        this.initialSpread = initialSpread;
        this.finalSpread = finalSpread;
        this.initialSpeed = initialSpeed;
        this.finalSpeed = finalSpeed;
        this.forwardDrift = forwardDrift;
        this.upwardDrift = upwardDrift;
        this.originMode = originMode;
    }

    public int getStartDelayTicks() {
        return startDelayTicks;
    }

    public int getDurationTicks() {
        return durationTicks;
    }

    public int getCadenceTicks() {
        return cadenceTicks;
    }

    public int getAttachedTicks() {
        return attachedTicks;
    }

    public int getInitialCount() {
        return initialCount;
    }

    public int getFinalCount() {
        return finalCount;
    }

    public double getInitialSpread() {
        return initialSpread;
    }

    public double getFinalSpread() {
        return finalSpread;
    }

    public double getInitialSpeed() {
        return initialSpeed;
    }

    public double getFinalSpeed() {
        return finalSpeed;
    }

    public double getForwardDrift() {
        return forwardDrift;
    }

    public double getUpwardDrift() {
        return upwardDrift;
    }

    public LingeringOriginMode getOriginMode() {
        return originMode;
    }

    public boolean isEnabled() {
        return durationTicks > 0 && cadenceTicks > 0;
    }

    public boolean isActiveAge(int ageTicks) {
        return ageTicks >= startDelayTicks && ageTicks < (startDelayTicks + durationTicks);
    }

    public boolean shouldEmitAtAge(int ageTicks) {
        if (!isActiveAge(ageTicks)) {
            return false;
        }
        int localAge = ageTicks - startDelayTicks;
        return localAge % cadenceTicks == 0;
    }

    public float progressAtAge(int ageTicks) {
        if (durationTicks <= 0) {
            return 1.0F;
        }
        float raw = (ageTicks - startDelayTicks) / (float) durationTicks;
        if (raw < 0.0F) {
            return 0.0F;
        }
        if (raw > 1.0F) {
            return 1.0F;
        }
        return raw;
    }

    public int sampleCount(int ageTicks) {
        return Math.toIntExact(Math.max(1, Math.round(lerp(initialCount, finalCount, progressAtAge(ageTicks)))));
    }

    public double sampleSpread(int ageTicks) {
        return lerp(initialSpread, finalSpread, progressAtAge(ageTicks));
    }

    public double sampleSpeed(int ageTicks) {
        return lerp(initialSpeed, finalSpeed, progressAtAge(ageTicks));
    }

    protected static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    protected static double lerp(double a, double b, float t) {
        return a + (b - a) * t;
    }

    protected static double lerp(int a, int b, float t) {
        return a + (b - a) * t;
    }
}