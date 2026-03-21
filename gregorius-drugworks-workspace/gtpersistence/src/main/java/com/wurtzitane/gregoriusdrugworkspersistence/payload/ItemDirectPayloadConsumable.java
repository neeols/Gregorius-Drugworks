package com.wurtzitane.gregoriusdrugworkspersistence.payload;

import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCompatibility;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadKeys;
import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksCreativeTabs;
import com.wurtzitane.gregoriusdrugworkspersistence.trip.TripHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Immediate right-click consumable that routes through the payload runtime.
 *
 * @author wurtzitane
 */
public class ItemDirectPayloadConsumable extends Item {

    private final String payloadId;
    private final String modeId;
    private final EnumRarity rarity;
    @Nullable
    private final ResourceLocation useSoundId;
    private final int cooldownTicks;

    public ItemDirectPayloadConsumable(String itemId,
                                       int maxStackSize,
                                       EnumRarity rarity,
                                       String payloadId,
                                       @Nullable String modeId,
                                       @Nullable ResourceLocation useSoundId,
                                       int cooldownTicks) {
        this.payloadId = payloadId;
        this.modeId = modeId == null ? null : modeId.trim();
        this.rarity = rarity;
        this.useSoundId = useSoundId;
        this.cooldownTicks = Math.max(0, cooldownTicks);

        setRegistryName(GregoriusDrugworksUtil.makeName(itemId));
        GregoriusDrugworksUtil.setTranslationKeyCompat(this, Tags.MOD_ID + "." + itemId);
        GregoriusDrugworksUtil.setCreativeTabCompat(this, GregoriusDrugworksCreativeTabs.MAIN);
        GregoriusDrugworksUtil.setMaxStackSizeCompat(this, maxStackSize);
    }

    @Nonnull
    @Override
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return rarity;
    }

    @Override
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return rarity == EnumRarity.RARE || rarity == EnumRarity.EPIC;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world,
                                                    @Nonnull EntityPlayer player,
                                                    @Nonnull EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (held.isEmpty()) {
            return new ActionResult<>(EnumActionResult.FAIL, held);
        }

        player.swingArm(hand);

        if (world.isRemote) {
            return new ActionResult<>(EnumActionResult.SUCCESS, held);
        }

        if (!(player instanceof EntityPlayerMP)) {
            return new ActionResult<>(EnumActionResult.FAIL, held);
        }

        EntityPlayerMP serverPlayer = (EntityPlayerMP) player;
        if (TripHooks.isTripRunning(serverPlayer)) {
            return new ActionResult<>(EnumActionResult.FAIL, held);
        }

        GregoriusDrugworksPayloadRegistry.ResolvedPayload payload = createResolvedPayload(held);
        if (payload == null) {
            return new ActionResult<>(EnumActionResult.FAIL, held);
        }

        playUseSound(world, serverPlayer);
        GregoriusDrugworksPayloadRegistry.applyResolved(serverPlayer, held, payload);

        if (!serverPlayer.capabilities.isCreativeMode) {
            held.shrink(1);
        }

        if (held.isEmpty()) {
            serverPlayer.setHeldItem(hand, ItemStack.EMPTY);
        }

        if (cooldownTicks > 0) {
            serverPlayer.getCooldownTracker().setCooldown(this, cooldownTicks);
        }

        serverPlayer.inventory.markDirty();
        serverPlayer.openContainer.detectAndSendChanges();
        return new ActionResult<>(EnumActionResult.SUCCESS, serverPlayer.getHeldItem(hand));
    }

    @Nullable
    private GregoriusDrugworksPayloadRegistry.ResolvedPayload createResolvedPayload(ItemStack template) {
        ItemStack loaded = template.copy();
        loaded.setCount(1);
        NBTTagCompound extraData = new NBTTagCompound();
        if (modeId != null && !modeId.isEmpty()) {
            extraData.setString(PayloadKeys.MODE_KEY, modeId);
        }

        boolean ok = PayloadLoaderUtil.loadIntoCarrier(
                loaded,
                PayloadCompatibility.UNIVERSAL,
                payloadId,
                1,
                extraData
        );
        return ok ? PayloadLoaderUtil.resolve(loaded) : null;
    }

    private void playUseSound(World world, EntityPlayer player) {
        if (useSoundId == null) {
            return;
        }

        SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(useSoundId);
        if (soundEvent == null) {
            return;
        }

        world.playSound(
                null,
                player.posX,
                player.posY,
                player.posZ,
                soundEvent,
                SoundCategory.PLAYERS,
                0.85F,
                1.05F
        );
    }
}
