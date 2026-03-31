package com.wurtzitane.gregoriusdrugworkspersistence.command.sub;

import com.wurtzitane.gregoriusdrugworkspersistence.command.CommandUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.command.GdwDevSubcommand;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.server.GregoriusDrugworksInhalationServerHooks;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

public final class GdwLingerSubcommand implements GdwDevSubcommand {

    @Override
    public String getName() {
        return "linger";
    }

    @Override
    public String getUsage() {
        return "/gdwdev linger [list|clear] [player|all]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        String mode = args.length == 0 ? "list" : args[0].toLowerCase();

        if ("list".equals(mode)) {
            List<String> lines = GregoriusDrugworksInhalationServerHooks.describeActive(server);
            if (lines.isEmpty()) {
                CommandUtil.send(sender, "No active lingering emissions.");
                return;
            }
            for (String line : lines) {
                CommandUtil.send(sender, line);
            }
            return;
        }

        if ("clear".equals(mode)) {
            if (args.length < 2 || "all".equalsIgnoreCase(args[1])) {
                GregoriusDrugworksInhalationServerHooks.clearAll();
                CommandUtil.send(sender, "Cleared all lingering emissions.");
                return;
            }

            EntityPlayerMP player = CommandUtil.resolvePlayer(server, sender, args, 1);
            GregoriusDrugworksInhalationServerHooks.clearForPlayer(player.getUniqueID());
            CommandUtil.send(sender, "Cleared lingering emissions for " + player.getName());
            return;
        }

        throw new CommandException(getUsage());
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return CommandUtil.filterPrefix(Arrays.asList("list", "clear"), args[0]);
        }

        if (args.length == 2 && "clear".equalsIgnoreCase(args[0])) {
            java.util.ArrayList<String> names = new java.util.ArrayList<>();
            names.add("all");
            for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
                names.add(player.getName());
            }
            return CommandUtil.filterPrefix(names, args[1]);
        }

        return GdwDevSubcommand.super.getTabCompletions(server, sender, args, pos);
    }
}