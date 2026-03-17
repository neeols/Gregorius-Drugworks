package com.wurtzitane.gregoriusdrugworkspersistence.inhalation.client;

import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.ItemInhalationConsumable;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketCancelInhalationSequence;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketStartInhalationSequence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class GregoriusDrugworksInhalationClientHooks {

    private static final Map<Integer, InhalationSequenceState> ACTIVE = new HashMap<>();
    private static boolean initialised = false;

    private GregoriusDrugworksInhalationClientHooks() {
    }

    public static void preInit() {
        if (initialised) {
            return;
        }
        initialised = true;

        MinecraftForge.EVENT_BUS.register(new GregoriusDrugworksInhalationClientHooks());
    }

    public static void startSequence(PacketStartInhalationSequence message) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world == null) {
            return;
        }

        InhalationSequenceState existing = ACTIVE.get(message.getPlayerEntityId());
        if (existing != null && existing.getSequenceId() > message.getSequenceId()) {
            return;
        }

        ACTIVE.put(
                message.getPlayerEntityId(),
                new InhalationSequenceState(
                        message.getPlayerEntityId(),
                        message.getItemId(),
                        message.getHand(),
                        minecraft.world.getTotalWorldTime(),
                        message.getDurationTicks(),
                        message.getInhaleStartTick(),
                        message.getInhaleEndTick(),
                        message.getExhaleStartTick(),
                        message.getExhaleEndTick(),
                        message.isLocalCameraNudge(),
                        message.getSequenceId()
                )
        );
    }

    public static void cancelSequence(PacketCancelInhalationSequence message) {
        InhalationSequenceState state = ACTIVE.get(message.getPlayerEntityId());
        if (state != null && state.getSequenceId() <= message.getSequenceId()) {
            ACTIVE.remove(message.getPlayerEntityId());
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world == null) {
            ACTIVE.clear();
            return;
        }

        long worldTime = minecraft.world.getTotalWorldTime();

        Iterator<Map.Entry<Integer, InhalationSequenceState>> iterator = ACTIVE.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, InhalationSequenceState> entry = iterator.next();
            InhalationSequenceState state = entry.getValue();

            Entity entity = minecraft.world.getEntityByID(state.getPlayerEntityId());
            if (!(entity instanceof EntityPlayer) || state.isExpired(worldTime)) {
                iterator.remove();
                continue;
            }

            if (entity == minecraft.player && state.isLocalCameraNudge() && minecraft.gameSettings.thirdPersonView == 0) {
                applyLocalCameraNudge((EntityPlayerSP) entity, state, worldTime);
            }
        }
    }

    private static void applyLocalCameraNudge(EntityPlayerSP player, InhalationSequenceState state, long worldTime) {
        InhalationDefinition definition = ItemInhalationConsumable.getDefinition(state.getItemId());
        if (definition == null) {
            return;
        }

        float glow = state.getGlow(worldTime, 1.0F, definition.getGlowMin(), definition.getGlowMax());
        float targetPitch = -4.0F - glow * 3.0F;
        player.rotationPitch = MathHelper.clamp(player.rotationPitch + (targetPitch - player.rotationPitch) * 0.15F, -89.9F, 89.9F);
        player.prevRotationPitch = player.rotationPitch;
    }
}
