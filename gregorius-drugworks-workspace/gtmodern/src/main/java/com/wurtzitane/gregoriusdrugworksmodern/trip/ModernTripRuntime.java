package com.wurtzitane.gregoriusdrugworksmodern.trip;

import com.wurtzitane.gregoriusdrugworks.common.trip.model.EffectSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.ParticleSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.SoundSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public final class ModernTripRuntime implements TripRuntime {

    private final MinecraftServer server;

    public ModernTripRuntime(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public long getCurrentTick() {
        return server.getTickCount();
    }

    @Override
    public TripPlayer resolvePlayer(UUID uuid, String username) {
        ServerPlayer byUuid = server.getPlayerList().getPlayer(uuid);
        if (byUuid != null) {
            return new ModernTripPlayer(byUuid);
        }

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.getGameProfile().getName().equals(username)) {
                return new ModernTripPlayer(player);
            }
        }

        return null;
    }

    @Override
    public long getPersistentLong(TripPlayer player, String key) {
        CompoundTag tag = getOrCreateTripData(((ModernTripPlayer) player).player());
        return tag.contains(key) ? tag.getLong(key) : 0L;
    }

    @Override
    public void setPersistentLong(TripPlayer player, String key, long value) {
        CompoundTag tag = getOrCreateTripData(((ModernTripPlayer) player).player());
        tag.putLong(key, value);
    }

    @Override
    public boolean getPersistentBoolean(TripPlayer player, String key) {
        CompoundTag tag = getOrCreateTripData(((ModernTripPlayer) player).player());
        return tag.contains(key) && tag.getBoolean(key);
    }

    @Override
    public void setPersistentBoolean(TripPlayer player, String key, boolean value) {
        CompoundTag tag = getOrCreateTripData(((ModernTripPlayer) player).player());
        tag.putBoolean(key, value);
    }

    @Override
    public void removePersistentKey(TripPlayer player, String key) {
        CompoundTag tag = getOrCreateTripData(((ModernTripPlayer) player).player());
        tag.remove(key);
    }

    @Override
    public void sendMessage(TripPlayer player, String message, String color) {
        ((ModernTripPlayer) player).player().sendSystemMessage(Component.literal(message));
    }

    @Override
    public void applyEffect(TripPlayer player, EffectSpec effect) {
        ServerPlayer serverPlayer = ((ModernTripPlayer) player).player();
        MobEffect mobEffect = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.parse(effect.getId()));
        if (mobEffect == null) {
            log("[TRIP][MODERN][EFFECTMISS] " + effect.getId());
            return;
        }

        serverPlayer.addEffect(new MobEffectInstance(
            mobEffect,
            effect.getSeconds() * 20,
            effect.getAmplifier(),
            false,
            !effect.isHideParticles(),
            true
        ));
    }

    @Override
    public void clearEffect(TripPlayer player, String effectId) {
        ServerPlayer serverPlayer = ((ModernTripPlayer) player).player();
        MobEffect mobEffect = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.parse(effectId));
        if (mobEffect != null) {
            serverPlayer.removeEffect(mobEffect);
        }
    }

    @Override
    public void spawnParticles(TripPlayer player, ParticleSpec particle) {
        ServerPlayer serverPlayer = ((ModernTripPlayer) player).player();
        ParticleOptions particleType = BuiltInRegistries.PARTICLE_TYPE.get(ResourceLocation.parse(particle.getId()));
        if (particleType == null) {
            log("[TRIP][MODERN][PARTMISS] " + particle.getId());
            return;
        }

        serverPlayer.serverLevel().sendParticles(
            particleType,
            serverPlayer.getX(),
            serverPlayer.getY() + 1.0D,
            serverPlayer.getZ(),
            particle.getCount(),
            particle.getSpread(),
            particle.getSpread(),
            particle.getSpread(),
            particle.getSpeed()
        );
    }

    @Override
    public void playSound(TripPlayer player, SoundSpec sound) {
        ServerPlayer serverPlayer = ((ModernTripPlayer) player).player();
        if (BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse(sound.getId())) == null) {
            log("[TRIP][MODERN][SOUNDMISS] " + sound.getId());
            return;
        }

        serverPlayer.serverLevel().playSound(
            null,
            serverPlayer.blockPosition(),
            BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse(sound.getId())),
            SoundSource.PLAYERS,
            sound.getVolume(),
            sound.getPitch()
        );
    }

    @Override
    public void consumeHeldItem(TripPlayer player, int amount) {
        ServerPlayer serverPlayer = ((ModernTripPlayer) player).player();
        ItemStack stack = serverPlayer.getMainHandItem();
        if (!stack.isEmpty()) {
            stack.shrink(amount);
        }
    }

    @Override
    public void log(String message) {
        System.out.println(message);
    }

    private static CompoundTag getOrCreateTripData(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains("gregoriusdrugworks")) {
            persistent.put("gregoriusdrugworks", new CompoundTag());
        }
        return persistent.getCompound("gregoriusdrugworks");
    }

    public record ModernTripPlayer(ServerPlayer player) implements TripPlayer {
        @Override
        public UUID getUuid() {
            return player.getUUID();
        }

        @Override
        public String getUsername() {
            return player.getGameProfile().getName();
        }
    }
}