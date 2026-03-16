package com.wurtzitane.gregoriusdrugworkspersistence.network;

import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.network.packet.PacketStartPillAnimation;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.ItemPillBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class GregoriusDrugworksNetworkHandler {

    private static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MOD_ID);
    private static boolean initialised = false;

    private GregoriusDrugworksNetworkHandler() {
    }

    public static void onConstruction() {
        if (initialised) {
            return;
        }

        CHANNEL.registerMessage(
                PacketStartPillAnimation.Handler.class,
                PacketStartPillAnimation.class,
                0,
                Side.CLIENT
        );

        initialised = true;
    }

    public static void sendPillUseAnimation(EntityPlayerMP player, ItemPillBase pillItem, EnumHand hand, int sequenceId) {
        PacketStartPillAnimation packet = new PacketStartPillAnimation(
                player.getEntityId(),
                hand,
                pillItem.getDefinition(),
                sequenceId
        );

        CHANNEL.sendToAllAround(
                packet,
                new NetworkRegistry.TargetPoint(
                        player.dimension,
                        player.posX,
                        player.posY,
                        player.posZ,
                        64.0D
                )
        );

        CHANNEL.sendTo(packet, player);
    }
}