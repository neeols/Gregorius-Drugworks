package com.wurtzitane.gregoriusdrugworkspersistence.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class CommandUtil {

    private CommandUtil() {
    }

    public static void send(ICommandSender sender, String message) {
        sender.sendMessage(new TextComponentString(message));
    }

    public static EntityPlayerMP resolvePlayer(MinecraftServer server, ICommandSender sender, String[] args, int index) throws CommandException {
        if (args.length <= index) {
            if (sender.getCommandSenderEntity() instanceof EntityPlayerMP) {
                return (EntityPlayerMP) sender.getCommandSenderEntity();
            }
            throw new CommandException("Player argument required.");
        }

        EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(args[index]);
        if (player == null) {
            throw new CommandException("Unknown player: " + args[index]);
        }
        return player;
    }

    public static List<String> filterPrefix(Collection<String> values, String prefix) {
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();
        String lower = prefix == null ? "" : prefix.toLowerCase();

        for (String value : values) {
            if (value.toLowerCase().startsWith(lower)) {
                result.add(value);
            }
        }

        return result;
    }
}