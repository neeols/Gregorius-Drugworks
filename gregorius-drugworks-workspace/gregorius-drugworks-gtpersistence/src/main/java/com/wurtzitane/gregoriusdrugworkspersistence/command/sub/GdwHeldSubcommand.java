package com.wurtzitane.gregoriusdrugworkspersistence.command.sub;

import com.wurtzitane.gregoriusdrugworks.common.catalog.ContentCatalogEntry;
import com.wurtzitane.gregoriusdrugworkspersistence.catalog.GregoriusDrugworksContentCatalogs;
import com.wurtzitane.gregoriusdrugworkspersistence.command.CommandUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.command.GdwDevSubcommand;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.ItemInhalationConsumable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public final class GdwHeldSubcommand implements GdwDevSubcommand {

    @Override
    public String getName() {
        return "held";
    }

    @Override
    public String getUsage() {
        return "/gdwdev held [player]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = CommandUtil.resolvePlayer(server, sender, args, 0);
        ItemStack stack = player.getHeldItemMainhand();

        if (stack.isEmpty()) {
            CommandUtil.send(sender, player.getName() + " is not holding anything.");
            return;
        }

        String id = stack.getItem().getRegistryName() == null ? "<no_registry>" : stack.getItem().getRegistryName().toString();
        ContentCatalogEntry<?> entry = GregoriusDrugworksContentCatalogs.getItem(id);

        CommandUtil.send(sender, "Held item: " + id);
        if (entry != null) {
            CommandUtil.send(sender, "Catalog family: " + entry.getFamily().name().toLowerCase() + " | sample=" + entry.isSample());
        }

        if (stack.getItem() instanceof ItemInhalationConsumable) {
            NBTTagCompound tag = stack.getTagCompound();
            int uses = tag == null ? 0 : tag.getInteger("InhalationUses");
            InhalationDefinition def = ((ItemInhalationConsumable) stack.getItem()).getDefinition();
            int remaining = Math.max(0, def.getMaxUses() - uses);
            CommandUtil.send(sender, "Inhalation uses: " + uses + "/" + def.getMaxUses() + " | remaining=" + remaining);
        }

        if (stack.hasTagCompound()) {
            CommandUtil.send(sender, "NBT: " + stack.getTagCompound().toString());
        }
    }
}