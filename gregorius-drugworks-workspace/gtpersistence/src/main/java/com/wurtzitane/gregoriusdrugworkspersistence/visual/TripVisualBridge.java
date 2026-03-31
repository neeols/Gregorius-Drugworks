package com.wurtzitane.gregoriusdrugworkspersistence.visual;

import net.minecraft.entity.player.EntityPlayerMP;

public final class TripVisualBridge {

    private TripVisualBridge() {
    }

    public static void activate(EntityPlayerMP player, String profileId, int durationTicks) {
        GregoriusDrugworksVisualProfiles.startFor(player, profileId, durationTicks);
    }

    public static void clear(EntityPlayerMP player) {
        GregoriusDrugworksVisualProfiles.stopFor(player);
    }
}