package com.wurtzitane.gregoriusdrugworkspersistence.integration.jei;

import gregtech.integration.jei.utils.render.FluidStackTextRenderer;
import mezz.jei.api.gui.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class ChemicalPlantAtmosphereRenderer extends FluidStackTextRenderer {

    private final boolean renderFluid;

    public ChemicalPlantAtmosphereRenderer(int fluidAmount, int width, int height, @Nullable IDrawable overlay,
                                           boolean notConsumed, boolean renderFluid) {
        super(fluidAmount, false, width, height, overlay);
        this.renderFluid = renderFluid;
        setNotConsumed(notConsumed);
    }

    @Override
    public void render(Minecraft minecraft, int xPosition, int yPosition, @Nullable FluidStack fluidStack) {
        if (!renderFluid || fluidStack == null) {
            return;
        }
        super.render(minecraft, xPosition, yPosition, fluidStack);
    }
}
