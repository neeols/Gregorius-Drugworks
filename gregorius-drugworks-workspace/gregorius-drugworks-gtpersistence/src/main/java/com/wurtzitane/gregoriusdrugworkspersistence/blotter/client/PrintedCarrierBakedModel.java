package com.wurtzitane.gregoriusdrugworkspersistence.blotter.client;

import com.wurtzitane.gregoriusdrugworkspersistence.blotter.BlotterPrintData;
import com.wurtzitane.gregoriusdrugworkspersistence.blotter.BlotterPrintableRegistry;
import com.wurtzitane.gregoriusdrugworkspersistence.blotter.PrintableCarrierKind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper model that swaps in generated printed textures based on item NBT.
 *
 * @author wurtzitane
 */
public final class PrintedCarrierBakedModel implements IBakedModel {

    private final PrintableCarrierKind carrierKind;
    private final IBakedModel blankModel;
    private final ItemOverrideList overrides;
    private final Map<String, IBakedModel> variantCache = new HashMap<>();

    public PrintedCarrierBakedModel(PrintableCarrierKind carrierKind, IBakedModel blankModel) {
        this.carrierKind = carrierKind;
        this.blankModel = blankModel;
        this.overrides = new ItemOverrideList(Collections.emptyList()) {
            @Override
            public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world,
                                               @Nullable EntityLivingBase entity) {
                if (stack.isEmpty() || !BlotterPrintData.hasPrint(stack)) {
                    return PrintedCarrierBakedModel.this.blankModel;
                }
                String variantId = BlotterPrintData.getVariantId(stack);
                if (variantId.isEmpty() || BlotterPrintableRegistry.findByVariantId(variantId) == null) {
                    return PrintedCarrierBakedModel.this.blankModel;
                }
                int opacity = BlotterPrintData.getOpacity(stack);
                String cacheKey = variantId + "|" + opacity;
                IBakedModel cached = variantCache.get(cacheKey);
                if (cached != null) {
                    return cached;
                }

                ResourceLocation spriteLocation =
                        BlotterPrintableRegistry.getGeneratedSpriteLocation(carrierKind, variantId, opacity);
                TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks()
                        .getAtlasSprite(spriteLocation.toString());
                if (sprite == null || "missingno".equals(sprite.getIconName())) {
                    return PrintedCarrierBakedModel.this.blankModel;
                }

                IBakedModel baked = new PrintedCarrierSpriteBakedModel(PrintedCarrierBakedModel.this.blankModel, sprite);
                variantCache.put(cacheKey, baked);
                return baked;
            }
        };
    }

    @Override
    public List<BakedQuad> getQuads(net.minecraft.block.state.IBlockState state, EnumFacing side, long rand) {
        return blankModel.getQuads(state, side, rand);
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
        return blankModel.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return overrides;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        Pair<? extends IBakedModel, Matrix4f> blankPerspective = blankModel.handlePerspective(cameraTransformType);
        return Pair.of(this, blankPerspective.getRight());
    }
}
