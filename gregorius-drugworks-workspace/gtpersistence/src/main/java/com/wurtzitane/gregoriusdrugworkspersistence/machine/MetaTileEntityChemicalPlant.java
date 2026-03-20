package com.wurtzitane.gregoriusdrugworkspersistence.machine;

import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksBlocks;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksCreativeTabs;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksMetaTileEntities;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.ChemicalPlantTierProperty;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksRecipeMaps;
import gregtech.api.GTValues;
import gregtech.api.block.IHeatingCoilBlockStats;
import gregtech.api.capability.GregtechDataCodes;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockDisplayText;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.BlockWorldState;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.MultiblockShapeInfo;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.pattern.PatternStringError;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.recipeproperties.TemperatureProperty;
import gregtech.api.util.BlockInfo;
import gregtech.api.util.GTUtility;
import gregtech.api.util.TextComponentUtil;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.cube.SimpleCubeRenderer;
import gregtech.common.ConfigHolder;
import gregtech.common.blocks.BlockMachineCasing;
import gregtech.common.blocks.BlockWireCoil.CoilType;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.core.sound.GTSoundEvents;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MetaTileEntityChemicalPlant extends RecipeMapMultiblockController {

    private static final int PARALLEL_LIMIT = 6;
    private static final int LIVE_SHELL_CASING_MIN = 66;
    private static final int UPDATE_FORMED_TIER_STATE = 0x47445701;
    private static final String TIER_CONTEXT_KEY = "GregoriusDrugworksChemicalPlantTier";
    private static final String TIER_MISMATCH_KEY =
            "gregoriusdrugworkspersistence.multiblock.pattern.error.chemical_plant_tier";
    private static final int[] CHEMICAL_PLANT_TIERS = {
            GTValues.MV, GTValues.HV, GTValues.EV, GTValues.IV, GTValues.LuV, GTValues.ZPM, GTValues.UV
    };
    private static final BlockMachineCasing.MachineCasingType[] GT_MACHINE_CASING_TYPES = {
            BlockMachineCasing.MachineCasingType.MV,
            BlockMachineCasing.MachineCasingType.HV,
            BlockMachineCasing.MachineCasingType.EV,
            BlockMachineCasing.MachineCasingType.IV,
            BlockMachineCasing.MachineCasingType.LuV,
            BlockMachineCasing.MachineCasingType.ZPM,
            BlockMachineCasing.MachineCasingType.UV
    };
    private static final Block[] PIPE_CASINGS = {
            GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T1,
            GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T2,
            GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T3,
            GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T4,
            GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T5,
            GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T6,
            GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T7
    };
    private static final Block[] SHELL_CASINGS = {
            GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T1,
            GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T2,
            GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T3,
            GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T4,
            GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T5,
            GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T6,
            GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T7
    };
    private static final IBlockState[] GT_MACHINE_CASING_STATES = createMachineCasingStates();
    private static final CoilType[] PREVIEW_COILS = {
            CoilType.CUPRONICKEL,
            CoilType.KANTHAL,
            CoilType.NICHROME,
            CoilType.RTM_ALLOY,
            CoilType.HSS_G,
            CoilType.NAQUADAH,
            CoilType.TRINIUM
    };
    private static final ICubeRenderer[] BASE_TEXTURES = {
            new SimpleCubeRenderer("gregoriusdrugworkspersistence:block/chemplant_machine_casing_t1"),
            new SimpleCubeRenderer("gregoriusdrugworkspersistence:block/chemplant_machine_casing_t2"),
            new SimpleCubeRenderer("gregoriusdrugworkspersistence:block/chemplant_machine_casing_t3"),
            new SimpleCubeRenderer("gregoriusdrugworkspersistence:block/chemplant_machine_casing_t4"),
            new SimpleCubeRenderer("gregoriusdrugworkspersistence:block/chemplant_machine_casing_t5"),
            new SimpleCubeRenderer("gregoriusdrugworkspersistence:block/chemplant_machine_casing_t6"),
            new SimpleCubeRenderer("gregoriusdrugworkspersistence:block/chemplant_machine_casing_t7")
    };

    private int chemicalPlantTier = -1;
    private int coilTier = -1;
    private int coilTemperature = 0;

    public MetaTileEntityChemicalPlant(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, GregoriusDrugworksRecipeMaps.CHEMICAL_PLANT_RECIPES);
        this.recipeMapWorkable = new ChemicalPlantWorkableHandler(this);
        this.recipeMapWorkable.setParallelLimit(PARALLEL_LIMIT);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityChemicalPlant(metaTileEntityId);
    }

    @Override
    protected @Nonnull BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("CCCCCCC", "C     C", "C     C", "C     C", "C     C", "C     C", "CCCCCCC")
                .aisle("CMMMMMC", " MMMMM ", "       ", "       ", "       ", " MMMMM ", "CCCCCCC")
                .aisle("CMMMMMC", " MHHHM ", "  PPP  ", "  HHH  ", "  PPP  ", " MHHHM ", "CCCCCCC")
                .aisle("CMMMMMC", " MHHHM ", "  PPP  ", "  H H  ", "  PPP  ", " MHHHM ", "CCCCCCC")
                .aisle("CMMMMMC", " MHHHM ", "  PPP  ", "  HHH  ", "  PPP  ", " MHHHM ", "CCCCCCC")
                .aisle("CMMMMMC", " MMMMM ", "       ", "       ", "       ", " MMMMM ", "CCCCCCC")
                .aisle("CCC~CCC", "C     C", "C     C", "C     C", "C     C", "C     C", "CCCCCCC")
                .where('~', selfPredicate())
                .where('H', heatingCoils())
                .where('M', chemicalPlantTieredMachineCasings())
                .where('P', chemicalPlantTieredPipeCasings())
                .where('C',
                        chemicalPlantTieredShellCasings()
                                .setMinGlobalLimited(LIVE_SHELL_CASING_MIN)
                                .or(abilities(MultiblockAbility.IMPORT_ITEMS).setMaxGlobalLimited(6).setPreviewCount(1))
                                .or(abilities(MultiblockAbility.EXPORT_ITEMS).setMaxGlobalLimited(6).setPreviewCount(1))
                                .or(abilities(MultiblockAbility.IMPORT_FLUIDS).setMaxGlobalLimited(6).setPreviewCount(1))
                                .or(abilities(MultiblockAbility.EXPORT_FLUIDS).setMaxGlobalLimited(6).setPreviewCount(1))
                                .or(abilities(MultiblockAbility.INPUT_ENERGY).setMaxGlobalLimited(1).setPreviewCount(1))
                                .or(abilities(MultiblockAbility.MAINTENANCE_HATCH).setExactLimit(1).setPreviewCount(1)))
                .where(' ', air())
                .build();
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>(CHEMICAL_PLANT_TIERS.length);
        for (int i = 0; i < CHEMICAL_PLANT_TIERS.length; i++) {
            int tier = CHEMICAL_PLANT_TIERS[i];
            IBlockState shellState = GregoriusDrugworksBlocks.getDefaultState(SHELL_CASINGS[i]);
            MultiblockShapeInfo shape = MultiblockShapeInfo.builder()
                    .aisle("CCCCCCC", "C     C", "C     C", "C     C", "C     C", "C     C", "CCCCCCC")
                    .aisle("CMMMMMC", " MMMMM ", "       ", "       ", "       ", " MMMMM ", "CCCCCCC")
                    .aisle("CMMMMMC", " MHHHM ", "  PPP  ", "  HHH  ", "  PPP  ", " MHHHM ", "CCCCCCC")
                    .aisle("CMMMMMC", " MHHHM ", "  PPP  ", "  H H  ", "  PPP  ", " MHHHM ", "CCCCCCC")
                    .aisle("CMMMMMC", " MHHHM ", "  PPP  ", "  HHH  ", "  PPP  ", " MHHHM ", "CCCCCCC")
                    .aisle("CMMMMMC", " MMMMM ", "       ", "       ", "       ", " MMMMM ", "CCCCCCC")
                    .aisle("CIFSOEC", "N     R", "C     C", "C     C", "C     C", "C     C", "CCCCCCC")
                    .where('S', GregoriusDrugworksMetaTileEntities.CHEMICAL_PLANT, EnumFacing.SOUTH)
                    .where('C', shellState)
                    .where('M', GT_MACHINE_CASING_STATES[i])
                    .where('P', GregoriusDrugworksBlocks.getDefaultState(PIPE_CASINGS[i]))
                    .where('H', MetaBlocks.WIRE_COIL.getState(PREVIEW_COILS[i]))
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[tier], EnumFacing.SOUTH)
                    .where('O', MetaTileEntities.ITEM_EXPORT_BUS[tier], EnumFacing.SOUTH)
                    .where('F', MetaTileEntities.FLUID_IMPORT_HATCH[tier], EnumFacing.SOUTH)
                    .where('R', MetaTileEntities.FLUID_EXPORT_HATCH[tier], EnumFacing.SOUTH)
                    .where('E', MetaTileEntities.ENERGY_INPUT_HATCH[tier], EnumFacing.SOUTH)
                    .where('N', () -> ConfigHolder.machines.enableMaintenance ? MetaTileEntities.MAINTENANCE_HATCH :
                            shellState, EnumFacing.SOUTH)
                    .where(' ', Blocks.AIR.getDefaultState())
                    .build();
            shapeInfos.add(shape);
        }
        return shapeInfos;
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        Integer tier = context.get(TIER_CONTEXT_KEY);
        this.chemicalPlantTier = tier == null ? -1 : tier;
        Object coilType = context.get("CoilType");
        if (coilType instanceof IHeatingCoilBlockStats) {
            IHeatingCoilBlockStats stats = (IHeatingCoilBlockStats) coilType;
            this.coilTier = stats.getTier();
            this.coilTemperature = stats.getCoilTemperature();
        } else {
            this.coilTier = 0;
            this.coilTemperature = 0;
        }
        syncFormedTierState();
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        this.chemicalPlantTier = -1;
        this.coilTier = -1;
        this.coilTemperature = 0;
        syncFormedTierState();
    }

    @Override
    public boolean checkRecipe(@Nonnull Recipe recipe, boolean consumeIfSuccess) {
        int requiredTier = recipe.getProperty(ChemicalPlantTierProperty.getInstance(), -1);
        if (requiredTier > 0 && chemicalPlantTier < requiredTier) {
            return false;
        }
        int requiredTemperature = recipe.getProperty(TemperatureProperty.getInstance(), 0);
        return requiredTemperature <= 0 || coilTemperature >= requiredTemperature;
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        MultiblockDisplayText.builder(textList, isStructureFormed())
                .setWorkingStatus(recipeMapWorkable.isWorkingEnabled(), recipeMapWorkable.isActive())
                .addEnergyUsageLine(recipeMapWorkable.getEnergyContainer())
                .addEnergyTierLine(GTUtility.getTierByVoltage(recipeMapWorkable.getMaxVoltage()))
                .addCustom(text -> {
                    if (isStructureFormed() && hasValidChemicalPlantTier()) {
                        text.add(TextComponentUtil.translationWithColor(
                                TextFormatting.GRAY,
                                "gregoriusdrugworkspersistence.multiblock.chemical_plant.structure_tier",
                                TextComponentUtil.stringWithColor(TextFormatting.AQUA,
                                        GTValues.VN[chemicalPlantTier])));
                        text.add(TextComponentUtil.translationWithColor(
                                TextFormatting.GRAY,
                                "gregoriusdrugworkspersistence.multiblock.chemical_plant.coil_temperature",
                                TextComponentUtil.stringWithColor(TextFormatting.GOLD,
                                        Integer.toString(coilTemperature))));
                    }
                })
                .addParallelsLine(recipeMapWorkable.getParallelLimit())
                .addWorkingStatusLine()
                .addProgressLine(recipeMapWorkable.getProgressPercent());
    }

    @Override
    public boolean isInCreativeTab(CreativeTabs creativeTab) {
        return super.isInCreativeTab(creativeTab) || creativeTab == GregoriusDrugworksCreativeTabs.MAIN;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return BASE_TEXTURES[getTierIndex(chemicalPlantTier)];
    }

    @Override
    public SoundEvent getBreakdownSound() {
        return GTSoundEvents.BREAKDOWN_ELECTRICAL;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return Textures.LARGE_CHEMICAL_REACTOR_OVERLAY;
    }

    @Override
    public void writeInitialSyncData(PacketBuffer buf) {
        super.writeInitialSyncData(buf);
        writeTierSyncData(buf);
    }

    @Override
    public void receiveInitialSyncData(PacketBuffer buf) {
        super.receiveInitialSyncData(buf);
        readTierSyncData(buf);
        refreshTieredAppearance();
    }

    @Override
    public void receiveCustomData(int dataId, PacketBuffer buf) {
        super.receiveCustomData(dataId, buf);
        if (dataId == UPDATE_FORMED_TIER_STATE) {
            readTierSyncData(buf);
            refreshTieredAppearance();
        }
    }

    private boolean hasValidChemicalPlantTier() {
        return chemicalPlantTier >= GTValues.MV && chemicalPlantTier <= GTValues.UV;
    }

    private int getChemicalPlantTier() {
        return chemicalPlantTier;
    }

    private static int getTierIndex(int tier) {
        int index = tier - GTValues.MV;
        if (index < 0 || index >= BASE_TEXTURES.length) {
            return 0;
        }
        return index;
    }

    private static IBlockState[] createMachineCasingStates() {
        IBlockState[] states = new IBlockState[GT_MACHINE_CASING_TYPES.length];
        for (int i = 0; i < GT_MACHINE_CASING_TYPES.length; i++) {
            states[i] = MetaBlocks.MACHINE_CASING.getState(GT_MACHINE_CASING_TYPES[i]);
        }
        return states;
    }

    private static TraceabilityPredicate chemicalPlantTieredMachineCasings() {
        return new TraceabilityPredicate(blockWorldState -> matchTieredState(blockWorldState, GT_MACHINE_CASING_STATES),
                () -> toBlockInfos(GT_MACHINE_CASING_STATES))
                .addTooltips("tooltip.gregoriusdrugworkspersistence.chemplant_tier_rule");
    }

    private static TraceabilityPredicate chemicalPlantTieredPipeCasings() {
        return new TraceabilityPredicate(blockWorldState -> matchTieredBlock(blockWorldState, PIPE_CASINGS),
                () -> toBlockInfos(PIPE_CASINGS))
                        .addTooltips("tooltip.gregoriusdrugworkspersistence.chemplant_tier_rule");
    }

    private static TraceabilityPredicate chemicalPlantTieredShellCasings() {
        return new TraceabilityPredicate(blockWorldState -> matchTieredBlock(blockWorldState, SHELL_CASINGS),
                () -> toBlockInfos(SHELL_CASINGS))
                        .addTooltips("tooltip.gregoriusdrugworkspersistence.chemplant_tier_rule");
    }

    private static boolean matchTieredState(BlockWorldState blockWorldState, IBlockState[] states) {
        IBlockState foundState = blockWorldState.getBlockState();
        for (int i = 0; i < states.length; i++) {
            if (states[i].equals(foundState)) {
                return matchStructureTier(blockWorldState, CHEMICAL_PLANT_TIERS[i]);
            }
        }
        return false;
    }

    private static boolean matchTieredBlock(BlockWorldState blockWorldState, Block[] blocks) {
        Block foundBlock = blockWorldState.getBlockState().getBlock();
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] == foundBlock) {
                return matchStructureTier(blockWorldState, CHEMICAL_PLANT_TIERS[i]);
            }
        }
        return false;
    }

    private static boolean matchStructureTier(BlockWorldState blockWorldState, int tier) {
        Integer existingTier = blockWorldState.getMatchContext().getOrPut(TIER_CONTEXT_KEY, tier);
        if (existingTier != tier) {
            blockWorldState.setError(new PatternStringError(TIER_MISMATCH_KEY));
            return false;
        }
        return true;
    }

    private static BlockInfo[] toBlockInfos(Block[] blocks) {
        BlockInfo[] blockInfos = new BlockInfo[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            blockInfos[i] = new BlockInfo(blocks[i]);
        }
        return blockInfos;
    }

    private static BlockInfo[] toBlockInfos(IBlockState[] states) {
        BlockInfo[] blockInfos = new BlockInfo[states.length];
        for (int i = 0; i < states.length; i++) {
            blockInfos[i] = new BlockInfo(states[i]);
        }
        return blockInfos;
    }

    private void syncFormedTierState() {
        if (getWorld() == null || getWorld().isRemote) {
            return;
        }
        writeCustomData(UPDATE_FORMED_TIER_STATE, this::writeTierSyncData);
        notifyBlockUpdate();
    }

    private void writeTierSyncData(PacketBuffer buf) {
        buf.writeInt(chemicalPlantTier);
        buf.writeInt(coilTier);
        buf.writeInt(coilTemperature);
    }

    private void readTierSyncData(PacketBuffer buf) {
        this.chemicalPlantTier = buf.readInt();
        this.coilTier = buf.readInt();
        this.coilTemperature = buf.readInt();
    }

    @SideOnly(Side.CLIENT)
    private void refreshTieredAppearance() {
        scheduleRenderUpdate();
        if (getWorld() == null || getPos() == null) {
            return;
        }
        getWorld().markBlockRangeForRenderUpdate(
                getPos().getX() - 4, getPos().getY() - 1, getPos().getZ() - 4,
                getPos().getX() + 4, getPos().getY() + 7, getPos().getZ() + 4);
    }

    private static final class ChemicalPlantWorkableHandler extends MultiblockRecipeLogic {

        private ChemicalPlantWorkableHandler(MetaTileEntityChemicalPlant chemicalPlant) {
            super(chemicalPlant);
        }

        @Override
        public long getMaxVoltage() {
            return getTierCappedVoltage(super.getMaxVoltage());
        }

        @Override
        public long getMaximumOverclockVoltage() {
            return getTierCappedVoltage(super.getMaximumOverclockVoltage());
        }

        private long getTierCappedVoltage(long baseVoltage) {
            MetaTileEntityChemicalPlant chemicalPlant = (MetaTileEntityChemicalPlant) metaTileEntity;
            if (!chemicalPlant.hasValidChemicalPlantTier()) {
                return baseVoltage;
            }
            return Math.min(baseVoltage, GTValues.V[chemicalPlant.getChemicalPlantTier()]);
        }
    }
}
