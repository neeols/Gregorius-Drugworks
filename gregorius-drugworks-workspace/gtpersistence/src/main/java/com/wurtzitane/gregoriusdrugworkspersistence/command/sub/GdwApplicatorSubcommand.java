package com.wurtzitane.gregoriusdrugworkspersistence.command.sub;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.command.CommandUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.command.GdwDevSubcommand;
import com.wurtzitane.gregoriusdrugworkspersistence.medical.GregoriusDrugworksApplicatorPayloads;
import com.wurtzitane.gregoriusdrugworkspersistence.medical.GregoriusDrugworksMedicalApplicators;
import com.wurtzitane.gregoriusdrugworkspersistence.medical.ItemMedicalApplicator;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class GdwApplicatorSubcommand implements GdwDevSubcommand {

    @Override
    public String getName() {
        return "applicator";
    }

    @Override
    public String getUsage() {
        return "/gdwdev applicator <payloads|inspect|load|clear>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            throw new CommandException(getUsage());
        }

        String mode = args[0].toLowerCase();

        if ("payloads".equals(mode)) {
            for (PayloadDefinition def : GregoriusDrugworksApplicatorPayloads.allDefinitions()) {
                CommandUtil.send(
                        sender,
                        def.getId()
                                + " | category=" + def.getCategory().name().toLowerCase()
                                + " | compatibility=" + def.getCompatibility().name().toLowerCase()
                                + " | charges=" + def.getDefaultCharges()
                                + " | triggerBundle=" + def.getTriggerBundleId()
                );
            }
            return;
        }

        if ("inspect".equals(mode)) {
            EntityPlayerMP player = CommandUtil.resolvePlayer(server, sender, args, 1);
            ItemStack held = player.getHeldItemMainhand();

            if (held.isEmpty() || !(held.getItem() instanceof ItemMedicalApplicator)) {
                CommandUtil.send(sender, player.getName() + " is not holding a medical applicator.");
                return;
            }

            CommandUtil.send(sender, GregoriusDrugworksApplicatorPayloads.describeResolved(held));
            if (held.hasTagCompound()) {
                CommandUtil.send(sender, "NBT: " + held.getTagCompound().toString());
            }
            return;
        }

        if ("load".equals(mode)) {
            if (args.length < 2) {
                throw new CommandException("/gdwdev applicator load <payloadId> [charges] [player]");
            }

            String payloadId = args[1];
            int charges = 0;
            int playerArgIndex = 2;

            if (args.length >= 3) {
                try {
                    charges = Integer.parseInt(args[2]);
                    playerArgIndex = 3;
                } catch (NumberFormatException ignored) {
                    charges = 0;
                    playerArgIndex = 2;
                }
            }

            EntityPlayerMP player = CommandUtil.resolvePlayer(server, sender, args, playerArgIndex);
            ItemStack held = player.getHeldItemMainhand();

            if (!held.isEmpty() && held.getItem() instanceof ItemMedicalApplicator) {
                boolean ok = GregoriusDrugworksApplicatorPayloads.loadPayloadIntoApplicator(held, payloadId, charges, null);
                if (!ok) {
                    throw new CommandException("Unknown payload id: " + payloadId);
                }
                CommandUtil.send(sender, "Loaded held applicator for " + player.getName() + " with " + payloadId);
                return;
            }

            ItemStack loaded = GregoriusDrugworksApplicatorPayloads.createLoadedApplicatorStack(
                    GregoriusDrugworksMedicalApplicators.MEDICAL_APPLICATOR,
                    payloadId,
                    charges,
                    (NBTTagCompound) null
            );
            player.inventory.addItemStackToInventory(loaded);
            CommandUtil.send(sender, "Gave loaded applicator to " + player.getName() + " with " + payloadId);
            return;
        }

        if ("clear".equals(mode)) {
            EntityPlayerMP player = CommandUtil.resolvePlayer(server, sender, args, 1);
            ItemStack held = player.getHeldItemMainhand();

            if (held.isEmpty() || !(held.getItem() instanceof ItemMedicalApplicator)) {
                CommandUtil.send(sender, player.getName() + " is not holding a medical applicator.");
                return;
            }

            GregoriusDrugworksApplicatorPayloads.clearPayload(held);
            CommandUtil.send(sender, "Cleared applicator payload for " + player.getName());
            return;
        }

        throw new CommandException(getUsage());
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return CommandUtil.filterPrefix(Arrays.asList("payloads", "inspect", "load", "clear"), args[0]);
        }

        if (args.length == 2 && "load".equalsIgnoreCase(args[0])) {
            return CommandUtil.filterPrefix(GregoriusDrugworksApplicatorPayloads.allPayloadIds(), args[1]);
        }

        if (args.length >= 2 && ("inspect".equalsIgnoreCase(args[0]) || "clear".equalsIgnoreCase(args[0]))) {
            List<String> players = new ArrayList<>();
            for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
                players.add(player.getName());
            }
            return CommandUtil.filterPrefix(players, args[1]);
        }

        if (args.length >= 3 && "load".equalsIgnoreCase(args[0])) {
            List<String> players = new ArrayList<>();
            for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
                players.add(player.getName());
            }
            return CommandUtil.filterPrefix(players, args[args.length - 1]);
        }

        return GdwDevSubcommand.super.getTabCompletions(server, sender, args, pos);
    }
}