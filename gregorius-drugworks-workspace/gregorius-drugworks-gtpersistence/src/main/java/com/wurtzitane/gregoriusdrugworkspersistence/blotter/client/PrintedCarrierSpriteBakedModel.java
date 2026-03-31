package com.wurtzitane.gregoriusdrugworkspersistence.blotter.client;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ItemLayerModel;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Cached baked item model for one generated printed sprite.
 *
 * @author wurtzitane
 */
public final class PrintedCarrierSpriteBakedModel implements IBakedModel {

    private final IBakedModel blankModel;
    private final TextureAtlasSprite sprite;
    private final List<BakedQuad> quads;

    public PrintedCarrierSpriteBakedModel(IBakedModel blankModel, TextureAtlasSprite sprite) {
        this.blankModel = blankModel;
        this.sprite = sprite;
        this.quads = ItemLayerModel.getQuadsForSprite(0, sprite, DefaultVertexFormats.ITEM, Optional.empty());
    }

    @Override
    public List<BakedQuad> getQuads(net.minecraft.block.state.IBlockState state, EnumFacing side, long rand) {
        return side == null ? quads : Collections.emptyList();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return blankModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return blankModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return blankModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return sprite;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        Pair<? extends IBakedModel, Matrix4f> blankPerspective = blankModel.handlePerspective(cameraTransformType);
        return Pair.of(this, blankPerspective.getRight());
    }
}
