package com.wurtzitane.gregoriusdrugworkspersistence.trip;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public final class TripDebugAccess {

    private static final String PERSISTENT_ROOT = "gregoriusdrugworks";

    private TripDebugAccess() {
    }

    public static NBTTagCompound getSnapshot(EntityPlayerMP player) {
        NBTTagCompound root = player.getEntityData();
        if (!root.hasKey(PERSISTENT_ROOT)) {
            return new NBTTagCompound();
        }
        return root.getCompoundTag(PERSISTENT_ROOT).copy();
    }

    public static void clear(EntityPlayerMP player) {
        player.getEntityData().removeTag(PERSISTENT_ROOT);
    }
}