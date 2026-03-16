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
        LabsTextures.preInit();
    }

    public static void latePreInit() {
        if (Loader.isModLoaded(LabsValues.BQU_MODID))
            LabsTierHelper.preInit();
    }

    public static void postInit() {
        // Load EnderIO Keybinds, Make Sure Loaded Before Groovy Keybind Overrides
        if (Loader.isModLoaded(LabsValues.ENDER_IO_MODID)) {
            try {
                Class.forName("crazypants.enderio.base.handler.KeyTracker");
            } catch (ClassNotFoundException e) {
                GTAddon.LOGGER.error(
                        "Failed to load EnderIO's KeyTracker Class! Overrides for Ender IO Keybindings may not be available!");
            }
        }

        // Register Find Me's Fluid Keyubind
        if (Loader.isModLoaded(LabsValues.FIND_ME_MODID))
            FindMeKeybindRegister.register();

        // Register Better P2P Custom Filters
        if (Loader.isModLoaded(LabsValues.BETTER_P2P_MODID))
            LabsFilters.postInit();
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        LabsItems.registerModels();
        LabsFluids.registerFluidBlockModels();
        LabsMetaBlocks.registerModels();
    }

    @SubscribeEvent
    public static void registerFluidModels(TextureStitchEvent.Pre event) {
        LabsFluids.registerFluidModels(event);
    }

    @SubscribeEvent
    public static void addTooltipNormal(ItemTooltipEvent event) {
        TooltipChanger.addTooltipNormal(event.getToolTip(), event.getItemStack());
        TooltipChanger.addTooltipClearing(event.getToolTip(), event.getItemStack(), event.getEntityPlayer());
    }

    @SubscribeEvent
    public static void languageChanged(LabsResourcesRefreshedEvent event) {
        if (Loader.isModLoaded(LabsValues.BETTER_P2P_MODID))
            ModeDescriptionsHandler.refreshDescriptions();
    }

    @SubscribeEvent
    public static void onScriptReload(GroovyReloadEvent event) {
        LabsJEIPlugin.onReload();
        OreByProductChangeStorage.clear();
        GroovyTooltipChanger.clear();
        NBTClearingRecipe.NBT_CLEARERS.clear();
    }

    @SubscribeEvent
    public static void afterScriptLoad(ScriptRunEvent.Post event) {
        GTAddon.LOGGER.info("Reloading Options File.");
        FMLClientHandler.instance().getClient().gameSettings.loadOptions();
    }

    @SubscribeEvent
    public static void onMouseEvent(MouseEvent event) {
        if (!Loader.isModLoaded(LabsValues.AE2_MODID)) return;

        int scroll = event.getDwheel();
        if (scroll == 0 || !LabsTooltipHelper.isShiftDown()) return;
        byte offset = (byte) (scroll < 0 ? 1 : -1);

        // Handle P2P Scroll
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayer player = minecraft.player;
        if (player == null || minecraft.currentScreen != null) return;

        if (LabsP2PCycleMessage.MessageHandler.getP2ps()
                .containsKey(new ItemMeta(player.getHeldItem(EnumHand.MAIN_HAND)))) {
            LabsNetworkHandler.NETWORK_HANDLER
                    .sendToServer(new LabsP2PCycleMessage(player, EnumHand.MAIN_HAND, offset));
            event.setCanceled(true);
        } else if (LabsP2PCycleMessage.MessageHandler.getP2ps()
                .containsKey(new ItemMeta(player.getHeldItem(EnumHand.OFF_HAND)))) {
            LabsNetworkHandler.NETWORK_HANDLER
                    .sendToServer(new LabsP2PCycleMessage(player, EnumHand.OFF_HAND, offset));
            event.setCanceled(true);
        }
}
