package com.wurtzitane.gregoriusdrugworkspersistence.inhalation;

import net.minecraft.util.EnumParticleTypes;

public final class InhalationParticleSpec {

    private final EnumParticleTypes particleType;
    private final int count;
    private final double spreadX;
    private final double spreadY;
    private final double spreadZ;
    private final double speed;
    private final double forwardBias;
    private final double upwardBias;
    private final int weight;

    public InhalationParticleSpec(
            EnumParticleTypes particleType,
            int count,
            double spreadX,
            double spreadY,
            double spreadZ,
            double speed,
            double forwardBias,
            double upwardBias,
            int weight
    ) {
        this.particleType = particleType;
        this.count = count;
        this.spreadX = spreadX;
        this.spreadY = spreadY;
        this.spreadZ = spreadZ;
        this.speed = speed;
        this.forwardBias = forwardBias;
        this.upwardBias = upwardBias;
        this.weight = weight;
    }

    public EnumParticleTypes getParticleType() {
        return particleType;
    }

    public int getCount() {
        return count;
    }

    public double getSpreadX() {
        return spreadX;
    }

    public double getSpreadY() {
        return spreadY;
    }

    public double getSpreadZ() {
        return spreadZ;
    }

    public double getSpeed() {
        return speed;
    }

    public double getForwardBias() {
        return forwardBias;
    }

    public double getUpwardBias() {
        return upwardBias;
    }

    public int getWeight() {
        return weight;
    }
}