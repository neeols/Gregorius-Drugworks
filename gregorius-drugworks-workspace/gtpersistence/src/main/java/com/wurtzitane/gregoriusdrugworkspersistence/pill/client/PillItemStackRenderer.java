package com.wurtzitane.gregoriusdrugworkspersistence.pill.client;

import com.wurtzitane.gregoriusdrugworkspersistence.pill.ItemPillBase;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.PillItemDefinition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

public final class PillItemStackRenderer extends TileEntityItemStackRenderer {

    public static final PillItemStackRenderer INSTANCE = new PillItemStackRenderer();

    private static final ModelCapsulePill MODEL = new ModelCapsulePill();

    private PillItemStackRenderer() {
    }

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        if (!(stack.getItem() instanceof ItemPillBase)) {
            return;
        }

        PillItemDefinition definition = ((ItemPillBase) stack.getItem()).getDefinition();

        Minecraft.getMinecraft().getTextureManager().bindTexture(definition.getModelTexture());

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.enableRescaleNormal();

        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(35.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(0.11F, 0.11F, 0.11F);

        MODEL.render(0.0625F);

        GlStateManager.disableRescaleNormal();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }
}