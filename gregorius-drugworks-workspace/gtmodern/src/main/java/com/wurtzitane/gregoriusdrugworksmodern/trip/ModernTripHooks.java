package com.wurtzitane.gregoriusdrugworksmodern.trip;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import com.wurtzitane.gregoriusdrugworks.common.trip.api.TripRegistrationApi;
import com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripManager;

public final class ModernTripHooks {

    private static TripManager manager;
    private static ModernTripRuntime runtime;

    private ModernTripHooks() {}

    public static void attachServer(MinecraftServer server) {
        runtime = new ModernTripRuntime(server);
        manager = new TripManager(runtime, TripRegistrationApi.registry());

        // Intentionally disabled for now.
        // ModernTripBootstrap.registerDefaults();
    }

    public static void serverTick() {
        if (manager != null) {
            manager.tick();
        }
    }

    public static void onPlayerLogin(ServerPlayer player) {
        if (manager != null) {
            manager.onPlayerLogin(new ModernTripRuntime.ModernTripPlayer(player));
        }
    }

    public static void onPlayerLogout(ServerPlayer player) {
        if (manager != null) {
            manager.onPlayerLogout(new ModernTripRuntime.ModernTripPlayer(player));
        }
    }

    public static boolean onItemUse(ServerPlayer player, String itemId) {
        if (manager == null) {
            return false;
        }
        return manager.handleItemUse(new ModernTripRuntime.ModernTripPlayer(player), itemId);
    }

    public static TripManager getManager() {
        return manager;
    }
}
