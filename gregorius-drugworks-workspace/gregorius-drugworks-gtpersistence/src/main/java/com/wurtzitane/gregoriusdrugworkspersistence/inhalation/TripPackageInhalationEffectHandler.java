package com.wurtzitane.gregoriusdrugworkspersistence.inhalation;

import com.wurtzitane.gregoriusdrugworkspersistence.trip.TripHooks;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.EnumSet;
import java.util.Set;

public final class TripPackageInhalationEffectHandler implements InhalationEffectHandler {

    private final Set<InhalationUsePhase> activePhases;

    public TripPackageInhalationEffectHandler(Set<InhalationUsePhase> activePhases) {
        this.activePhases = EnumSet.copyOf(activePhases);
    }

    @Override
    public void onPhase(EntityPlayerMP player, ItemStack stack, InhalationDefinition definition, InhalationUsePhase phase, boolean exhausted) {
        if (!activePhases.contains(phase)) {
            return;
        }

        if (stack.getItem().getRegistryName() != null) {
            TripHooks.onItemUse(player, stack.getItem().getRegistryName().toString());
        }
    }
}