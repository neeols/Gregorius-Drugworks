package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.blotter.ItemPrintableCarrier;
import com.wurtzitane.gregoriusdrugworkspersistence.blotter.BlotterPrintableRegistry;
import com.wurtzitane.gregoriusdrugworkspersistence.hook.GregoriusDrugworksNativeHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript.GregoriusDrugworksGroovyScriptBridge;
import com.wurtzitane.gregoriusdrugworkspersistence.medical.GregoriusDrugworksMedicalApplicators;
import com.wurtzitane.gregoriusdrugworkspersistence.network.GregoriusDrugworksNetworkHandler;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadCarriers;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadRegistry;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadSources;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.ConsumablePayloadCarrierAdapter;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadCarrierAdapter;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.GregoriusDrugworksPillColors;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksRecipeHandler;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksUnificationHelper;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.PayloadFoodLacingRegistry;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.PayloadPillCraftingRegistry;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.RecipeLaceFoods;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.RecipeLoadMedicalApplicator;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.RecipeLoadPayloadPill;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.RecipeRevealPayloadCarrier;
import com.wurtzitane.gregoriusdrugworkspersistence.trigger.GregoriusDrugworksTriggerBundles;
import com.wurtzitane.gregoriusdrugworkspersistence.visual.GregoriusDrugworksVisualProfiles;
import gregtech.api.unification.material.event.MaterialEvent;
import gregtech.api.unification.material.event.PostMaterialEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class CommonProxy {

    private static boolean machineRecipesInitialized;

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
        GregoriusDrugworksPillColors.preInit();
        BlotterPrintableRegistry.preInit();
        PayloadFoodLacingRegistry.preInit();
        PayloadPillCraftingRegistry.preInit();
        GregoriusDrugworksTriggerBundles.preInit();
        GregoriusDrugworksVisualProfiles.preInit();
        GregoriusDrugworksNativeHooks.register();
        GregoriusDrugworksGroovyScriptBridge.onCommonPreInit();

        GregoriusDrugworksSounds.register();
    }

    public static void postInit() {
        initMachineRecipes();
    }

    public static void loadComplete() {
        initMachineRecipes();
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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new RecipeLoadMedicalApplicator());
        event.getRegistry().register(new RecipeLoadPayloadPill());
        event.getRegistry().register(new RecipeRevealPayloadCarrier());
        event.getRegistry().register(new RecipeLaceFoods());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void repairMaterialUnification(RegistryEvent.Register<IRecipe> event) {
        GregoriusDrugworksUnificationHelper.repairNumericMaterialUnification();
    }

    private static void initMachineRecipes() {
        if (machineRecipesInitialized) {
            return;
        }
        GregoriusDrugworksUnificationHelper.repairNumericMaterialUnification();
        GregoriusDrugworksRecipeHandler.init();
        machineRecipesInitialized = true;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void handlePayloadConsumableFinish(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving().world.isRemote || !(event.getEntityLiving() instanceof EntityPlayerMP)) {
            return;
        }

        ItemStack usedStack = event.getItem();
        if (usedStack.isEmpty()) {
            return;
        }
        if (usedStack.getItem() instanceof ItemPrintableCarrier) {
            return;
        }

        PayloadCarrierAdapter adapter = GregoriusDrugworksPayloadCarriers.find(usedStack);
        if (!(adapter instanceof ConsumablePayloadCarrierAdapter)) {
            return;
        }

        GregoriusDrugworksPayloadRegistry.ResolvedPayload payload = adapter.resolve(usedStack);
        if (payload == null) {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
        GregoriusDrugworksPayloadRegistry.applyResolved(player, usedStack, payload);

        if (usedStack.getItem() instanceof ItemFood) {
            return;
        }

        ItemStack resultStack = event.getResultStack();
        if (resultStack.isEmpty()) {
            return;
        }

        PayloadCarrierAdapter resultAdapter = GregoriusDrugworksPayloadCarriers.find(resultStack);
        if (resultAdapter instanceof ConsumablePayloadCarrierAdapter && resultAdapter.hasPayload(resultStack)) {
            resultAdapter.decrementOrClear(resultStack, false);
            event.setResultStack(resultStack);
        }
    }
}
