package com.wurtzitane.gregoriusdrugworkspersistence.inhalation.server;

import com.wurtzitane.gregoriusdrugworks.common.debug.DebugFormatters;
import com.wurtzitane.gregoriusdrugworks.common.debug.GdwDebugCategory;
import com.wurtzitane.gregoriusdrugworks.common.inhalation.LingeringOriginMode;
import com.wurtzitane.gregoriusdrugworks.common.util.WeightedPicker;
import com.wurtzitane.gregoriusdrugworkspersistence.debug.GregoriusDrugworksDebug;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationLingeringSpec;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationParticleSpec;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationParticleMotion;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class GregoriusDrugworksInhalationServerHooks {

    private static final Map<UUID, ActiveLingeringEmission> ACTIVE = new LinkedHashMap<>();

    private GregoriusDrugworksInhalationServerHooks() {
    }

    public static void schedule(EntityPlayerMP player, InhalationDefinition definition) {
        InhalationLingeringSpec spec = definition.getLingeringSpec();
        if (spec == null || !spec.isEnabled() || spec.getParticles().isEmpty()) {
            return;
        }

        Vec3d plumeDirection = InhalationParticleMotion.plumeDirection(player);
        Vec3d drift = new Vec3d(
                plumeDirection.x * spec.getForwardDrift(),
                plumeDirection.y * spec.getForwardDrift() + spec.getUpwardDrift(),
                plumeDirection.z * spec.getForwardDrift()
        );

        ActiveLingeringEmission emission = new ActiveLingeringEmission(
                player.getUniqueID(),
                definition,
                spec,
                player.world.getTotalWorldTime(),
                drift,
                plumeDirection
        );

        ACTIVE.put(player.getUniqueID(), emission);

        GregoriusDrugworksDebug.log(
                GdwDebugCategory.LINGER,
                DebugFormatters.join(
                        "[LINGER][SCHEDULE]",
                        DebugFormatters.kv("player", player.getName()),
                        DebugFormatters.kv("item", definition.getItemId())
                )
        );
    }

    public static void tick() {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server == null) {
            ACTIVE.clear();
            return;
        }

        Iterator<Map.Entry<UUID, ActiveLingeringEmission>> iterator = ACTIVE.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, ActiveLingeringEmission> entry = iterator.next();
            ActiveLingeringEmission emission = entry.getValue();

            EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(entry.getKey());
            if (player == null || player.isDead) {
                iterator.remove();
                continue;
            }

            InhalationLingeringSpec spec = emission.getSpec();
            int ageTicks = (int) (player.world.getTotalWorldTime() - emission.getStartTick());

            if (!spec.isActiveAge(ageTicks) && ageTicks >= spec.getStartDelayTicks() + spec.getDurationTicks()) {
                iterator.remove();
                continue;
            }

            if (!spec.shouldEmitAtAge(ageTicks)) {
                continue;
            }

            InhalationParticleSpec picked = WeightedPicker.pick(player.getRNG(), spec.getParticles(), InhalationParticleSpec::getWeight);
            if (picked == null || picked.getParticleType() == null) {
                continue;
            }

            Vec3d origin = resolveOrigin(player, emission, ageTicks);
            int count = spec.sampleCount(ageTicks);
            double spread = spec.sampleSpread(ageTicks);
            double speed = spec.sampleSpeed(ageTicks);

            emitDirectionalParticles(player, emission, picked, origin, count, spread, speed);
        }
    }

    private static void emitDirectionalParticles(
            EntityPlayerMP player,
            ActiveLingeringEmission emission,
            InhalationParticleSpec spec,
            Vec3d origin,
            int count,
            double spread,
            double speed
    ) {
        java.util.Random random = player.getRNG();
        int particles = Math.max(1, count);
        Vec3d plumeDirection = emission.getLaunchDirection();

        for (int i = 0; i < particles; i++) {
            double ox = (random.nextDouble() - 0.5D) * spread;
            double oy = (random.nextDouble() - 0.25D) * spread;
            double oz = (random.nextDouble() - 0.5D) * spread;

            double mx = emission.getDriftPerEmission().x + plumeDirection.x * spec.getForwardBias() + random.nextGaussian() * speed * 0.08D;
            double my = emission.getDriftPerEmission().y
                    + plumeDirection.y * spec.getForwardBias()
                    + spec.getUpwardBias()
                    + Math.abs(random.nextGaussian()) * speed * 0.05D;
            double mz = emission.getDriftPerEmission().z + plumeDirection.z * spec.getForwardBias() + random.nextGaussian() * speed * 0.08D;
            my = Math.max(0.02D, my);

            player.getServerWorld().spawnParticle(
                    spec.getParticleType(),
                    true,
                    origin.x + ox,
                    origin.y + oy,
                    origin.z + oz,
                    0,
                    mx,
                    my,
                    mz,
                    1.0D
            );
        }
    }

    public static void clearForPlayer(UUID playerUuid) {
        ACTIVE.remove(playerUuid);
    }

    public static void clearAll() {
        ACTIVE.clear();
    }

    public static List<String> describeActive(MinecraftServer server) {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<UUID, ActiveLingeringEmission> entry : ACTIVE.entrySet()) {
            ActiveLingeringEmission emission = entry.getValue();
            EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(entry.getKey());
            String playerName = player == null ? "<offline>" : player.getName();

            lines.add(
                    "linger"
                            + " | player=" + playerName
                            + " | item=" + emission.getDefinition().getItemId()
                            + " | origin=" + emission.getSpec().getOriginMode().name().toLowerCase()
                            + " | detached=" + emission.isDetached()
            );
        }
        return lines;
    }

    private static Vec3d resolveOrigin(EntityPlayerMP player, ActiveLingeringEmission emission, int ageTicks) {
        InhalationLingeringSpec spec = emission.getSpec();
        Vec3d forward = new Vec3d(emission.getLaunchDirection().x, 0.0D, emission.getLaunchDirection().z).normalize();
        if (forward.lengthSquared() < 1.0E-6D) {
            forward = InhalationParticleMotion.horizontalForward(player);
        }

        switch (spec.getOriginMode()) {
            case AT_MOUTH:
                return InhalationParticleMotion.mouthOrigin(player, 0.08D, -0.10D);

            case ATTACHED_PLAYER:
                return InhalationParticleMotion.mouthOrigin(player, 0.26D, -0.05D);

            case DETACHED_WORLD_CLOUD:
                if (!emission.isDetached() && ageTicks >= spec.getAttachedTicks()) {
                    Vec3d detachStart = InhalationParticleMotion.mouthOrigin(player, 0.34D, -0.04D);
                    emission.detachAt(detachStart);
                }

                if (emission.isDetached()) {
                    emission.moveDetached(emission.getDriftPerEmission());
                    return emission.getDetachedPosition();
                }

                return InhalationParticleMotion.mouthOrigin(player, 0.22D, -0.05D);

            case FRONT_OF_FACE:
            default:
                return InhalationParticleMotion.mouthOrigin(player, 0.42D, -0.05D);
        }
    }
}
