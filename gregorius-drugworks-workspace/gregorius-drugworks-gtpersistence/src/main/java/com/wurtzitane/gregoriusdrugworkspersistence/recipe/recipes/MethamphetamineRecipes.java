package com.wurtzitane.gregoriusdrugworkspersistence.recipe.recipes;

import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksBlocks;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksMetaItems;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.ChemicalPlantRecipeBuilder;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksRecipeMaps;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksUnificationHelper;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.GregoriusDrugworksChancedInputSupport;
import gregtech.api.metatileentity.multiblock.CleanroomType;
import gregtech.api.recipes.ModHandler;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.common.items.MetaItems;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static gregtech.api.GTValues.*;
import static gregtech.api.unification.ore.OrePrefix.*;

public final class MethamphetamineRecipes {

    private MethamphetamineRecipes() {
    }

    public static void init() {
        RecipeBuilder builder;

        // chloroacetone
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Chlorine.getFluid(1000), Materials.Acetone.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Chloroacetone.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.duration(240);
        builder.circuitMeta(1);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // chloroacetone 2
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Chlorine.getFluid(10000), Materials.Acetone.getFluid(10000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Chloroacetone.getFluid(10000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(10000));
        builder.duration(280);
        builder.circuitMeta(2);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // phenylacetone
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.notConsumable(dust, GregoriusDrugworksMaterials.AluminiumTrichloride, 1);
        builder.fluidInputs(Materials.Benzene.getFluid(1000), GregoriusDrugworksMaterials.Chloroacetone.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Phenylacetone.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.duration(480);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // methylamine
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Methanol.getFluid(2000), Materials.Ammonia.getFluid(2000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Methylamine.getFluid(2000), Materials.Water.getFluid(2000));
        builder.duration(300);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // methamphetamine
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.LithiumAluminiumHydride, 1);
        builder.fluidInputs(GregoriusDrugworksMaterials.Methylamine.getFluid(1000), GregoriusDrugworksMaterials.Phenylacetone.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Methamphetamine.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(600);
        builder.cleanroom(CleanroomType.CLEANROOM);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.FLUID_SOLIDFICATION_RECIPES.recipeBuilder();
        builder.notConsumable(MetaItems.SHAPE_MOLD_PLATE.getStackForm());
        builder.fluidInputs(GregoriusDrugworksMaterials.Methamphetamine.getFluid(144));
        builder.output(plate, GregoriusDrugworksMaterials.Methamphetamine, 1);
        builder.duration(10);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.FLUID_SOLIDFICATION_RECIPES.recipeBuilder();
        builder.input(GregoriusDrugworksMetaItems.SHAPE_GLOVE, 1);
        builder.fluidInputs(Materials.PolyvinylChloride.getFluid(144));
        builder.output(GregoriusDrugworksMetaItems.PVC_GLOVE, 1);
        builder.duration(10);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.FORGE_HAMMER_RECIPES.recipeBuilder();
        builder.input(plate, GregoriusDrugworksMaterials.Methamphetamine, 1);
        builder.output(GregoriusDrugworksMetaItems.CRYSTALMETH, 1);
        builder.duration(10);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.COMPRESSOR_RECIPES.recipeBuilder();
        builder.input(GregoriusDrugworksMetaItems.CRYSTALMETH, 9);
        builder.output(GregoriusDrugworksBlocks.METHBLOCK, 1);
        builder.duration(10);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.COMPRESSOR_RECIPES.recipeBuilder();
        builder.input(GregoriusDrugworksBlocks.METHBLOCK, 9);
        builder.output(GregoriusDrugworksBlocks.COMPRESSEDMETHBLOCK, 1);
        builder.duration(10);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        ModHandler.addShapedRecipe("methblock_craft", new ItemStack(GregoriusDrugworksBlocks.METHBLOCK), "PPP", "PPP", "PPP",
                'P', GregoriusDrugworksMetaItems.CRYSTALMETH);

        ModHandler.addShapedRecipe("compressedmethblock_craft", new ItemStack(GregoriusDrugworksBlocks.COMPRESSEDMETHBLOCK), "PPP", "PPP", "PPP",
                'P', GregoriusDrugworksBlocks.METHBLOCK);
    }
}
