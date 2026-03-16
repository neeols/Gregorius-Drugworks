package com.wurtzitane.gregoriusdrugworkspersistence;

import com.wurtzitane.gregoriusdrugworkspersistence.command.GregoriusDrugworksCommands;
import com.wurtzitane.gregoriusdrugworkspersistence.event.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.event.ClientProxy;

@net.minecraftforge.fml.common.Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
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

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        //DataFixerHandler.close();
    }
}