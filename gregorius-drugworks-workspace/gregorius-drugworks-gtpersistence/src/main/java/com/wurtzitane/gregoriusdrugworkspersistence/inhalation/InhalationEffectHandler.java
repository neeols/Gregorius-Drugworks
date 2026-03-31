package com.wurtzitane.gregoriusdrugworkspersistence.inhalation;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public interface InhalationEffectHandler {

    void onPhase(EntityPlayerMP player, ItemStack stack, InhalationDefinition definition, InhalationUsePhase phase, boolean exhausted);

    InhalationEffectHandler NO_OP = (player, stack, definition, phase, exhausted) -> {
    };
}