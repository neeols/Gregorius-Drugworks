package com.wurtzitane.gregoriusdrugworkspersistence.network.packet;

import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.client.GregoriusDrugworksInhalationClientHooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PacketStartInhalationSequence implements IMessage {

    private int playerEntityId;
    private String itemId;
    private int handOrdinal;
    private int durationTicks;
    private int inhaleStartTick;
    private int inhaleEndTick;
    private int exhaleStartTick;
    private int exhaleEndTick;
    private boolean localCameraNudge;
    private int sequenceId;

    public PacketStartInhalationSequence() {
    }

    public PacketStartInhalationSequence(int playerEntityId, EnumHand hand, InhalationDefinition definition, int sequenceId) {
        this.playerEntityId = playerEntityId;
        this.itemId = definition.getItemId();
        this.handOrdinal = hand.ordinal();
        this.durationTicks = definition.getTotalUseTicks();
        this.inhaleStartTick = definition.getInhaleStartTick();
        this.inhaleEndTick = definition.getInhaleEndTick();
        this.exhaleStartTick = definition.getExhaleStartTick();
        this.exhaleEndTick = definition.getExhaleEndTick();
        this.localCameraNudge = definition.isLocalCameraNudge();
        this.sequenceId = sequenceId;
    }

    public int getPlayerEntityId() { return playerEntityId; }
    public String getItemId() { return itemId; }
    public EnumHand getHand() { return EnumHand.values()[handOrdinal]; }
    public int getDurationTicks() { return durationTicks; }
    public int getInhaleStartTick() { return inhaleStartTick; }
    public int getInhaleEndTick() { return inhaleEndTick; }
    public int getExhaleStartTick() { return exhaleStartTick; }
    public int getExhaleEndTick() { return exhaleEndTick; }
    public boolean isLocalCameraNudge() { return localCameraNudge; }
    public int getSequenceId() { return sequenceId; }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.playerEntityId = buf.readInt();
        this.itemId = ByteBufUtils.readUTF8String(buf);
        this.handOrdinal = buf.readInt();
        this.durationTicks = buf.readInt();
        this.inhaleStartTick = buf.readInt();
        this.inhaleEndTick = buf.readInt();
        this.exhaleStartTick = buf.readInt();
        this.exhaleEndTick = buf.readInt();
        this.localCameraNudge = buf.readBoolean();
        this.sequenceId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(playerEntityId);
        ByteBufUtils.writeUTF8String(buf, itemId);
        buf.writeInt(handOrdinal);
        buf.writeInt(durationTicks);
        buf.writeInt(inhaleStartTick);
        buf.writeInt(inhaleEndTick);
        buf.writeInt(exhaleStartTick);
        buf.writeInt(exhaleEndTick);
        buf.writeBoolean(localCameraNudge);
        buf.writeInt(sequenceId);
    }

    public static final class Handler implements IMessageHandler<PacketStartInhalationSequence, IMessage> {
        @Override
        public IMessage onMessage(PacketStartInhalationSequence message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> GregoriusDrugworksInhalationClientHooks.startSequence(message));
            return null;
        }
    }
}