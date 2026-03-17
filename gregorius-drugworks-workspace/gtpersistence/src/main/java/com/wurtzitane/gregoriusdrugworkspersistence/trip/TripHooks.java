package com.wurtzitane.gregoriusdrugworkspersistence.trip;

import com.wurtzitane.gregoriusdrugworks.common.trip.api.TripRegistrationApi;
import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public final class TripHooks {

    private static TripManager manager;
    private static TripRuntime runtime;

    private TripHooks() {
    }

    public static void attachServer(MinecraftServer server) {
        TripBootstrap.registerDefaults();
        runtime = new TripRuntime(server);
        manager = new TripManager(runtime, TripRegistrationApi.registry());
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

        String normalizedId = normalizeItemId(itemId);
        TripRuntime.PersistenceTripPlayer tripPlayer = new TripRuntime.PersistenceTripPlayer(player);

        if (manager.handleItemUse(tripPlayer, normalizedId)) {
            return true;
        }

        TripBootstrap.registerDefaults();

        if (!normalizedId.equals(itemId) && manager.handleItemUse(tripPlayer, itemId)) {
            return true;
        }

        return manager.handleItemUse(tripPlayer, normalizedId);
    }

    private static String normalizeItemId(String itemId) {
        if (itemId == null || itemId.isEmpty() || itemId.indexOf(':') >= 0) {
            return itemId;
        }
        return Tags.MOD_ID + ":" + itemId;
    }

    public static TripManager getManager() {
        return manager;
    }
}