package com.wurtzitane.gregoriusdrugworkspersistence.visual;

import com.wurtzitane.gregoriusdrugworks.common.visual.VisualColorMode;
import com.wurtzitane.gregoriusdrugworks.common.visual.VisualEffectProfile;
import com.wurtzitane.gregoriusdrugworks.common.visual.VisualProfileCatalog;
import com.wurtzitane.gregoriusdrugworkspersistence.network.GregoriusDrugworksNetworkHandler;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nullable;
import java.util.Collection;

public final class GregoriusDrugworksVisualProfiles {

    private static final VisualProfileCatalog CATALOG = new VisualProfileCatalog();
    private static boolean bootstrapped = false;

    private GregoriusDrugworksVisualProfiles() {
    }

    public static void preInit() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;

        CATALOG.register(
                new VisualEffectProfile(
                        "gregoriusdrugworkspersistence:anomaly_rainbow",
                        "Anomaly Rainbow",
                        VisualColorMode.RAINBOW,
                        0x55FF00FF,
                        0.18F,
                        0.45F,
                        0.22F,
                        0.30F,
                        0.30F,
                        0.18F,
                        0.18F,
                        0.55F,
                        0.12F,
                        0.20F,
                        3,
                        true,
                        ""
                )
        );
    }

    public static void register(VisualEffectProfile profile) {
        CATALOG.register(profile);
    }

    @Nullable
    public static VisualEffectProfile get(String id) {
        return CATALOG.get(id);
    }

    public static Collection<VisualEffectProfile> all() {
        return CATALOG.all();
    }

    public static void startFor(EntityPlayerMP player, String profileId, int durationTicks) {
        VisualEffectProfile profile = get(profileId);
        if (profile == null) {
            return;
        }

        GregoriusDrugworksNetworkHandler.sendVisualEffectStart(player, profile, durationTicks);
    }

    public static void stopFor(EntityPlayerMP player) {
        GregoriusDrugworksNetworkHandler.sendVisualEffectStop(player);
    }
}
