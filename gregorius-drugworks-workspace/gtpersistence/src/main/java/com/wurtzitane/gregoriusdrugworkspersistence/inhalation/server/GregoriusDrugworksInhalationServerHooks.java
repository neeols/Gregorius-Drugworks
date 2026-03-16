package com.wurtzitane.gregoriusdrugworkspersistence.inhalation.server;

import com.wurtzitane.gregoriusdrugworks.common.debug.DebugFormatters;
import com.wurtzitane.gregoriusdrugworks.common.debug.GdwDebugCategory;
import com.wurtzitane.gregoriusdrugworks.common.inhalation.LingeringOriginMode;
import com.wurtzitane.gregoriusdrugworks.common.util.WeightedPicker;
import com.wurtzitane.gregoriusdrugworkspersistence.debug.GregoriusDrugworksDebug;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationLingeringSpec;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationParticleSpec;
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

        Vec3d look = player.getLookVec().normalize();
        Vec3d drift = new Vec3d(
                look.x * spec.getForwardDrift(),
                spec.getUpwardDrift(),
                look.z * spec.getForwardDrift()
        );

        ActiveLingeringEmission emission = new ActiveLingeringEmission(
                player.getUniqueID(),
                definition,
                spec,
                player.world.getTotalWorldTime(),
                drift
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

            player.getServerWorld().spawnParticle(
                    picked.getParticleType(),
                    true,
                    origin.x,
                    origin.y,
                    origin.z,
                    count,
                    spread,
                    spread,
                    spread,
                    speed
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
        Vec3d look = player.getLookVec().normalize();
        Vec3d eye = new Vec3d(
                player.posX,
                player.posY + player.getEyeHeight(),
                player.posZ
        );

        switch (spec.getOriginMode()) {
            case AT_MOUTH:
                return eye.add(look.scale(0.10D)).add(0.0D, -0.10D, 0.0D);

            case ATTACHED_PLAYER:
                return eye.add(look.scale(0.28D)).add(0.0D, -0.03D, 0.0D);

            case DETACHED_WORLD_CLOUD:
                if (!emission.isDetached() && ageTicks >= spec.getAttachedTicks()) {
                    Vec3d detachStart = eye.add(look.scale(0.35D)).add(0.0D, -0.02D, 0.0D);
                    emission.detachAt(detachStart);
                }

                if (emission.isDetached()) {
                    emission.moveDetached(emission.getDriftPerEmission());
                    return emission.getDetachedPosition();
                }

                return eye.add(look.scale(0.22D)).add(0.0D, -0.04D, 0.0D);

            case FRONT_OF_FACE:
            default:
                return eye.add(look.scale(0.45D)).add(0.0D, -0.03D, 0.0D);
        }
    }
}