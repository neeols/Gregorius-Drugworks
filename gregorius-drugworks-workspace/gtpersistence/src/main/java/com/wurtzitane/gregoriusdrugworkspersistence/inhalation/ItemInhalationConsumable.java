package com.wurtzitane.gregoriusdrugworkspersistence.inhalation;

import com.wurtzitane.gregoriusdrugworks.common.debug.DebugFormatters;
import com.wurtzitane.gregoriusdrugworks.common.debug.GdwDebugCategory;
import com.wurtzitane.gregoriusdrugworks.common.inhalation.InhalationMath;
import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.debug.GregoriusDrugworksDebug;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksCreativeTabs;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.server.GregoriusDrugworksInhalationServerHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.network.GregoriusDrugworksNetworkHandler;
import com.wurtzitane.gregoriusdrugworkspersistence.trip.ITripUseDeferredItem;
import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksInventoryUtil;
import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ItemInhalationConsumable extends Item implements ITripUseDeferredItem {

    private static final String NBT_USES = "InhalationUses";
    private static final String NBT_SEQUENCE = "InhalationSequence";
    private static final String NBT_FLAGS = "InhalationFlags";
    private static final String NBT_COMPLETED = "InhalationCompleted";
    private static final String NBT_HAND = "InhalationHand";

    private static final int FLAG_INHALE_START = 1;
    private static final int FLAG_INHALE_COMPLETE = 2;
    private static final int FLAG_EXHALE_START = 4;

    private static final Map<String, InhalationDefinition> DEFINITIONS = new LinkedHashMap<>();
    private static final Map<UUID, Integer> USE_SEQUENCES = new ConcurrentHashMap<>();

    private final InhalationDefinition definition;

    public ItemInhalationConsumable(InhalationDefinition definition) {
        this.definition = definition;
        this.setRegistryName(GregoriusDrugworksUtil.makeName(definition.getItemId()));
        this.setTranslationKey(Tags.MOD_ID + "." + definition.getItemId());
        this.setCreativeTab(GregoriusDrugworksCreativeTabs.MAIN);
        this.setMaxStackSize(1);
        this.setMaxDamage(definition.getMaxUses());

        DEFINITIONS.put(definition.getItemId(), definition);
    }

    public static InhalationDefinition getDefinition(String itemId) {
        return DEFINITIONS.get(itemId);
    }

    public static Collection<InhalationDefinition> getDefinitions() {
        return Collections.unmodifiableCollection(DEFINITIONS.values());
    }

    public InhalationDefinition getDefinition() {
        return definition;
    }

    @Nonnull
    @Override
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return definition.getRarity();
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return definition.getTotalUseTicks();
    }

    @Override
    public boolean showDurabilityBar(@Nonnull ItemStack stack) {
        return getUses(stack) > 0;
    }

    @Override
    public double getDurabilityForDisplay(@Nonnull ItemStack stack) {
        if (definition.getMaxUses() <= 0) {
            return 0.0D;
        }
        double used = getUses(stack);
        double max = definition.getMaxUses();
        if (used <= 0.0D) {
            return 0.0D;
        }
        return Math.min(1.0D, used / max);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);

        if (!player.capabilities.isCreativeMode && getUses(held) >= definition.getMaxUses()) {
            if (!world.isRemote && player instanceof EntityPlayerMP) {
                playWorldSound(world, player, definition.getExhaustedSoundId());
                replaceSpentHeldStack((EntityPlayerMP) player, hand, held);
            }
            return new ActionResult<>(EnumActionResult.FAIL, player.getHeldItem(hand));
        }

        player.setActiveHand(hand);

        if (!world.isRemote && player instanceof EntityPlayerMP) {
            EntityPlayerMP serverPlayer = (EntityPlayerMP) player;
            int sequenceId = nextSequenceId(serverPlayer);
            resetSequenceData(held, sequenceId, hand);

            definition.getEffectHandler().onPhase(serverPlayer, held, definition, InhalationUsePhase.USE_START, false);
            GregoriusDrugworksNetworkHandler.sendInhalationStart(serverPlayer, this, hand, sequenceId);
            playWorldSound(world, serverPlayer, definition.getStartSoundId());
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, held);
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase entityLiving, int count) {
        if (!(entityLiving instanceof EntityPlayerMP)) {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) entityLiving;
        int elapsed = definition.getTotalUseTicks() - count;
        int flags = getFlags(stack);

        if (elapsed >= definition.getInhaleStartTick() && (flags & FLAG_INHALE_START) == 0) {
            setFlags(stack, flags | FLAG_INHALE_START);
            definition.getEffectHandler().onPhase(player, stack, definition, InhalationUsePhase.INHALE_START, false);
            playWorldSound(player.world, player, definition.getInhaleSoundId());
            spawnParticleSpecs((WorldServer) player.world, player, definition.getInhaleParticles());
        }

        flags = getFlags(stack);
        if (elapsed >= definition.getInhaleEndTick() && (flags & FLAG_INHALE_COMPLETE) == 0) {
            setFlags(stack, flags | FLAG_INHALE_COMPLETE);
            definition.getEffectHandler().onPhase(player, stack, definition, InhalationUsePhase.INHALE_COMPLETE, false);
        }

        flags = getFlags(stack);
        if (elapsed >= definition.getExhaleStartTick() && (flags & FLAG_EXHALE_START) == 0) {
            setFlags(stack, flags | FLAG_EXHALE_START);
            definition.getEffectHandler().onPhase(player, stack, definition, InhalationUsePhase.EXHALE_START, false);
            playWorldSound(player.world, player, definition.getExhaleSoundId());
            spawnParticleSpecs((WorldServer) player.world, player, definition.getExhaleParticles());
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        if (world.isRemote || !(entityLiving instanceof EntityPlayerMP)) {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) entityLiving;
        float completionRatio = InhalationMath.completionRatio(definition.getTotalUseTicks(), timeLeft);
        boolean shouldComplete = completionRatio >= definition.getMinimumCompletionRatio() || definition.isConsumeOnInterrupt();

        GregoriusDrugworksDebug.log(
                GdwDebugCategory.INHALATION,
                DebugFormatters.join(
                        "[INHALE][STOP]",
                        DebugFormatters.kv("player", player.getName()),
                        DebugFormatters.kv("item", definition.getItemId()),
                        DebugFormatters.kv("completionRatio", completionRatio),
                        DebugFormatters.kv("complete", shouldComplete)
                )
        );

        if (shouldComplete) {
            finishUseServer(player, stack);
            GregoriusDrugworksNetworkHandler.sendInhalationCancel(player, stack, true);
        } else {
            definition.getEffectHandler().onPhase(player, stack, definition, InhalationUsePhase.INTERRUPTED, false);
            GregoriusDrugworksNetworkHandler.sendInhalationCancel(player, stack, false);
        }

        clearTransientSequenceData(stack);
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull EntityLivingBase entityLiving) {
        if (!world.isRemote && entityLiving instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entityLiving;
            finishUseServer(player, stack);
            GregoriusDrugworksNetworkHandler.sendInhalationCancel(player, stack, true);
            clearTransientSequenceData(stack);
        }
        return resolvePostUseStack(entityLiving, stack);
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

    private void finishUseServer(EntityPlayerMP player, ItemStack stack) {
        NBTTagCompound tag = getOrCreateTag(stack);
        if (tag.getBoolean(NBT_COMPLETED)) {
            return;
        }
        tag.setBoolean(NBT_COMPLETED, true);

        int currentUses = getUses(stack);
        if (!player.capabilities.isCreativeMode && currentUses >= definition.getMaxUses()) {
            replaceSpentHeldStack(player, readTrackedHand(stack), stack);
            return;
        }
        int loss = resolveDurabilityLoss(currentUses, player.getRNG());
        int newUses = currentUses + loss;
        boolean exhausted = newUses >= definition.getMaxUses();

        setUses(stack, Math.min(newUses, definition.getMaxUses()));
        stack.setItemDamage(Math.min(newUses, definition.getMaxUses()));

        definition.getEffectHandler().onPhase(player, stack, definition, InhalationUsePhase.USE_FINISH, exhausted);

        if (definition.getCooldownTicks() > 0) {
            player.getCooldownTracker().setCooldown(this, definition.getCooldownTicks());
        }

        playWorldSound(player.world, player, definition.getFinishSoundId());
        GregoriusDrugworksInventoryUtil.grantRemainders(player, definition.getPerUseRemainders());

        if (definition.getLingeringSpec() != null && definition.getLingeringSpec().isEnabled()) {
            GregoriusDrugworksInhalationServerHooks.schedule(player, definition);
        }

        if (exhausted) {
            definition.getEffectHandler().onPhase(player, stack, definition, InhalationUsePhase.EXHAUSTION_ONLY, true);
            playWorldSound(player.world, player, definition.getExhaustedSoundId());
            if (player.capabilities.isCreativeMode) {
                GregoriusDrugworksInventoryUtil.grantRemainders(player, definition.getExhaustedRemainders());
            } else {
                replaceSpentHeldStack(player, readTrackedHand(stack), stack);
            }
        }
        GregoriusDrugworksDebug.log(
                GdwDebugCategory.OUTPUTS,
                DebugFormatters.join(
                        "[INHALE][FINISH]",
                        DebugFormatters.kv("player", player.getName()),
                        DebugFormatters.kv("item", definition.getItemId()),
                        DebugFormatters.kv("uses", newUses),
                        DebugFormatters.kv("exhausted", exhausted)
                )
        );
    }

    private int resolveDurabilityLoss(int currentUses, Random random) {
        switch (definition.getDurabilityLossMode()) {
            case RANDOM_RANGE:
                return definition.getRandomMinLoss() + random.nextInt(definition.getRandomMaxLoss() - definition.getRandomMinLoss() + 1);
            case REMAINING_CURVE:
                return Math.max(1, (definition.getMaxUses() - currentUses) <= 2 ? 2 : 1);
            case FIXED:
            default:
                return definition.getFixedLoss();
        }
    }

    private void spawnParticleSpecs(WorldServer world, EntityPlayerMP player, java.util.List<InhalationParticleSpec> specs) {
        if (specs == null || specs.isEmpty()) {
            return;
        }

        Vec3d look = player.getLookVec().normalize();
        double px = player.posX + look.x * 0.35D;
        double py = player.posY + player.getEyeHeight() - 0.08D + look.y * 0.15D;
        double pz = player.posZ + look.z * 0.35D;

        java.util.Random random = player.getRNG();

        for (InhalationParticleSpec spec : specs) {
            for (int i = 0; i < Math.max(1, spec.getCount()); i++) {
                double ox = (random.nextDouble() - 0.5D) * spec.getSpreadX();
                double oy = (random.nextDouble() - 0.5D) * spec.getSpreadY();
                double oz = (random.nextDouble() - 0.5D) * spec.getSpreadZ();

                double mx = look.x * spec.getForwardBias() + random.nextGaussian() * spec.getSpeed() * 0.15D;
                double my = spec.getUpwardBias() + random.nextGaussian() * spec.getSpeed() * 0.10D;
                double mz = look.z * spec.getForwardBias() + random.nextGaussian() * spec.getSpeed() * 0.15D;

                world.spawnParticle(
                        spec.getParticleType(),
                        true,
                        px + ox,
                        py + oy,
                        pz + oz,
                        0,
                        mx,
                        my,
                        mz,
                        1.0D
                );
            }
        }
    }

    private void playWorldSound(World world, EntityPlayer player, ResourceLocation soundId) {
        if (soundId == null) {
            return;
        }
        SoundEvent sound = SoundEvent.REGISTRY.getObject(soundId);
        if (sound == null) {
            return;
        }
        world.playSound(null, player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    private static int nextSequenceId(EntityPlayerMP player) {
        UUID uuid = player.getUniqueID();
        int next = USE_SEQUENCES.getOrDefault(uuid, 0) + 1;
        USE_SEQUENCES.put(uuid, next);
        return next;
    }

    private static void resetSequenceData(ItemStack stack, int sequenceId, EnumHand hand) {
        NBTTagCompound tag = getOrCreateTag(stack);
        tag.setInteger(NBT_SEQUENCE, sequenceId);
        tag.setInteger(NBT_FLAGS, 0);
        tag.setBoolean(NBT_COMPLETED, false);
        tag.setInteger(NBT_HAND, hand.ordinal());
    }

    private static void clearTransientSequenceData(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            return;
        }
        tag.removeTag(NBT_SEQUENCE);
        tag.removeTag(NBT_FLAGS);
        tag.removeTag(NBT_HAND);
    }

    private static EnumHand readTrackedHand(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || !tag.hasKey(NBT_HAND)) {
            return EnumHand.MAIN_HAND;
        }
        int ordinal = tag.getInteger(NBT_HAND);
        return ordinal >= 0 && ordinal < EnumHand.values().length ? EnumHand.values()[ordinal] : EnumHand.MAIN_HAND;
    }

    private void replaceSpentHeldStack(EntityPlayerMP player, EnumHand hand, ItemStack exhaustedStack) {
        java.util.Random random = player.getRNG();
        ItemStack handReplacement = ItemStack.EMPTY;

        for (InhalationRemainderSpec spec : definition.getExhaustedRemainders()) {
            if (random.nextFloat() > spec.getChance()) {
                continue;
            }

            ItemStack generated = spec.createStackCopy();
            if (handReplacement.isEmpty() && !generated.isEmpty() && spec.getChance() >= 1.0F) {
                handReplacement = generated;
                continue;
            }

            GregoriusDrugworksInventoryUtil.giveOrDrop(player, generated, spec.isDropIfInventoryFull());
        }

        player.setHeldItem(hand, handReplacement);
        player.inventory.markDirty();
        player.openContainer.detectAndSendChanges();
    }

    private static int getFlags(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag == null ? 0 : tag.getInteger(NBT_FLAGS);
    }

    private static void setFlags(ItemStack stack, int flags) {
        getOrCreateTag(stack).setInteger(NBT_FLAGS, flags);
    }

    private static int getUses(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag == null ? 0 : tag.getInteger(NBT_USES);
    }

    private static void setUses(ItemStack stack, int value) {
        getOrCreateTag(stack).setInteger(NBT_USES, value);
    }

    private static NBTTagCompound getOrCreateTag(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        return tag;
    }
}