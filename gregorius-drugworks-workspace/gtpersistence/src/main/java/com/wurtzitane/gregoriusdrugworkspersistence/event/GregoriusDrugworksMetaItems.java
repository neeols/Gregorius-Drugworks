package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GregoriusDrugworksMetaItems {

    private GregoriusDrugworksMetaItems() {
    }

    public static final List<Item> META_ITEMS = new ArrayList<>();

    public static Item EMPTY_GLASS_AMPOULE;

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

    public static void preInit() {
        META_ITEMS.clear();

        EMPTY_GLASS_AMPOULE = createBasicItem("empty_glass_ampoule", 64);

        NALOXONE_AUTOINJECTOR = createInjector("naloxone_autoinjector", 16, EnumRarity.RARE, EnumAction.BOW);
        FLUMAZENIL_AMPOULE = createInjector("flumazenil_ampoule", 16, EnumRarity.RARE, EnumAction.BOW);
        ATROPINE_2PAM_AUTOINJECTOR = createInjector("atropine_2pam_autoinjector", 8, EnumRarity.EPIC, EnumAction.BOW);
        NAC_INFUSION = createInjector("nac_infusion", 16, EnumRarity.UNCOMMON, EnumAction.DRINK);
        FOMEPIZOLE_VIAL = createInjector("fomepizole_vial", 16, EnumRarity.RARE, EnumAction.BOW);
        HYDROXOCOBALAMIN_KIT = createInjector("hydroxocobalamin_kit", 8, EnumRarity.EPIC, EnumAction.BOW);
        VITAMIN_K_AMPOULE = createInjector("vitamin_k_ampoule", 16, EnumRarity.UNCOMMON, EnumAction.BOW);
        PROTAMINE_VIAL = createInjector("protamine_vial", 16, EnumRarity.UNCOMMON, EnumAction.BOW);
        GLUCAGON_INJECTOR = createInjector("glucagon_injector", 16, EnumRarity.RARE, EnumAction.BOW);
        DIGOXIN_FAB = createInjector("digoxin_fab", 8, EnumRarity.EPIC, EnumAction.BOW);
        KAPPA_RESET_AMPOULE = createInjector("kappa_reset_ampoule", 16, EnumRarity.RARE, EnumAction.BOW);
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

    private static Item createInjector(final String name, final int maxStackSize, final EnumRarity rarity, final EnumAction action) {
        final GregoriusDrugworksInjectorItem item = new GregoriusDrugworksInjectorItem(rarity, action);
        item.setRegistryName(GregoriusDrugworksUtil.makeName(name));
        item.setTranslationKey(Tags.MOD_ID + "." + name);
        item.setCreativeTab(GregoriusDrugworksCreativeTabs.MAIN);
        item.setMaxStackSize(maxStackSize);
        META_ITEMS.add(item);
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