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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;

import java.util.Locale;
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
        TextComponentString component = new TextComponentString(message);
        TextFormatting formatting = resolveTextFormatting(color);
        if (formatting != null) {
            component.setStyle(new Style().setColor(formatting));
        }
        ((PersistenceTripPlayer) player).player().sendMessage(component);
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
        EntityPlayerMP serverPlayer = ((PersistenceTripPlayer) player).player();
        if (!(serverPlayer.world instanceof WorldServer)) {
            return;
        }

        EnumParticleTypes particleType = resolveParticleType(particle.getId());
        if (particleType == null) {
            log("[TRIP][PERSIST][PARTICLEMISS] " + particle.getId());
            return;
        }

        int count = clamp(particle.getCount(), 1, 160);
        double spread = Math.max(0.01D, particle.getSpread());
        double speed = Math.max(0.0D, particle.getSpeed());
        Vec3d origin = new Vec3d(
                serverPlayer.posX,
                serverPlayer.posY + (serverPlayer.getEyeHeight() * 0.55D),
                serverPlayer.posZ
        );

        ((WorldServer) serverPlayer.world).spawnParticle(
                particleType,
                true,
                origin.x,
                origin.y,
                origin.z,
                count,
                spread,
                spread * 0.75D,
                spread,
                speed
        );
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
        EnumHand hand = serverPlayer.isHandActive() ? serverPlayer.getActiveHand() : EnumHand.MAIN_HAND;
        ItemStack stack = serverPlayer.getHeldItem(hand);

        if (stack.isEmpty() && hand != EnumHand.MAIN_HAND) {
            hand = EnumHand.MAIN_HAND;
            stack = serverPlayer.getHeldItem(hand);
        }

        if (!stack.isEmpty()) {
            stack.shrink(amount);
            if (stack.isEmpty()) {
                serverPlayer.setHeldItem(hand, ItemStack.EMPTY);
            }
            serverPlayer.inventory.markDirty();
            serverPlayer.openContainer.detectAndSendChanges();
        }
    }

    @Override
    public void log(String message) {
        System.out.println(message);
    }

    private static TextFormatting resolveTextFormatting(String color) {
        if (color == null || color.trim().isEmpty()) {
            return null;
        }

        String normalized = color.trim()
                .replace('-', '_')
                .replace(' ', '_')
                .toUpperCase(Locale.ROOT);

        try {
            return TextFormatting.valueOf(normalized);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private static EnumParticleTypes resolveParticleType(String particleId) {
        if (particleId == null || particleId.trim().isEmpty()) {
            return null;
        }

        String raw = particleId.trim();
        String path = raw.indexOf(':') >= 0 ? raw.substring(raw.indexOf(':') + 1) : raw;
        EnumParticleTypes direct = EnumParticleTypes.getByName(path);
        if (direct != null) {
            return direct;
        }

        String normalizedInput = normalizeParticleKey(path);
        if ("enchant".equals(normalizedInput)) {
            normalizedInput = "enchantmenttable";
        }

        for (EnumParticleTypes particleType : EnumParticleTypes.values()) {
            if (normalizeParticleKey(particleType.getParticleName()).equals(normalizedInput)
                    || normalizeParticleKey(particleType.name()).equals(normalizedInput)) {
                return particleType;
            }
        }

        return null;
    }

    private static String normalizeParticleKey(String value) {
        StringBuilder builder = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                builder.append(Character.toLowerCase(c));
            }
        }
        return builder.toString();
    }

    private static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
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
