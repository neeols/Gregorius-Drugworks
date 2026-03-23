package com.wurtzitane.gregoriusdrugworks.common.trip.runtime;

import java.util.Objects;
import java.util.UUID;

public final class PlayerRef {

    private final UUID uuid;
    private final String username;

    public PlayerRef(UUID uuid, String username) {
        this.uuid = Objects.requireNonNull(uuid, "uuid");
        this.username = Objects.requireNonNull(username, "username");
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }
}