package com.wurtzitane.gregoriusdrugworkspersistence.network.packet;

import com.wurtzitane.gregoriusdrugworkspersistence.visual.client.GregoriusDrugworksVisualClientHooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PacketStartVisualEffect implements IMessage {

    private String profileId;
    private int durationTicks;
    private int sequenceId;

    public PacketStartVisualEffect() {
    }

    public PacketStartVisualEffect(String profileId, int durationTicks, int sequenceId) {
        this.profileId = profileId;
        this.durationTicks = durationTicks;
        this.sequenceId = sequenceId;
    }

    public String getProfileId() {
        return profileId;
    }

    public int getDurationTicks() {
        return durationTicks;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.profileId = ByteBufUtils.readUTF8String(buf);
        this.durationTicks = buf.readInt();
        this.sequenceId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, profileId);
        buf.writeInt(durationTicks);
        buf.writeInt(sequenceId);
    }

    public static final class Handler implements IMessageHandler<PacketStartVisualEffect, IMessage> {
        @Override
        public IMessage onMessage(final PacketStartVisualEffect message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    GregoriusDrugworksVisualClientHooks.start(message);
                }
            });
            return null;
        }
    }
}