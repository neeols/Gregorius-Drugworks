package com.wurtzitane.gregoriusdrugworkspersistence.pill;

import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksCreativeTabs;
import com.wurtzitane.gregoriusdrugworkspersistence.network.GregoriusDrugworksNetworkHandler;
import com.wurtzitane.gregoriusdrugworkspersistence.trip.ITripUseDeferredItem;
import com.wurtzitane.gregoriusdrugworkspersistence.trip.TripHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ItemPillBase extends Item implements ITripUseDeferredItem {

    private static final Map<String, PillItemDefinition> DEFINITIONS = new LinkedHashMap<>();
    private static final Map<UUID, Integer> USE_SEQUENCES = new ConcurrentHashMap<>();

    private final PillItemDefinition definition;

    public ItemPillBase(PillItemDefinition definition) {
        this.definition = definition;
        this.setRegistryName(GregoriusDrugworksUtil.makeName(definition.getItemId()));
        GregoriusDrugworksUtil.setTranslationKeyCompat(this, Tags.MOD_ID + "." + definition.getItemId());
        GregoriusDrugworksUtil.setCreativeTabCompat(this, GregoriusDrugworksCreativeTabs.MAIN);
        GregoriusDrugworksUtil.setMaxStackSizeCompat(this, definition.getMaxStackSize());

        DEFINITIONS.put(definition.getItemId(), definition);
    }

    public PillItemDefinition getDefinition() {
        return definition;
    }

    public static PillItemDefinition getDefinition(String itemId) {
        return DEFINITIONS.get(itemId);
    }

    public static Collection<PillItemDefinition> getDefinitions() {
        return Collections.unmodifiableCollection(DEFINITIONS.values());
    }

    private static int nextSequenceId(EntityPlayerMP player) {
        UUID uuid = player.getUniqueID();
        int next = USE_SEQUENCES.getOrDefault(uuid, 0) + 1;
        USE_SEQUENCES.put(uuid, next);
        return next;
    }

    @Nonnull
    @Override
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return definition.getRarity();
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return EnumAction.NONE;
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return definition.getUseDurationTicks();
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);

        if (!world.isRemote && player instanceof EntityPlayerMP) {
            EntityPlayerMP serverPlayer = (EntityPlayerMP) player;
            if (TripHooks.isTripRunning(serverPlayer)) {
                return new ActionResult<>(EnumActionResult.FAIL, held);
            }
            int sequenceId = nextSequenceId(serverPlayer);
            if (!PillUseTracker.beginUse(serverPlayer, held, hand, resolveTripItemId(), sequenceId, definition.getUseDurationTicks())) {
                return new ActionResult<>(EnumActionResult.FAIL, held);
            }
            serverPlayer.getCooldownTracker().setCooldown(this, Math.max(1, definition.getUseDurationTicks()));
            GregoriusDrugworksNetworkHandler.sendPillUseAnimation(serverPlayer, held.copy(), hand, sequenceId);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, held);
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull EntityLivingBase entityLiving) {
        if (!world.isRemote && entityLiving instanceof EntityPlayerMP) {
            finishPendingUse((EntityPlayerMP) entityLiving, resolveUsedStack(entityLiving, stack));
        }

        return resolvePostUseStack(entityLiving, stack);
    }

    void finishPendingUse(EntityPlayerMP player, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }

        PillUseTracker.clearUseData(stack);
        playFinishSound(player);

        if (definition.isTripHookEnabled()) {
            TripHooks.onItemUse(player, resolveTripItemId());
        }

        onPillConsumedServer(player, stack);

        if (!player.capabilities.isCreativeMode) {
            stack.shrink(1);
        }

        player.inventory.markDirty();
        player.openContainer.detectAndSendChanges();
    }

    protected void onPillConsumedServer(EntityPlayerMP player, ItemStack stack) {
        // Extension point for future pill-specific behaviour.
    }

    private String resolveTripItemId() {
        return getRegistryName() != null ? getRegistryName().toString() : Tags.MOD_ID + ":" + definition.getItemId();
    }

    private void playFinishSound(EntityPlayerMP player) {
        SoundEvent finishSound = SoundEvent.REGISTRY.getObject(definition.getFinishSoundId());
        if (finishSound == null) {
            return;
        }

        player.world.playSound(
                null,
                player.posX,
                player.posY,
                player.posZ,
                finishSound,
                SoundCategory.PLAYERS,
                1.0F,
                1.0F
        );
    }

    private static ItemStack resolveUsedStack(EntityLivingBase entityLiving, ItemStack fallback) {
        if (!(entityLiving instanceof EntityPlayer)) {
            return fallback;
        }
        EntityPlayer player = (EntityPlayer) entityLiving;
        EnumHand hand = player.getActiveHand();
        if (hand == null) {
            return fallback;
        }
        ItemStack held = player.getHeldItem(hand);
        return held.isEmpty() ? fallback : held;
    }

    private static ItemStack resolvePostUseStack(EntityLivingBase entityLiving, ItemStack fallback) {
        if (!(entityLiving instanceof EntityPlayer)) {
            return fallback;
        }
        EntityPlayer player = (EntityPlayer) entityLiving;
        EnumHand hand = player.getActiveHand();
        if (hand == null) {
            return fallback;
        }
        return player.getHeldItem(hand);
    }
}
