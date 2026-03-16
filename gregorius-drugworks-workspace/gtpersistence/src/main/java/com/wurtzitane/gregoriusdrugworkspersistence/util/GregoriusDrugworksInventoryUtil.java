package com.wurtzitane.gregoriusdrugworkspersistence.util;

import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationRemainderSpec;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Random;

public final class GregoriusDrugworksInventoryUtil {

    private GregoriusDrugworksInventoryUtil() {
    }

    public static boolean giveOrDrop(EntityPlayerMP player, ItemStack stack, boolean dropIfInventoryFull) {
        if (stack.isEmpty()) {
            return true;
        }

        boolean inserted = player.inventory.addItemStackToInventory(stack.copy());
        if (!inserted && dropIfInventoryFull) {
            player.dropItem(stack.copy(), false);
            return true;
        }

        return inserted;
    }

    public static void grantRemainders(EntityPlayerMP player, List<InhalationRemainderSpec> specs) {
        if (specs == null || specs.isEmpty()) {
            return;
        }

        Random random = player.getRNG();
        for (InhalationRemainderSpec spec : specs) {
            if (random.nextFloat() > spec.getChance()) {
                continue;
            }
            giveOrDrop(player, spec.createStackCopy(), spec.isDropIfInventoryFull());
        }
    }
}