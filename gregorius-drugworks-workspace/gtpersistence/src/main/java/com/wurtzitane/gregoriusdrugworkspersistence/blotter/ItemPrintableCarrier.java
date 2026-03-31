package com.wurtzitane.gregoriusdrugworkspersistence.blotter;

import com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadRegistry;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadLoaderUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.trip.TripHooks;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
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

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        GregoriusDrugworksPayloadRegistry.ResolvedPayload payload = PayloadLoaderUtil.resolve(stack);
        if (payload == null || !"blotter".equals(payload.getModeId())) {
            return super.getItemStackDisplayName(stack);
        }

        String payloadName = I18n.canTranslate(payload.getDefinition().getDisplayNameKey())
                ? I18n.translateToLocal(payload.getDefinition().getDisplayNameKey())
                : payload.getDefinition().getId();
        String soakedNameKey = carrierKind == PrintableCarrierKind.BLOTTER_PAPER
                ? "item.gregoriusdrugworkspersistence.blotter_paper.soaked.name"
                : "item.gregoriusdrugworkspersistence.single_tab.soaked.name";
        return I18n.canTranslate(soakedNameKey)
                ? I18n.translateToLocalFormatted(soakedNameKey, payloadName)
                : super.getItemStackDisplayName(stack);
    }

    private boolean isUsableCarrier(ItemStack stack) {
        return carrierKind == PrintableCarrierKind.SINGLE_TAB && PayloadLoaderUtil.hasPayload(stack);
    }

    @Nonnull
    @Override
    public net.minecraft.item.EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return net.minecraft.item.EnumAction.NONE;
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return 0;
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

        player.swingArm(hand);

        if (!world.isRemote && player instanceof EntityPlayerMP) {
            EntityPlayerMP serverPlayer = (EntityPlayerMP) player;
            GregoriusDrugworksPayloadRegistry.ResolvedPayload payload = PayloadLoaderUtil.resolve(held);
            if (payload == null) {
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, held);
            }

            GregoriusDrugworksPayloadRegistry.applyResolved(serverPlayer, held, payload);

            if (!serverPlayer.capabilities.isCreativeMode) {
                held.shrink(1);
            }

            if (held.isEmpty()) {
                serverPlayer.setHeldItem(hand, ItemStack.EMPTY);
            }

            serverPlayer.inventory.markDirty();
            serverPlayer.openContainer.detectAndSendChanges();
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, held);
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack,
                                     @Nonnull World world,
                                     @Nonnull EntityLivingBase entityLiving) {
        return stack;
    }
}
