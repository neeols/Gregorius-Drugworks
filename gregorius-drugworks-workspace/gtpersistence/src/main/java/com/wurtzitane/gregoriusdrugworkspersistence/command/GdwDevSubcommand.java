package com.wurtzitane.gregoriusdrugworkspersistence.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;

public interface GdwDevSubcommand {

    String getName();

    String getUsage();

    void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;

    default List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return Collections.emptyList();
    }
}