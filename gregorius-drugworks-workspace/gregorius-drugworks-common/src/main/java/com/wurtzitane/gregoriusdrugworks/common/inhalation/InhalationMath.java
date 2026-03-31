package com.wurtzitane.gregoriusdrugworks.common.inhalation;

public final class InhalationMath {

    private InhalationMath() {
    }

    public static float completionRatio(int totalUseTicks, int timeLeft) {
        if (totalUseTicks <= 0) {
            return 1.0F;
        }
        float ratio = (totalUseTicks - timeLeft) / (float) totalUseTicks;
        if (ratio < 0.0F) {
            return 0.0F;
        }
        if (ratio > 1.0F) {
            return 1.0F;
        }
        return ratio;
    }

    public static int remainingUses(int maxUses, int currentUses) {
        return Math.max(0, maxUses - currentUses);
    }

    public static double lerp(double a, double b, float t) {
        return a + (b - a) * t;
    }

    public static int lerpInt(int a, int b, float t) {
        return Math.round((float) (a + (b - a) * t));
    }
}