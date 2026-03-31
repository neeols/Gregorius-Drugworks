package com.wurtzitane.gregoriusdrugworkspersistence.payload;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class PayloadStackAdapter {

    private static final String ROOT = "GdwPayload";

    public static String getPayloadId(ItemStack stack) {

        if (!stack.hasTagCompound()) return null;

        NBTTagCompound tag = stack.getTagCompound();
        if (!tag.hasKey(ROOT)) return null;

        return tag.getCompoundTag(ROOT).getString("id");
    }

}