package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.GTAddon;
import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import gregtech.api.GregTechAPI;
import gregtech.api.unification.material.event.MaterialEvent;
import gregtech.api.unification.material.event.MaterialRegistryEvent;
import gregtech.api.unification.material.event.PostMaterialEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.registries.IForgeRegistry;

public class CommonProxy {

    public static void onConstruction() {
        //GregoriusDrugworksNetworkHandler.onConstruction();
    }

    public static void preInit() {
        GregoriusDrugworksCreativeTabs.preInit();
        
        GregoriusDrugworksItems.preInit();
        GregoriusDrugworksBlocks.preInit();
        GregoriusDrugworksFluids.preInit();
        GregoriusDrugworksMetaItems.preInit();
        GregoriusDrugworksMetaBlocks.preInit();

        GregoriusDrugworksSounds.register();
        //GregoriusDrugworksRemappers.preInit();
        GregoriusDrugworksRecipeMaps.preInit();
        GregoriusDrugworksMetaTileEntities.preInit();

        //DataFixerHandler.preInit();
        //FluidRegistryMixinHelper.preInit();

        //GregoriusDrugworksNetworkHandler.preInit();
        
        //GregoriusDrugworksDimensions.register();
    }

    public static void postInit() {
        // recipes
    }

    public static void loadComplete() {
        //FluidRegistryMixinHelper.loadComplete();

        //RecipeMapLogic.clearAll();
        //ResourcesObserver.onLoadComplete();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        GregoriusDrugworksItems.register(registry);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        GregoriusDrugworksBlocks.register(registry);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerMaterials(MaterialEvent event) {
            //GregoriusDrugworksOrePrefix.init();
            //GregoriusDrugworksMaterialFlags.init();
            GregoriusDrugworksMaterials.init();
    }

    @SubscribeEvent
    public static void materialChanges(PostMaterialEvent event) {
            GregoriusDrugworksMaterials.materialChanges();
    }

    @SubscribeEvent
    public static void createMaterialRegistry(MaterialRegistryEvent event) {
        GregTechAPI.materialManager.createRegistry(Tags.MOD_ID);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
            //PerfectGemsCutterRecipes.initRecipes();

    }
}
