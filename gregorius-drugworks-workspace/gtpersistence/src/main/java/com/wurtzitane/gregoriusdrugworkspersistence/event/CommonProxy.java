package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript.GregoriusDrugworksGroovyScriptBridge;
import com.wurtzitane.gregoriusdrugworkspersistence.medical.GregoriusDrugworksMedicalApplicators;
import com.wurtzitane.gregoriusdrugworkspersistence.network.GregoriusDrugworksNetworkHandler;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadCarriers;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadRegistry;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadSources;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksRecipeHandler;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.RecipeLoadMedicalApplicator;
import com.wurtzitane.gregoriusdrugworkspersistence.trigger.GregoriusDrugworksTriggerBundles;
import com.wurtzitane.gregoriusdrugworkspersistence.visual.GregoriusDrugworksVisualProfiles;
import gregtech.api.unification.material.event.MaterialEvent;
import gregtech.api.unification.material.event.PostMaterialEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class CommonProxy {

    public static void onConstruction() {
        GregoriusDrugworksNetworkHandler.onConstruction();
    }

    public static void preInit() {
        GregoriusDrugworksCreativeTabs.preInit();

        GregoriusDrugworksItems.preInit();
        GregoriusDrugworksBlocks.preInit();
        GregoriusDrugworksFluids.preInit();
        GregoriusDrugworksMetaItems.preInit();
        GregoriusDrugworksMetaBlocks.preInit();
        GregoriusDrugworksMetaTileEntities.preInit();
        GregoriusDrugworksMedicalApplicators.preInit();
        GregoriusDrugworksPayloadRegistry.preInit();
        GregoriusDrugworksPayloadSources.preInit();
        GregoriusDrugworksPayloadCarriers.preInit();
        GregoriusDrugworksTriggerBundles.preInit();
        GregoriusDrugworksVisualProfiles.preInit();
        GregoriusDrugworksGroovyScriptBridge.onCommonPreInit();

        GregoriusDrugworksSounds.register();
    }

    public static void postInit() {
    }

    public static void loadComplete() {
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        GregoriusDrugworksItems.register(registry);
        GregoriusDrugworksMedicalApplicators.register(registry);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        GregoriusDrugworksBlocks.register(registry);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerMaterials(MaterialEvent event) {
        GregoriusDrugworksMaterials.init();
    }

    @SubscribeEvent
    public static void materialChanges(PostMaterialEvent event) {
        GregoriusDrugworksMaterials.materialChanges();
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new RecipeLoadMedicalApplicator());
        GregoriusDrugworksRecipeHandler.init();
    }
}
