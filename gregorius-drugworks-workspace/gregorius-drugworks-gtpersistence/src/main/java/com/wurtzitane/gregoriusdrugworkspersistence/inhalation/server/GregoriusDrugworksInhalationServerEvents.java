package com.wurtzitane.gregoriusdrugworkspersistence.inhalation.server;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public final class GregoriusDrugworksInhalationServerEvents {

    private GregoriusDrugworksInhalationServerEvents() {
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            GregoriusDrugworksInhalationServerHooks.tick();
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        GregoriusDrugworksInhalationServerHooks.clearForPlayer(event.player.getUniqueID());
    }

    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        GregoriusDrugworksInhalationServerHooks.clearForPlayer(event.player.getUniqueID());
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayerMP) {
            GregoriusDrugworksInhalationServerHooks.clearForPlayer(event.getEntityLiving().getUniqueID());
        }
    }
}