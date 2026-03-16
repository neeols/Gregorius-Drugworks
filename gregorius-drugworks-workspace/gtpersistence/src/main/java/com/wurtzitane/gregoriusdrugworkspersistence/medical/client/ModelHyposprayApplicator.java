package com.wurtzitane.gregoriusdrugworkspersistence.medical.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public final class ModelHyposprayApplicator extends ModelBase {

    private final ModelRenderer body;
    private final ModelRenderer grip;
    private final ModelRenderer head;

    public ModelHyposprayApplicator() {
        this.textureWidth = 32;
        this.textureHeight = 16;

        this.body = new ModelRenderer(this, 0, 0);
        this.body.addBox(-1.0F, -1.0F, -5.0F, 2, 2, 8);

        this.grip = new ModelRenderer(this, 0, 10);
        this.grip.addBox(-1.5F, -1.5F, 3.0F, 3, 3, 2);

        this.head = new ModelRenderer(this, 12, 0);
        this.head.addBox(-1.2F, -1.2F, -7.0F, 2, 2, 2);
    }

    public void render(float scale) {
        body.render(scale);
        grip.render(scale);
        head.render(scale);
    }
}