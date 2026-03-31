package com.wurtzitane.gregoriusdrugworkspersistence.network.packet;

import com.wurtzitane.gregoriusdrugworks.common.medical.ApplicatorUseProfile;
import com.wurtzitane.gregoriusdrugworkspersistence.medical.client.GregoriusDrugworksApplicatorClientHooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PacketStartApplicatorAnimation implements IMessage {

    private int playerEntityId;
    private int handOrdinal;
    private int totalUseTicks;
    private int raiseEndTick;
    private int applyStartTick;
    private int applyEndTick;
    private int holdEndTick;
    private int finishTick;
    private boolean localCameraPolish;
    private int sequenceId;

    public PacketStartApplicatorAnimation() {
    }

    public PacketStartApplicatorAnimation(int playerEntityId, EnumHand hand, ApplicatorUseProfile profile, boolean localCameraPolish, int sequenceId) {
        this.playerEntityId = playerEntityId;
        this.handOrdinal = hand.ordinal();
        this.totalUseTicks = profile.getTotalUseTicks();
        this.raiseEndTick = profile.getRaiseEndTick();
        this.applyStartTick = profile.getApplyStartTick();
        this.applyEndTick = profile.getApplyEndTick();
        this.holdEndTick = profile.getHoldEndTick();
        this.finishTick = profile.getFinishTick();
        this.localCameraPolish = localCameraPolish;
        this.sequenceId = sequenceId;
    }

    public int getPlayerEntityId() {
        return playerEntityId;
    }

    public EnumHand getHand() {
        return EnumHand.values()[handOrdinal];
    }

    public int getTotalUseTicks() {
        return totalUseTicks;
    }

    public int getRaiseEndTick() {
        return raiseEndTick;
    }

    public int getApplyStartTick() {
        return applyStartTick;
    }

    public int getApplyEndTick() {
        return applyEndTick;
    }

    public int getHoldEndTick() {
        return holdEndTick;
    }

    public int getFinishTick() {
        return finishTick;
    }

    public boolean isLocalCameraPolish() {
        return localCameraPolish;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.playerEntityId = buf.readInt();
        this.handOrdinal = buf.readInt();
        this.totalUseTicks = buf.readInt();
        this.raiseEndTick = buf.readInt();
        this.applyStartTick = buf.readInt();
        this.applyEndTick = buf.readInt();
        this.holdEndTick = buf.readInt();
        this.finishTick = buf.readInt();
        this.localCameraPolish = buf.readBoolean();
        this.sequenceId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(playerEntityId);
        buf.writeInt(handOrdinal);
        buf.writeInt(totalUseTicks);
        buf.writeInt(raiseEndTick);
        buf.writeInt(applyStartTick);
        buf.writeInt(applyEndTick);
        buf.writeInt(holdEndTick);
        buf.writeInt(finishTick);
        buf.writeBoolean(localCameraPolish);
        buf.writeInt(sequenceId);
    }

    public static final class Handler implements IMessageHandler<PacketStartApplicatorAnimation, IMessage> {
        @Override
        public IMessage onMessage(final PacketStartApplicatorAnimation message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    GregoriusDrugworksApplicatorClientHooks.start(message);
                }
            });
            return null;
        }
    }
}