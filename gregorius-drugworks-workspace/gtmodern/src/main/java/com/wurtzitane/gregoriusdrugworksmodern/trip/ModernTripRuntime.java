package com.wurtzitane.gregoriusdrugworksmodern.trip;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import com.wurtzitane.gregoriusdrugworks.common.trip.model.EffectSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.ParticleSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.SoundSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime;

import java.util.UUID;

public final class ModernTripRuntime implements TripRuntime {

    private static final String PERSISTENT_ROOT = "gregoriusdrugworks";

    private final MinecraftServer server;

    public ModernTripRuntime(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public long getCurrentTick() {
        return server.getTickCount();
    }

    @Override
    public TripRuntime.TripPlayer resolvePlayer(UUID uuid, String username) {
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
    public long getPersistentLong(TripRuntime.TripPlayer player, String key) {
        CompoundTag tag = getOrCreateTripData(((ModernTripPlayer) player).player());
        return tag.contains(key) ? tag.getLong(key) : 0L;
    }

    @Override
    public void setPersistentLong(TripRuntime.TripPlayer player, String key, long value) {
        CompoundTag tag = getOrCreateTripData(((ModernTripPlayer) player).player());
        tag.putLong(key, value);
    }

    @Override
    public boolean getPersistentBoolean(TripRuntime.TripPlayer player, String key) {
        CompoundTag tag = getOrCreateTripData(((ModernTripPlayer) player).player());
        return tag.contains(key) && tag.getBoolean(key);
    }

    @Override
    public void setPersistentBoolean(TripRuntime.TripPlayer player, String key, boolean value) {
        CompoundTag tag = getOrCreateTripData(((ModernTripPlayer) player).player());
        tag.putBoolean(key, value);
    }

    @Override
    public void removePersistentKey(TripRuntime.TripPlayer player, String key) {
        CompoundTag tag = getOrCreateTripData(((ModernTripPlayer) player).player());
        tag.remove(key);
    }

    @Override
    public void sendMessage(TripRuntime.TripPlayer player, String message, String color) {
        ((ModernTripPlayer) player).player().sendSystemMessage(Component.literal(message));
    }

    @Override
    public void applyEffect(TripRuntime.TripPlayer player, EffectSpec effect) {
        ServerPlayer serverPlayer = ((ModernTripPlayer) player).player();

        ResourceLocation id = ResourceLocation.tryParse(effect.getId());
        if (id == null) {
            log("[TRIP][MODERN][BAD_EFFECT_ID] " + effect.getId());
            return;
        }

        MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(id);
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
                true));
    }

    @Override
    public void clearEffect(TripRuntime.TripPlayer player, String effectId) {
        ServerPlayer serverPlayer = ((ModernTripPlayer) player).player();

        ResourceLocation id = ResourceLocation.tryParse(effectId);
        if (id == null) {
            log("[TRIP][MODERN][BAD_EFFECT_ID] " + effectId);
            return;
        }

        MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(id);
        if (mobEffect != null) {
            serverPlayer.removeEffect(mobEffect);
        }
    }

    @Override
    public void spawnParticles(TripRuntime.TripPlayer player, ParticleSpec particle) {
        ServerPlayer serverPlayer = ((ModernTripPlayer) player).player();

        ResourceLocation id = ResourceLocation.tryParse(particle.getId());
        if (id == null) {
            log("[TRIP][MODERN][BAD_PARTICLE_ID] " + particle.getId());
            return;
        }

        ParticleType<?> particleType = ForgeRegistries.PARTICLE_TYPES.getValue(id);
        if (!(particleType instanceof SimpleParticleType simpleParticleType)) {
            log("[TRIP][MODERN][PARTMISS_OR_NOT_SIMPLE] " + particle.getId());
            return;
        }

        serverPlayer.serverLevel().sendParticles(
                simpleParticleType,
                serverPlayer.getX(),
                serverPlayer.getY() + 1.0D,
                serverPlayer.getZ(),
                particle.getCount(),
                particle.getSpread(),
                particle.getSpread(),
                particle.getSpread(),
                particle.getSpeed());
    }

    @Override
    public void playSound(TripRuntime.TripPlayer player, SoundSpec sound) {
        ServerPlayer serverPlayer = ((ModernTripPlayer) player).player();

        ResourceLocation id = ResourceLocation.tryParse(sound.getId());
        if (id == null) {
            log("[TRIP][MODERN][BAD_SOUND_ID] " + sound.getId());
            return;
        }

        SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(id);
        if (soundEvent == null) {
            log("[TRIP][MODERN][SOUNDMISS] " + sound.getId());
            return;
        }

        serverPlayer.serverLevel().playSound(
                null,
                serverPlayer.blockPosition(),
                soundEvent,
                SoundSource.PLAYERS,
                sound.getVolume(),
                sound.getPitch());
    }

    @Override
    public void consumeHeldItem(TripRuntime.TripPlayer player, int amount) {
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
        if (!persistent.contains(PERSISTENT_ROOT)) {
            persistent.put(PERSISTENT_ROOT, new CompoundTag());
        }
        return persistent.getCompound(PERSISTENT_ROOT);
    }

    public static final class ModernTripPlayer implements TripRuntime.TripPlayer {

        private final ServerPlayer player;

        public ModernTripPlayer(ServerPlayer player) {
            this.player = player;
        }

        public ServerPlayer player() {
            return player;
        }

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
