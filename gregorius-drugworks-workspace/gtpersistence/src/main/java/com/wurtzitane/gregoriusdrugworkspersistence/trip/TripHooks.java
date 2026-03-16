package com.wurtzitane.gregoriusdrugworkspersistence.trip;

import com.wurtzitane.gregoriusdrugworks.common.trip.api.TripRegistrationApi;
import com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public final class TripHooks {

    private static TripManager manager;
    private static TripRuntime runtime;

    private TripHooks() {
    }

    public static void attachServer(MinecraftServer server) {
        runtime = new TripRuntime(server);
        manager = new TripManager(runtime, TripRegistrationApi.registry());

        // Intentionally disabled for now.
        // PersistenceTripBootstrap.registerDefaults();
    }

    public static void serverTick() {
        if (manager != null) {
            manager.tick();
        }
    }

    public static void onPlayerLogin(EntityPlayerMP player) {
        if (manager != null) {
            manager.onPlayerLogin(new TripRuntime.PersistenceTripPlayer(player));
        }
    }

    public static void onPlayerLogout(EntityPlayerMP player) {
        if (manager != null) {
            manager.onPlayerLogout(new TripRuntime.PersistenceTripPlayer(player));
        }
    }

    public static boolean onItemUse(EntityPlayerMP player, String itemId) {
        if (manager == null) {
            return false;
        }
        return manager.handleItemUse(new TripRuntime.PersistenceTripPlayer(player), itemId);
    }

    public static TripManager getManager() {
        return manager;
    }
}