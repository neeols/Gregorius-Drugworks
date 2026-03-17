package com.wurtzitane.gregoriusdrugworkspersistence.inhalation.client;

import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.ItemInhalationConsumable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

public final class InhalationItemStackRenderer extends TileEntityItemStackRenderer {

    public static final InhalationItemStackRenderer INSTANCE = new InhalationItemStackRenderer();
    private static final ModelBreathTube MODEL = new ModelBreathTube();

    private InhalationItemStackRenderer() {
    }

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        if (!(stack.getItem() instanceof ItemInhalationConsumable)) {
            return;
        }

        InhalationDefinition definition = ((ItemInhalationConsumable) stack.getItem()).getDefinition();
        Minecraft.getMinecraft().getTextureManager().bindTexture(definition.getModelTexture());

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(30.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(0.42F, 0.42F, 0.42F);
        MODEL.render(0.0625F);
        GlStateManager.popMatrix();
    }
}