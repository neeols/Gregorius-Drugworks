package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.client.GregoriusDrugworksHeldItemLayerHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.debug.GregoriusDrugworksDebug;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.client.GregoriusDrugworksInhalationClientHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.medical.client.GregoriusDrugworksApplicatorClientHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.client.GregoriusDrugworksPillClientHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.visual.client.GregoriusDrugworksVisualClientHooks;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy {

    public static void earlyPreInit() {
        GregoriusDrugworksDebug.setEnabled(true);
        GregoriusDrugworksTextures.preInit();
    }

    public static void latePreInit() {
        GregoriusDrugworksHeldItemLayerHooks.preInit();
        GregoriusDrugworksPillClientHooks.preInit();
        GregoriusDrugworksInhalationClientHooks.preInit();
        GregoriusDrugworksApplicatorClientHooks.preInit();
        GregoriusDrugworksVisualClientHooks.preInit();
    }

    public static void postInit() {
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        GregoriusDrugworksItems.registerModels();
        GregoriusDrugworksFluids.registerFluidBlockModels();
        GregoriusDrugworksMetaBlocks.registerModels();
        com.wurtzitane.gregoriusdrugworkspersistence.medical.GregoriusDrugworksMedicalApplicators.registerModels();
    }

    @SubscribeEvent
    public static void registerFluidModels(TextureStitchEvent.Pre event) {
        GregoriusDrugworksFluids.registerFluidModels(event);
    }

    @SubscribeEvent
    public static void addTooltipNormal(ItemTooltipEvent event) {
        //TooltipChanger.addTooltipNormal(event.getToolTip(), event.getItemStack());
        //TooltipChanger.addTooltipClearing(event.getToolTip(), event.getItemStack(), event.getEntityPlayer());
    }
}
