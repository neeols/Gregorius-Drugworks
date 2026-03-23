package com.wurtzitane.gregoriusdrugworkspersistence.command.sub;

import com.wurtzitane.gregoriusdrugworks.common.catalog.ContentCatalogEntry;
import com.wurtzitane.gregoriusdrugworkspersistence.catalog.GregoriusDrugworksContentCatalogs;
import com.wurtzitane.gregoriusdrugworkspersistence.command.CommandUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.command.GdwDevSubcommand;
import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksInventoryUtil;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public final class GdwGiveSubcommand implements GdwDevSubcommand {

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getUsage() {
        return "/gdwdev give <catalogId> [player]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new CommandException(getUsage());
        }

        ContentCatalogEntry<Item> entry = GregoriusDrugworksContentCatalogs.getItem(args[0]);
        if (entry == null) {
            throw new CommandException("Unknown catalog item id: " + args[0]);
        }

        EntityPlayerMP target = CommandUtil.resolvePlayer(server, sender, args, 1);
        GregoriusDrugworksInventoryUtil.giveOrDrop(target, new ItemStack(entry.getPayload()), true);

        CommandUtil.send(sender, "Given " + entry.getId() + " to " + target.getName());
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            List<String> ids = new ArrayList<>();
            for (ContentCatalogEntry<Item> entry : GregoriusDrugworksContentCatalogs.allItems()) {
                ids.add(entry.getId());
            }
            return CommandUtil.filterPrefix(ids, args[0]);
        }

        if (args.length == 2) {
            List<String> players = new ArrayList<>();
            for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
                players.add(player.getName());
            }
            return CommandUtil.filterPrefix(players, args[1]);
        }

        return GdwDevSubcommand.super.getTabCompletions(server, sender, args, pos);
    }
}