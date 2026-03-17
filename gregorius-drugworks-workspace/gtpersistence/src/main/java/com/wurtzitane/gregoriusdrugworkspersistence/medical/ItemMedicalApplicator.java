package com.wurtzitane.gregoriusdrugworkspersistence.medical;

import com.wurtzitane.gregoriusdrugworks.common.medical.ApplicatorPayloadCategory;
import com.wurtzitane.gregoriusdrugworks.common.medical.ApplicatorUseProfile;
import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksCreativeTabs;
import com.wurtzitane.gregoriusdrugworkspersistence.network.GregoriusDrugworksNetworkHandler;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadRegistry;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCategory;
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadDefinition;
import com.wurtzitane.gregoriusdrugworkspersistence.trip.ITripUseDeferredItem;
import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class ItemMedicalApplicator extends Item implements ITripUseDeferredItem {

    private static final String USE_ROOT = "GdwApplicatorUse";
    private static final String SEQUENCE_ID_KEY = "SequenceId";
    private static final String COMPLETED_KEY = "Completed";

    private static final Map<UUID, Integer> USE_SEQUENCES = new ConcurrentHashMap<>();

    private final ApplicatorUseProfile useProfile;
    private final boolean consumeWhenDepleted;
    private final ResourceLocation startSoundId;
    private final ResourceLocation finishSoundId;
    private final ResourceLocation failSoundId;

    public ItemMedicalApplicator(
            String itemId,
            ApplicatorUseProfile useProfile,
            boolean consumeWhenDepleted,
            ResourceLocation startSoundId,
            ResourceLocation finishSoundId,
            ResourceLocation failSoundId
    ) {
        this.useProfile = useProfile;
        this.consumeWhenDepleted = consumeWhenDepleted;
        this.startSoundId = startSoundId;
        this.finishSoundId = finishSoundId;
        this.failSoundId = failSoundId;

        setRegistryName(GregoriusDrugworksUtil.makeName(itemId));
        setTranslationKey(Tags.MOD_ID + "." + itemId);
        setCreativeTab(GregoriusDrugworksCreativeTabs.MAIN);
        setMaxStackSize(1);
    }

    public ApplicatorUseProfile getUseProfile() {
        return useProfile;
    }

    public boolean isConsumeWhenDepleted() {
        return consumeWhenDepleted;
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return EnumAction.NONE;
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return useProfile.getTotalUseTicks();
    }

    @Nonnull
    @Override
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return GregoriusDrugworksApplicatorPayloads.hasPayload(stack) ? EnumRarity.UNCOMMON : EnumRarity.COMMON;
    }

    @Override
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return GregoriusDrugworksApplicatorPayloads.hasPayload(stack) || super.hasEffect(stack);
    }

    @Override
    public boolean showDurabilityBar(@Nonnull ItemStack stack) {
        com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadRegistry.ResolvedPayload payload = GregoriusDrugworksApplicatorPayloads.resolve(stack);
        return payload != null && payload.getMaxCharges() > 1;
    }

    @Override
    public double getDurabilityForDisplay(@Nonnull ItemStack stack) {
        com.wurtzitane.gregoriusdrugworkspersistence.payload.GregoriusDrugworksPayloadRegistry.ResolvedPayload payload = GregoriusDrugworksApplicatorPayloads.resolve(stack);
        if (payload == null || payload.getMaxCharges() <= 0) {
            return 0.0D;
        }

        double usedFraction = 1.0D - (payload.getCharges() / (double) payload.getMaxCharges());
        if (usedFraction < 0.0D) {
            return 0.0D;
        }
        if (usedFraction > 1.0D) {
            return 1.0D;
        }
        return usedFraction;
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> tooltip, @Nonnull ITooltipFlag flag) {
        GregoriusDrugworksPayloadRegistry.ResolvedPayload payload = GregoriusDrugworksApplicatorPayloads.resolve(stack);

        if (payload == null) {
            tooltip.add("§7Empty applicator");
            tooltip.add("§8Load a payload before use");
            return;
        }

        PayloadDefinition definition = payload.getDefinition();

        tooltip.add("§7Loaded applicator");
        tooltip.add("§7Payload: §f" + definition.getId());
        tooltip.add("§7Category: §f" + describePayloadCategory(definition));
        tooltip.add("§7Charges: §f" + payload.getCharges() + "/" + payload.getMaxCharges());

        if (definition.getTriggerBundleId() != null && !definition.getTriggerBundleId().isEmpty()) {
            tooltip.add("§8Trigger bundle: " + definition.getTriggerBundleId());
        }

        if (definition.getForwardItemId() != null && !definition.getForwardItemId().isEmpty()) {
            tooltip.add("§8Delivery: " + definition.getForwardItemId());
        }

        if (definition.getVisualProfileId() != null && !definition.getVisualProfileId().isEmpty()) {
            tooltip.add("§8Visual: " + definition.getVisualProfileId());
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);

        if (!GregoriusDrugworksApplicatorPayloads.hasPayload(held)) {
            if (!world.isRemote) {
                playSound(world, player, failSoundId);
            }
            return new ActionResult<>(EnumActionResult.FAIL, held);
        }

        player.setActiveHand(hand);

        if (!world.isRemote && player instanceof EntityPlayerMP) {
            EntityPlayerMP serverPlayer = (EntityPlayerMP) player;
            int sequenceId = nextSequenceId(serverPlayer);
            resetUseData(held, sequenceId);

            playSound(world, serverPlayer, startSoundId);
            GregoriusDrugworksNetworkHandler.sendApplicatorStart(serverPlayer, this, hand, sequenceId);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, held);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        if (world.isRemote || !(entityLiving instanceof EntityPlayerMP)) {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) entityLiving;
        float completion = (useProfile.getTotalUseTicks() - timeLeft) / (float) useProfile.getTotalUseTicks();

        if (completion >= 0.75F) {
            completeUse(player, stack);
            GregoriusDrugworksNetworkHandler.sendApplicatorCancel(player, stack, true);
        } else {
            GregoriusDrugworksNetworkHandler.sendApplicatorCancel(player, stack, false);
        }

        clearTransientUseData(stack);
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull EntityLivingBase entityLiving) {
        if (!world.isRemote && entityLiving instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entityLiving;
            completeUse(player, stack);
            GregoriusDrugworksNetworkHandler.sendApplicatorCancel(player, stack, true);
            clearTransientUseData(stack);
        }
        return resolvePostUseStack(entityLiving, stack);
    }

    private static String describePayloadCategory(PayloadDefinition definition) {
        PayloadCategory category = definition.getCategory();
        if (category == PayloadCategory.STAGED_EFFECT) {
            return "Psychedelic";
        }
        if (category == PayloadCategory.COSMETIC
                && ((definition.getTriggerBundleId() != null && !definition.getTriggerBundleId().isEmpty())
                || (definition.getVisualProfileId() != null && !definition.getVisualProfileId().isEmpty()))) {
            return "Psychedelic";
        }
        String raw = category.name().toLowerCase().replace('_', ' ');
        return Character.toUpperCase(raw.charAt(0)) + raw.substring(1);
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

    private void completeUse(EntityPlayerMP player, ItemStack applicatorStack) {
        NBTTagCompound useRoot = getOrCreateUseRoot(applicatorStack);
        if (useRoot.getBoolean(COMPLETED_KEY)) {
            return;
        }
        useRoot.setBoolean(COMPLETED_KEY, true);

        GregoriusDrugworksPayloadRegistry.ResolvedPayload payload = GregoriusDrugworksApplicatorPayloads.resolve(applicatorStack);
        if (payload == null) {
            return;
        }

        GregoriusDrugworksPayloadRegistry.applyResolved(player, applicatorStack, payload);

        playSound(player.world, player, finishSoundId);
        GregoriusDrugworksApplicatorPayloads.decrementChargeOrClear(applicatorStack, consumeWhenDepleted);
    }

    private void playSound(World world, EntityPlayer player, ResourceLocation soundId) {
        if (soundId == null) {
            return;
        }

        SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(soundId);
        if (soundEvent == null) {
            return;
        }

        world.playSound(null, player.posX, player.posY, player.posZ, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    private static int nextSequenceId(EntityPlayerMP player) {
        UUID uuid = player.getUniqueID();
        int next = USE_SEQUENCES.containsKey(uuid) ? USE_SEQUENCES.get(uuid) + 1 : 1;
        USE_SEQUENCES.put(uuid, next);
        return next;
    }

    private static void resetUseData(ItemStack stack, int sequenceId) {
        NBTTagCompound root = getOrCreateUseRoot(stack);
        root.setInteger(SEQUENCE_ID_KEY, sequenceId);
        root.setBoolean(COMPLETED_KEY, false);
    }

    private static void clearTransientUseData(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || !tag.hasKey(USE_ROOT)) {
            return;
        }
        NBTTagCompound root = tag.getCompoundTag(USE_ROOT);
        root.removeTag(SEQUENCE_ID_KEY);
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
}