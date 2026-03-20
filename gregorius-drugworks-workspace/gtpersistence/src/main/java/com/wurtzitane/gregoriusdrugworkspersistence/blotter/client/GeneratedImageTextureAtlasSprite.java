package com.wurtzitane.gregoriusdrugworkspersistence.blotter.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

/**
 * Disk-backed atlas sprite for generated blotter print textures.
 *
 * @author wurtzitane
 */
public final class GeneratedImageTextureAtlasSprite extends TextureAtlasSprite {

    private final File sourceFile;

    public GeneratedImageTextureAtlasSprite(ResourceLocation spriteLocation, File sourceFile) {
        super(spriteLocation.toString());
        this.sourceFile = sourceFile;
    }

    @Override
    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }

    @Override
    public boolean load(IResourceManager manager, ResourceLocation location,
                        Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
        if (sourceFile == null || !sourceFile.isFile()) {
            return true;
        }
        try {
            BufferedImage image = ImageIO.read(sourceFile);
            if (image == null) {
                return true;
            }
            clearFramesTextureData();
            setIconWidth(image.getWidth());
            setIconHeight(image.getHeight());

            int minDimension = Math.max(1, Math.min(image.getWidth(), image.getHeight()));
            int maxMipLevels = MathHelper.log2(Integer.highestOneBit(minDimension)) + 1;
            int[][] pixels = new int[Math.max(1, maxMipLevels)][];
            pixels[0] = new int[image.getWidth() * image.getHeight()];
            image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels[0], 0, image.getWidth());
            this.framesTextureData.add(pixels);
            return false;
        } catch (IOException exception) {
            return true;
        }
    }
}
