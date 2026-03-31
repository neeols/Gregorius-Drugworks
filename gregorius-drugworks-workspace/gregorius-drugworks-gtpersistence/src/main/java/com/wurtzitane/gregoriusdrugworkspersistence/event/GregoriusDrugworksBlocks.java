package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import gregtech.api.block.VariantActiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class GregoriusDrugworksBlocks {

    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final List<ItemBlock> ITEM_BLOCKS = new ArrayList<>();

    public static Block CHEMPLANT_PIPE_CASING_T1;
    public static Block CHEMPLANT_PIPE_CASING_T2;
    public static Block CHEMPLANT_PIPE_CASING_T3;
    public static Block CHEMPLANT_PIPE_CASING_T4;
    public static Block CHEMPLANT_PIPE_CASING_T5;
    public static Block CHEMPLANT_PIPE_CASING_T6;
    public static Block CHEMPLANT_PIPE_CASING_T7;

    public static Block CHEMPLANT_MACHINE_CASING_T1;
    public static Block CHEMPLANT_MACHINE_CASING_T2;
    public static Block CHEMPLANT_MACHINE_CASING_T3;
    public static Block CHEMPLANT_MACHINE_CASING_T4;
    public static Block CHEMPLANT_MACHINE_CASING_T5;
    public static Block CHEMPLANT_MACHINE_CASING_T6;
    public static Block CHEMPLANT_MACHINE_CASING_T7;

    public static Block FLUOROPOLYMER_FRACTIONATION_CASING;
    public static Block POLYETHERIMIDE_THERMAL_CASING;
    public static Block MOLECULAR_MEMBRANE_CASING;
    public static Block POLYSILOXANE_VAPOR_CONTROL_CASING;

    public static Block CARBONIZED_REACTOR_CASING;
    public static Block OBSIDIAN_FORGED_THERMAL_CASING;
    public static Block THERMOCRACK_MATRIX_CASING;

    public static Block METHBLOCK;
    public static Block COMPRESSEDMETHBLOCK;

    private GregoriusDrugworksBlocks() {
    }

    public static void preInit() {
        BLOCKS.clear();
        ITEM_BLOCKS.clear();
        gregtech.common.blocks.GregoriusDrugworksBlocks.init();

        CHEMPLANT_PIPE_CASING_T1 = gregtech.common.blocks.GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T1;
        CHEMPLANT_PIPE_CASING_T2 = gregtech.common.blocks.GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T2;
        CHEMPLANT_PIPE_CASING_T3 = gregtech.common.blocks.GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T3;
        CHEMPLANT_PIPE_CASING_T4 = gregtech.common.blocks.GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T4;
        CHEMPLANT_PIPE_CASING_T5 = gregtech.common.blocks.GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T5;
        CHEMPLANT_PIPE_CASING_T6 = gregtech.common.blocks.GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T6;
        CHEMPLANT_PIPE_CASING_T7 = gregtech.common.blocks.GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T7;

        CHEMPLANT_MACHINE_CASING_T1 = gregtech.common.blocks.GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T1;
        CHEMPLANT_MACHINE_CASING_T2 = gregtech.common.blocks.GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T2;
        CHEMPLANT_MACHINE_CASING_T3 = gregtech.common.blocks.GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T3;
        CHEMPLANT_MACHINE_CASING_T4 = gregtech.common.blocks.GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T4;
        CHEMPLANT_MACHINE_CASING_T5 = gregtech.common.blocks.GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T5;
        CHEMPLANT_MACHINE_CASING_T6 = gregtech.common.blocks.GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T6;
        CHEMPLANT_MACHINE_CASING_T7 = gregtech.common.blocks.GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T7;

        FLUOROPOLYMER_FRACTIONATION_CASING =
                gregtech.common.blocks.GregoriusDrugworksBlocks.FLUOROPOLYMER_FRACTIONATION_CASING;
        POLYETHERIMIDE_THERMAL_CASING =
                gregtech.common.blocks.GregoriusDrugworksBlocks.POLYETHERIMIDE_THERMAL_CASING;
        MOLECULAR_MEMBRANE_CASING = gregtech.common.blocks.GregoriusDrugworksBlocks.MOLECULAR_MEMBRANE_CASING;
        POLYSILOXANE_VAPOR_CONTROL_CASING =
                gregtech.common.blocks.GregoriusDrugworksBlocks.POLYSILOXANE_VAPOR_CONTROL_CASING;

        CARBONIZED_REACTOR_CASING = gregtech.common.blocks.GregoriusDrugworksBlocks.CARBONIZED_REACTOR_CASING;
        OBSIDIAN_FORGED_THERMAL_CASING =
                gregtech.common.blocks.GregoriusDrugworksBlocks.OBSIDIAN_FORGED_THERMAL_CASING;
        THERMOCRACK_MATRIX_CASING = gregtech.common.blocks.GregoriusDrugworksBlocks.THERMOCRACK_MATRIX_CASING;
        METHBLOCK = createMetalBlock("methblock", 5.0F, 6.0F);
        COMPRESSEDMETHBLOCK = createMetalBlock("compressedmethblock", 5.0F, 6.0F);
    }

    public static void register(IForgeRegistry<Block> registry) {
        for (Block block : BLOCKS) {
            registry.register(block);
        }
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        for (ItemBlock itemBlock : ITEM_BLOCKS) {
            registry.register(itemBlock);
        }
    }

    public static void registerModels() {
        for (ItemBlock itemBlock : ITEM_BLOCKS) {
            Block block = itemBlock.getBlock();
            if (block instanceof GregoriusDrugworksActiveCasingBlock) {
                ((GregoriusDrugworksActiveCasingBlock) block).onModelRegister();
                continue;
            }
            ResourceLocation registryName = requireRegistryName(itemBlock);
            ModelLoader.setCustomModelResourceLocation(itemBlock, 0,
                    new ModelResourceLocation(registryName, "inventory"));
        }
    }

    public static List<Block> getBlocks() {
        return Collections.unmodifiableList(BLOCKS);
    }

    public static List<ItemBlock> getItemBlocks() {
        return Collections.unmodifiableList(ITEM_BLOCKS);
    }

    public static IBlockState getDefaultState(Block block) {
        if (block == null) {
            throw new IllegalStateException("Requested default state for a null block");
        }
        return block.getDefaultState();
    }

    private static Block createMetalBlock(String name, float hardness, float resistance) {
        return createBasicBlock(name, Material.IRON, hardness, resistance, SoundType.METAL);
    }

    private static Block createActiveMetalBlock(String name, float hardness, float resistance) {
        return createActiveBlock(name, Material.IRON, hardness, resistance, SoundType.METAL);
    }

    private static Block createBasicBlock(String name,
                                          Material material,
                                          float hardness,
                                          float resistance,
                                          SoundType soundType) {
        GregoriusDrugworksBlock block = new GregoriusDrugworksBlock(material, hardness, resistance, soundType);
        configureBlock(block, name);
        registerBlockWithItem(block, new ItemBlock(block));
        return block;
    }

    private static Block createActiveBlock(String name,
                                           Material material,
                                           float hardness,
                                           float resistance,
                                           SoundType soundType) {
        GregoriusDrugworksActiveCasingBlock block =
                new GregoriusDrugworksActiveCasingBlock(name, material, hardness, resistance, soundType);
        configureBlock(block, name);
        registerBlockWithItem(block, new ItemBlock(block));
        return block;
    }

    private static void configureBlock(Block block, String name) {
        block.setRegistryName(GregoriusDrugworksUtil.makeName(name));
        GregoriusDrugworksUtil.setTranslationKeyCompat(block, Tags.MOD_ID + "." + name);
        GregoriusDrugworksUtil.setCreativeTabCompat(block, GregoriusDrugworksCreativeTabs.MAIN);
    }

    private static void registerBlockWithItem(Block block, ItemBlock itemBlock) {
        BLOCKS.add(block);
        itemBlock.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
        ITEM_BLOCKS.add(itemBlock);
    }

    private static ResourceLocation requireRegistryName(Item item) {
        ResourceLocation registryName = item.getRegistryName();
        if (registryName == null) {
            throw new IllegalStateException("Registry name was null during model registration for " + item);
        }
        return registryName;
    }

    private static class GregoriusDrugworksBlock extends Block {

        private GregoriusDrugworksBlock(Material material,
                                        float hardness,
                                        float resistance,
                                        SoundType soundType) {
            super(material);
            GregoriusDrugworksUtil.setHardnessCompat(this, hardness);
            GregoriusDrugworksUtil.setResistanceCompat(this, resistance);
            setHarvestLevel("pickaxe", 1);
            GregoriusDrugworksUtil.setSoundTypeCompat(this, soundType);
        }

        @Nonnull
        @Override
        public String getTranslationKey() {
            return super.getTranslationKey();
        }
    }

    private static final class GregoriusDrugworksActiveCasingBlock extends VariantActiveBlock<GregoriusDrugworksActiveCasingBlock.ActiveCasingVariant> {

        private GregoriusDrugworksActiveCasingBlock(String name,
                                                    Material material,
                                                    float hardness,
                                                    float resistance,
                                                    SoundType soundType) {
            super(material);
            GregoriusDrugworksUtil.setHardnessCompat(this, hardness);
            GregoriusDrugworksUtil.setResistanceCompat(this, resistance);
            setHarvestLevel("pickaxe", 1);
            GregoriusDrugworksUtil.setSoundTypeCompat(this, soundType);
            setDefaultState(getState(ActiveCasingVariant.NORMAL));
            GregoriusDrugworksUtil.setTranslationKeyCompat(this, Tags.MOD_ID + "." + name);
            GregoriusDrugworksUtil.setCreativeTabCompat(this, GregoriusDrugworksCreativeTabs.MAIN);
        }
        
        public enum ActiveCasingVariant implements IStringSerializable {
            NORMAL("normal");

            private final String name;

            ActiveCasingVariant(String name) {
                this.name = name;
            }

            @Nonnull
            @Override
            public String getName() {
                return name;
            }
        }
    }
}
