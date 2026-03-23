package com.wurtzitane.gregoriusdrugworks.common.util;

import java.util.List;
import java.util.Random;
import java.util.function.ToIntFunction;

public final class WeightedPicker {

    private WeightedPicker() {
    }

    public static <T> T pick(Random random, List<T> values, ToIntFunction<T> weightFunction) {
        if (values == null || values.isEmpty()) {
            return null;
        }

        int totalWeight = 0;
        for (T value : values) {
            int weight = Math.max(0, weightFunction.applyAsInt(value));
            totalWeight += weight;
        }

        if (totalWeight <= 0) {
            return values.get(0);
        }

        int roll = random.nextInt(totalWeight);
        int running = 0;

        for (T value : values) {
            running += Math.max(0, weightFunction.applyAsInt(value));
            if (roll < running) {
                return value;
            }
        }

        return values.get(values.size() - 1);
    }
}