package com.wurtzitane.gregoriusdrugworkspersistence.network.packet;

import com.wurtzitane.gregoriusdrugworkspersistence.visual.client.GregoriusDrugworksVisualClientHooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PacketStopVisualEffect implements IMessage {

    private int sequenceId;

    public PacketStopVisualEffect() {
    }

    public PacketStopVisualEffect(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.sequenceId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(sequenceId);
    }

    public static final class Handler implements IMessageHandler<PacketStopVisualEffect, IMessage> {
        @Override
        public IMessage onMessage(final PacketStopVisualEffect message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    GregoriusDrugworksVisualClientHooks.stop(message);
                }
            });
            return null;
        }
    }
}