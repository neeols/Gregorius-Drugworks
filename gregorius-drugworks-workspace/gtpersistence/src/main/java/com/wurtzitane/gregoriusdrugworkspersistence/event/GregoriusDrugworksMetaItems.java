package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworks.common.catalog.ContentFamily;
import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.catalog.GregoriusDrugworksContentCatalogs;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.*;
import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.ItemPillBase;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.PillItemDefinition;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import static com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksItems.USED_AROMA_TUBE;

public final class GregoriusDrugworksMetaItems {

    private GregoriusDrugworksMetaItems() {
    }

    public static final List<Item> META_ITEMS = new ArrayList<>();
    private static boolean bootstrapped = false;

    public static Item EMPTY_GLASS_AMPOULE;
    public static Item EMPTY_CAPSULE_PILL;

    public static Item NALOXONE_AUTOINJECTOR;
    public static Item FLUMAZENIL_AMPOULE;
    public static Item ATROPINE_2PAM_AUTOINJECTOR;
    public static Item NAC_INFUSION;
    public static Item FOMEPIZOLE_VIAL;
    public static Item HYDROXOCOBALAMIN_KIT;
    public static Item VITAMIN_K_AMPOULE;
    public static Item PROTAMINE_VIAL;
    public static Item GLUCAGON_INJECTOR;
    public static Item DIGOXIN_FAB;
    public static Item KAPPA_RESET_AMPOULE;

    public static Item SAMPLE_AROMA_TUBE;
    public static Item SALVINORIN_A_PILL;

    public static void preInit() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;
        META_ITEMS.clear();
        GregoriusDrugworksContentCatalogs.clear();

        EMPTY_GLASS_AMPOULE = createBasicItem("empty_glass_ampoule", 64);

