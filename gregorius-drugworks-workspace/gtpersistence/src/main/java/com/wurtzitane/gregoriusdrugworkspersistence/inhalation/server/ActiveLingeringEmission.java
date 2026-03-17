package com.wurtzitane.gregoriusdrugworkspersistence.inhalation.server;

import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationLingeringSpec;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public final class ActiveLingeringEmission {

    private final UUID playerUuid;
    private final InhalationDefinition definition;
    private final InhalationLingeringSpec spec;
    private final long startTick;
    private final Vec3d driftPerEmission;
    private final Vec3d launchDirection;
    private boolean detached;
    private Vec3d detachedPosition;

    public ActiveLingeringEmission(
            UUID playerUuid,
            InhalationDefinition definition,
            InhalationLingeringSpec spec,
            long startTick,
            Vec3d driftPerEmission,
            Vec3d launchDirection
    ) {
        this.playerUuid = playerUuid;
        this.definition = definition;
        this.spec = spec;
        this.startTick = startTick;
        this.driftPerEmission = driftPerEmission;
        this.launchDirection = launchDirection;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public InhalationDefinition getDefinition() {
        return definition;
    }

    public InhalationLingeringSpec getSpec() {
        return spec;
    }

    public long getStartTick() {
        return startTick;
    }

    public Vec3d getDriftPerEmission() {
        return driftPerEmission;
    }

    public Vec3d getLaunchDirection() {
        return launchDirection;
    }

    public boolean isDetached() {
        return detached;
    }

    public void detachAt(Vec3d position) {
        this.detached = true;
        this.detachedPosition = position;
    }

    public Vec3d getDetachedPosition() {
        return detachedPosition;
    }

    public void moveDetached(Vec3d delta) {
        if (detachedPosition == null) {
            detachedPosition = delta;
        } else {
            detachedPosition = detachedPosition.add(delta);
        }
    }
}
