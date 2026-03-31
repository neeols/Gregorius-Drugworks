package com.wurtzitane.gregoriusdrugworkspersistence.pill;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber
public final class PillUseTracker {

    private static final String USE_ROOT = "GdwPillUse";
    private static final String SEQUENCE_ID_KEY = "SequenceId";
    private static final String ITEM_ID_KEY = "ItemId";

    private static final Map<UUID, PendingPillUse> PENDING = new ConcurrentHashMap<>();

    private PillUseTracker() {
    }

    public static boolean beginUse(EntityPlayerMP player, ItemStack stack, EnumHand hand, String itemId, int sequenceId, int durationTicks) {
        if (player == null || stack.isEmpty() || itemId == null || itemId.isEmpty()) {
            return false;
        }

        UUID uuid = player.getUniqueID();
        if (PENDING.containsKey(uuid)) {
            return false;
        }

        MinecraftServer server = player.getServer();
        long completeTick = (server == null ? 0L : server.getTickCounter()) + Math.max(1, durationTicks);

        NBTTagCompound root = getOrCreateUseRoot(stack);
        root.setInteger(SEQUENCE_ID_KEY, sequenceId);
        root.setString(ITEM_ID_KEY, itemId);

        PENDING.put(uuid, new PendingPillUse(sequenceId, itemId, hand, completeTick));
        return true;
    }

    static void clearUseData(ItemStack stack) {
        if (stack.isEmpty() || !stack.hasTagCompound()) {
            return;
        }

        NBTTagCompound tag = stack.getTagCompound();
        if (!tag.hasKey(USE_ROOT)) {
            return;
        }

        tag.removeTag(USE_ROOT);
        if (tag.isEmpty()) {
            stack.setTagCompound(null);
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || PENDING.isEmpty()) {
            return;
        }

        MinecraftServer server = net.minecraftforge.fml.common.FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server == null) {
            return;
        }

        long nowTick = server.getTickCounter();
        Iterator<Map.Entry<UUID, PendingPillUse>> iterator = PENDING.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, PendingPillUse> entry = iterator.next();
            PendingPillUse pending = entry.getValue();

            if (pending.completeTick > nowTick) {
                continue;
            }

            EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(entry.getKey());
            if (player == null || !player.isEntityAlive()) {
                iterator.remove();
                continue;
            }

            ItemStack trackedStack = findTrackedStack(player, pending);
            if (trackedStack.isEmpty() || !(trackedStack.getItem() instanceof ItemPillBase)) {
                iterator.remove();
                continue;
            }

            iterator.remove();
            ((ItemPillBase) trackedStack.getItem()).finishPendingUse(player, trackedStack);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            clearPending((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            clearPending((EntityPlayerMP) event.player);
        }
    }

    private static void clearPending(EntityPlayerMP player) {
        PENDING.remove(player.getUniqueID());

        for (ItemStack stack : player.inventory.mainInventory) {
            clearUseData(stack);
        }

        for (ItemStack stack : player.inventory.offHandInventory) {
            clearUseData(stack);
        }
    }

    private static ItemStack findTrackedStack(EntityPlayerMP player, PendingPillUse pending) {
        for (ItemStack stack : player.inventory.offHandInventory) {
            if (matches(stack, pending)) {
                return stack;
            }
        }

        for (ItemStack stack : player.inventory.mainInventory) {
            if (matches(stack, pending)) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    private static boolean matches(ItemStack stack, PendingPillUse pending) {
        if (stack.isEmpty() || !stack.hasTagCompound()) {
            return false;
        }

        NBTTagCompound tag = stack.getTagCompound();
        if (!tag.hasKey(USE_ROOT)) {
            return false;
        }

        NBTTagCompound root = tag.getCompoundTag(USE_ROOT);
        return root.getInteger(SEQUENCE_ID_KEY) == pending.sequenceId
                && pending.itemId.equals(root.getString(ITEM_ID_KEY));
    }

    private static NBTTagCompound getOrCreateUseRoot(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();
        if (!tag.hasKey(USE_ROOT)) {
            tag.setTag(USE_ROOT, new NBTTagCompound());
        }

        return tag.getCompoundTag(USE_ROOT);
    }

    private static final class PendingPillUse {
        private final int sequenceId;
        private final String itemId;
        private final EnumHand hand;
        private final long completeTick;

        private PendingPillUse(int sequenceId, String itemId, EnumHand hand, long completeTick) {
            this.sequenceId = sequenceId;
            this.itemId = itemId;
            this.hand = hand;
            this.completeTick = completeTick;
        }
    }
}