        NALOXONE_AUTOINJECTOR = createSourceVial("naloxone_autoinjector", 16, EnumRarity.RARE);
        FLUMAZENIL_AMPOULE = createSourceVial("flumazenil_ampoule", 16, EnumRarity.RARE);
        ATROPINE_2PAM_AUTOINJECTOR = createSourceVial("atropine_2pam_autoinjector", 8, EnumRarity.EPIC);
        NAC_INFUSION = createSourceVial("nac_infusion", 16, EnumRarity.UNCOMMON);
        FOMEPIZOLE_VIAL = createSourceVial("fomepizole_vial", 16, EnumRarity.RARE);
        HYDROXOCOBALAMIN_KIT = createSourceVial("hydroxocobalamin_kit", 8, EnumRarity.EPIC);
        VITAMIN_K_AMPOULE = createSourceVial("vitamin_k_ampoule", 16, EnumRarity.UNCOMMON);
        PROTAMINE_VIAL = createSourceVial("protamine_vial", 16, EnumRarity.UNCOMMON);
        GLUCAGON_INJECTOR = createSourceVial("glucagon_injector", 16, EnumRarity.RARE);
        DIGOXIN_FAB = createSourceVial("digoxin_fab", 8, EnumRarity.EPIC);
        KAPPA_RESET_AMPOULE = createSourceVial("kappa_reset_ampoule", 16, EnumRarity.RARE);
        SALVINORIN_A_PILL = createPill(
                PillItemDefinition.builder("salvinorin_a_pill")
                        .maxStackSize(16)
                        .useDurationTicks(18)
                        .arcHeight(1.15F)
                        .launchForward(0.35F)
                        .mouthOffsetY(-0.10F)
                        .spinXPerTick(24.0F)
                        .spinYPerTick(40.0F)
                        .spinZPerTick(17.0F)
                        .lockCamera(true)
                        .tripHookEnabled(true)
                        .rarity(EnumRarity.UNCOMMON)
                        .finishSoundId(GregoriusDrugworksUtil.makeName("pill_gulp"))
                        .modelTexture(GregoriusDrugworksUtil.makeName("textures/item/salvinorin_a_pill.png"))
                        .build()
        );
        EMPTY_CAPSULE_PILL = createPill(
                PillItemDefinition.builder("empty_capsule_pill")
                        .maxStackSize(16)
                        .useDurationTicks(18)
                        .arcHeight(1.15F)
                        .launchForward(0.35F)
                        .mouthOffsetY(-0.10F)
                        .spinXPerTick(24.0F)
                        .spinYPerTick(40.0F)
                        .spinZPerTick(17.0F)
                        .lockCamera(true)
                        .tripHookEnabled(false)
                        .rarity(EnumRarity.COMMON)
                        .finishSoundId(GregoriusDrugworksUtil.makeName("pill_gulp"))
                        .modelTexture(GregoriusDrugworksUtil.makeName("textures/item/empty_capsule_pill.png"))
                        .build()
        );
        SAMPLE_AROMA_TUBE = createInhalation(
                InhalationDefinition.builder("sample_aroma_tube")
                        .rarity(EnumRarity.UNCOMMON)
                        .maxUses(8)
                        .totalUseTicks(24)
                        .raiseTicks(4)
                        .inhaleStartTick(4)
                        .inhaleEndTick(12)
                        .holdTicks(2)
                        .exhaleStartTick(14)
                        .exhaleEndTick(22)
                        .finishTicks(24)
                        .durabilityLossMode(DurabilityLossMode.FIXED)
                        .fixedLoss(1)
                        .minimumCompletionRatio(0.55F)
                        .consumeOnInterrupt(false)
                        .cooldownTicks(5)
                        .useCustomRenderer(true)
                        .localCameraNudge(true)
                        .glowRange(0.15F, 1.0F)
                        .startSoundId(GregoriusDrugworksUtil.makeName("inhalation_start"))
                        .inhaleSoundId(GregoriusDrugworksUtil.makeName("inhalation_inhale"))
                        .exhaleSoundId(GregoriusDrugworksUtil.makeName("inhalation_exhale"))
                        .finishSoundId(GregoriusDrugworksUtil.makeName("inhalation_finish"))
                        .exhaustedSoundId(GregoriusDrugworksUtil.makeName("inhalation_exhausted"))
                        .modelTexture(GregoriusDrugworksUtil.makeName("textures/item/sample_aroma_tube.png"))
                        .addInhaleParticle(new InhalationParticleSpec(
                                net.minecraft.util.EnumParticleTypes.CLOUD,
                                2,
                                0.01D,
                                0.01D,
                                0.01D,
                                0.005D,
                                0.03D,
                                0.01D,
                                1
                        ))
                        .addExhaleParticle(new InhalationParticleSpec(
                                net.minecraft.util.EnumParticleTypes.CLOUD,
                                8,
                                0.05D,
                                0.03D,
                                0.05D,
                                0.02D,
                                0.12D,
                                0.03D,
                                1
                        ))
                        .lingeringSpec(
                                InhalationLingeringSpec.builder()
                                        .startDelayTicks(2)
                                        .durationTicks(26)
                                        .cadenceTicks(2)
                                        .attachedTicks(5)
                                        .initialCount(4)
                                        .finalCount(1)
                                        .initialSpread(0.03D)
                                        .finalSpread(0.11D)
                                        .initialSpeed(0.015D)
                                        .finalSpeed(0.004D)
                                        .forwardDrift(0.008D)
                                        .upwardDrift(0.018D)
                                        .originMode(com.wurtzitane.gregoriusdrugworks.common.inhalation.LingeringOriginMode.DETACHED_WORLD_CLOUD)
                                        .addParticle(new InhalationParticleSpec(
                                                net.minecraft.util.EnumParticleTypes.CLOUD,
                                                2,
                                                0.02D,
                                                0.02D,
                                                0.02D,
                                                0.004D,
                                                0.02D,
                                                0.01D,
                                                3
                                        ))
                                        .addParticle(new InhalationParticleSpec(
                                                net.minecraft.util.EnumParticleTypes.SMOKE_NORMAL,
                                                1,
                                                0.01D,
                                                0.01D,
                                                0.01D,
                                                0.002D,
                                                0.01D,
                                                0.02D,
                                                1
                                        ))
                                        .build()
                        )
                        .addExhaustedRemainder(new InhalationRemainderSpec(
                                new ItemStack(USED_AROMA_TUBE),
                                1.0F,
                                true
                        ))
                        .effectHandler(new TripPackageInhalationEffectHandler(
                                EnumSet.of(InhalationUsePhase.USE_FINISH)
                        ))
                        .build(),
                true
        );

    }

    public static void register(final IForgeRegistry<Item> registry) {
        for (Item item : META_ITEMS) {
            registry.register(item);
        }
    }

    public static void registerModels() {
        for (Item item : META_ITEMS) {
            if (item.getRegistryName() == null) {
                throw new IllegalStateException("Registry name was null during model registration for " + item);
            }
            ModelLoader.setCustomModelResourceLocation(
                    item,
                    0,
                    new ModelResourceLocation(item.getRegistryName(), "inventory")
            );
        }
    }

    public static List<Item> getMetaItems() {
        return Collections.unmodifiableList(META_ITEMS);
    }

    private static Item createBasicItem(final String name, final int maxStackSize) {
        final Item item = new Item();
        item.setRegistryName(GregoriusDrugworksUtil.makeName(name));
        item.setTranslationKey(Tags.MOD_ID + "." + name);
        item.setCreativeTab(GregoriusDrugworksCreativeTabs.MAIN);
        item.setMaxStackSize(maxStackSize);
        META_ITEMS.add(item);
        return item;
    }

    private static Item createSourceVial(final String name, final int maxStackSize, final EnumRarity rarity) {
        final Item item = new Item() {
            @Nonnull
            @Override
            public EnumRarity getRarity(@Nonnull ItemStack stack) {
                return rarity;
            }

            @Override
            public boolean hasEffect(@Nonnull ItemStack stack) {
                return rarity == EnumRarity.RARE || rarity == EnumRarity.EPIC;
            }
        };

        item.setRegistryName(GregoriusDrugworksUtil.makeName(name));
        item.setTranslationKey(Tags.MOD_ID + "." + name);
        item.setCreativeTab(GregoriusDrugworksCreativeTabs.MAIN);
        item.setMaxStackSize(maxStackSize);
        META_ITEMS.add(item);
        return item;
    }

    private static Item createPill(PillItemDefinition definition) {
        Item item = new ItemPillBase(definition);
        META_ITEMS.add(item);
        GregoriusDrugworksContentCatalogs.registerItem(item, ContentFamily.PILL, false);
        return item;
    }

    private static Item createInhalation(InhalationDefinition definition, boolean sample) {
        Item item = new ItemInhalationConsumable(definition);
        META_ITEMS.add(item);
        GregoriusDrugworksContentCatalogs.registerItem(item, ContentFamily.INHALATION, sample);
        return item;
    }

    private static final class GregoriusDrugworksInjectorItem extends Item {

        private final EnumRarity rarity;
        private final EnumAction action;

        private GregoriusDrugworksInjectorItem(final EnumRarity rarity, final EnumAction action) {
            this.rarity = rarity;
            this.action = action;
            this.setMaxDamage(0);
        }

        @Nonnull
        @Override
        public EnumAction getItemUseAction(@Nonnull final ItemStack stack) {
            return this.action;
        }

        @Override
        public int getMaxItemUseDuration(@Nonnull final ItemStack stack) {
            return 32;
        }

        @Nonnull
        @Override
        public EnumRarity getRarity(@Nonnull final ItemStack stack) {
            return this.rarity;
        }

        @Nonnull
        @Override
        public ActionResult<ItemStack> onItemRightClick(@Nonnull final World world, @Nonnull final EntityPlayer player, @Nonnull final EnumHand hand) {
            final ItemStack stack = player.getHeldItem(hand);
            player.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        @Nonnull
        @Override
        public ItemStack onItemUseFinish(@Nonnull final ItemStack stack, @Nonnull final World world, @Nonnull final EntityLivingBase entityLiving) {
            if (!world.isRemote) {
                world.playSound(
                        null,
                        entityLiving.posX,
                        entityLiving.posY,
                        entityLiving.posZ,
                        GregoriusDrugworksSounds.ANTIDOTE_INJECT,
                        SoundCategory.PLAYERS,
                        1.0F,
                        1.0F
                );
            }
            return stack;
        }

        @Override
        public boolean hasEffect(@Nonnull final ItemStack stack) {
            return this.rarity == EnumRarity.RARE || this.rarity == EnumRarity.EPIC;
        }
    }
}