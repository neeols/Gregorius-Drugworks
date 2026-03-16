package com.wurtzitane.gregoriusdrugworksmodern.trip;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;

import com.wurtzitane.gregoriusdrugworksmodern.Mod;

@EventBusSubscriber(modid = Mod.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public final class ModernTripEvents {

    private ModernTripEvents() {}

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        ModernTripHooks.attachServer(event.getServer());
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ModernTripHooks.serverTick();
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer)) {
            return;
        }

        ServerPlayer player = (ServerPlayer) event.getEntity();
        ModernTripHooks.onPlayerLogin(player);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer)) {
            return;
        }

        ServerPlayer player = (ServerPlayer) event.getEntity();
        ModernTripHooks.onPlayerLogout(player);
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer)) {
            return;
        }

        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        ServerPlayer player = (ServerPlayer) event.getEntity();
        ResourceLocation itemKey = ForgeRegistries.ITEMS.getKey(player.getMainHandItem().getItem());
        if (itemKey == null) {
            return;
        }

        String itemId = itemKey.toString();
        if (ModernTripHooks.onItemUse(player, itemId)) {
            event.setCanceled(true);
        }
    }
}
