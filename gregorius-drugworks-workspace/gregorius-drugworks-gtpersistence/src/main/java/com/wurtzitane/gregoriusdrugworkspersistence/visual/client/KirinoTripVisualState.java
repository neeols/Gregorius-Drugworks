package com.wurtzitane.gregoriusdrugworkspersistence.visual.client;

public final class KirinoTripVisualState {

    private final float age;
    private final float progress;
    private final float intensity;
    private final float prism;
    private final float tunnel;
    private final float ribbon;
    private final float wobble;

    public KirinoTripVisualState(
            float age,
            float progress,
            float intensity,
            float prism,
            float tunnel,
            float ribbon,
            float wobble
    ) {
        this.age = age;
        this.progress = progress;
        this.intensity = intensity;
        this.prism = prism;
        this.tunnel = tunnel;
        this.ribbon = ribbon;
        this.wobble = wobble;
    }

    public float getAge() {
        return age;
    }

    public float getProgress() {
        return progress;
    }

    public float getIntensity() {
        return intensity;
    }

    public float getPrism() {
        return prism;
    }

    public float getTunnel() {
        return tunnel;
    }

    public float getRibbon() {
        return ribbon;
    }

    public float getWobble() {
        return wobble;
    }
}
