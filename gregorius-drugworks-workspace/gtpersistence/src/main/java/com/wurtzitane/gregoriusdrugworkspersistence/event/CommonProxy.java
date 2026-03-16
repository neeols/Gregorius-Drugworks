package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.GTAddon;
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

    public static boolean serverStartedMsg = false;

    public static void onConstruction() {
        LabsNetworkHandler.onConstruction();
    }

    public static void preInit() {
        LabsModeHelper.check();

        if (Loader.isModLoaded(LabsValues.ARCHITECTURE_MODID) &&
                LabsConfig.modIntegration.enableArchitectureCraftIntegration)
            LabsShapes.preInit();

        if (Loader.isModLoaded(LabsValues.BETTER_P2P_MODID))
            LabsBetterMemoryCardModes.preInit();

        LabsCreativeTabs.preInit();

        if (LabsConfig.content.customContent.enableItems)
            LabsItems.preInit();
        if (LabsConfig.content.customContent.enableBlocks)
            LabsBlocks.preInit();
        if (LabsConfig.content.customContent.enableFluids)
            LabsFluids.preInit();

        if (LabsConfig.content.gtCustomContent.enableItems)
            LabsMetaItems.preInit();
        if (LabsConfig.content.gtCustomContent.enableBlocks)
            LabsMetaBlocks.preInit();

        LabsSounds.register();
        LabsRemappers.preInit();
        LabsRecipeMaps.preInit();
        LabsMetaTileEntities.preInit();

        if (LabsConfig.modIntegration.enableTOPIntegration && Loader.isModLoaded(LabsValues.TOP_MODID))
            LabsTOPManager.register();

        DataFixerHandler.preInit();
        FluidRegistryMixinHelper.preInit();

        LabsNetworkHandler.preInit();

        if (LabsConfig.content.customContent.enableVoidDimension)
            LabsDimensions.register();
    }

    public static void postInit() {
        LabsModeHelper.onPostInit();

        // Fix AE2Stuff Tools
        if (Loader.isModLoaded(LabsValues.AE2_STUFF_MODID))
            AE2StuffToolChanges.apply();

        if (Loader.isModLoaded(LabsValues.NAE2_MODID) && Loader.isModLoaded(LabsValues.AE2FC_MODID))
            AE2FCIntegration.postInit();
    }

    public static void loadComplete() {
        FluidRegistryMixinHelper.loadComplete();

        RecipeMapLogic.clearAll();
        ResourcesObserver.onLoadComplete();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        LabsItems.register(registry);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        LabsBlocks.register(registry);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerMaterials(MaterialEvent event) {
        if (LabsConfig.content.gtCustomContent.enablePerfectGems) {
            /* Initialize Custom OrePrefixes & Material Flags */
            LabsOrePrefix.init();
            LabsMaterialFlags.init();
        }
        if (LabsConfig.content.gtCustomContent.enableMaterials)
            LabsMaterials.init();
    }

    @SubscribeEvent
    public static void materialChanges(PostMaterialEvent event) {
        if (LabsConfig.content.gtCustomContent.enableMaterials)
            LabsMaterials.materialChanges();
    }

    @SubscribeEvent
    public static void createMaterialRegistry(MaterialRegistryEvent event) {
        GregTechAPI.materialManager.createRegistry(LabsValues.LABS_MODID);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        if (LabsConfig.content.gtCustomContent.enablePerfectGems && LabsOrePrefix.GEM_PERFECT != null)
            PerfectGemsCutterRecipes.initRecipes();
        if (LabsConfig.content.customContent.enableComplexRecipes && LabsItems.HAND_FRAMING_TOOL != null)
            event.getRegistry().register(new HandFramingRecipe(LabsNames.makeLabsName("hand_framing_recipe")));

        // com.nomiceu.GTAddon.recipe.LabsTestRecipes.initRecipes();
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        var toLock = LabsDifficultyHelper.getLockedDifficulty();
        if (toLock == null) return;

        // Lock Difficulty
        event.getWorld().getWorldInfo().setDifficulty(toLock);
        event.getWorld().getWorldInfo().setDifficultyLocked(true);
    }

    @SubscribeEvent
    public static void onTickEnd(TickEvent.ServerTickEvent event) {
        if (serverStartedMsg || !LabsSide.isDedicatedServer() || event.phase != TickEvent.Phase.END)
            return;

        serverStartedMsg = true;
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        GTAddon.LOGGER.info("=========================================================");
        GTAddon.LOGGER.info("{} Server Successfully Started!", LabsConfig.advanced.serverWelcomeName);
        GTAddon.LOGGER.info(" - Pack Version: {}", LabsVersionConfig.formattedVersion);
        GTAddon.LOGGER.info(" - Mode: {}", LabsModeHelper.getFormattedMode());
        GTAddon.LOGGER.info(" - Port: {}", server.getServerPort());
        GTAddon.LOGGER.info("Players Can Now Join!");
        GTAddon.LOGGER.info("=========================================================");
    }

    @SubscribeEvent
    public static void onEquipmentChangeEvent(LivingEquipmentChangeEvent event) {
        ItemExcitationCoil.onEquipmentChange(event);
    }

    @SubscribeEvent
    public static void syncConfigValues(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(LabsValues.LABS_MODID))
            ConfigManager.sync(LabsValues.LABS_MODID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public static void missingItemMappings(MissingMappings<Item> event) {
        LabsRemappers.remapAndIgnoreEntries(event, Remapper.RemapTypes.ITEM);
    }

    @SubscribeEvent
    public static void missingBlockMappings(MissingMappings<Block> event) {
        LabsRemappers.remapAndIgnoreEntries(event, Remapper.RemapTypes.BLOCK);
    }

    @SubscribeEvent
    public static void missingEntityMappings(MissingMappings<EntityEntry> event) {
        LabsRemappers.remapAndIgnoreEntries(event, Remapper.RemapTypes.ENTITY);
    }

    @SubscribeEvent
    public static void missingBiomeMappings(MissingMappings<Biome> event) {
        LabsRemappers.remapAndIgnoreEntries(event, Remapper.RemapTypes.BIOME);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void gsHandAdditions(GsHandEvent event) {
        if (LabsConfig.groovyScriptSettings.enableGroovyHandAdditions)
            GroovyScriptHandManager.addToHand(event);
    }

    @SubscribeEvent
    public static void onScriptReload(GroovyReloadEvent event) {
        if (Loader.isModLoaded(LabsValues.NUCLEARCRAFT_MODID)) {
            NCActiveCoolerHelper.onReload();
        }
    }

    @SubscribeEvent
    public static void afterScriptLoad(ScriptRunEvent.Post event) {
        if (Loader.isModLoaded(LabsValues.NUCLEARCRAFT_MODID)) {
            NCActiveCoolerHelper.afterScriptLoad();
        }

        if (LabsConfig.groovyScriptSettings.craftingOutputCacheMode ==
                LabsConfig.GroovyScriptSettings.CraftingOutputCacheMode.DISCARDED)
            CraftingOutputCache.cache = null;

        // GT reloads oredict caches depending on load state.
        // Labs always fits the state with GrS and JEI loaded (due to deps), meaning that oredict caches are refreshed
        // ONLY on JEI plugin register.
        // However, that event appears not to take place on dedicated servers, hence breaking oredicts there.
        // Hence, reloads caches on GrS script load, ONLY ON dedicated servers.
        // See: https://github.com/GregTechCEu/GregTech/pull/2771
        if (LabsSide.isDedicatedServer()) {
            GTAddon.LOGGER.info("Fixing GT Ore Dict Caches... (DEDICATED SERVER ONLY)");
            // noinspection UnstableApiUsage
            GTRecipeOreInput.refreshStackCache();
        }
    }
}
