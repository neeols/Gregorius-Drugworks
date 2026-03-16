package com.wurtzitane.gregoriusdrugworkspersistence.command;

import com.wurtzitane.gregoriusdrugworkspersistence.command.sub.GdwApplicatorSubcommand;
import com.wurtzitane.gregoriusdrugworkspersistence.command.sub.GdwCatalogSubcommand;
import com.wurtzitane.gregoriusdrugworkspersistence.command.sub.GdwGiveSubcommand;
import com.wurtzitane.gregoriusdrugworkspersistence.command.sub.GdwHeldSubcommand;
import com.wurtzitane.gregoriusdrugworkspersistence.command.sub.GdwLingerSubcommand;
import com.wurtzitane.gregoriusdrugworkspersistence.command.sub.GdwTripSubcommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CommandGregoriusDrugworksDev extends CommandBase {

    private final Map<String, GdwDevSubcommand> subcommands = new LinkedHashMap<>();

    public CommandGregoriusDrugworksDev() {
        register(new GdwCatalogSubcommand());
        register(new GdwGiveSubcommand());
        register(new GdwHeldSubcommand());
        register(new GdwLingerSubcommand());
        register(new GdwTripSubcommand());
        register(new GdwApplicatorSubcommand());
    }

    private void register(GdwDevSubcommand subcommand) {
        subcommands.put(subcommand.getName(), subcommand);
    }

    @Override
    public String getName() {
        return "gdwdev";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/gdwdev <catalog|give|held|linger|trip|applicator>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            CommandUtil.send(sender, getUsage(sender));
            for (GdwDevSubcommand subcommand : subcommands.values()) {
                CommandUtil.send(sender, " - " + subcommand.getUsage());
            }
            return;
        }

        GdwDevSubcommand subcommand = subcommands.get(args[0].toLowerCase());
        if (subcommand == null) {
            throw new CommandException("Unknown subcommand: " + args[0]);
        }

        String[] shifted = new String[Math.max(0, args.length - 1)];
        if (args.length > 1) {
            System.arraycopy(args, 1, shifted, 0, args.length - 1);
        }

        subcommand.execute(server, sender, shifted);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return CommandUtil.filterPrefix(subcommands.keySet(), args[0]);
        }

        GdwDevSubcommand subcommand = subcommands.get(args[0].toLowerCase());
        if (subcommand == null) {
            return new ArrayList<>();
        }

        String[] shifted = new String[Math.max(0, args.length - 1)];
        if (args.length > 1) {
            System.arraycopy(args, 1, shifted, 0, args.length - 1);
        }

        return subcommand.getTabCompletions(server, sender, shifted, targetPos);
    }
}