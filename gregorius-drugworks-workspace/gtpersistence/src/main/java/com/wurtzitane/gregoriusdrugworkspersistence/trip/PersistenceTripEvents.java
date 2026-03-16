package com.wurtzitane.gregoriusdrugworkspersistence.trip;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

@Mod.EventBusSubscriber
public final class PersistenceTripEvents {

    private static boolean attached = false;

    private PersistenceTripEvents() {
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (!attached && FMLCommonHandler.instance().getMinecraftServerInstance() != null) {
            PersistenceTripHooks.attachServer(FMLCommonHandler.instance().getMinecraftServerInstance());
            attached = true;
        }

        if (event.phase == TickEvent.Phase.END) {
            PersistenceTripHooks.serverTick();
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP player) {
            PersistenceTripHooks.onPlayerLogin(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player instanceof EntityPlayerMP player) {
            PersistenceTripHooks.onPlayerLogout(player);
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntityPlayer() instanceof EntityPlayerMP player)) {
            return;
        }

        if (player.getHeldItemMainhand().isEmpty() || player.getHeldItemMainhand().getItem().getRegistryName() == null) {
            return;
        }

        String itemId = player.getHeldItemMainhand().getItem().getRegistryName().toString();
        if (PersistenceTripHooks.onItemUse(player, itemId)) {
            event.setCanceled(true);
        }
    }
}