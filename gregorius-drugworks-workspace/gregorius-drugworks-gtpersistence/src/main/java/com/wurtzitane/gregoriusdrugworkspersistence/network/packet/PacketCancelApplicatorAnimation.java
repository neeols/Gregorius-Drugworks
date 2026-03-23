package com.wurtzitane.gregoriusdrugworkspersistence.network.packet;

import com.wurtzitane.gregoriusdrugworkspersistence.medical.client.GregoriusDrugworksApplicatorClientHooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PacketCancelApplicatorAnimation implements IMessage {

    private int playerEntityId;
    private int sequenceId;
    private boolean completed;

    public PacketCancelApplicatorAnimation() {
    }

    public PacketCancelApplicatorAnimation(int playerEntityId, int sequenceId, boolean completed) {
        this.playerEntityId = playerEntityId;
        this.sequenceId = sequenceId;
        this.completed = completed;
    }

    public int getPlayerEntityId() {
        return playerEntityId;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.playerEntityId = buf.readInt();
        this.sequenceId = buf.readInt();
        this.completed = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(playerEntityId);
        buf.writeInt(sequenceId);
        buf.writeBoolean(completed);
    }

    public static final class Handler implements IMessageHandler<PacketCancelApplicatorAnimation, IMessage> {
        @Override
        public IMessage onMessage(final PacketCancelApplicatorAnimation message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    GregoriusDrugworksApplicatorClientHooks.cancel(message);
                }
            });
            return null;
        }
    }
}