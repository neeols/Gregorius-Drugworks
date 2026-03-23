package com.wurtzitane.gregoriusdrugworkspersistence.command.sub;

import com.wurtzitane.gregoriusdrugworks.common.catalog.ContentCatalogEntry;
import com.wurtzitane.gregoriusdrugworks.common.catalog.ContentCatalogFormatters;
import com.wurtzitane.gregoriusdrugworks.common.catalog.ContentFamily;
import com.wurtzitane.gregoriusdrugworkspersistence.catalog.GregoriusDrugworksContentCatalogs;
import com.wurtzitane.gregoriusdrugworkspersistence.command.CommandUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.command.GdwDevSubcommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

public final class GdwCatalogSubcommand implements GdwDevSubcommand {

    @Override
    public String getName() {
        return "catalog";
    }

    @Override
    public String getUsage() {
        return "/gdwdev catalog [all|basic|antidote|pill|inhalation|applicator]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0 || "all".equalsIgnoreCase(args[0])) {
            for (ContentCatalogEntry<Item> entry : GregoriusDrugworksContentCatalogs.allItems()) {
                CommandUtil.send(sender, ContentCatalogFormatters.describeEntry(entry));
            }
            return;
        }

        ContentFamily family;
        try {
            family = ContentFamily.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new CommandException("Unknown family: " + args[0]);
        }

        for (ContentCatalogEntry<Item> entry : GregoriusDrugworksContentCatalogs.byFamily(family)) {
            CommandUtil.send(sender, ContentCatalogFormatters.describeEntry(entry));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return CommandUtil.filterPrefix(
                    Arrays.asList("all", "basic", "antidote", "pill", "inhalation", "applicator"),
                    args[0]
            );
        }
        return GdwDevSubcommand.super.getTabCompletions(server, sender, args, pos);
    }
}