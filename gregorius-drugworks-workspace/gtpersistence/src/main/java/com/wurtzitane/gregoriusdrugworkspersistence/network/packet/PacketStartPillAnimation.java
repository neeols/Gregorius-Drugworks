package com.wurtzitane.gregoriusdrugworkspersistence.network.packet;

import com.wurtzitane.gregoriusdrugworkspersistence.pill.ItemPillBase;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.PillItemDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.client.GregoriusDrugworksPillClientHooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PacketStartPillAnimation implements IMessage {

    private int playerEntityId;
    private String itemId;
    private int handOrdinal;
    private int durationTicks;
    private float arcHeight;
    private float launchForward;
    private float mouthOffsetY;
    private float spinXPerTick;
    private float spinYPerTick;
    private float spinZPerTick;
    private boolean lockCamera;
    private int sequenceId;

    public PacketStartPillAnimation() {
    }

    public PacketStartPillAnimation(int playerEntityId, EnumHand hand, ItemPillBase pillItem, int sequenceId) {
        PillItemDefinition definition = pillItem.getDefinition();
        this.playerEntityId = playerEntityId;
        this.itemId = pillItem.getRegistryName() == null ? definition.getItemId() : pillItem.getRegistryName().toString();
        this.handOrdinal = hand.ordinal();
        this.durationTicks = definition.getUseDurationTicks();
        this.arcHeight = definition.getArcHeight();
        this.launchForward = definition.getLaunchForward();
        this.mouthOffsetY = definition.getMouthOffsetY();
        this.spinXPerTick = definition.getSpinXPerTick();
        this.spinYPerTick = definition.getSpinYPerTick();
        this.spinZPerTick = definition.getSpinZPerTick();
        this.lockCamera = definition.isLockCamera();
        this.sequenceId = sequenceId;
    }

    public int getPlayerEntityId() {
        return playerEntityId;
    }

    public String getItemId() {
        return itemId;
    }

    public EnumHand getHand() {
        return EnumHand.values()[handOrdinal];
    }

    public int getDurationTicks() {
        return durationTicks;
    }

    public float getArcHeight() {
        return arcHeight;
    }

    public float getLaunchForward() {
        return launchForward;
    }

    public float getMouthOffsetY() {
        return mouthOffsetY;
    }

    public float getSpinXPerTick() {
        return spinXPerTick;
    }

    public float getSpinYPerTick() {
        return spinYPerTick;
    }

    public float getSpinZPerTick() {
        return spinZPerTick;
    }

    public boolean isLockCamera() {
        return lockCamera;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.playerEntityId = buf.readInt();
        this.itemId = ByteBufUtils.readUTF8String(buf);
        this.handOrdinal = buf.readInt();
        this.durationTicks = buf.readInt();
        this.arcHeight = buf.readFloat();
        this.launchForward = buf.readFloat();
        this.mouthOffsetY = buf.readFloat();
        this.spinXPerTick = buf.readFloat();
        this.spinYPerTick = buf.readFloat();
        this.spinZPerTick = buf.readFloat();
        this.lockCamera = buf.readBoolean();
        this.sequenceId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(playerEntityId);
        ByteBufUtils.writeUTF8String(buf, itemId);
        buf.writeInt(handOrdinal);
        buf.writeInt(durationTicks);
        buf.writeFloat(arcHeight);
        buf.writeFloat(launchForward);
        buf.writeFloat(mouthOffsetY);
        buf.writeFloat(spinXPerTick);
        buf.writeFloat(spinYPerTick);
        buf.writeFloat(spinZPerTick);
        buf.writeBoolean(lockCamera);
        buf.writeInt(sequenceId);
    }

    public static final class Handler implements IMessageHandler<PacketStartPillAnimation, IMessage> {
        @Override
        public IMessage onMessage(PacketStartPillAnimation message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> GregoriusDrugworksPillClientHooks.startAnimation(message));
            return null;
        }
    }
}