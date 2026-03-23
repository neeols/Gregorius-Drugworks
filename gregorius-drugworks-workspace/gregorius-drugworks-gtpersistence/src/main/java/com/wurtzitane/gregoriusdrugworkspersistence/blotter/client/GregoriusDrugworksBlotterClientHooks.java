package com.wurtzitane.gregoriusdrugworkspersistence.blotter.client;

import com.wurtzitane.gregoriusdrugworkspersistence.blotter.BlotterPrintableRegistry;
import com.wurtzitane.gregoriusdrugworkspersistence.blotter.PrintableCarrierKind;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksItems;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Client-side printed blotter sprite and baked-model hooks.
 *
 * @author wurtzitane
 */
@SideOnly(Side.CLIENT)
public final class GregoriusDrugworksBlotterClientHooks {

    private static boolean initialised;

    private GregoriusDrugworksBlotterClientHooks() {
    }

    public static void preInit() {
        if (initialised) {
            return;
        }
        initialised = true;
        MinecraftForge.EVENT_BUS.register(new GregoriusDrugworksBlotterClientHooks());
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        TextureMap textureMap = event.getMap();
        for (BlotterPrintableRegistry.Entry entry : BlotterPrintableRegistry.all()) {
            for (int opacity = 0; opacity <= 100; opacity++) {
                registerGeneratedSprite(textureMap, PrintableCarrierKind.BLOTTER_PAPER, entry, opacity);
                registerGeneratedSprite(textureMap, PrintableCarrierKind.SINGLE_TAB, entry, opacity);
            }
        }
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        replacePrintableModel(event, GregoriusDrugworksItems.BLOTTER_PAPER, PrintableCarrierKind.BLOTTER_PAPER);
        replacePrintableModel(event, GregoriusDrugworksItems.SINGLE_TAB, PrintableCarrierKind.SINGLE_TAB);
    }

    private static void registerGeneratedSprite(TextureMap textureMap, PrintableCarrierKind kind,
                                                BlotterPrintableRegistry.Entry entry, int opacity) {
        textureMap.setTextureEntry(new GeneratedImageTextureAtlasSprite(
                BlotterPrintableRegistry.getGeneratedSpriteLocation(kind, entry.getVariantId(), opacity),
                BlotterPrintableRegistry.getGeneratedTextureFile(kind, entry.getVariantId(), opacity)
        ));
    }

    private static void replacePrintableModel(ModelBakeEvent event, net.minecraft.item.Item item,
                                              PrintableCarrierKind carrierKind) {
        if (item == null || item.getRegistryName() == null) {
            return;
        }
        ModelResourceLocation location = new ModelResourceLocation(item.getRegistryName(), "inventory");
        IBakedModel blankModel = event.getModelRegistry().getObject(location);
        if (blankModel != null) {
            event.getModelRegistry().putObject(location, new PrintedCarrierBakedModel(carrierKind, blankModel));
        }
    }
}
