package com.wurtzitane.gregoriusdrugworkspersistence.recipe.recipes;

import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksMetaItems;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.GregoriusDrugworksChancedInputSupport;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import net.minecraft.init.Items;

import static gregtech.api.GTValues.*;
import static gregtech.api.unification.ore.OrePrefix.dust;

public final class LSDRecipes {

    private LSDRecipes() {
    }

    public static void init() {
        RecipeBuilder builder;

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.SulfuricAcid.getFluid(1000), Materials.SaltWater.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.AcidifiedSaltWater.getFluid(2000));
        builder.duration(160);
        builder.circuitMeta(1);
        builder.EUt(VA[LV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.AcidifiedSaltWater.getFluid(2000), Materials.Chlorine.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.TreatedSaltWater.getFluid(2000), GregoriusDrugworksMaterials.BromineVapor.getFluid(1000));
        builder.duration(220);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.ELECTROLYZER_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.TreatedSaltWater.getFluid(1000));
        builder.output(dust, Materials.SodiumHydroxide, 3);
        builder.output(dust, Materials.Sulfur, 1);
        builder.fluidOutputs(Materials.Chlorine.getFluid(2000), Materials.Hydrogen.getFluid(1000));
        builder.duration(760);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.BromineVapor.getFluid(500), Materials.SulfurDioxide.getFluid(1000), Materials.Water.getFluid(2000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrobromicAcid.getFluid(2000), Materials.SulfuricAcid.getFluid(1000));
        builder.duration(220);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.HydrobromicAcid.getFluid(2000), Materials.Chlorine.getFluid(1000));
        builder.fluidOutputs(Materials.Bromine.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(2000));
        builder.duration(200);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Toluene.getFluid(1000), Materials.NitrationMixture.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.ParaNitrotoluene.getFluid(1000), Materials.Water.getFluid(1000),
                GregoriusDrugworksMaterials.AcidicWastewater.getFluid(500));
        builder.duration(280);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.notConsumable(dust, GregoriusDrugworksMaterials.AluminiumTrichloride, 1);
        builder.fluidInputs(Materials.Bromine.getFluid(1000), GregoriusDrugworksMaterials.ParaNitrotoluene.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenBromide.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.Material2Bromo4Nitrotoluene, 1);
        builder.duration(320);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.Material2Bromo4Nitrotoluene, 1);
        builder.fluidInputs(Materials.Hydrogen.getFluid(3000));
        builder.fluidOutputs(Materials.Water.getFluid(2000));
        builder.output(dust, GregoriusDrugworksMaterials.Material3Bromo4Methylaniline, 1);
        builder.duration(360);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.Material3Bromo4Methylaniline, 1);
        builder.fluidInputs(Materials.HydrochloricAcid.getFluid(1000), Materials.NitricAcid.getFluid(1000));
        builder.fluidOutputs(Materials.Water.getFluid(1000), GregoriusDrugworksMaterials.AcidicWastewater.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.Material3Bromo4MethylbenzenediazoniumChloride, 1);
        builder.duration(380);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.Material3Bromo4MethylbenzenediazoniumChloride, 1);
        builder.fluidInputs(Materials.Hydrogen.getFluid(1000));
        builder.fluidOutputs(Materials.Nitrogen.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Material2Bromotoluene.getFluid(1000));
        builder.duration(300);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Material2Bromotoluene.getFluid(1000), Materials.NitricAcid.getFluid(1000),
                Materials.SulfuricAcid.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.MixedBromonitrotolueneIsomers.getFluid(1000), Materials.Water.getFluid(1000),
                GregoriusDrugworksMaterials.AcidicWastewater.getFluid(1000));
        builder.duration(360);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CENTRIFUGE_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.MixedBromonitrotolueneIsomers.getFluid(4000));
        builder.output(dust, GregoriusDrugworksMaterials.Material2Bromo4Nitrotoluene, 3);
        builder.output(dust, GregoriusDrugworksMaterials.Material2Bromo3Nitrotoluene, 1);
        builder.duration(280);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.BLAST_RECIPES.recipeBuilder();
        builder.input(dust, Materials.Nickel, 1);
        builder.fluidInputs(Materials.Oxygen.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.NickelOxide, 1);
        builder.duration(200);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.BLAST_RECIPES.recipeBuilder();
        builder.input(dust, Materials.Magnesium, 1);
        builder.fluidInputs(Materials.Oxygen.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.MagnesiumOxide, 1);
        builder.duration(200);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.BLAST_RECIPES.recipeBuilder();
        builder.input(dust, Materials.Copper, 1);
        builder.fluidInputs(Materials.Oxygen.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.CopperOxide, 1);
        builder.duration(200);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, Materials.Quicklime, 1);
        builder.fluidInputs(Materials.CarbonDioxide.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.CalciumCarbonate, 1);
        builder.duration(200);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, Materials.Carbon, 1);
        builder.fluidInputs(Materials.SulfuricAcid.getFluid(1000));
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.MaterialH2SO4TreatedCarbon, 1);
        builder.duration(240);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedCatalystItemInput(builder,
                OreDictUnifier.get(dust, GregoriusDrugworksMaterials.VanadiumPentoxide, 1), 500, 0);
        builder.fluidInputs(GregoriusDrugworksMaterials.Acetylene.getFluid(2000), Materials.Oxygen.getFluid(4000));
        builder.fluidOutputs(Materials.Water.getFluid(2000));
        builder.output(dust, GregoriusDrugworksMaterials.MaleicAnhydride, 1);
        builder.duration(400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.MaleicAnhydride, 1);
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.MaleicAcid, 1);
        builder.duration(200);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.MaleicAcid, 1);
        builder.fluidInputs(Materials.Hydrogen.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.SuccinicAcid, 1);
        builder.duration(300);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedCatalystItemInput(builder, OreDictUnifier.get(dust, Materials.Rhenium, 1), 200, 0);
        builder.notConsumable(dust, GregoriusDrugworksMaterials.MaterialH2SO4TreatedCarbon, 1);
        builder.input(dust, GregoriusDrugworksMaterials.SuccinicAcid, 1);
        builder.fluidInputs(Materials.Hydrogen.getFluid(2000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Material14Butanediol.getFluid(1000), Materials.Water.getFluid(2000));
        builder.duration(420);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.AluminiumTrichloride, 1);
        builder.fluidInputs(Materials.NitricAcid.getFluid(3000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenChloride.getFluid(3000));
        builder.output(dust, GregoriusDrugworksMaterials.AluminiumNitrate, 1);
        builder.duration(280);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        builder = RecipeMaps.BLAST_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.AluminiumNitrate, 2);
        builder.output(dust, GregoriusDrugworksMaterials.Alumina, 1);
        builder.fluidOutputs(Materials.NitrogenDioxide.getFluid(6000), Materials.Oxygen.getFluid(3000));
        builder.duration(320);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.notConsumable(dust, GregoriusDrugworksMaterials.MolecularSieve4A, 1);
        builder.fluidInputs(GregoriusDrugworksMaterials.Dimethylformamide.getFluid(1000), Materials.Methanol.getFluid(2000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.DimethylformamideDimethylAcetal.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(420);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedCatalystItemInput(builder, OreDictUnifier.get(dust, GregoriusDrugworksMaterials.NickelOxide, 1), 250, 0);
        builder.notConsumable(dust, GregoriusDrugworksMaterials.Alumina, 1);
        builder.notConsumable(dust, Materials.CobaltOxide, 1);
        builder.fluidInputs(GregoriusDrugworksMaterials.Material14Butanediol.getFluid(1000), Materials.Ammonia.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Pyrolidine.getFluid(1000), Materials.Water.getFluid(2000));
        builder.duration(500);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.Material2Bromo3Nitrotoluene, 1);
        builder.notConsumable(dust, GregoriusDrugworksMaterials.Alumina, 1);
        builder.fluidInputs(GregoriusDrugworksMaterials.Pyrolidine.getFluid(1000),
                GregoriusDrugworksMaterials.DimethylformamideDimethylAcetal.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.PyrolidineMixture.getFluid(1000), Materials.Methanol.getFluid(1000));
        builder.duration(520);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Acetone.getFluid(1000), Materials.Hydrogen.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Isopropyl.getFluid(1000));
        builder.duration(200);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(Items.CLAY_BALL);
        builder.fluidInputs(Materials.Iron3Chloride.getFluid(1000));
        builder.output(GregoriusDrugworksMetaItems.MONTMORILLONITE_CLAY);
        builder.duration(200);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(GregoriusDrugworksMetaItems.MONTMORILLONITE_CLAY);
        builder.fluidInputs(GregoriusDrugworksMaterials.Isopropyl.getFluid(2000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.DiisopropylEther.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(260);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.PyrolidineMixture.getFluid(1000), GregoriusDrugworksMaterials.DiisopropylEther.getFluid(3000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.SolvatedPyrolidineMixture.getFluid(4000));
        builder.duration(220);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        builder = RecipeMaps.VACUUM_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SolvatedPyrolidineMixture.getFluid(4000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.DiisopropylEther.getFluid(3000));
        builder.output(dust, GregoriusDrugworksMaterials.NNDimethyl22Bromo6NitrophenylEthenylamine, 1);
        builder.duration(360);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, Materials.Titanium, 1);
        builder.fluidInputs(Materials.Chlorine.getFluid(3000));
        builder.output(dust, GregoriusDrugworksMaterials.TitaniumTrichloride, 1);
        builder.duration(220);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.CarbonDioxide.getFluid(1000), Materials.Ammonia.getFluid(2000), Materials.Water.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.AmmoniumCarbonate, 1);
        builder.duration(180);
        builder.EUt(VA[LV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.AmmoniumCarbonate, 1);
        builder.fluidInputs(Materials.AceticAcid.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.AmmoniumAcetate, 1);
        builder.fluidOutputs(Materials.Water.getFluid(1000), Materials.CarbonDioxide.getFluid(1000));
        builder.duration(240);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.MagnesiumOxide, 1);
        builder.fluidInputs(Materials.DilutedSulfuricAcid.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.MagnesiumSulfate, 1);
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.duration(200);
        builder.EUt(VA[LV]);
        builder.buildAndRegister();

        builder = RecipeMaps.MIXER_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.AmmoniumAcetate, 1);
        builder.input(dust, GregoriusDrugworksMaterials.TitaniumTrichloride, 1);
        builder.output(dust, GregoriusDrugworksMaterials.TiCl3AmmoniumAcetateMixture, 1);
        builder.duration(220);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.TiCl3AmmoniumAcetateMixture, 1);
        builder.input(dust, GregoriusDrugworksMaterials.NNDimethyl22Bromo6NitrophenylEthenylamine, 1);
        builder.fluidInputs(Materials.Methanol.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.LSDOrganicMixture, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.AcidicWastewater.getFluid(500));
        builder.duration(600);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CENTRIFUGE_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.LSDOrganicMixture, 1);
        builder.output(dust, GregoriusDrugworksMaterials.LSDCentrifugedOrganicMixture, 1);
        builder.fluidOutputs(Materials.Methanol.getFluid(500), GregoriusDrugworksMaterials.AcidicWastewater.getFluid(250));
        builder.duration(560);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.LSDCentrifugedOrganicMixture, 1);
        builder.fluidInputs(Materials.DilutedHydrochloricAcid.getFluid(1000), GregoriusDrugworksMaterials.DiisopropylEther.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.LSDCombinedOrganicExtract, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.AcidicWastewater.getFluid(500));
        builder.duration(380);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.VACUUM_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.LSDCombinedOrganicExtract, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Material4BromoindoleContainingOil.getFluid(1000));
        builder.duration(320);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, Materials.SiliconDioxide, 1);
        builder.fluidInputs(GregoriusDrugworksMaterials.Dichloromethane.getFluid(1000), GregoriusDrugworksMaterials.Hexane.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.ChromatographyPurifyingMixture.getFluid(2000));
        builder.duration(180);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Material4BromoindoleContainingOil.getFluid(1000),
                GregoriusDrugworksMaterials.ChromatographyPurifyingMixture.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.Material4Bromoindole, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Dichloromethane.getFluid(500), GregoriusDrugworksMaterials.Hexane.getFluid(500));
        builder.duration(420);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.CopperOxide, 1);
        builder.fluidInputs(Materials.HydrochloricAcid.getFluid(2000));
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.Copper2Chloride, 1);
        builder.duration(220);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, Materials.Palladium, 1);
        builder.fluidInputs(Materials.AquaRegia.getFluid(1000), Materials.Chlorine.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.Palladium2Chloride, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.AcidicWastewater.getFluid(500));
        builder.duration(320);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedCatalystItemInput(builder,
                OreDictUnifier.get(dust, GregoriusDrugworksMaterials.Palladium2Chloride, 1), 250, 0);
        GregoriusDrugworksChancedInputSupport.chancedCatalystItemInput(builder,
                OreDictUnifier.get(dust, GregoriusDrugworksMaterials.Copper2Chloride, 1), 250, 0);
        builder.fluidInputs(Materials.Ethylene.getFluid(1000), Materials.Water.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Acetaldehyde.getFluid(1000));
        builder.duration(360);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Acetaldehyde.getFluid(3000), Materials.SulfuricAcid.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.PolymerizedAcetaldehyde, 1);
        builder.duration(260);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.PolymerizedAcetaldehyde, 1);
        builder.input(dust, GregoriusDrugworksMaterials.CalciumCarbonate, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Paraldehyde.getFluid(1000), Materials.CarbonDioxide.getFluid(1000));
        builder.duration(320);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Paraldehyde.getFluid(1000), Materials.Ammonia.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Material5Ethyl2MethylpyridineCondensate.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(420);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.VACUUM_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Material5Ethyl2MethylpyridineCondensate.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Material5Ethyl2Methylpyridine.getFluid(1000), Materials.Water.getFluid(250));
        builder.duration(300);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, Materials.Copper, 1);
        builder.fluidInputs(Materials.NitricAcid.getFluid(2000));
        builder.output(dust, GregoriusDrugworksMaterials.CupricNitrate, 1);
        builder.fluidOutputs(Materials.NitricOxide.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(260);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.CupricNitrate, 1);
        builder.fluidInputs(Materials.NitricAcid.getFluid(1000), GregoriusDrugworksMaterials.Material5Ethyl2Methylpyridine.getFluid(2000));
        builder.output(dust, GregoriusDrugworksMaterials.CopperDiisocinchomeronate, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.AcidicWastewater.getFluid(500));
        builder.duration(420);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.CopperDiisocinchomeronate, 1);
        builder.fluidInputs(Materials.SulfuricAcid.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.IsocinchomeronicAcid, 1);
        builder.output(dust, GregoriusDrugworksMaterials.CopperOxide, 1);
        builder.duration(420);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.IsocinchomeronicAcid, 1);
        builder.input(dust, GregoriusDrugworksMaterials.Material4Bromoindole, 1);
        builder.fluidInputs(Materials.Hydrogen.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.LysergicAcid, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenBromide.getFluid(1000), Materials.CarbonDioxide.getFluid(1000));
        builder.duration(800);
        builder.EUt(VA[LuV]);
        builder.buildAndRegister();

        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.notConsumable(dust, GregoriusDrugworksMaterials.Alumina, 1);
        builder.fluidInputs(Materials.Ammonia.getFluid(1000), Materials.Ethanol.getFluid(2000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Diethylamine.getFluid(1000), Materials.Water.getFluid(2000));
        builder.duration(300);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.LysergicAcid, 1);
        builder.fluidInputs(GregoriusDrugworksMaterials.Diethylamine.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.SyntheticLSD, 1);
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.duration(900);
        builder.EUt(VA[LuV]);
        builder.buildAndRegister();
    }
}
