package com.wurtzitane.gregoriusdrugworkspersistence.blotter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * NBT helpers for printed blotter carriers.
 *
 * @author wurtzitane
 */
public final class BlotterPrintData {

    public static final String ROOT_TAG = "GdwPrint";
    public static final String VERSION_KEY = "Version";
    public static final int CURRENT_VERSION = 1;
    public static final String VARIANT_ID_KEY = "VariantId";
    public static final String SOURCE_PATH_KEY = "SourcePath";
    public static final String OPACITY_KEY = "Opacity";

    private BlotterPrintData() {
    }

    public static boolean hasPrint(ItemStack stack) {
        return getRoot(stack) != null && !getVariantId(stack).isEmpty();
    }

    public static void apply(ItemStack stack, String variantId, String sourcePath, int opacityPercent) {
        if (stack.isEmpty()) {
            return;
        }
        NBTTagCompound root = getOrCreateRoot(stack);
        root.setInteger(VERSION_KEY, CURRENT_VERSION);
        root.setString(VARIANT_ID_KEY, variantId == null ? "" : variantId);
        root.setString(SOURCE_PATH_KEY, sourcePath == null ? "" : sourcePath);
        root.setInteger(OPACITY_KEY, clampOpacity(opacityPercent));
    }

    public static void clear(ItemStack stack) {
        if (stack.hasTagCompound()) {
            stack.getTagCompound().removeTag(ROOT_TAG);
        }
    }

    public static void copy(ItemStack source, ItemStack destination) {
        if (destination.isEmpty()) {
            return;
        }
        NBTTagCompound root = getRoot(source);
        if (root == null) {
            clear(destination);
            return;
        }
        if (!destination.hasTagCompound()) {
            destination.setTagCompound(new NBTTagCompound());
        }
        destination.getTagCompound().setTag(ROOT_TAG, root.copy());
    }

    public static String getVariantId(ItemStack stack) {
        NBTTagCompound root = getRoot(stack);
        return root == null ? "" : root.getString(VARIANT_ID_KEY);
    }

    public static String getSourcePath(ItemStack stack) {
        NBTTagCompound root = getRoot(stack);
        return root == null ? "" : root.getString(SOURCE_PATH_KEY);
    }

    public static int getOpacity(ItemStack stack) {
        NBTTagCompound root = getRoot(stack);
        return root == null ? 100 : clampOpacity(root.getInteger(OPACITY_KEY));
    }

    @Nullable
    public static NBTTagCompound getRoot(ItemStack stack) {
        if (stack.isEmpty() || !stack.hasTagCompound() || !stack.getTagCompound().hasKey(ROOT_TAG)) {
            return null;
        }
        return stack.getTagCompound().getCompoundTag(ROOT_TAG);
    }

    private static NBTTagCompound getOrCreateRoot(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound tag = stack.getTagCompound();
        if (!tag.hasKey(ROOT_TAG)) {
            tag.setTag(ROOT_TAG, new NBTTagCompound());
        }
        return tag.getCompoundTag(ROOT_TAG);
    }

    public static int clampOpacity(int opacityPercent) {
        if (opacityPercent < 0) {
            return 0;
        }
        return Math.min(opacityPercent, 100);
    }
}
