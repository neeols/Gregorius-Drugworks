package com.wurtzitane.gregoriusdrugworkspersistence.pill.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public final class ModelCapsulePill extends ModelBase {

    private final ModelRenderer leftCap;
    private final ModelRenderer leftShoulder;
    private final ModelRenderer center;
    private final ModelRenderer rightShoulder;
    private final ModelRenderer rightCap;

    public ModelCapsulePill() {
        this.textureWidth = 32;
        this.textureHeight = 16;

        this.leftCap = new ModelRenderer(this, 0, 0);
        this.leftCap.addBox(-1.0F, -1.0F, -5.0F, 2, 2, 1);

        this.leftShoulder = new ModelRenderer(this, 0, 3);
        this.leftShoulder.addBox(-1.5F, -1.5F, -4.0F, 3, 3, 1);

        this.center = new ModelRenderer(this, 8, 0);
        this.center.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 6);

        this.rightShoulder = new ModelRenderer(this, 0, 7);
        this.rightShoulder.addBox(-1.5F, -1.5F, 3.0F, 3, 3, 1);

        this.rightCap = new ModelRenderer(this, 8, 10);
        this.rightCap.addBox(-1.0F, -1.0F, 4.0F, 2, 2, 1);
    }

    public void render(float scale) {
        leftCap.render(scale);
        leftShoulder.render(scale);
        center.render(scale);
        rightShoulder.render(scale);
        rightCap.render(scale);
    }
}