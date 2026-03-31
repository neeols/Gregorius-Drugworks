package com.wurtzitane.gregoriusdrugworkspersistence.pill;

import com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadRegistry;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoaderUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Generic payload-backed pill carrier.
 *
 * @author wurtzitane
 */
public final class ItemPayloadPill extends ItemPillBase {

    public ItemPayloadPill(PillItemDefinition definition) {
        super(definition);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (!PayloadLoaderUtil.hasPayload(held)) {
            return new ActionResult<>(EnumActionResult.FAIL, held);
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Nonnull
    @Override
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return PayloadLoaderUtil.hasPayload(stack) ? EnumRarity.UNCOMMON : EnumRarity.COMMON;
    }

    @Override
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return PayloadLoaderUtil.hasPayload(stack) || super.hasEffect(stack);
    }

    @Override
    protected void onPillConsumedServer(EntityPlayerMP player, ItemStack stack) {
        GregoriusDrugworksPayloadRegistry.ResolvedPayload payload = PayloadLoaderUtil.resolve(stack);
        if (payload != null) {
            GregoriusDrugworksPayloadRegistry.applyResolved(player, stack, payload);
        }
    }
}
