package com.wurtzitane.gregoriusdrugworkspersistence.medical.client;

import com.wurtzitane.gregoriusdrugworkspersistence.medical.GregoriusDrugworksApplicatorPayloads;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public final class ApplicatorItemStackRenderer extends TileEntityItemStackRenderer {

    public static final ApplicatorItemStackRenderer INSTANCE = new ApplicatorItemStackRenderer();

    private static final ModelHyposprayApplicator MODEL = new ModelHyposprayApplicator();
    private static final ResourceLocation EMPTY_TEXTURE = new ResourceLocation("gregoriusdrugworkspersistence", "textures/item/medical_applicator_empty.png");
    private static final ResourceLocation LOADED_TEXTURE = new ResourceLocation("gregoriusdrugworkspersistence", "textures/item/medical_applicator_loaded.png");

    private ApplicatorItemStackRenderer() {
    }

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(
                GregoriusDrugworksApplicatorPayloads.hasPayload(stack) ? LOADED_TEXTURE : EMPTY_TEXTURE
        );

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(30.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(0.36F, 0.36F, 0.36F);
        MODEL.render(0.0625F);
        GlStateManager.popMatrix();
    }
}