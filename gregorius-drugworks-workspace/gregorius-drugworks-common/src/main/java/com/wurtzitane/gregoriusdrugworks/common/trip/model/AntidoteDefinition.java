package com.wurtzitane.gregoriusdrugworks.common.trip.model;

public final class AntidoteDefinition {

    private final String itemId;
    private final boolean consumeOnUse;
    private final int consumeAmount;
    private final String cancelMessage;
    private final String cancelMessageColor;
    private final String noTripMessage;
    private final String noTripMessageColor;
    private final String wrongTripMessage;
    private final String wrongTripMessageColor;
    private final SoundSpec sound;
    private final ParticleSpec particles;

    private AntidoteDefinition(Builder builder) {
        this.itemId = builder.itemId;
        this.consumeOnUse = builder.consumeOnUse;
        this.consumeAmount = builder.consumeAmount;
        this.cancelMessage = builder.cancelMessage;
        this.cancelMessageColor = builder.cancelMessageColor;
        this.noTripMessage = builder.noTripMessage;
        this.noTripMessageColor = builder.noTripMessageColor;
        this.wrongTripMessage = builder.wrongTripMessage;
        this.wrongTripMessageColor = builder.wrongTripMessageColor;
        this.sound = builder.sound;
        this.particles = builder.particles;
    }

    public String getItemId() {
        return itemId;
    }

    public boolean isConsumeOnUse() {
        return consumeOnUse;
    }

    public int getConsumeAmount() {
        return consumeAmount;
    }

    public String getCancelMessage() {
        return cancelMessage;
    }

    public String getCancelMessageColor() {
        return cancelMessageColor;
    }

    public String getNoTripMessage() {
        return noTripMessage;
    }

    public String getNoTripMessageColor() {
        return noTripMessageColor;
    }

    public String getWrongTripMessage() {
        return wrongTripMessage;
    }

    public String getWrongTripMessageColor() {
        return wrongTripMessageColor;
    }

    public SoundSpec getSound() {
        return sound;
    }

    public ParticleSpec getParticles() {
        return particles;
    }

    public static Builder builder(String itemId) {
        return new Builder(itemId);
    }

    public static final class Builder {
        private final String itemId;
        private boolean consumeOnUse = true;
        private int consumeAmount = 1;
        private String cancelMessage = "Counter-item applied — status cleared.";
        private String cancelMessageColor = "aqua";
        private String noTripMessage = "No active staged effect is running.";
        private String noTripMessageColor = "gray";
        private String wrongTripMessage = "That counter-item does not work for this active effect.";
        private String wrongTripMessageColor = "gray";
        private SoundSpec sound;
        private ParticleSpec particles;

        private Builder(String itemId) {
            this.itemId = itemId;
        }

        public Builder consumeOnUse(boolean consumeOnUse) {
            this.consumeOnUse = consumeOnUse;
            return this;
        }

        public Builder consumeAmount(int consumeAmount) {
            this.consumeAmount = consumeAmount;
            return this;
        }

        public Builder cancelMessage(String message, String color) {
            this.cancelMessage = message;
            this.cancelMessageColor = color;
            return this;
        }

        public Builder noTripMessage(String message, String color) {
            this.noTripMessage = message;
            this.noTripMessageColor = color;
            return this;
        }

        public Builder wrongTripMessage(String message, String color) {
            this.wrongTripMessage = message;
            this.wrongTripMessageColor = color;
            return this;
        }

        public Builder sound(SoundSpec sound) {
            this.sound = sound;
            return this;
        }

        public Builder particles(ParticleSpec particles) {
            this.particles = particles;
            return this;
        }

        public AntidoteDefinition build() {
            return new AntidoteDefinition(this);
        }
    }
}