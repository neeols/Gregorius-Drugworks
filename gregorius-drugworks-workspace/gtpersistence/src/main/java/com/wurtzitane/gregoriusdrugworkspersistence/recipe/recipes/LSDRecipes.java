package com.wurtzitane.gregoriusdrugworkspersistence.recipe.recipes;

import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksBlocks;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksMetaItems;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.ChemicalPlantRecipeBuilder;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksRecipeMaps;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksUnificationHelper;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.GregoriusDrugworksChancedInputSupport;
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

public final class LSDRecipes {

    private LSDRecipes() {
    }

    public static void init() {
        RecipeBuilder builder;

        // acidified salt water
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.SulfuricAcid.getFluid(1000), Materials.SaltWater.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.AcidifiedSaltWater.getFluid(1000));
        builder.duration(200);
        builder.circuitMeta(1);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // bromine vapor
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.AcidifiedSaltWater.getFluid(1000), Materials.Chlorine.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.TreatedSaltWater.getFluid(1000), GregoriusDrugworksMaterials.BromineVapor.getFluid(1000));
        builder.duration(200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // put treated salt water electrolysis here

        // hydrobromic acid
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.BromineVapor.getFluid(1000), Materials.SulfurDioxide.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrobromicAcid.getFluid(1000), Materials.SulfuricAcid.getFluid(1000));
        builder.duration(200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // bromine from hydrobromic acid
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.HydrobromicAcid.getFluid(1000), Materials.Chlorine.getFluid(1000));
        builder.fluidOutputs(Materials.Bromine.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.duration(200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // nitrotoluene
        builder = RecipeMaps.MIXER_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Toluene.getFluid(220), Materials.NitrationMixture.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Material4Nitrotoluene.getFluid(1000));
        builder.duration(200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

    }
}
