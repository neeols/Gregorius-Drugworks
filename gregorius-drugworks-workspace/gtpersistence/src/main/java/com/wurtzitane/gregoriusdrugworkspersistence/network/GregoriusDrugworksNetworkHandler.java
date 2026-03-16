package com.wurtzitane.gregoriusdrugworkspersistence.network;

import com.wurtzitane.gregoriusdrugworks.common.visual.VisualEffectProfile;
import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.medical.ItemMedicalApplicator;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketCancelApplicatorAnimation;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketCancelInhalationSequence;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketStartApplicatorAnimation;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketStartInhalationSequence;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketStartPillAnimation;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketStartVisualEffect;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketStopVisualEffect;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.ItemPillBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class GregoriusDrugworksNetworkHandler {

    private static final String INHALATION_SEQUENCE_KEY = "InhalationSequence";
    private static final String APPLICATOR_USE_ROOT = "GdwApplicatorUse";
    private static final String APPLICATOR_SEQUENCE_KEY = "SequenceId";

    private static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MOD_ID);
    private static final Map<UUID, Integer> VISUAL_SEQUENCES = new ConcurrentHashMap<>();

    private static boolean initialised = false;

    private GregoriusDrugworksNetworkHandler() {
    }

    public static void onConstruction() {
        if (initialised) {
            return;
        }

        CHANNEL.registerMessage(PacketStartPillAnimation.Handler.class, PacketStartPillAnimation.class, 0, Side.CLIENT);
        CHANNEL.registerMessage(PacketStartInhalationSequence.Handler.class, PacketStartInhalationSequence.class, 1, Side.CLIENT);
        CHANNEL.registerMessage(PacketCancelInhalationSequence.Handler.class, PacketCancelInhalationSequence.class, 2, Side.CLIENT);
        CHANNEL.registerMessage(PacketStartApplicatorAnimation.Handler.class, PacketStartApplicatorAnimation.class, 3, Side.CLIENT);
        CHANNEL.registerMessage(PacketCancelApplicatorAnimation.Handler.class, PacketCancelApplicatorAnimation.class, 4, Side.CLIENT);
        CHANNEL.registerMessage(PacketStartVisualEffect.Handler.class, PacketStartVisualEffect.class, 5, Side.CLIENT);
        CHANNEL.registerMessage(PacketStopVisualEffect.Handler.class, PacketStopVisualEffect.class, 6, Side.CLIENT);

        initialised = true;
    }

    public static void sendPillUseAnimation(EntityPlayerMP player, ItemPillBase pillItem, EnumHand hand, int sequenceId) {
        PacketStartPillAnimation packet = new PacketStartPillAnimation(player.getEntityId(), hand, pillItem.getDefinition(), sequenceId);
        CHANNEL.sendToAllAround(packet, new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 64.0D));
        CHANNEL.sendTo(packet, player);
    }

    public static void sendInhalationStart(EntityPlayerMP player, com.wurtzitane.gregoriusdrugworkspersistence.inhalation.ItemInhalationConsumable item, EnumHand hand, int sequenceId) {
        PacketStartInhalationSequence packet = new PacketStartInhalationSequence(player.getEntityId(), hand, item.getDefinition(), sequenceId);
        CHANNEL.sendToAllAround(packet, new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 64.0D));
        CHANNEL.sendTo(packet, player);
    }

    public static void sendInhalationCancel(EntityPlayerMP player, ItemStack stack, boolean completed) {
        int sequenceId = readInhalationSequenceId(stack);
        if (sequenceId <= 0) {
            return;
        }

        PacketCancelInhalationSequence packet = new PacketCancelInhalationSequence(player.getEntityId(), sequenceId, completed);
        CHANNEL.sendToAllAround(packet, new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 64.0D));
        CHANNEL.sendTo(packet, player);
    }

    public static void sendApplicatorStart(EntityPlayerMP player, ItemMedicalApplicator item, EnumHand hand, int sequenceId) {
        PacketStartApplicatorAnimation packet = new PacketStartApplicatorAnimation(
                player.getEntityId(),
                hand,
                item.getUseProfile(),
                true,
                sequenceId
        );

        CHANNEL.sendToAllAround(packet, new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 64.0D));
        CHANNEL.sendTo(packet, player);
    }

    public static void sendApplicatorCancel(EntityPlayerMP player, ItemStack stack, boolean completed) {
        int sequenceId = readApplicatorSequenceId(stack);
        if (sequenceId <= 0) {
            return;
        }

        PacketCancelApplicatorAnimation packet = new PacketCancelApplicatorAnimation(player.getEntityId(), sequenceId, completed);
        CHANNEL.sendToAllAround(packet, new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 64.0D));
        CHANNEL.sendTo(packet, player);
    }

    public static void sendVisualEffectStart(EntityPlayerMP player, VisualEffectProfile profile, int durationTicks) {
        int sequenceId = nextVisualSequence(player);
        CHANNEL.sendTo(new PacketStartVisualEffect(profile.getId(), durationTicks, sequenceId), player);
    }

    public static void sendVisualEffectStop(EntityPlayerMP player) {
        int sequenceId = nextVisualSequence(player);
        CHANNEL.sendTo(new PacketStopVisualEffect(sequenceId), player);
    }

    private static int nextVisualSequence(EntityPlayerMP player) {
        UUID uuid = player.getUniqueID();
        int next = VISUAL_SEQUENCES.containsKey(uuid) ? VISUAL_SEQUENCES.get(uuid) + 1 : 1;
        VISUAL_SEQUENCES.put(uuid, next);
        return next;
    }

    private static int readInhalationSequenceId(ItemStack stack) {
        if (stack.isEmpty() || !stack.hasTagCompound()) {
            return 0;
        }
        return stack.getTagCompound().getInteger(INHALATION_SEQUENCE_KEY);
    }

    private static int readApplicatorSequenceId(ItemStack stack) {
        if (stack.isEmpty() || !stack.hasTagCompound()) {
            return 0;
        }

        NBTTagCompound tag = stack.getTagCompound();
        if (!tag.hasKey(APPLICATOR_USE_ROOT)) {
            return 0;
        }

        return tag.getCompoundTag(APPLICATOR_USE_ROOT).getInteger(APPLICATOR_SEQUENCE_KEY);
    }
}