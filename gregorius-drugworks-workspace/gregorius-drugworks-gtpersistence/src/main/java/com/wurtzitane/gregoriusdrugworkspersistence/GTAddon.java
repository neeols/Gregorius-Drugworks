package com.wurtzitane.gregoriusdrugworkspersistence;

import com.wurtzitane.gregoriusdrugworkspersistence.command.GregoriusDrugworksCommands;
import com.wurtzitane.gregoriusdrugworkspersistence.event.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.event.ClientProxy;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.registry.MTEManager;

@Mod(
        modid = Tags.MOD_ID,
        name = Tags.MOD_NAME,
        version = Tags.VERSION,
        dependencies = Tags.GREGTECH_DEPENDENCY
)
public class GTAddon {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_ID);

    public GTAddon() {
        FluidRegistry.enableUniversalBucket();
    }

    @EventHandler
    public void onConstruction(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        if (GregoriusDrugworksUtil.isClient()) {
            MinecraftForge.EVENT_BUS.register(ClientProxy.class);
        }
        MinecraftForge.EVENT_BUS.register(CommonProxy.class);
        CommonProxy.onConstruction();
        // GroovyPlugin.onConstruction();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        if (GregoriusDrugworksUtil.isClient())
            ClientProxy.earlyPreInit();
        CommonProxy.preInit();
        if (GregoriusDrugworksUtil.isClient())
            ClientProxy.latePreInit();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        CommonProxy.postInit();
        if (GregoriusDrugworksUtil.isClient())
            ClientProxy.postInit();
    }

    @EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        CommonProxy.loadComplete();
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        //event.registerServerCommand(new );
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        GregoriusDrugworksCommands.register(event);
    }

    @SubscribeEvent
    public void onMTERegistries(MTEManager.MTERegistryEvent event) {
        try {
            GregTechAPI.mteManager.getRegistry(Tags.MOD_ID);
        } catch (IllegalArgumentException ignored) {
            GregTechAPI.mteManager.createRegistry(Tags.MOD_ID);
        }
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        //DataFixerHandler.close();
    }
}
