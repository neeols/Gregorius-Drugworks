package com.wurtzitane.gregoriusdrugworkspersistence.trip;

import com.wurtzitane.gregoriusdrugworks.common.trip.model.EffectSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.ParticleSpec;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.SoundSpec;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;

import java.util.UUID;

public final class TripRuntime implements com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime {

    private static final String PERSISTENT_ROOT = "gregoriusdrugworks";

    private final MinecraftServer server;

    public TripRuntime(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public long getCurrentTick() {
        return server.getTickCounter();
    }

    @Override
    public com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime.TripPlayer resolvePlayer(UUID uuid, String username) {
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
    public long getPersistentLong(com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime.TripPlayer player, String key) {
        NBTTagCompound tag = getOrCreateTripData(((PersistenceTripPlayer) player).player());
        return tag.hasKey(key) ? tag.getLong(key) : 0L;
    }

    @Override
    public void setPersistentLong(com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime.TripPlayer player, String key, long value) {
        NBTTagCompound tag = getOrCreateTripData(((PersistenceTripPlayer) player).player());
        tag.setLong(key, value);
    }

    @Override
    public boolean getPersistentBoolean(com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime.TripPlayer player, String key) {
        NBTTagCompound tag = getOrCreateTripData(((PersistenceTripPlayer) player).player());
        return tag.hasKey(key) && tag.getBoolean(key);
    }

    @Override
    public void setPersistentBoolean(com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime.TripPlayer player, String key, boolean value) {
        NBTTagCompound tag = getOrCreateTripData(((PersistenceTripPlayer) player).player());
        tag.setBoolean(key, value);
    }

    @Override
    public void removePersistentKey(com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime.TripPlayer player, String key) {
        NBTTagCompound tag = getOrCreateTripData(((PersistenceTripPlayer) player).player());
        tag.removeTag(key);
    }

    @Override
    public void sendMessage(com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime.TripPlayer player, String message, String color) {
        ((PersistenceTripPlayer) player).player().sendMessage(new TextComponentString(message));
    }

    @Override
    public void applyEffect(com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime.TripPlayer player, EffectSpec effect) {
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
    public void executeTriggerBundle(
            com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime.TripPlayer player,
            String triggerBundleId
    ) {
        com.wurtzitane.gregoriusdrugworkspersistence.trigger.TriggerBundleRuntimeExecutor.executeById(
                ((PersistenceTripPlayer) player).player(),
                triggerBundleId
        );
    }

    @Override
    public void clearEffect(com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime.TripPlayer player, String effectId) {
        EntityPlayerMP serverPlayer = ((PersistenceTripPlayer) player).player();
        Potion potion = Potion.getPotionFromResourceLocation(effectId);
        if (potion != null) {
            serverPlayer.removePotionEffect(potion);
        }
    }

    @Override
    public void spawnParticles(com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime.TripPlayer player, ParticleSpec particle) {
        // 1.12.2 particle spawning can be wired here later using packets/world helpers.
        log("[TRIP][PERSIST][PARTICLE] " + particle.getId());
    }

    @Override
    public void playSound(com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime.TripPlayer player, SoundSpec sound) {
        EntityPlayerMP serverPlayer = ((PersistenceTripPlayer) player).player();
        SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(new ResourceLocation(sound.getId()));
        if (soundEvent == null) {
            log("[TRIP][PERSIST][SOUNDMISS] " + sound.getId());
            return;
        }

        serverPlayer.world.playSound(
                null,
                serverPlayer.posX,
                serverPlayer.posY,
                serverPlayer.posZ,
                soundEvent,
                SoundCategory.PLAYERS,
                sound.getVolume(),
                sound.getPitch()
        );
    }

    @Override
    public void consumeHeldItem(com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime.TripPlayer player, int amount) {
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
        if (!persisted.hasKey(PERSISTENT_ROOT)) {
            persisted.setTag(PERSISTENT_ROOT, new NBTTagCompound());
        }
        return persisted.getCompoundTag(PERSISTENT_ROOT);
    }

    public static final class PersistenceTripPlayer implements com.wurtzitane.gregoriusdrugworks.common.trip.runtime.TripRuntime.TripPlayer {
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