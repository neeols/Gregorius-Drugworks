package com.wurtzitane.gregoriusdrugworkspersistence.command.sub;

import com.wurtzitane.gregoriusdrugworkspersistence.command.CommandUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.command.GdwDevSubcommand;
import com.wurtzitane.gregoriusdrugworkspersistence.trip.TripDebugAccess;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

public final class GdwTripSubcommand implements GdwDevSubcommand {

    @Override
    public String getName() {
        return "trip";
    }

    @Override
    public String getUsage() {
        return "/gdwdev trip [show|clear] [player]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        String mode = args.length == 0 ? "show" : args[0].toLowerCase();
        EntityPlayerMP player = CommandUtil.resolvePlayer(server, sender, args, 1);

        if ("show".equals(mode)) {
            NBTTagCompound tag = TripDebugAccess.getSnapshot(player);
            CommandUtil.send(sender, "Trip state for " + player.getName() + ": " + tag.toString());
            return;
        }

        if ("clear".equals(mode)) {
            TripDebugAccess.clear(player);
            CommandUtil.send(sender, "Cleared trip state for " + player.getName());
            return;
        }

        throw new CommandException(getUsage());
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return CommandUtil.filterPrefix(Arrays.asList("show", "clear"), args[0]);
        }

        if (args.length == 2) {
            java.util.ArrayList<String> names = new java.util.ArrayList<>();
            for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
                names.add(player.getName());
            }
            return CommandUtil.filterPrefix(names, args[1]);
        }

        return GdwDevSubcommand.super.getTabCompletions(server, sender, args, pos);
    }
}