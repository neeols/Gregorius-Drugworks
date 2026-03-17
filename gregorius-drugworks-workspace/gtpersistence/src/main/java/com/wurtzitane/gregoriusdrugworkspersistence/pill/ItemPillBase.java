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
        this.setTranslationKey(Tags.MOD_ID + "." + definition.getItemId());
        this.setCreativeTab(GregoriusDrugworksCreativeTabs.MAIN);
        this.setMaxStackSize(definition.getMaxStackSize());

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
        player.setActiveHand(hand);

        if (!world.isRemote && player instanceof EntityPlayerMP) {
            EntityPlayerMP serverPlayer = (EntityPlayerMP) player;
            int sequenceId = nextSequenceId(serverPlayer);
            GregoriusDrugworksNetworkHandler.sendPillUseAnimation(serverPlayer, this, hand, sequenceId);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, held);
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull EntityLivingBase entityLiving) {
        if (!world.isRemote && entityLiving instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entityLiving;

            SoundEvent finishSound = SoundEvent.REGISTRY.getObject(definition.getFinishSoundId());
            if (finishSound != null) {
                world.playSound(
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

            boolean tripHandled = false;
            if (definition.isTripHookEnabled() && getRegistryName() != null) {
                tripHandled = TripHooks.onItemUse(player, getRegistryName().toString());
            }

            onPillConsumedServer(player, stack);

            if (!player.capabilities.isCreativeMode && !tripHandled) {
                stack.shrink(1);
                if (stack.isEmpty() && player.getActiveHand() != null) {
                    player.setHeldItem(player.getActiveHand(), ItemStack.EMPTY);
                }
                player.inventory.markDirty();
                player.openContainer.detectAndSendChanges();
            }
        }

        return stack;
    }

    protected void onPillConsumedServer(EntityPlayerMP player, ItemStack stack) {
        // Extension point for future pill-specific behaviour.
    }
}