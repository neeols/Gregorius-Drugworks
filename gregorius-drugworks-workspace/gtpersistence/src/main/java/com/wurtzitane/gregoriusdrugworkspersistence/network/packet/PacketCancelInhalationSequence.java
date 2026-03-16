package com.wurtzitane.gregoriusdrugworkspersistence.network.packet;

import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.client.GregoriusDrugworksInhalationClientHooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PacketCancelInhalationSequence implements IMessage {

    private int playerEntityId;
    private int sequenceId;
    private boolean completed;

    public PacketCancelInhalationSequence() {
    }

    public PacketCancelInhalationSequence(int playerEntityId, int sequenceId, boolean completed) {
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

    public static final class Handler implements IMessageHandler<PacketCancelInhalationSequence, IMessage> {
        @Override
        public IMessage onMessage(PacketCancelInhalationSequence message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> GregoriusDrugworksInhalationClientHooks.cancelSequence(message));
            return null;
        }
    }
}