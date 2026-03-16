package com.wurtzitane.gregoriusdrugworkspersistence.inhalation.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public final class ModelBreathTube extends ModelBase {

    private final ModelRenderer body;
    private final ModelRenderer tip;
    private final ModelRenderer mouthpiece;

    public ModelBreathTube() {
        this.textureWidth = 32;
        this.textureHeight = 16;

        this.body = new ModelRenderer(this, 0, 0);
        this.body.addBox(-1.0F, -1.0F, -6.0F, 2, 2, 10);

        this.tip = new ModelRenderer(this, 0, 12);
        this.tip.addBox(-1.5F, -1.5F, 4.0F, 3, 3, 2);

        this.mouthpiece = new ModelRenderer(this, 12, 12);
        this.mouthpiece.addBox(-1.2F, -1.2F, -8.0F, 2, 2, 2);
    }

    public void render(float scale) {
        body.render(scale);
        tip.render(scale);
        mouthpiece.render(scale);
    }
}