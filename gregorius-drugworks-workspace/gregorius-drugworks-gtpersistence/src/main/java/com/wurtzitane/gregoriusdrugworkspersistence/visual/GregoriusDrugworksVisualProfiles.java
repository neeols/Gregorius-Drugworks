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

        register(profile(
                "gregoriusdrugworkspersistence:salvinorin_a_onset",
                "Salvinorin A Onset",
                VisualColorMode.STATIC_TINT,
                0x4418FF7A,
                0.10F,
                0.18F,
                0.05F,
                0.08F,
                0.06F,
                0.04F,
                0.07F,
                0.020F,
                0.006F,
                0.07F,
                2,
                0.18F,
                0.04F,
                0.10F,
                0.08F,
                2,
                0.04F,
                0.03F,
                0.03F
        ));
        register(profile(
                "gregoriusdrugworkspersistence:salvinorin_a_veil_lift",
                "Salvinorin A Veil Lift",
                VisualColorMode.PULSE,
                0x5226FF9C,
                0.14F,
                0.24F,
                0.10F,
                0.12F,
                0.10F,
                0.07F,
                0.09F,
                0.035F,
                0.010F,
                0.09F,
                3,
                0.22F,
                0.10F,
                0.18F,
                0.14F,
                3,
                0.07F,
                0.05F,
                0.06F
        ));
        register(profile(
                "gregoriusdrugworkspersistence:salvinorin_a_fracture",
                "Salvinorin A Fracture",
                VisualColorMode.PULSE,
                0x5A62FFB8,
                0.18F,
                0.28F,
                0.16F,
                0.18F,
                0.18F,
                0.10F,
                0.13F,
                0.050F,
                0.014F,
                0.12F,
                4,
                0.28F,
                0.18F,
                0.26F,
                0.20F,
                4,
                0.10F,
                0.08F,
                0.10F
        ));
        register(profile(
                "gregoriusdrugworkspersistence:salvinorin_a_chrysanthemum",
                "Salvinorin A Chrysanthemum",
                VisualColorMode.RAINBOW,
                0x4CFFD45A,
                0.20F,
                0.34F,
                0.20F,
                0.24F,
                0.16F,
                0.12F,
                0.15F,
                0.060F,
                0.018F,
                0.13F,
                5,
                0.36F,
                0.26F,
                0.42F,
                0.30F,
                6,
                0.12F,
                0.10F,
                0.14F
        ));
        register(profile(
                "gregoriusdrugworkspersistence:salvinorin_a_machine_corridor",
                "Salvinorin A Machine Corridor",
                VisualColorMode.STATIC_TINT,
                0x4A73FFD6,
                0.12F,
                0.26F,
                0.22F,
                0.16F,
                0.22F,
                0.16F,
                0.12F,
                0.055F,
                0.022F,
                0.10F,
                4,
                0.44F,
                0.22F,
                0.52F,
                0.18F,
                4,
                0.15F,
                0.12F,
                0.18F
        ));
        register(profile(
                "gregoriusdrugworkspersistence:salvinorin_a_apex_prism",
                "Salvinorin A Apex Prism",
                VisualColorMode.RAINBOW,
                0x60FF00FF,
                0.24F,
                0.46F,
                0.30F,
                0.32F,
                0.28F,
                0.20F,
                0.20F,
                0.070F,
                0.028F,
                0.16F,
                6,
                0.52F,
                0.42F,
                0.72F,
                0.36F,
                7,
                0.18F,
                0.18F,
                0.24F
        ));
        register(profile(
                "gregoriusdrugworkspersistence:salvinorin_a_recursion",
                "Salvinorin A Recursion",
                VisualColorMode.RAINBOW,
                0x54A7FFED,
                0.18F,
                0.38F,
                0.26F,
                0.24F,
                0.24F,
                0.18F,
                0.18F,
                0.065F,
                0.024F,
                0.14F,
                5,
                0.48F,
                0.36F,
                0.62F,
                0.40F,
                8,
                0.20F,
                0.16F,
                0.30F
        ));
        register(profile(
                "gregoriusdrugworkspersistence:salvinorin_a_afterimage",
                "Salvinorin A Afterimage",
                VisualColorMode.PULSE,
                0x40C9FF96,
                0.12F,
                0.22F,
                0.18F,
                0.16F,
                0.12F,
                0.09F,
                0.10F,
                0.040F,
                0.012F,
                0.09F,
                3,
                0.28F,
                0.14F,
                0.30F,
                0.24F,
                5,
                0.10F,
                0.08F,
                0.22F
        ));
        register(profile(
                "gregoriusdrugworkspersistence:salvinorin_a_comedown",
                "Salvinorin A Comedown",
                VisualColorMode.STATIC_TINT,
                0x2E1CA35A,
                0.08F,
                0.12F,
                0.08F,
                0.06F,
                0.05F,
                0.03F,
                0.07F,
                0.018F,
                0.006F,
                0.06F,
                1,
                0.16F,
                0.04F,
                0.10F,
                0.06F,
                2,
                0.04F,
                0.03F,
                0.06F
        ));
        register(profile(
                "gregoriusdrugworkspersistence:salvinorin_a_afterglow",
                "Salvinorin A Afterglow",
                VisualColorMode.STATIC_TINT,
                0x1828D97A,
                0.05F,
                0.08F,
                0.02F,
                0.03F,
                0.02F,
                0.02F,
                0.04F,
                0.010F,
                0.003F,
                0.05F,
                1,
                0.08F,
                0.02F,
                0.04F,
                0.03F,
                1,
                0.02F,
                0.01F,
                0.03F
        ));
        register(profile(
                "gregoriusdrugworkspersistence:lsd_rainbow",
                "LSD Rainbow",
                VisualColorMode.RAINBOW,
                0xA0FFFFFF,
                0.24F,
                0.34F,
                0.28F,
                0.20F,
                0.18F,
                0.10F,
                0.22F,
                0.080F,
                0.055F,
                0.18F,
                6,
                0.30F,
                0.42F,
                0.48F,
                0.34F,
                8,
                0.16F,
                0.18F,
                0.34F
        ));
        register(profile(
                "gregoriusdrugworkspersistence:methamphetamine_rush",
                "Methamphetamine Rush",
                VisualColorMode.PULSE,
                0x58A9F9FF,
                0.34F,
                0.28F,
                0.22F,
                0.14F,
                0.08F,
                0.03F,
                0.30F,
                0.045F,
                0.080F,
                0.32F,
                4,
                0.14F,
                0.10F,
                0.08F,
                0.26F,
                9,
                0.24F,
                0.08F,
                0.16F
        ));
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

    private static VisualEffectProfile profile(
            String id,
            String debugName,
            VisualColorMode colorMode,
            int tintArgb,
            float pulseSpeed,
            float pulseAmplitude,
            float flashFrequency,
            float flashIntensity,
            float yawDrift,
            float pitchDrift,
            float wobbleSpeed,
            float wobbleAmplitude,
            float fovPulseAmount,
            float fovPulseSpeed,
            int particleDensity,
            float vignetteStrength,
            float prismSeparation,
            float tunnelStrength,
            float ribbonIntensity,
            int ribbonDensity,
            float scanlineStrength,
            float rollDrift,
            float afterimageStrength
    ) {
        return new VisualEffectProfile(
                id,
                debugName,
                colorMode,
                tintArgb,
                pulseSpeed,
                pulseAmplitude,
                flashFrequency,
                flashIntensity,
                yawDrift,
                pitchDrift,
                wobbleSpeed,
                wobbleAmplitude,
                fovPulseAmount,
                fovPulseSpeed,
                particleDensity,
                vignetteStrength,
                prismSeparation,
                tunnelStrength,
                ribbonIntensity,
                ribbonDensity,
                scanlineStrength,
                rollDrift,
                afterimageStrength,
                true,
                ""
        );
    }
}
