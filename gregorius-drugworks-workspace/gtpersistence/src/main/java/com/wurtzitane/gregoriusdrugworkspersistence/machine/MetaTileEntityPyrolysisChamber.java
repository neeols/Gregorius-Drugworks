package com.wurtzitane.gregoriusdrugworkspersistence.machine;

import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksBlocks;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksCreativeTabs;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksRecipeMaps;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.unification.material.Materials;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.cube.SimpleCubeRenderer;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.core.sound.GTSoundEvents;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class MetaTileEntityPyrolysisChamber extends RecipeMapMultiblockController {

    private static final int PARALLEL_LIMIT = 3;
    private static final ICubeRenderer BASE_TEXTURE =
            new SimpleCubeRenderer("gregoriusdrugworkspersistence:block/carbonized_reactor_casing");

    public MetaTileEntityPyrolysisChamber(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, GregoriusDrugworksRecipeMaps.PYROLYSIS_CHAMBER_RECIPES);
        this.recipeMapWorkable.setParallelLimit(PARALLEL_LIMIT);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityPyrolysisChamber(metaTileEntityId);
    }

    @Override
    protected @Nonnull BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("CCC CCC", "BBB AAA", "BBB ADA", "    ADA", "    AAA")
                .aisle("CCC CCC", "BHBBBBA", "BBB DED", "    DED", "    AAA")
                .aisle("CCC CCC", "B~B AAA", "BBB ADA", "    ADA", "    AAA")
                .where('~', selfPredicate())
                .where('H', heatingCoils())
                .where('C', blocks(GregoriusDrugworksBlocks.OBSIDIAN_FORGED_THERMAL_CASING))
                .where('A', blocks(GregoriusDrugworksBlocks.THERMOCRACK_MATRIX_CASING))
                .where('D', states(MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.LAMINATED_GLASS)))
                .where('E', states(MetaBlocks.COMPRESSED.get(Materials.BorosilicateGlass).getBlock(Materials.BorosilicateGlass)))
                .where('B',
                        states(GregoriusDrugworksBlocks.getDefaultState(GregoriusDrugworksBlocks.CARBONIZED_REACTOR_CASING))
                                .or(abilities(MultiblockAbility.IMPORT_ITEMS).setMaxGlobalLimited(3).setPreviewCount(1))
                                .or(abilities(MultiblockAbility.EXPORT_ITEMS).setMaxGlobalLimited(6).setPreviewCount(1))
                                .or(abilities(MultiblockAbility.IMPORT_FLUIDS).setMaxGlobalLimited(3).setPreviewCount(1))
                                .or(abilities(MultiblockAbility.EXPORT_FLUIDS).setMaxGlobalLimited(6).setPreviewCount(1))
                                .or(abilities(MultiblockAbility.INPUT_ENERGY).setMaxGlobalLimited(1).setPreviewCount(1))
                                .or(abilities(MultiblockAbility.MAINTENANCE_HATCH).setExactLimit(1).setPreviewCount(1)))
                .where(' ', air())
                .build();
    }

    @Override
    public boolean isInCreativeTab(CreativeTabs creativeTab) {
        return super.isInCreativeTab(creativeTab) || creativeTab == GregoriusDrugworksCreativeTabs.MAIN;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return BASE_TEXTURE;
    }

    @Override
    public SoundEvent getBreakdownSound() {
        return GTSoundEvents.BREAKDOWN_ELECTRICAL;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return Textures.PYROLYSE_OVEN_OVERLAY;
    }
}
