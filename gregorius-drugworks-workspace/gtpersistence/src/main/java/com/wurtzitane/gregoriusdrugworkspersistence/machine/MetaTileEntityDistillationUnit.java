package com.wurtzitane.gregoriusdrugworkspersistence.machine;

import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksBlocks;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksCreativeTabs;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksMetaTileEntities;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksRecipeMaps;
import gregtech.api.GregTechAPI;
import gregtech.api.block.IHeatingCoilBlockStats;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockDisplayText;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.MultiblockShapeInfo;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.recipeproperties.TemperatureProperty;
import gregtech.api.util.GTUtility;
import gregtech.api.util.TextComponentUtil;
import gregtech.api.util.TextFormattingUtil;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.cube.SimpleCubeRenderer;
import gregtech.common.ConfigHolder;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.BlockCleanroomCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.core.sound.GTSoundEvents;
import net.minecraft.init.Blocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MetaTileEntityDistillationUnit extends RecipeMapMultiblockController {

    private static final int PARALLEL_LIMIT = 2;
    private static final ICubeRenderer BASE_TEXTURE =
            new SimpleCubeRenderer("gregoriusdrugworkspersistence:block/fluoropolymer_fractionation_casing");
    private int coilTemperature;

    public MetaTileEntityDistillationUnit(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, GregoriusDrugworksRecipeMaps.DISTILLATION_UNIT_RECIPES);
        this.recipeMapWorkable.setParallelLimit(PARALLEL_LIMIT);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityDistillationUnit(metaTileEntityId);
    }

    @Override
    protected @Nonnull BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("    BBBB    ", "    DDDD    ", "    DDDD    ", "    D  D    ", "    D  D    ", "            ", "            ")
                .aisle("AA  BBBB  AA", "CCEEEEEEEECC", "CC  DBBD  CC", "FF        FF", "CC        CC", "CC        CC", "AA        AA")
                .aisle("AA  BBBB  AA", "CC  DZZD  CC", "CC  DDDD  CC", "FF        FF", "CC        CC", "CC        CC", "AA        AA")
                .aisle("    BBBB    ", "    DD~D    ", "    DDDD    ", "            ", "            ", "            ", "            ")
                .where('~', selfPredicate())
                .where('A', states(GregoriusDrugworksBlocks.getDefaultState(GregoriusDrugworksBlocks.POLYETHERIMIDE_THERMAL_CASING)))
                .where('B', states(MetaBlocks.CLEANROOM_CASING.getState(BlockCleanroomCasing.CasingType.FILTER_CASING)))
                .where('C', states(GregoriusDrugworksBlocks.getDefaultState(GregoriusDrugworksBlocks.POLYSILOXANE_VAPOR_CONTROL_CASING)))
                .where('E', states(MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TUNGSTENSTEEL_PIPE)))
                .where('F', blocks(GregoriusDrugworksBlocks.MOLECULAR_MEMBRANE_CASING))
                .where('Z', heatingCoils().setMinGlobalLimited(2).setMaxGlobalLimited(2))
                .where('D',
                        states(GregoriusDrugworksBlocks.getDefaultState(GregoriusDrugworksBlocks.FLUOROPOLYMER_FRACTIONATION_CASING))
                                .or(abilities(MultiblockAbility.IMPORT_ITEMS).setMaxGlobalLimited(2).setPreviewCount(1))
                                .or(abilities(MultiblockAbility.EXPORT_ITEMS).setMaxGlobalLimited(6).setPreviewCount(1))
                                .or(abilities(MultiblockAbility.IMPORT_FLUIDS).setMaxGlobalLimited(2).setPreviewCount(1))
                                .or(abilities(MultiblockAbility.EXPORT_FLUIDS).setMaxGlobalLimited(9).setPreviewCount(1))
                                .or(abilities(MultiblockAbility.INPUT_ENERGY).setMaxGlobalLimited(1).setPreviewCount(1))
                                .or(abilities(MultiblockAbility.MAINTENANCE_HATCH).setExactLimit(1).setPreviewCount(1)))
                .where(' ', air())
                .build();
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        ArrayList<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        MultiblockShapeInfo.Builder builder = MultiblockShapeInfo.builder()
                .aisle("    BBBB    ", "    DDDD    ", "    DDDD    ", "    D  D    ", "    D  D    ", "            ", "            ")
                .aisle("AA  BBBB  AA", "CCEEEEEEEECC", "CC  IBBO  CC", "FF        FF", "CC        CC", "CC        CC", "AA        AA")
                .aisle("AA  BBBB  AA", "CC  LZZR  CC", "CC  DDDD  CC", "FF        FF", "CC        CC", "CC        CC", "AA        AA")
                .aisle("    BBBB    ", "    MQ~D    ", "    DDDD    ", "            ", "            ", "            ", "            ")
                .where('~', GregoriusDrugworksMetaTileEntities.DISTILLATION_UNIT, EnumFacing.SOUTH)
                .where('A', GregoriusDrugworksBlocks.getDefaultState(GregoriusDrugworksBlocks.POLYETHERIMIDE_THERMAL_CASING))
                .where('B', MetaBlocks.CLEANROOM_CASING.getState(BlockCleanroomCasing.CasingType.FILTER_CASING))
                .where('C', GregoriusDrugworksBlocks.getDefaultState(GregoriusDrugworksBlocks.POLYSILOXANE_VAPOR_CONTROL_CASING))
                .where('D', GregoriusDrugworksBlocks.getDefaultState(GregoriusDrugworksBlocks.FLUOROPOLYMER_FRACTIONATION_CASING))
                .where('E', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TUNGSTENSTEEL_PIPE))
                .where('F', GregoriusDrugworksBlocks.getDefaultState(GregoriusDrugworksBlocks.MOLECULAR_MEMBRANE_CASING))
                .where('I', MetaTileEntities.ITEM_IMPORT_BUS[gregtech.api.GTValues.HV], EnumFacing.SOUTH)
                .where('O', MetaTileEntities.ITEM_EXPORT_BUS[gregtech.api.GTValues.HV], EnumFacing.SOUTH)
                .where('L', MetaTileEntities.FLUID_IMPORT_HATCH[gregtech.api.GTValues.HV], EnumFacing.SOUTH)
                .where('R', MetaTileEntities.FLUID_EXPORT_HATCH[gregtech.api.GTValues.HV], EnumFacing.SOUTH)
                .where('Q', MetaTileEntities.ENERGY_INPUT_HATCH[gregtech.api.GTValues.HV], EnumFacing.SOUTH)
                .where('M', () -> ConfigHolder.machines.enableMaintenance ? MetaTileEntities.MAINTENANCE_HATCH :
                        GregoriusDrugworksBlocks.getDefaultState(GregoriusDrugworksBlocks.FLUOROPOLYMER_FRACTIONATION_CASING),
                        EnumFacing.SOUTH)
                .where(' ', Blocks.AIR.getDefaultState());
        GregTechAPI.HEATING_COILS.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> entry.getValue().getTier()))
                .forEach(entry -> shapeInfos.add(builder.shallowCopy().where('Z', entry.getKey()).build()));
        return shapeInfos;
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        Object coilType = context.get("CoilType");
        if (coilType instanceof IHeatingCoilBlockStats) {
            coilTemperature = ((IHeatingCoilBlockStats) coilType).getCoilTemperature();
        } else {
            coilTemperature = 0;
        }
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        coilTemperature = 0;
    }

    @Override
    public boolean checkRecipe(@Nonnull Recipe recipe, boolean consumeIfSuccess) {
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
                    if (isStructureFormed()) {
                        text.add(TextComponentUtil.translationWithColor(
                                TextFormatting.GRAY,
                                "gregoriusdrugworkspersistence.multiblock.distillation_unit.coil_temperature",
                                TextComponentUtil.stringWithColor(
                                        TextFormatting.GOLD,
                                        TextFormattingUtil.formatNumbers(coilTemperature))));
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

    @Override
    public boolean allowsExtendedFacing() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return BASE_TEXTURE;
    }

    @Override
    public boolean allowSameFluidFillForOutputs() {
        return false;
    }

    @Override
    public SoundEvent getBreakdownSound() {
        return GTSoundEvents.BREAKDOWN_ELECTRICAL;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return Textures.DISTILLATION_TOWER_OVERLAY;
    }
}
