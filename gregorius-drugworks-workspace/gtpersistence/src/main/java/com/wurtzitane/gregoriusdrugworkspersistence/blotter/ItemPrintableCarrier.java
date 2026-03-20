package com.wurtzitane.gregoriusdrugworkspersistence.blotter;

import com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadRegistry;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoaderUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.trip.TripHooks;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Ordinary item that can carry printed-image data in NBT.
 *
 * @author wurtzitane
 */
public class ItemPrintableCarrier extends Item {

    private final PrintableCarrierKind carrierKind;

    public ItemPrintableCarrier(PrintableCarrierKind carrierKind) {
        this.carrierKind = carrierKind;
    }

    public PrintableCarrierKind getCarrierKind() {
        return carrierKind;
    }

    private boolean isUsableCarrier(ItemStack stack) {
        return carrierKind == PrintableCarrierKind.SINGLE_TAB && PayloadLoaderUtil.hasPayload(stack);
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return EnumAction.NONE;
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return isUsableCarrier(stack) ? 20 : 0;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world,
                                                    @Nonnull EntityPlayer player,
                                                    @Nonnull EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (!isUsableCarrier(held)) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, held);
        }

        if (!world.isRemote && player instanceof EntityPlayerMP
                && TripHooks.isTripRunning((EntityPlayerMP) player)) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, held);
        }

        player.setActiveHand(hand);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, held);
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack,
                                     @Nonnull World world,
                                     @Nonnull EntityLivingBase entityLiving) {
        if (!world.isRemote && entityLiving instanceof EntityPlayerMP && isUsableCarrier(stack)) {
            EntityPlayerMP player = (EntityPlayerMP) entityLiving;
            GregoriusDrugworksPayloadRegistry.ResolvedPayload payload = PayloadLoaderUtil.resolve(stack);
            if (payload != null) {
                GregoriusDrugworksPayloadRegistry.applyResolved(player, stack, payload);
            }

            if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
            player.inventory.markDirty();
            player.openContainer.detectAndSendChanges();
        }
        return stack;
    }
}
