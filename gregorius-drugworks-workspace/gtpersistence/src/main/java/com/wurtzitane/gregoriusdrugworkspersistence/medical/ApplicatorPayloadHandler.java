package com.wurtzitane.gregoriusdrugworkspersistence.medical;

import com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadRegistry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public interface ApplicatorPayloadHandler {

    void apply(EntityPlayerMP player, ItemStack applicatorStack, GregoriusDrugworksPayloadRegistry.ResolvedPayload payload);
}