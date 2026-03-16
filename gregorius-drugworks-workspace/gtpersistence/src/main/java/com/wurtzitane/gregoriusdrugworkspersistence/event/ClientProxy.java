package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.GTAddon;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Every texture is registered, in case something in that registry, not in that config, is enabled.
 * Nothing happens if each classes registries are empty.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy {
    public static void earlyPreInit() {
        GregoriusDrugworksTextures.preInit();
    }

    public static void latePreInit() {

    }

    public static void postInit() {

    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        GregoriusDrugworksItems.registerModels();
        GregoriusDrugworksFluids.registerFluidBlockModels();
        GregoriusDrugworksMetaBlocks.registerModels();
    }

    //@SubscribeEvent
    //public static void registerFluidModels(TextureStitchEvent.Pre event) {
    //    GregoriusDrugworksFluids.registerFluidModels(event);
    //}

    @SubscribeEvent
    public static void addTooltipNormal(ItemTooltipEvent event) {
        //TooltipChanger.addTooltipNormal(event.getToolTip(), event.getItemStack());
        //TooltipChanger.addTooltipClearing(event.getToolTip(), event.getItemStack(), event.getEntityPlayer());
    }

}
