package com.wurtzitane.gregoriusdrugworkspersistence.trip;

import com.wurtzitane.gregoriusdrugworks.common.trip.api.TripRegistrationApi;
import com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public final class PersistenceTripHooks {

    private static TripManager manager;
    private static PersistenceTripRuntime runtime;

    private PersistenceTripHooks() {
    }

    public static void attachServer(MinecraftServer server) {
        runtime = new PersistenceTripRuntime(server);
        manager = new TripManager(runtime, TripRegistrationApi.registry());

        // Intentionally disabled for now.
        // Register your trips later from Java, Scala, and/or GroovyScript bridge code.
        // PersistenceTripBootstrap.registerDefaults();
    }

    public static void serverTick() {
        if (manager != null) {
            manager.tick();
        }
    }

    public static void onPlayerLogin(EntityPlayerMP player) {
        if (manager != null) {
            manager.onPlayerLogin(new PersistenceTripRuntime.PersistenceTripPlayer(player));
        }
    }

    public static void onPlayerLogout(EntityPlayerMP player) {
        if (manager != null) {
            manager.onPlayerLogout(new PersistenceTripRuntime.PersistenceTripPlayer(player));
        }
    }

    public static boolean onItemUse(EntityPlayerMP player, String itemId) {
        if (manager == null) {
            return false;
        }
        return manager.handleItemUse(new PersistenceTripRuntime.PersistenceTripPlayer(player), itemId);
    }

    public static TripManager getManager() {
        return manager;
    }
}