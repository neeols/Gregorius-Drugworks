package com.wurtzitane.gregoriusdrugworkspersistence.trip;

import com.wurtzitane.gregoriusdrugworks.common.trip.model.EffectSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.ParticleSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.SoundSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public final class PersistenceTripRuntime implements TripRuntime {

    private final MinecraftServer server;

    public PersistenceTripRuntime(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public long getCurrentTick() {
        return server.getTickCounter();
    }

    @Override
    public TripPlayer resolvePlayer(UUID uuid, String username) {
        EntityPlayerMP byUuid = server.getPlayerList().getPlayerByUUID(uuid);
        if (byUuid != null) {
            return new PersistenceTripPlayer(byUuid);
        }

        EntityPlayerMP byName = server.getPlayerList().getPlayerByUsername(username);
        if (byName != null) {
            return new PersistenceTripPlayer(byName);
        }

        return null;
    }

    @Override
    public long getPersistentLong(TripPlayer player, String key) {
        NBTTagCompound tag = getOrCreateTripData(((PersistenceTripPlayer) player).player());
        return tag.hasKey(key) ? tag.getLong(key) : 0L;
    }

    @Override
    public void setPersistentLong(TripPlayer player, String key, long value) {
        NBTTagCompound tag = getOrCreateTripData(((PersistenceTripPlayer) player).player());
        tag.setLong(key, value);
    }

    @Override
    public boolean getPersistentBoolean(TripPlayer player, String key) {
        NBTTagCompound tag = getOrCreateTripData(((PersistenceTripPlayer) player).player());
        return tag.hasKey(key) && tag.getBoolean(key);
    }

    @Override
    public void setPersistentBoolean(TripPlayer player, String key, boolean value) {
        NBTTagCompound tag = getOrCreateTripData(((PersistenceTripPlayer) player).player());
        tag.setBoolean(key, value);
    }

    @Override
    public void removePersistentKey(TripPlayer player, String key) {
        NBTTagCompound tag = getOrCreateTripData(((PersistenceTripPlayer) player).player());
        tag.removeTag(key);
    }

    @Override
    public void sendMessage(TripPlayer player, String message, String color) {
        ((PersistenceTripPlayer) player).player().sendMessage(new net.minecraft.util.text.TextComponentString(message));
    }

    @Override
    public void applyEffect(TripPlayer player, EffectSpec effect) {
        EntityPlayerMP serverPlayer = ((PersistenceTripPlayer) player).player();
        Potion potion = Potion.getPotionFromResourceLocation(effect.getId());
        if (potion == null) {
            log("[TRIP][PERSIST][EFFECTMISS] " + effect.getId());
            return;
        }

        serverPlayer.addPotionEffect(new PotionEffect(
            potion,
            effect.getSeconds() * 20,
            effect.getAmplifier(),
            false,
            !effect.isHideParticles()
        ));
    }

    @Override
    public void clearEffect(TripPlayer player, String effectId) {
        EntityPlayerMP serverPlayer = ((PersistenceTripPlayer) player).player();
        Potion potion = Potion.getPotionFromResourceLocation(effectId);
        if (potion != null) {
            serverPlayer.removePotionEffect(potion);
        }
    }

    @Override
    public void spawnParticles(TripPlayer player, ParticleSpec particle) {
        // 1.12.2 particle spawning can be wired here later using world/server packet utilities.
        log("[TRIP][PERSIST][PARTICLE] " + particle.getId());
    }

    @Override
    public void playSound(TripPlayer player, SoundSpec sound) {
        EntityPlayerMP serverPlayer = ((PersistenceTripPlayer) player).player();
        serverPlayer.world.playSound(
            null,
            serverPlayer.posX,
            serverPlayer.posY,
            serverPlayer.posZ,
            net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation(sound.getId())),
            SoundCategory.PLAYERS,
            sound.getVolume(),
            sound.getPitch()
        );
    }

    @Override
    public void consumeHeldItem(TripPlayer player, int amount) {
        EntityPlayerMP serverPlayer = ((PersistenceTripPlayer) player).player();
        ItemStack stack = serverPlayer.getHeldItemMainhand();
        if (!stack.isEmpty()) {
            stack.shrink(amount);
        }
    }

    @Override
    public void log(String message) {
        System.out.println(message);
    }

    private static NBTTagCompound getOrCreateTripData(EntityPlayerMP player) {
        NBTTagCompound persisted = player.getEntityData();
        if (!persisted.hasKey("gregoriusdrugworks")) {
            persisted.setTag("gregoriusdrugworks", new NBTTagCompound());
        }
        return persisted.getCompoundTag("gregoriusdrugworks");
    }

    public static final class PersistenceTripPlayer implements TripPlayer {
        private final EntityPlayerMP player;

        public PersistenceTripPlayer(EntityPlayerMP player) {
            this.player = player;
        }

        public EntityPlayerMP player() {
            return player;
        }

        @Override
        public UUID getUuid() {
            return player.getUniqueID();
        }

        @Override
        public String getUsername() {
            return player.getName();
        }
    }
}