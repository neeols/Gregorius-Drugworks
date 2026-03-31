package com.wurtzitane.gregoriusdrugworkspersistence.trip;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public final class TripEvents {

    private static boolean attached = false;

    private TripEvents() {
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (!attached && FMLCommonHandler.instance().getMinecraftServerInstance() != null) {
            TripHooks.attachServer(FMLCommonHandler.instance().getMinecraftServerInstance());
            attached = true;
        }

        if (event.phase == TickEvent.Phase.END) {
            TripHooks.serverTick();
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            TripHooks.onPlayerLogin((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            TripHooks.onPlayerLogout((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntityPlayer() instanceof EntityPlayerMP)) {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();

        if (player.getHeldItemMainhand().isEmpty()
                || player.getHeldItemMainhand().getItem().getRegistryName() == null) {
            return;
        }

        if (player.getHeldItemMainhand().getItem() instanceof ITripUseDeferredItem) {
            return;
        }

        String itemId = player.getHeldItemMainhand().getItem().getRegistryName().toString();
        if (TripHooks.onItemUse(player, itemId)) {
            event.setCanceled(true);
        }
    }
}