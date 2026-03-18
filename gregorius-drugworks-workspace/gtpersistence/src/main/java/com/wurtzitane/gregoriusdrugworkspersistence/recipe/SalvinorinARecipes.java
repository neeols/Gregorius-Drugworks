package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksBlocks;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksItems;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksMetaItems;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksMetaTileEntities;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance.GregoriusDrugworksChancedInputSupport;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.MarkerMaterials;
import gregtech.api.unification.material.Materials;
import gregtech.common.items.MetaItems;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static gregtech.api.GTValues.*;
import static gregtech.api.unification.ore.OrePrefix.*;

public final class SalvinorinARecipes {

    private SalvinorinARecipes() {}

    private static final int DISTILLATION_COIL_CUPRONICKEL = 1800;
    private static final int DISTILLATION_COIL_KANTHAL = 2700;
    private static final int DISTILLATION_COIL_NICHROME = 3600;
    private static final int DISTILLATION_COIL_RTM = 4500;

    private static ChemicalPlantRecipeBuilder chemicalPlantBuilder(int tier) {
        return GregoriusDrugworksRecipeMaps.CHEMICAL_PLANT_RECIPES.recipeBuilder()
                .chemicalPlantTier(tier)
                .coilTemperature(minimumChemicalPlantCoilTemperature(tier));
    }

    private static RecipeBuilder chemicalPlantAtmosphere(RecipeBuilder builder, net.minecraftforge.fluids.FluidStack atmosphere) {
        if (!(builder instanceof ChemicalPlantRecipeBuilder chemicalPlantBuilder)) {
            throw new IllegalStateException("Chemical Plant atmosphere can only be applied to Chemical Plant recipes.");
        }
        return chemicalPlantBuilder.atmosphere(atmosphere);
    }

    private static int minimumChemicalPlantCoilTemperature(int tier) {
        switch (tier) {
            case MV:
                return 1800;
            case HV:
                return 2700;
            case EV:
                return 3600;
            case IV:
                return 4500;
            case LuV:
                return 5400;
            case ZPM:
                return 7200;
            case UV:
                return 9001;
            default:
                throw new IllegalArgumentException("Unsupported Chemical Plant tier: " + tier);
        }
    }

    public static void init() {
        RecipeBuilder builder;

        // sal_a_terpene_like_ketone_core
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Isoprene.getFluid(4000), Materials.Acetone.getFluid(1000));
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, Materials.SulfuricAcid.getFluid(100), 500, 0);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SalATerpeneLikeKetoneCore.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // sal_a_enone_precursor
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SalATerpeneLikeKetoneCore.getFluid(1000));
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, GregoriusDrugworksMaterials.AluminiumTrichloride, 1), 500, 0);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SalAEnonePrecursor.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // sal_a_enone_4
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SalAEnonePrecursor.getFluid(1000), Materials.Oxygen.getFluid(2000));
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.SalAEnone4, 1);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // crude_pyridine_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Acetaldehyde.getFluid(2000), GregoriusDrugworksMaterials.Formaldehyde.getFluid(1000), Materials.Ammonia.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.CrudePyridine.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // pyridinium_chlorochromate
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000), GregoriusDrugworksMaterials.Pyridine.getFluid(1000));
        builder.input(dust, Materials.ChromiumTrioxide, 1);
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.PyridiniumChlorochromate, 1);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // chloroacetic_acid_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.AceticAcid.getFluid(1000), Materials.Chlorine.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000), GregoriusDrugworksMaterials.ChloroaceticAcid.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // iodoacetic_acid_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.AceticAcid.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.PotassiumIodide, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.IodoaceticAcid.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.PotassiumChloride, 1);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // acetylene
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.CalciumCarbide, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Acetylene.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // aluminium_chloride
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Chlorine.getFluid(3000));
        builder.input(dust, Materials.Aluminium, 1);
        builder.output(dust, GregoriusDrugworksMaterials.AluminiumTrichloride, 1);
        builder.duration(800);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // methylbromide
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Methane.getFluid(1000), Materials.Bromine.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Methylbromide.getFluid(1000), GregoriusDrugworksMaterials.HydrogenBromide.getFluid(1000));
        builder.duration(800);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // methylchloride
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Methane.getFluid(1000), Materials.Chlorine.getFluid(1000));
        builder.circuitMeta(4);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Methylchloride.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.duration(800);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // hmds_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Chlorotrimethylsilane.getFluid(2000), Materials.Ammonia.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Hmds.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(2000));
        builder.duration(900);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // nahmds_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Hmds.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.SodiumHydride, 1);
        builder.fluidOutputs(Materials.Hydrogen.getFluid(1000), GregoriusDrugworksMaterials.Nahmds.getFluid(1000));
        builder.duration(900);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // methyl_triphenyl_phosphonium_bromide_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Methylbromide.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.Triphenylphosphine, 1);
        builder.output(dust, GregoriusDrugworksMaterials.MethylTriphenylPhosphoniumBromide, 1);
        builder.duration(800);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // chloroethane_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Ethylene.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Chloroethane.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // diethyl_ether_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Chloroethane.getFluid(1000), Materials.Ethanol.getFluid(1000));
        builder.input(dust, Materials.SodiumHydroxide, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.DiethylEther.getFluid(1000), Materials.Water.getFluid(1000));
        builder.output(dust, Materials.Salt, 2);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // lithium_hydride_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Hydrogen.getFluid(2000));
        builder.input(dust, Materials.Lithium, 1);
        builder.output(dust, GregoriusDrugworksMaterials.LithiumHydride, 1);
        builder.duration(1600);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // lithium_aluminium_hydride_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.LithiumHydride, 4);
        builder.input(dust, GregoriusDrugworksMaterials.AluminiumTrichloride, 1);
        builder.output(dust, GregoriusDrugworksMaterials.LithiumAluminiumHydride, 1);
        builder.output(dust, Materials.LithiumChloride, 3);
        builder.duration(1600);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // methylchloride_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Methanol.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Methylchloride.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(800);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // isobutylene_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Methanol.getFluid(1000), Materials.Propene.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Isobutylene.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // isobutanol_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Isobutylene.getFluid(1000), Materials.Water.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Isobutanol.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // ethyl_chloride_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Ethanol.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.EthylChloride.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(800);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // triethylamine_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.EthylChloride.getFluid(1000), Materials.Ammonia.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenChloride.getFluid(3000), GregoriusDrugworksMaterials.Triethylamine.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // hydrogen_fluoride_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.SulfuricAcid.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.Fluorite, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenFluoride.getFluid(2000));
        builder.output(dust, GregoriusDrugworksMaterials.CalciumSulfate, 1);
        builder.duration(400);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // potassium_fluoride_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.HydrogenFluoride.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.PotassiumHydroxide, 1);
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.PotassiumFluoride, 1);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // butyl_chloride_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Butanol.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.circuitMeta(5);
        builder.fluidOutputs(Materials.Water.getFluid(1000), GregoriusDrugworksMaterials.ButylChloride.getFluid(1000));
        builder.duration(800);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // tributylamine_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.ButylChloride.getFluid(3000), Materials.Ammonia.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenChloride.getFluid(3000), GregoriusDrugworksMaterials.Tributylamine.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // tbacl_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Tributylamine.getFluid(1000), GregoriusDrugworksMaterials.ButylChloride.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.Tbacl, 1);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // tbaf_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Thf.getFluid(500), 1000, 0);
        builder.input(dust, GregoriusDrugworksMaterials.PotassiumFluoride, 1);
        builder.input(dust, GregoriusDrugworksMaterials.Tbacl, 1);
        builder.output(dust, GregoriusDrugworksMaterials.PotassiumChloride, 1);
        builder.output(dust, GregoriusDrugworksMaterials.Tbaf, 1);
        builder.duration(900);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // tbsf_recycle
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.Tbsf, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenFluoride.getFluid(1000), GregoriusDrugworksMaterials.Tertbutyldimethylsilanol.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // anisole_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Phenol.getFluid(1000), Materials.Methanol.getFluid(1000));
        builder.fluidOutputs(Materials.Water.getFluid(1000), GregoriusDrugworksMaterials.Anisole.getFluid(1000));
        builder.circuitMeta(2);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // boric_acid_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.HydrochloricAcid.getFluid(2000));
        builder.input(dust, GregoriusDrugworksMaterials.SodiumBorate, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Brine.getFluid(2000), GregoriusDrugworksMaterials.BoricAcid.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // sodium_acetate_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.AceticAcid.getFluid(1000));
        builder.input(dust, Materials.SodiumHydroxide, 1);
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.SodiumAcetate, 1);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // sodium_methoxide_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Methanol.getFluid(1000));
        builder.input(dust, Materials.Sodium, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SodiumMethoxide.getFluid(1000), Materials.Hydrogen.getFluid(1000));
        builder.circuitMeta(4);
        builder.duration(1200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // ethylene_oxide_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Ethylene.getFluid(1000), Materials.Oxygen.getFluid(1000));
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, Materials.Silver, 1), 500, 0);
        builder.fluidOutputs(GregoriusDrugworksMaterials.EthyleneOxide.getFluid(1000));
        builder.circuitMeta(4);
        builder.duration(1200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // ethylene_glycol_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.EthyleneOxide.getFluid(1000), Materials.Water.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.EthyleneGlycol.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // ethylene_to_acetaldehyde_oxidation
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, Materials.Palladium, 1), 500, 0);
        builder.fluidInputs(Materials.Ethylene.getFluid(1000), Materials.Oxygen.getFluid(1000), Materials.Water.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Acetaldehyde.getFluid(1000));
        builder.duration(1200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // carbon_monoxide_to_acetaldehyde
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, Materials.Rhodium, 1), 500, 0);
        builder.fluidInputs(Materials.CarbonMonoxide.getFluid(2000), Materials.Hydrogen.getFluid(4000), Materials.Water.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Acetaldehyde.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(1200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // acetaldehyde_to_butanol
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Acetaldehyde.getFluid(2000), Materials.Hydrogen.getFluid(2000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Butanol.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // butanol_to_butanone
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Butanol.getFluid(1000), Materials.Oxygen.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Butanone.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // furfural_to_furan
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Furfural.getFluid(1000), Materials.Hydrogen.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Furan.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // furan_to_3_bromofuran
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Furan.getFluid(1000), Materials.Bromine.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Material3Bromofuran.getFluid(1000), GregoriusDrugworksMaterials.HydrogenBromide.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // tertbutylchloride_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Isobutylene.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Tertbutylchloride.getFluid(1000));
        builder.duration(800);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // ddq_unreduction
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Oxygen.getFluid(500));
        builder.input(dust, GregoriusDrugworksMaterials.DdqReduced, 1);
        builder.fluidOutputs(Materials.Water.getFluid(250));
        builder.output(dust, GregoriusDrugworksMaterials.Ddq, 1);
        builder.duration(1200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // vanadium_pentoxide_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Oxygen.getFluid(5000));
        builder.input(dust, Materials.Vanadium, 1);
        builder.output(dust, GregoriusDrugworksMaterials.VanadiumPentoxide, 1);
        builder.duration(1200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // 2methyl2butene_from_isobutanol
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, Materials.SulfuricAcid.getFluid(100), 500, 0);
        builder.fluidInputs(GregoriusDrugworksMaterials.Isobutanol.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Material2Methyl2Butene.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // cyclohexylamine_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Benzene.getFluid(1000), Materials.Hydrogen.getFluid(6000), Materials.Ammonia.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Cyclohexylamine.getFluid(1000));
        builder.circuitMeta(10);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // dicyclohexylurea_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Cyclohexylamine.getFluid(1000), Materials.CarbonDioxide.getFluid(1000));
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.Dicyclohexylurea, 1);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // stabilized_carrier_solution
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Water.getFluid(1000), Materials.Ethanol.getFluid(500), Materials.Glycerol.getFluid(250));
        builder.fluidOutputs(GregoriusDrugworksMaterials.StabilizedCarrierSolution.getFluid(1000));
        builder.duration(400);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // kappa_reset_concentrate
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(Items.GLOWSTONE_DUST, 1);
        builder.input(dust, GregoriusDrugworksMaterials.ActivatedCarbon, 1);
        builder.input(dust, Materials.SodiumBicarbonate, 1);
        builder.fluidInputs(GregoriusDrugworksMaterials.StabilizedCarrierSolutionWarmed.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.KappaResetConcentrate.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_terpene_like_ketone_core
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Isoprene.getFluid(4000), Materials.Acetone.getFluid(1000));
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, Materials.SulfuricAcid.getFluid(100), 500, 0);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SalATerpeneLikeKetoneCore.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // sal_a_enone_precursor
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SalATerpeneLikeKetoneCore.getFluid(1000));
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, GregoriusDrugworksMaterials.AluminiumTrichloride, 1), 500, 0);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SalAEnonePrecursor.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // sal_a_enone_4
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SalAEnonePrecursor.getFluid(1000), Materials.Oxygen.getFluid(2000));
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.SalAEnone4, 1);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // crude_pyridine_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Acetaldehyde.getFluid(2000), GregoriusDrugworksMaterials.Formaldehyde.getFluid(1000), Materials.Ammonia.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.CrudePyridine.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // pyridinium_chlorochromate
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000), GregoriusDrugworksMaterials.Pyridine.getFluid(1000));
        builder.input(dust, Materials.ChromiumTrioxide, 1);
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.PyridiniumChlorochromate, 1);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // chloroacetic_acid_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.AceticAcid.getFluid(1000), Materials.Chlorine.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000), GregoriusDrugworksMaterials.ChloroaceticAcid.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // iodoacetic_acid_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.AceticAcid.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.PotassiumIodide, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.IodoaceticAcid.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.PotassiumChloride, 1);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // acetylene
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.CalciumCarbide, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Acetylene.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // aluminium_chloride
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Chlorine.getFluid(3000));
        builder.input(dust, Materials.Aluminium, 1);
        builder.output(dust, GregoriusDrugworksMaterials.AluminiumTrichloride, 1);
        builder.duration(800);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // methylbromide
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Methane.getFluid(1000), Materials.Bromine.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Methylbromide.getFluid(1000), GregoriusDrugworksMaterials.HydrogenBromide.getFluid(1000));
        builder.duration(800);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // methylchloride
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Methane.getFluid(1000), Materials.Chlorine.getFluid(1000));
        builder.circuitMeta(4);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Methylchloride.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.duration(800);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // hmds_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Chlorotrimethylsilane.getFluid(2000), Materials.Ammonia.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Hmds.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(2000));
        builder.duration(900);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // nahmds_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Hmds.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.SodiumHydride, 1);
        builder.fluidOutputs(Materials.Hydrogen.getFluid(1000), GregoriusDrugworksMaterials.Nahmds.getFluid(1000));
        builder.duration(900);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // methyl_triphenyl_phosphonium_bromide_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Methylbromide.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.Triphenylphosphine, 1);
        builder.output(dust, GregoriusDrugworksMaterials.MethylTriphenylPhosphoniumBromide, 1);
        builder.duration(800);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // chloroethane_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Ethylene.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Chloroethane.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // diethyl_ether_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Chloroethane.getFluid(1000), Materials.Ethanol.getFluid(1000));
        builder.input(dust, Materials.SodiumHydroxide, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.DiethylEther.getFluid(1000), Materials.Water.getFluid(1000));
        builder.output(dust, Materials.Salt, 2);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // lithium_hydride_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Hydrogen.getFluid(2000));
        builder.input(dust, Materials.Lithium, 1);
        builder.output(dust, GregoriusDrugworksMaterials.LithiumHydride, 1);
        builder.duration(1600);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // lithium_aluminium_hydride_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.LithiumHydride, 4);
        builder.input(dust, GregoriusDrugworksMaterials.AluminiumTrichloride, 1);
        builder.output(dust, GregoriusDrugworksMaterials.LithiumAluminiumHydride, 1);
        builder.output(dust, Materials.LithiumChloride, 3);
        builder.duration(1600);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // methylchloride_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Methanol.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Methylchloride.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(800);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // isobutylene_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Methanol.getFluid(1000), Materials.Propene.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Isobutylene.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // isobutanol_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Isobutylene.getFluid(1000), Materials.Water.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Isobutanol.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // ethyl_chloride_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Ethanol.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.EthylChloride.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(800);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // triethylamine_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.EthylChloride.getFluid(1000), Materials.Ammonia.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenChloride.getFluid(3000), GregoriusDrugworksMaterials.Triethylamine.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // hydrogen_fluoride_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.SulfuricAcid.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.Fluorite, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenFluoride.getFluid(2000));
        builder.output(dust, GregoriusDrugworksMaterials.CalciumSulfate, 1);
        builder.duration(400);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // potassium_fluoride_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.HydrogenFluoride.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.PotassiumHydroxide, 1);
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.PotassiumFluoride, 1);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // butyl_chloride_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Butanol.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.circuitMeta(5);
        builder.fluidOutputs(Materials.Water.getFluid(1000), GregoriusDrugworksMaterials.ButylChloride.getFluid(1000));
        builder.duration(800);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // tributylamine_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.ButylChloride.getFluid(3000), Materials.Ammonia.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenChloride.getFluid(3000), GregoriusDrugworksMaterials.Tributylamine.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // tbacl_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Tributylamine.getFluid(1000), GregoriusDrugworksMaterials.ButylChloride.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.Tbacl, 1);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // tbaf_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Thf.getFluid(500), 1000, 0);
        builder.input(dust, GregoriusDrugworksMaterials.PotassiumFluoride, 1);
        builder.input(dust, GregoriusDrugworksMaterials.Tbacl, 1);
        builder.output(dust, GregoriusDrugworksMaterials.PotassiumChloride, 1);
        builder.output(dust, GregoriusDrugworksMaterials.Tbaf, 1);
        builder.duration(900);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // tbsf_recycle
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.Tbsf, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenFluoride.getFluid(1000), GregoriusDrugworksMaterials.Tertbutyldimethylsilanol.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // anisole_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Phenol.getFluid(1000), Materials.Methanol.getFluid(1000));
        builder.fluidOutputs(Materials.Water.getFluid(1000), GregoriusDrugworksMaterials.Anisole.getFluid(1000));
        builder.circuitMeta(2);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // boric_acid_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.HydrochloricAcid.getFluid(2000));
        builder.input(dust, GregoriusDrugworksMaterials.SodiumBorate, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Brine.getFluid(2000), GregoriusDrugworksMaterials.BoricAcid.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // sodium_acetate_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.AceticAcid.getFluid(1000));
        builder.input(dust, Materials.SodiumHydroxide, 1);
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.SodiumAcetate, 1);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // sodium_methoxide_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Methanol.getFluid(1000));
        builder.input(dust, Materials.Sodium, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SodiumMethoxide.getFluid(1000), Materials.Hydrogen.getFluid(1000));
        builder.circuitMeta(4);
        builder.duration(1200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // ethylene_oxide_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Ethylene.getFluid(1000), Materials.Oxygen.getFluid(1000));
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, Materials.Silver, 1), 500, 0);
        builder.fluidOutputs(GregoriusDrugworksMaterials.EthyleneOxide.getFluid(1000));
        builder.circuitMeta(4);
        builder.duration(1200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // ethylene_glycol_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.EthyleneOxide.getFluid(1000), Materials.Water.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.EthyleneGlycol.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // ethylene_to_acetaldehyde_oxidation
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, Materials.Palladium, 1), 500, 0);
        builder.fluidInputs(Materials.Ethylene.getFluid(1000), Materials.Oxygen.getFluid(1000), Materials.Water.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Acetaldehyde.getFluid(1000));
        builder.duration(1200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // carbon_monoxide_to_acetaldehyde
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, Materials.Rhodium, 1), 500, 0);
        builder.fluidInputs(Materials.CarbonMonoxide.getFluid(2000), Materials.Hydrogen.getFluid(4000), Materials.Water.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Acetaldehyde.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(1200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // acetaldehyde_to_butanol
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Acetaldehyde.getFluid(2000), Materials.Hydrogen.getFluid(2000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Butanol.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // butanol_to_butanone
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Butanol.getFluid(1000), Materials.Oxygen.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Butanone.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // furfural_to_furan
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Furfural.getFluid(1000), Materials.Hydrogen.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Furan.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // furan_to_3_bromofuran
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Furan.getFluid(1000), Materials.Bromine.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Material3Bromofuran.getFluid(1000), GregoriusDrugworksMaterials.HydrogenBromide.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // tertbutylchloride_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Isobutylene.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Tertbutylchloride.getFluid(1000));
        builder.duration(800);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // ddq_unreduction
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Oxygen.getFluid(500));
        builder.input(dust, GregoriusDrugworksMaterials.DdqReduced, 1);
        builder.fluidOutputs(Materials.Water.getFluid(250));
        builder.output(dust, GregoriusDrugworksMaterials.Ddq, 1);
        builder.duration(1200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // vanadium_pentoxide_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Oxygen.getFluid(5000));
        builder.input(dust, Materials.Vanadium, 1);
        builder.output(dust, GregoriusDrugworksMaterials.VanadiumPentoxide, 1);
        builder.duration(1200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // 2methyl2butene_from_isobutanol
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, Materials.SulfuricAcid.getFluid(100), 500, 0);
        builder.fluidInputs(GregoriusDrugworksMaterials.Isobutanol.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Material2Methyl2Butene.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // cyclohexylamine_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Benzene.getFluid(1000), Materials.Hydrogen.getFluid(6000), Materials.Ammonia.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Cyclohexylamine.getFluid(1000));
        builder.circuitMeta(10);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // dicyclohexylurea_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Cyclohexylamine.getFluid(1000), Materials.CarbonDioxide.getFluid(1000));
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.Dicyclohexylurea, 1);
        builder.duration(600);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // ethyl_iodoacetate_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, Materials.SulfuricAcid.getFluid(250), 2000, 0);
        builder.fluidInputs(GregoriusDrugworksMaterials.IodoaceticAcid.getFluid(1000), Materials.Ethanol.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.EthylIodoacetate.getFluid(1000), Materials.Water.getFluid(1000));
        builder.chancedFluidOutput(GregoriusDrugworksMaterials.EthylAcetate.getFluid(50), 500, 0);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // butanediol_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Acetylene.getFluid(1000), GregoriusDrugworksMaterials.Formaldehyde.getFluid(2000), Materials.Hydrogen.getFluid(2000));
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, Materials.Copper, 1), 500, 0);
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, Materials.Nickel, 1), 500, 0);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Material14Butanediol.getFluid(1000));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // thf_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, Materials.SulfuricAcid.getFluid(250), 2000, 0);
        builder.fluidInputs(GregoriusDrugworksMaterials.Material14Butanediol.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Thf.getFluid(1000), Materials.Water.getFluid(1000));
        builder.chancedFluidOutput(Materials.Butene.getFluid(50), 1000, 0);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // butene_oligomerisation_to_1_hexene
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Butene.getFluid(2000));
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, Materials.Nickel, 1), 500, 0);
        builder.circuitMeta(1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Material1Hexene.getFluid(1000), Materials.Ethylene.getFluid(1000));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // hexane_hydrogenation
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Material1Hexene.getFluid(1000), Materials.Hydrogen.getFluid(2000));
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, Materials.Nickel, 1), 500, 0);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Hexane.getFluid(1000));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // 2methyl2butene_isomerisation
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Butene.getFluid(1000));
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, GregoriusDrugworksMaterials.AluminiumTrichloride, 1), 500, 0);
        builder.circuitMeta(2);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Material2Methyl2Butene.getFluid(1000));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // ethyl_acetate_to_ethyl_iodoacetate
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, Materials.Iodine, 1), 5000, 0);
        builder.fluidInputs(GregoriusDrugworksMaterials.EthylAcetate.getFluid(1000), Materials.Oxygen.getFluid(1000));
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, Materials.Phosphorus, 1), 500, 0);
        builder.fluidOutputs(GregoriusDrugworksMaterials.EthylIodoacetate.getFluid(1000), Materials.Water.getFluid(1000));
        builder.chancedFluidOutput(Materials.Butene.getFluid(50), 1000, 0);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // chlorotrimethylsilane
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Methylchloride.getFluid(3000), Materials.Chlorine.getFluid(1000));
        builder.input(dust, Materials.Silicon, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Chlorotrimethylsilane.getFluid(1000), Materials.Hydrogen.getFluid(500));
        builder.chancedFluidOutput(GregoriusDrugworksMaterials.SiliconTetrachloride.getFluid(250), 500, 200);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // triphenylphosphine_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Benzene.getFluid(3000), GregoriusDrugworksMaterials.PhosphorusTrichloride.getFluid(1000), Materials.Hydrogen.getFluid(3000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenChloride.getFluid(3000));
        builder.output(dust, GregoriusDrugworksMaterials.Triphenylphosphine, 1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // tertbutyldimethylsilanol_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Methylchloride.getFluid(2000), GregoriusDrugworksMaterials.Isobutylene.getFluid(1000), Materials.Water.getFluid(1000));
        builder.input(dust, Materials.Silicon, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Tertbutyldimethylsilanol.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(2000));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // tbscl_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.ThionylChloride.getFluid(1000), GregoriusDrugworksMaterials.Tertbutyldimethylsilanol.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Tbscl.getFluid(1000), Materials.SulfurDioxide.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.duration(1800);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // aminopyridine_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Pyridine.getFluid(1000), Materials.Ammonia.getFluid(1000), Materials.Hydrogen.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Aminopyridine.getFluid(1000), Materials.Water.getFluid(1000));
        builder.circuitMeta(1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // dmap_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Aminopyridine.getFluid(1000), GregoriusDrugworksMaterials.Methylchloride.getFluid(2000));
        builder.output(dust, GregoriusDrugworksMaterials.Dmap, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenChloride.getFluid(2000));
        builder.circuitMeta(1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // methoxybenzyl_alcohol_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Anisole.getFluid(1000), Materials.Hydrogen.getFluid(1000), GregoriusDrugworksMaterials.Formaldehyde.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.MethoxybenzylAlcohol.getFluid(1000));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // 4_methoxybenzyl_chloride_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.MethoxybenzylAlcohol.getFluid(1000), GregoriusDrugworksMaterials.ThionylChloride.getFluid(1000));
        builder.fluidOutputs(Materials.SulfurDioxide.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000), GregoriusDrugworksMaterials.Material4MethoxybenzylChloride.getFluid(1000));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // borane_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, Materials.Magnesium, 3);
        builder.fluidInputs(Materials.Hydrogen.getFluid(6000), GregoriusDrugworksMaterials.BoricAcid.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Borane.getFluid(1000), Materials.Water.getFluid(3000));
        builder.output(dust, GregoriusDrugworksMaterials.MagnesiumOxide, 3);
        builder.duration(1800);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sodium_dichromate_solution_lcr
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, Materials.ChromiumTrioxide, 2);
        builder.input(dust, Materials.SodiumHydroxide, 2);
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.SodiumDichromateSolution.getFluid(2000));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // pyridinium_dichromate_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SodiumDichromateSolution.getFluid(1000), GregoriusDrugworksMaterials.Pyridine.getFluid(1000), Materials.SulfuricAcid.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Brine.getFluid(2000));
        builder.output(dust, GregoriusDrugworksMaterials.PyridiniumDichromate, 2);
        builder.circuitMeta(3);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sodium_aluminate_solution_lcr
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.Alumina, 1);
        builder.input(dust, Materials.SodiumHydroxide, 2);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SodiumAluminateSolution.getFluid(1000));
        builder.circuitMeta(7);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sodium_silicate_solution_lcr
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.SiliconDioxide, 1);
        builder.input(dust, Materials.SodiumHydroxide, 2);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SodiumSilicateSolution.getFluid(1000));
        builder.circuitMeta(7);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // p_toluenesulfonic_acid_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Water.getFluid(1000), Materials.SulfurTrioxide.getFluid(1000), Materials.Toluene.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.PToluenesulfonicAcid, 1);
        builder.circuitMeta(2);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // 2ethyl2methyl13dioxolane_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Butanone.getFluid(1000), GregoriusDrugworksMaterials.EthyleneGlycol.getFluid(1000));
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, GregoriusDrugworksMaterials.PToluenesulfonicAcid, 1), 500, 0);
        builder.fluidOutputs(Materials.Water.getFluid(1000), GregoriusDrugworksMaterials.Material2Ethyl2Methyl13Dioxolane.getFluid(1000));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // biomass_fractioning
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Biomass.getFluid(4000), Materials.Steam.getFluid(4000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.HemicelluloseSlurry.getFluid(2000));
        builder.output(dust, GregoriusDrugworksMaterials.CellulosePulp, 2);
        builder.output(dust, GregoriusDrugworksMaterials.LigninResidue, 1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // pentose_solution
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.HemicelluloseSlurry.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(250));
        builder.fluidOutputs(GregoriusDrugworksMaterials.PentoseSolution.getFluid(750), GregoriusDrugworksMaterials.AcidicWastewater.getFluid(1000));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // acidic_wastewater_processing
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.AcidicWastewater.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.CalciumHydroxide, 1);
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.output(dust, Materials.Gypsum, 1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // t_buli_in_pentane_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Pentane.getFluid(1000), GregoriusDrugworksMaterials.Tertbutylchloride.getFluid(1000));
        builder.input(dust, Materials.Lithium, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.TBuliInPentane.getFluid(1000));
        builder.output(dust, Materials.LithiumChloride, 1);
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // 1_4_benzoquinone_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, GregoriusDrugworksMaterials.VanadiumPentoxide, 1), 500, 0);
        builder.fluidInputs(Materials.Benzene.getFluid(1000), Materials.Oxygen.getFluid(2000));
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        ItemStack benzoquinone = GregoriusDrugworksUnificationHelper.get(dust,
                GregoriusDrugworksMaterials.Material14Benzoquinone, 1);
        if (benzoquinone.isEmpty()) {
            throw new IllegalStateException("Failed to resolve dust output for 1,4-Benzoquinone.");
        }
        builder.outputs(benzoquinone);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // ddq_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, Materials.Copper, 1), 500, 0);
        builder.fluidInputs(GregoriusDrugworksMaterials.Material14Benzoquinone.getFluid(1000), GregoriusDrugworksMaterials.HydrogenCyanide.getFluid(2000), Materials.Chlorine.getFluid(1000));
        builder.fluidOutputs(Materials.Hydrogen.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.Ddq, 1);
        builder.duration(1800);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // dcc_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.ThionylChloride.getFluid(500));
        builder.input(dust, GregoriusDrugworksMaterials.Dicyclohexylurea, 1);
        builder.fluidOutputs(Materials.SulfurDioxide.getFluid(500), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(500));
        builder.output(dust, GregoriusDrugworksMaterials.Dcc, 1);
        builder.duration(1800);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // triethylsilanol_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.EthylChloride.getFluid(3000), Materials.Water.getFluid(1000));
        builder.input(dust, Materials.Silicon, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenChloride.getFluid(3000), GregoriusDrugworksMaterials.Triethylsilanol.getFluid(1000));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // tescl_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Triethylsilanol.getFluid(1000), GregoriusDrugworksMaterials.ThionylChloride.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Tescl.getFluid(1000), Materials.SulfurDioxide.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.duration(1800);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // mchlorobenzoic_acid_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Chlorobenzene.getFluid(1000), Materials.Oxygen.getFluid(2000), Materials.Water.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.MchlorobenzoicAcid.getFluid(1000));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // mchloroperoxybenzoic_acid_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.MchlorobenzoicAcid.getFluid(1000), GregoriusDrugworksMaterials.HydrogenPeroxide.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.MchloroperoxybenzoicAcid.getFluid(1000), Materials.Water.getFluid(1000));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // washed_cpba_in_toluene
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.CpbaToluene.getFluid(1000), GregoriusDrugworksMaterials.SodiumBicarbonateSolution.getFluid(250));
        builder.fluidOutputs(GregoriusDrugworksMaterials.WashedCpbaToluene.getFluid(1000), Materials.Water.getFluid(250), Materials.CarbonDioxide.getFluid(250));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // diad_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Isopropanol.getFluid(2000), Materials.CarbonDioxide.getFluid(2000), Materials.Ammonia.getFluid(1000), Materials.Oxygen.getFluid(2000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Diad.getFluid(1000), Materials.Water.getFluid(3000));
        builder.duration(1800);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // thionyl_chloride_synthesis
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Chlorine.getFluid(1000), Materials.SulfurDioxide.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.ThionylChloride.getFluid(1000));
        builder.chancedFluidOutput(GregoriusDrugworksMaterials.HydrogenChloride.getFluid(250), 1000, 0);
        builder.chancedFluidOutput(GregoriusDrugworksMaterials.SulfurylChloride.getFluid(100), 500, 0);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_mother_liquor_processing
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SalAMotherLiquorE4ToA5.getFluid(15), Materials.Water.getFluid(250));
        builder.input(dust, Materials.AmmoniumChloride, 1);
        builder.fluidOutputs(Materials.Ammonia.getFluid(1000), Materials.SaltWater.getFluid(250));
        builder.output(dust, GregoriusDrugworksMaterials.PotassiumChloride, 1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // pyridine_hydrochloride_processing
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.PyridineHydrochloride, 1);
        builder.input(dust, Materials.SodiumHydroxide, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.CrudePyridineSolution.getFluid(2000));
        builder.output(dust, GregoriusDrugworksMaterials.SodiumChloride, 1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // pyridine_water_azeotrope_processing
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.PyridineWaterAzeotrope.getFluid(400));
        builder.input(dust, Materials.Quicklime, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Pyridine.getFluid(180), GregoriusDrugworksMaterials.PyridineWasteWater.getFluid(250));
        builder.output(dust, GregoriusDrugworksMaterials.CalciumHydroxide, 1);
        builder.output(dustTiny, Materials.Salt, 1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // settle_pyridine_salt_waste_slurry_and_pyridine_waste_water
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.PyridineSaltWasteSlurry.getFluid(700), GregoriusDrugworksMaterials.PyridineWasteWater.getFluid(250));
        builder.fluidOutputs(GregoriusDrugworksMaterials.PyridineWasteBrine.getFluid(600), GregoriusDrugworksMaterials.PyridineWasteSludge.getFluid(200), GregoriusDrugworksMaterials.PyridineTreatedWater.getFluid(150));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_ester_7_to_spiro_ketone_carboxylate_intermediate
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.SalAEster7, 1);
        builder.input(dust, Materials.SodiumHydroxide, 1);
        builder.fluidOutputs(Materials.Ethanol.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.SalASpiroKetoneCarboxylateIntermediate, 1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // settle_sal_a_waste_water_e5_to_e6_and_7
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SalAWasteWaterE5ToE6And7.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.SalAOrganicResidueE5ToE6And7.getFluid(100), GregoriusDrugworksMaterials.SalAWasteSludgeE5ToE6And7.getFluid(200), GregoriusDrugworksMaterials.SalATreatedWaterE5ToE6And7.getFluid(700));
        builder.duration(3000);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_acidic_acqueous_layer_d6_to_d8_processing
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SalAAcidicAcqueousLayerD6ToD8.getFluid(1200), Materials.Water.getFluid(300));
        builder.input(dust, Materials.SodiumBicarbonate, 1);
        builder.fluidOutputs(Materials.CarbonDioxide.getFluid(1000), GregoriusDrugworksMaterials.Brine.getFluid(1500));
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SodiumChloride, 1, 2500, 0);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // triphenylphosphine_oxide_to_triphenylphosphine
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.ThionylChloride.getFluid(250), 8000, 0);
        builder.fluidInputs(Materials.Hydrogen.getFluid(2000));
        builder.input(dust, GregoriusDrugworksMaterials.TriphenylphosphineOxide, 1);
        builder.fluidOutputs(Materials.SulfurDioxide.getFluid(250), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(500));
        builder.output(dust, GregoriusDrugworksMaterials.Triphenylphosphine, 1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // pyridinium_salt_waste_recycle
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.PyridiniumSaltWaste, 1);
        builder.input(dust, Materials.SodiumHydroxide, 2);
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Brine.getFluid(1000), GregoriusDrugworksMaterials.Pyridine.getFluid(1000));
        builder.output(dust, Materials.Salt, 1);
        builder.chancedOutput(dustTiny, GregoriusDrugworksMaterials.ChromiumOxide, 1, 1000, 0);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_wittig_organic_waste_d8_to_b9_processing
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.SalAWittigOrganicWasteD8ToB9, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Hmds.getFluid(100));
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SodiumBromide, 1, 5000, 0);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.TriphenylphosphineOxide, 1, 5000, 0);
        builder.output(dust, Materials.Carbon, 1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_crude_organic_residue_d8_to_b9_processing
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.SodiumSulfate, 1);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.EthylAcetate.getFluid(500), 1000, 0);
        builder.fluidInputs(GregoriusDrugworksMaterials.SalACrudeOrganicResidueD8ToB9.getFluid(200), GregoriusDrugworksMaterials.AmmoniumChlorideSolution.getFluid(500), GregoriusDrugworksMaterials.Brine.getFluid(500));
        builder.fluidOutputs(GregoriusDrugworksMaterials.SalAContaminatedWasteWaterD8ToB9.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.SalABisolefin9, 1);
        builder.output(dust, GregoriusDrugworksMaterials.SodiumSulfateHydrated, 1);
        builder.output(dust, GregoriusDrugworksMaterials.SalAMixedSaltsD8ToB9, 1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // triethylammonium_chloride_to_triethylamine
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.notConsumable(Materials.Water.getFluid(1000));
        builder.input(dust, Materials.SodiumHydroxide, 1);
        builder.input(dust, GregoriusDrugworksMaterials.TriethylammoniumChloride, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Triethylamine.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.SodiumChloride, 1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_aldehyde_base_waste_11_3_to_12_processing
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.SalAAldehydeBaseWaste113To12, 1);
        builder.fluidInputs(Materials.Oxygen.getFluid(1000));
        builder.fluidOutputs(Materials.CarbonDioxide.getFluid(1000), Materials.Water.getFluid(500));
        builder.output(dustTiny, Materials.Ash, 1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // aluminium_chloride_dust
        builder = RecipeMaps.BLAST_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Chlorine.getFluid(1000));
        builder.input(dust, Materials.Aluminium, 1);
        builder.output(dust, GregoriusDrugworksMaterials.AluminiumTrichloride, 1);
        builder.circuitMeta(3);
        builder.duration(4200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // calcium_carbide_dust
        builder = RecipeMaps.BLAST_RECIPES.recipeBuilder();
        builder.input(dust, Materials.Quicklime, 1);
        builder.input(dust, Materials.Coke, 1);
        builder.output(dust, GregoriusDrugworksMaterials.CalciumCarbide, 1);
        builder.circuitMeta(3);
        builder.duration(4200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // hydrogen_chloride_dust
        builder = RecipeMaps.BLAST_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Hydrogen.getFluid(1000));
        builder.input(dust, Materials.Chlorine, 1);
        builder.output(dust, GregoriusDrugworksMaterials.HydrogenChloride, 1);
        builder.duration(4200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // chromium_ingot_from_chromium_trioxide_1
        builder = RecipeMaps.BLAST_RECIPES.recipeBuilder();
        builder.input(dust, Materials.ChromiumTrioxide, 1);
        builder.input(dust, Materials.Carbon, 3);
        builder.output(ingot, Materials.Chrome, 2);
        builder.fluidOutputs(Materials.CarbonMonoxide.getFluid(3000));
        builder.duration(4200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // chromium_ingot_from_chromium_trioxide_2
        builder = RecipeMaps.BLAST_RECIPES.recipeBuilder();
        builder.input(dust, Materials.ChromiumTrioxide, 1);
        builder.input(dust, Materials.Coke, 2);
        builder.output(ingot, Materials.Chrome, 2);
        builder.fluidOutputs(Materials.CarbonMonoxide.getFluid(3000));
        builder.duration(4200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // zeolite_a_gel_to_molecular_sieve_4a_in_ebf
        builder = RecipeMaps.BLAST_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.ZeoliteAGel.getFluid(1000));
        builder.fluidOutputs(Materials.Steam.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.MolecularSieve4A, 1);
        builder.circuitMeta(7);
        builder.duration(4200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sodium_sulfate_dehydration
        builder = RecipeMaps.BLAST_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.SodiumSulfateHydrated, 1);
        builder.fluidOutputs(Materials.Steam.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.SodiumSulfate, 1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // hydrogen_chloride
        builder = RecipeMaps.EXTRACTOR_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.HydrogenChloride, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.HydrogenChloride.getFluid(1000));
        builder.duration(400);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // 1_4_benzoquinone_extract
        builder = RecipeMaps.EXTRACTOR_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.Material14Benzoquinone, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Material14Benzoquinone.getFluid(1000));
        builder.duration(400);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // crude_pyridine_distillation
        builder = GregoriusDrugworksRecipeMaps.distillationUnitBuilder(DISTILLATION_COIL_CUPRONICKEL);
        builder.fluidInputs(GregoriusDrugworksMaterials.CrudePyridine.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Pyridine.getFluid(800), GregoriusDrugworksMaterials.PyridineResidue.getFluid(200));
        builder.duration(2400);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // pentose_solution_distillation
        builder = GregoriusDrugworksRecipeMaps.distillationUnitBuilder(DISTILLATION_COIL_KANTHAL);
        builder.fluidInputs(GregoriusDrugworksMaterials.PentoseSolution.getFluid(1500));
        builder.fluidOutputs(Materials.Water.getFluid(900), GregoriusDrugworksMaterials.Furfural.getFluid(600));
        builder.output(dust, GregoriusDrugworksMaterials.Humins, 1);
        builder.duration(2400);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // crude_pyridine_solution_distillation
        builder = GregoriusDrugworksRecipeMaps.distillationUnitBuilder(DISTILLATION_COIL_KANTHAL);
        builder.fluidInputs(GregoriusDrugworksMaterials.CrudePyridineSolution.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Pyridine.getFluid(400), GregoriusDrugworksMaterials.PyridineWaterAzeotrope.getFluid(200), GregoriusDrugworksMaterials.PyridineOrganicResidue.getFluid(50), GregoriusDrugworksMaterials.PyridineSaltWasteSlurry.getFluid(350));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // pyridine_treated_water_processing
        builder = GregoriusDrugworksRecipeMaps.distillationUnitBuilder(DISTILLATION_COIL_CUPRONICKEL);
        builder.fluidInputs(GregoriusDrugworksMaterials.PyridineTreatedWater.getFluid(150), GregoriusDrugworksMaterials.PyridineWasteWater.getFluid(100));
        builder.fluidOutputs(Materials.DistilledWater.getFluid(200));
        builder.output(dustTiny, Materials.Salt, 1);
        builder.duration(2400);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_organic_residue_e5_to_e6_and_7_processing
        builder = GregoriusDrugworksRecipeMaps.distillationUnitBuilder(DISTILLATION_COIL_NICHROME);
        builder.fluidInputs(GregoriusDrugworksMaterials.SalAOrganicResidueE5ToE6And7.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.EthylAcetate.getFluid(400), GregoriusDrugworksMaterials.Hexane.getFluid(400), GregoriusDrugworksMaterials.Thf.getFluid(100), GregoriusDrugworksMaterials.SalAHeavyOrganicResidueE5ToE6And7.getFluid(100));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_contaminated_waste_water_d8_to_b9_distillation
        builder = GregoriusDrugworksRecipeMaps.distillationUnitBuilder(DISTILLATION_COIL_KANTHAL);
        builder.fluidInputs(GregoriusDrugworksMaterials.SalAContaminatedWasteWaterD8ToB9.getFluid(1000));
        builder.fluidOutputs(Materials.Water.getFluid(800), Materials.Ammonia.getFluid(100));
        builder.output(dust, Materials.AmmoniumChloride, 1);
        builder.output(dust, GregoriusDrugworksMaterials.SodiumChloride, 1);
        builder.output(dust, GregoriusDrugworksMaterials.SalAMixedSaltsD8ToB9, 1);
        builder.duration(2400);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_furyl_organic_waste_14_to_15_and_16_distillation
        builder = GregoriusDrugworksRecipeMaps.distillationUnitBuilder(DISTILLATION_COIL_RTM);
        builder.input(dust, GregoriusDrugworksMaterials.SalAFurylOrganicWaste14To15And16, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Material3Bromofuran.getFluid(250), GregoriusDrugworksMaterials.Furan.getFluid(250), GregoriusDrugworksMaterials.SalAHeavyOrganicResidue14To15And16.getFluid(500));
        builder.duration(2400);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_anisole_waste_fluid_17_to_17_2_distillation
        builder = GregoriusDrugworksRecipeMaps.distillationUnitBuilder(DISTILLATION_COIL_NICHROME);
        builder.fluidInputs(GregoriusDrugworksMaterials.SalAAnisoleWasteFluid17To172.getFluid(500));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Anisole.getFluid(400), Materials.Toluene.getFluid(300), GregoriusDrugworksMaterials.SalAHeavyAromaticResidue17To172.getFluid(300));
        builder.duration(2400);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_triphenyl_like_aromatics_distillation
        builder = GregoriusDrugworksRecipeMaps.distillationUnitBuilder(DISTILLATION_COIL_RTM);
        builder.fluidInputs(GregoriusDrugworksMaterials.SalATriphenylLikeAromatics.getFluid(1000));
        builder.fluidOutputs(Materials.Benzene.getFluid(300), Materials.Toluene.getFluid(300), Materials.Chlorobenzene.getFluid(100), GregoriusDrugworksMaterials.SalAHeavyAromaticResidue.getFluid(300));
        builder.duration(2400);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // pyridine_residue_centrifuge
        builder = RecipeMaps.CENTRIFUGE_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.PyridineResidue.getFluid(100));
        builder.fluidOutputs(GregoriusDrugworksMaterials.PyridineWasteWater.getFluid(200));
        builder.output(dust, Materials.Carbon, 1);
        builder.duration(300);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // pyridine_organic_residue_processing
        builder = RecipeMaps.CENTRIFUGE_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.PyridineOrganicResidue.getFluid(100));
        builder.fluidOutputs(GregoriusDrugworksMaterials.PyridineWasteWater.getFluid(50));
        builder.output(dust, Materials.Carbon, 1);
        builder.output(dustTiny, Materials.Salt, 1);
        builder.duration(300);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // sal_a_silylation_organic_waste_b9_to_d10_centrifuge
        builder = RecipeMaps.CENTRIFUGE_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.SalASilylationOrganicWasteB9ToD10, 1);
        builder.fluidOutputs(Materials.Hydrogen.getFluid(250), Materials.Methane.getFluid(250));
        builder.output(dust, Materials.Carbon, 1);
        builder.duration(300);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // sal_a_silylation_organic_waste_d10_to_10_2_centrifuge
        builder = RecipeMaps.CENTRIFUGE_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.SalASilylationOrganicWasteD10To102, 1);
        builder.output(dust, Materials.Carbon, 1);
        builder.output(dustTiny, GregoriusDrugworksMaterials.SiliconDioxide, 2);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.TriethylammoniumChloride, 1, 2500, 0);
        builder.chancedFluidOutput(GregoriusDrugworksMaterials.Tbscl.getFluid(50), 1000, 0);
        builder.duration(300);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // sal_a_pdc_13_2_to_14_centrifuge
        builder = RecipeMaps.CENTRIFUGE_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.SalAPdc132To14, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SalATriphenylLikeAromatics.getFluid(1000));
        builder.output(dust, Materials.Carbon, 1);
        builder.duration(300);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // sal_a_11_2_to_11_3_chromium_oxidation_organic_waste_to_triphenyl_like_aromatics_centrifuge
        builder = RecipeMaps.CENTRIFUGE_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.SalAChromiumOxidationOrganicWaste112To113, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SalATriphenylLikeAromatics.getFluid(1000));
        builder.output(dust, Materials.Carbon, 1);
        builder.duration(300);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // nahmds_in_thf
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Nahmds.getFluid(1000), GregoriusDrugworksMaterials.Thf.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Nahmdsinthf.getFluid(2000));
        builder.duration(200);
        builder.EUt(VA[LV]);
        builder.buildAndRegister();

        // brine_mixer
        builder = RecipeMaps.MIXER_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.input(dust, Materials.Salt, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Brine.getFluid(1000));
        builder.duration(200);
        builder.EUt(VA[LV]);
        builder.buildAndRegister();

        // ammonium_chloride_solution_mixer
        builder = RecipeMaps.MIXER_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.input(dust, Materials.AmmoniumChloride, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.AmmoniumChlorideSolution.getFluid(1000));
        builder.duration(200);
        builder.EUt(VA[LV]);
        builder.buildAndRegister();

        // borane_thf_mixer
        builder = RecipeMaps.MIXER_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Borane.getFluid(1000), GregoriusDrugworksMaterials.Thf.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.BoraneThf.getFluid(2000));
        builder.duration(200);
        builder.EUt(VA[LV]);
        builder.buildAndRegister();

        // zeolite_a_gel_mixing
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SodiumSilicateSolution.getFluid(1000), GregoriusDrugworksMaterials.SodiumAluminateSolution.getFluid(1000), Materials.Water.getFluid(2000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.ZeoliteAGel.getFluid(2000));
        builder.circuitMeta(7);
        builder.duration(200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // 5_percent_aqueous_acetone_mixer
        builder = RecipeMaps.MIXER_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Acetone.getFluid(950), Materials.Water.getFluid(50));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Material5AqueousAcetone.getFluid(1000));
        builder.circuitMeta(5);
        builder.duration(200);
        builder.EUt(VA[LV]);
        builder.buildAndRegister();

        // cpba_in_toluene_mixer
        builder = RecipeMaps.MIXER_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.MchloroperoxybenzoicAcid.getFluid(1000), Materials.Toluene.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.CpbaToluene.getFluid(1000));
        builder.duration(200);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // sodium_bicarbonate_solution_mixer
        builder = RecipeMaps.MIXER_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.input(dust, Materials.SodiumBicarbonate, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SodiumBicarbonateSolution.getFluid(1000));
        builder.duration(200);
        builder.EUt(VA[LV]);
        builder.buildAndRegister();

        // sal_a_mixed_salts_solution_d8_to_b9_mixer
        builder = RecipeMaps.MIXER_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.SalAMixedSaltsD8ToB9, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SalAMixedSaltsSolutionD8ToB9.getFluid(1000));
        builder.duration(300);
        builder.EUt(VA[LV]);
        builder.buildAndRegister();

        // sodium_bromide_brine_mixer
        builder = RecipeMaps.MIXER_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.SodiumBromide, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SodiumBromideBrine.getFluid(1000));
        builder.duration(200);
        builder.EUt(VA[LV]);
        builder.buildAndRegister();

        // sal_a_mixed_salts_solution_10_2_to_11_mixer
        builder = RecipeMaps.MIXER_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Water.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.SalAMixedSalts102To11, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SalAMixedSaltsSolution102To11.getFluid(1000));
        builder.duration(200);
        builder.EUt(VA[LV]);
        builder.buildAndRegister();

        // borax_to_sodium_borate
        builder = GregoriusDrugworksRecipeMaps.PYROLYSIS_CHAMBER_RECIPES.recipeBuilder();
        builder.input(dust, Materials.Borax, 1);
        builder.fluidOutputs(Materials.Steam.getFluid(10000));
        builder.output(dust, GregoriusDrugworksMaterials.SodiumBorate, 1);
        builder.duration(3000);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // lignin_residue_pyrolysis
        builder = GregoriusDrugworksRecipeMaps.PYROLYSIS_CHAMBER_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.LigninResidue, 1);
        builder.fluidOutputs(Materials.Phenol.getFluid(300), Materials.Benzene.getFluid(200), Materials.Toluene.getFluid(200));
        builder.output(dust, Materials.Carbon, 1);
        builder.duration(3000);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // humins_pyrolysis
        builder = GregoriusDrugworksRecipeMaps.PYROLYSIS_CHAMBER_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.Humins, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Furfural.getFluid(200), Materials.CarbonMonoxide.getFluid(500), Materials.Hydrogen.getFluid(500));
        builder.output(dust, Materials.Carbon, 1);
        builder.duration(3000);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_spiro_ketone_carboxylate_intermediate_to_enone_5
        builder = GregoriusDrugworksRecipeMaps.PYROLYSIS_CHAMBER_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.SalASpiroKetoneCarboxylateIntermediate, 1);
        builder.fluidOutputs(Materials.CarbonDioxide.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.SalAEnone5, 1);
        builder.duration(2400);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_organic_waste_e5_to_e6_and_7_processing
        builder = GregoriusDrugworksRecipeMaps.PYROLYSIS_CHAMBER_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.SalAOrganicWasteE5ToE6And7, 1);
        builder.fluidOutputs(Materials.Hydrogen.getFluid(250), Materials.Methane.getFluid(250), Materials.Ammonia.getFluid(100));
        builder.output(dust, Materials.Carbon, 1);
        builder.duration(3000);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_heavy_organic_residue_e5_to_e6_and_7_processing
        builder = GregoriusDrugworksRecipeMaps.PYROLYSIS_CHAMBER_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SalAHeavyOrganicResidueE5ToE6And7.getFluid(100));
        builder.fluidOutputs(Materials.Methane.getFluid(50), GregoriusDrugworksMaterials.SalAWasteWaterE5ToE6And7.getFluid(400));
        builder.output(dustTiny, Materials.Carbon, 1);
        builder.duration(2400);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_treated_water_e5_to_e6_and_7_processing
        builder = GregoriusDrugworksRecipeMaps.PYROLYSIS_CHAMBER_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SalATreatedWaterE5ToE6And7.getFluid(700));
        builder.fluidOutputs(Materials.DistilledWater.getFluid(650));
        builder.output(dustTiny, Materials.Salt, 1);
        builder.duration(3000);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sulfuryl_chloride_pyrolysis
        builder = GregoriusDrugworksRecipeMaps.PYROLYSIS_CHAMBER_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SulfurylChloride.getFluid(1000));
        builder.fluidOutputs(Materials.SulfurDioxide.getFluid(1000), Materials.Chlorine.getFluid(1000));
        builder.duration(3000);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_organic_waste_10_2_to_11_pyrolysis
        builder = GregoriusDrugworksRecipeMaps.PYROLYSIS_CHAMBER_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.SalAOrganicWaste102To11, 1);
        builder.fluidOutputs(Materials.Toluene.getFluid(250), Materials.Methanol.getFluid(250), Materials.Hydrogen.getFluid(250));
        builder.output(dustTiny, Materials.Carbon, 1);
        builder.duration(3000);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_hydroboration_organic_waste_11_to_11_2_pyrolysis
        builder = GregoriusDrugworksRecipeMaps.PYROLYSIS_CHAMBER_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.SalAHydroborationOrganicWaste11To112, 1);
        builder.fluidOutputs(Materials.Hydrogen.getFluid(250), Materials.Methane.getFluid(250));
        builder.output(dust, Materials.Carbon, 1);
        builder.duration(3000);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_heavy_organic_residue_14_to_15_and_16_pyrolysis
        builder = GregoriusDrugworksRecipeMaps.PYROLYSIS_CHAMBER_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SalAHeavyOrganicResidue14To15And16.getFluid(500));
        builder.fluidOutputs(Materials.CarbonMonoxide.getFluid(500), Materials.Hydrogen.getFluid(500), Materials.Methane.getFluid(250));
        builder.output(dust, Materials.Carbon, 1);
        builder.duration(3000);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_heavy_aromatic_residue_17_to_17_2_pyrolysis
        builder = GregoriusDrugworksRecipeMaps.PYROLYSIS_CHAMBER_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SalAHeavyAromaticResidue17To172.getFluid(300));
        builder.fluidOutputs(Materials.CarbonMonoxide.getFluid(300), Materials.Hydrogen.getFluid(300));
        builder.output(dust, Materials.Carbon, 1);
        builder.duration(3000);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_hydrazinedicarboxylate_waste_pyrolysis
        builder = GregoriusDrugworksRecipeMaps.PYROLYSIS_CHAMBER_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.SalAHydrazinedicarboxylateWaste, 1);
        builder.fluidOutputs(Materials.CarbonDioxide.getFluid(1000), Materials.Nitrogen.getFluid(500), Materials.Ammonia.getFluid(500), Materials.Methanol.getFluid(250));
        builder.output(dustTiny, Materials.Carbon, 1);
        builder.duration(3000);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_heavy_aromatic_residue_pyrolysis
        builder = GregoriusDrugworksRecipeMaps.PYROLYSIS_CHAMBER_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SalAHeavyAromaticResidue.getFluid(300));
        builder.fluidOutputs(Materials.Methane.getFluid(300), Materials.Hydrogen.getFluid(200), Materials.CarbonMonoxide.getFluid(200));
        builder.output(dust, Materials.Carbon, 1);
        builder.duration(3000);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // pyridine_waste_brine_processing
        builder = RecipeMaps.ELECTROLYZER_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.PyridineWasteBrine.getFluid(400));
        builder.fluidOutputs(Materials.Chlorine.getFluid(300), Materials.Hydrogen.getFluid(300));
        builder.output(dust, Materials.SodiumHydroxide, 1);
        builder.duration(1200);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // sal_a_mixed_salts_solution_d8_to_b9_electrolysis
        builder = RecipeMaps.ELECTROLYZER_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SalAMixedSaltsSolutionD8ToB9.getFluid(1000));
        builder.fluidOutputs(Materials.Water.getFluid(500), Materials.Hydrogen.getFluid(500), Materials.Chlorine.getFluid(500), Materials.Bromine.getFluid(500));
        builder.output(dust, Materials.Lithium, 1);
        builder.output(dust, Materials.Sodium, 1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sodium_bromide_brine_electrolysis
        builder = RecipeMaps.ELECTROLYZER_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SodiumBromideBrine.getFluid(1000));
        builder.fluidOutputs(Materials.Bromine.getFluid(500), Materials.Hydrogen.getFluid(500));
        builder.output(dust, Materials.SodiumHydroxide, 1);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // sal_a_mixed_salts_solution_10_2_to_11_electrolysis
        builder = RecipeMaps.ELECTROLYZER_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SalAMixedSaltsSolution102To11.getFluid(1000));
        builder.fluidOutputs(Materials.Chlorine.getFluid(500), Materials.Hydrogen.getFluid(500), Materials.SodiumHydroxide.getFluid(1000));
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // pyridine_waste_sludge_processing
        builder = RecipeMaps.CENTRIFUGE_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.PyridineWasteSludge.getFluid(200));
        builder.fluidOutputs(GregoriusDrugworksMaterials.PyridineWasteWater.getFluid(100));
        builder.output(dustSmall, Materials.Salt, 1);
        builder.output(dustTiny, Materials.ChromiumTrioxide, 1);
        builder.duration(300);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // sal_a_waste_sludge_e5_to_e6_and_7_processing
        builder = RecipeMaps.CENTRIFUGE_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SalAWasteSludgeE5ToE6And7.getFluid(200));
        builder.output(dustTiny, GregoriusDrugworksMaterials.SodiumSulfate, 1);
        builder.output(dustTiny, Materials.LithiumChloride, 1);
        builder.output(dustSmall, Materials.Salt, 1);
        builder.duration(300);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // sal_a_enone_4_to_alcohol_5
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, Materials.Methanol.getFluid(1000), 1000, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.input(dust, GregoriusDrugworksMaterials.PotassiumHydroxide, 1);
        builder.input(dust, GregoriusDrugworksMaterials.SalAEnone4, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SalAMotherLiquorE4ToA5.getFluid(15));
        builder.chancedOutput(dustTiny, GregoriusDrugworksMaterials.PotassiumCarbonate, 1, 1000, 0);
        builder.chancedOutput(dustSmall, Materials.DarkAsh, 1, 500, 0);
        builder.output(dust, GregoriusDrugworksMaterials.SalAAlcohol5, 1);
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_alcohol_5_to_enone_5
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Dichloromethane.getFluid(1000), 2000, 0);
        builder.input(dust, GregoriusDrugworksMaterials.PyridiniumChlorochromate, 1);
        builder.input(dust, GregoriusDrugworksMaterials.SalAAlcohol5, 1);
        builder.output(dust, GregoriusDrugworksMaterials.SalAEnone5, 1);
        builder.output(dust, Materials.ChromiumTrioxide, 1);
        builder.output(dust, GregoriusDrugworksMaterials.PyridineHydrochloride, 1);
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_enone_5_to_dioxolane_6
        builder = chemicalPlantBuilder(EV);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.input(dust, Materials.Lithium, 3);
        builder.input(dust, GregoriusDrugworksMaterials.SalAEnone5, 1);
        builder.fluidInputs(GregoriusDrugworksMaterials.EthylIodoacetate.getFluid(1000), Materials.Ammonia.getFluid(1000), GregoriusDrugworksMaterials.Thf.getFluid(500));
        builder.fluidOutputs(Materials.Ammonia.getFluid(1000), GregoriusDrugworksMaterials.SalAWasteWaterE5ToE6And7.getFluid(1500));
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalADioxolane6, 1, 5100, 0);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalAEster7, 1, 2100, 0);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalAOrganicWasteE5ToE6And7, 1, 2800, 0);
        builder.output(dust, GregoriusDrugworksMaterials.LithiumIodide, 1);
        builder.output(dust, Materials.LithiumChloride, 1);
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_dioxolane_6_to_diketone_8
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, Materials.Ethanol.getFluid(1000), 2000, 0);
        builder.fluidInputs(Materials.HydrochloricAcid.getFluid(1000), Materials.Water.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.SalADioxolane6, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.EthyleneGlycol.getFluid(1000), GregoriusDrugworksMaterials.SalAAcidicAcqueousLayerD6ToD8.getFluid(1200));
        builder.output(dust, GregoriusDrugworksMaterials.SalADiketone8, 1);
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_diketone_8_to_bisolefin_9
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Thf.getFluid(1000), 2000, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.input(dust, GregoriusDrugworksMaterials.MethylTriphenylPhosphoniumBromide, 6);
        builder.input(dust, GregoriusDrugworksMaterials.SalADiketone8, 1);
        builder.fluidInputs(GregoriusDrugworksMaterials.Nahmdsinthf.getFluid(5000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.SalACrudeOrganicResidueD8ToB9.getFluid(200));
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalABisolefin9, 1, 8500, 0);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalAWittigOrganicWasteD8ToB9, 1, 1500, 0);
        builder.output(dust, GregoriusDrugworksMaterials.TriphenylphosphineOxide, 2);
        builder.output(dust, GregoriusDrugworksMaterials.SodiumBromide, 6);
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_bisolefin_9_to_reduced_intermediate_slurry_b9_to_d10
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.DiethylEther.getFluid(1000), 1500, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.input(dust, GregoriusDrugworksMaterials.SalABisolefin9, 1);
        builder.input(dust, GregoriusDrugworksMaterials.LithiumAluminiumHydride, 1);
        builder.fluidOutputs(Materials.Hydrogen.getFluid(500));
        builder.output(dust, GregoriusDrugworksMaterials.SalAReducedIntermediateSlurryB9ToD10, 1);
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_reduced_intermediate_slurry_b9_to_d10_reduction
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.DiethylEther.getFluid(300), 2000, 0);
        builder.fluidInputs(Materials.Water.getFluid(500), GregoriusDrugworksMaterials.AmmoniumChlorideSolution.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.SalAReducedIntermediateSlurryB9ToD10, 1);
        builder.input(dust, GregoriusDrugworksMaterials.SodiumSulfate, 1);
        builder.fluidOutputs(Materials.Hydrogen.getFluid(500), Materials.Ammonia.getFluid(500));
        builder.output(dust, GregoriusDrugworksMaterials.SodiumSulfateHydrated, 1);
        builder.output(dust, GregoriusDrugworksMaterials.AluminiumHydroxide, 2);
        builder.output(dust, Materials.LithiumChloride, 1);
        builder.output(dust, Materials.AmmoniumChloride, 1);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalADiol10, 1, 5700, 0);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalASilylationOrganicWasteB9ToD10, 1, 4300, 0);
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_diol_10_to_tbs_10_2
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Dichloromethane.getFluid(1000), 1000, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        GregoriusDrugworksChancedInputSupport.chancedCatalystItemInput(builder, OreDictUnifier.get(dust, GregoriusDrugworksMaterials.Dmap, 1), 500, 0);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Tbscl.getFluid(2000), 1250, 0);
        builder.fluidInputs(GregoriusDrugworksMaterials.Triethylamine.getFluid(1200));
        builder.input(dust, GregoriusDrugworksMaterials.SalADiol10, 1);
        builder.output(dust, GregoriusDrugworksMaterials.TriethylammoniumChloride, 1);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalATbs102, 1, 9900, 0);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalASilylationOrganicWasteD10To102, 1, 100, 0);
        builder.duration(1800);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_tbs_10_2_to_alcohol_10_2
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Thf.getFluid(500), 2000, 0);
        builder.input(dust, GregoriusDrugworksMaterials.SalATbs102, 1);
        builder.input(dust, GregoriusDrugworksMaterials.Tbaf, 1);
        builder.output(dust, GregoriusDrugworksMaterials.Tbsf, 1);
        builder.output(dust, GregoriusDrugworksMaterials.SalAAlcohol102, 1);
        builder.duration(1800);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_alcohol_10_2_to_exomethylene_11
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Dimethylformamide.getFluid(1000), 1500, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.fluidInputs(GregoriusDrugworksMaterials.Material4MethoxybenzylChloride.getFluid(1200));
        builder.input(dust, GregoriusDrugworksMaterials.SalAAlcohol102, 1);
        builder.input(dust, GregoriusDrugworksMaterials.SodiumHydride, 3);
        builder.output(dust, GregoriusDrugworksMaterials.SodiumChloride, 1);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalAExomethylene11, 1, 9400, 0);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalAOrganicWaste102To11, 1, 600, 0);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SalACrudeOrganics102To11.getFluid(150), Materials.Hydrogen.getFluid(3000));
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_crude_organics_10_2_to_11_to_mixed_salts
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.EthylAcetate.getFluid(500), 1000, 0);
        builder.fluidInputs(GregoriusDrugworksMaterials.AmmoniumChlorideSolution.getFluid(1000), GregoriusDrugworksMaterials.Brine.getFluid(500), GregoriusDrugworksMaterials.SalACrudeOrganics102To11.getFluid(150));
        builder.input(dust, GregoriusDrugworksMaterials.SodiumSulfate, 1);
        builder.output(dust, GregoriusDrugworksMaterials.SodiumSulfateHydrated, 1);
        builder.output(dust, Materials.AmmoniumChloride, 1);
        builder.output(dust, GregoriusDrugworksMaterials.SodiumChloride, 1);
        builder.output(dust, GregoriusDrugworksMaterials.SalAMixedSalts102To11, 1);
        builder.fluidOutputs(Materials.Water.getFluid(900));
        builder.duration(1800);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_exomethylene_11_to_organoborane_intermediate_11_to_11_2
        builder = chemicalPlantBuilder(IV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Thf.getFluid(1000), 1000, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.fluidInputs(GregoriusDrugworksMaterials.BoraneThf.getFluid(5000));
        builder.input(dust, GregoriusDrugworksMaterials.SalAExomethylene11, 1);
        builder.output(dust, GregoriusDrugworksMaterials.SalAOrganoboraneIntermediate11To112, 1);
        builder.duration(3000);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // sal_a_11_to_11_2_organoborane_intermediate_to_alcohol_11_2
        builder = chemicalPlantBuilder(IV);
        builder.fluidInputs(GregoriusDrugworksMaterials.SodiumHydroxideSolution.getFluid(40000), GregoriusDrugworksMaterials.HydrogenPeroxide.getFluid(40000), Materials.HydrochloricAcid.getFluid(1000), Materials.Water.getFluid(2000));
        builder.input(dust, GregoriusDrugworksMaterials.SalAOrganoboraneIntermediate11To112, 1);
        builder.output(dust, GregoriusDrugworksMaterials.SodiumChloride, 1);
        builder.output(dust, GregoriusDrugworksMaterials.SodiumBorate, 1);
        builder.fluidOutputs(Materials.Oxygen.getFluid(1000), Materials.Water.getFluid(3000));
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalAAlcohol112, 1, 9500, 0);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalAHydroborationOrganicWaste11To112, 1, 500, 0);
        builder.duration(3000);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // sal_a_alcohol_11_2_to_bisaldehyde_11_3
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedCatalystItemInput(builder, OreDictUnifier.get(dust, GregoriusDrugworksMaterials.MolecularSieve4A, 1), 1000, 0);
        GregoriusDrugworksChancedInputSupport.chancedItemInput(builder, OreDictUnifier.get(dust, GregoriusDrugworksMaterials.SodiumAcetate, 6), 9250, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.fluidInputs(GregoriusDrugworksMaterials.Dichloromethane.getFluid(1000));
        builder.input(dust, GregoriusDrugworksMaterials.SalAAlcohol112, 1);
        builder.input(dust, GregoriusDrugworksMaterials.PyridiniumDichromate, 6);
        builder.output(dust, GregoriusDrugworksMaterials.ChromiumOxide, 1);
        builder.output(dust, GregoriusDrugworksMaterials.PyridiniumSaltWaste, 2);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalABisaldehyde113, 1, 8700, 0);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalAChromiumOxidationOrganicWaste112To113, 1, 1300, 0);
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_bisaldehyde_11_3_to_bisaldehyde_12
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, Materials.Methanol.getFluid(1000), 8500, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.fluidInputs(GregoriusDrugworksMaterials.SodiumMethoxide.getFluid(3000));
        builder.input(dust, GregoriusDrugworksMaterials.SalABisaldehyde113, 1);
        builder.output(dust, GregoriusDrugworksMaterials.ChromiumOxide, 1);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalABisaldehyde12, 1, 7300, 0);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalAAldehydeBaseWaste113To12, 1, 2700, 0);
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_bisaldehyde_12_to_ketal_13
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedCatalystItemInput(builder, OreDictUnifier.get(dust, GregoriusDrugworksMaterials.PToluenesulfonicAcid, 1), 500, 0);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Material2Ethyl2Methyl13Dioxolane.getFluid(1000), 1000, 0);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.EthyleneGlycol.getFluid(2000), 4000, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.input(dust, GregoriusDrugworksMaterials.SalABisaldehyde12, 1);
        builder.output(dust, GregoriusDrugworksMaterials.SalAKetal13, 1);
        builder.fluidOutputs(Materials.Water.getFluid(2000));
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_ketal_13_to_alcohol_13_2
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Thf.getFluid(1000), 1000, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.input(dust, GregoriusDrugworksMaterials.SalAKetal13, 1);
        builder.input(dust, GregoriusDrugworksMaterials.Tbaf, 2);
        builder.output(dust, GregoriusDrugworksMaterials.SalAAlcohol132, 1);
        builder.output(dust, GregoriusDrugworksMaterials.Tbsf, 1);
        builder.output(dust, GregoriusDrugworksMaterials.Tbacl, 1);
        builder.fluidOutputs(Materials.Water.getFluid(2000));
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_alcohol_13_2_to_aldehyde_14
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedCatalystItemInput(builder, OreDictUnifier.get(dust, GregoriusDrugworksMaterials.MolecularSieve4A, 1), 1000, 0);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Dichloromethane.getFluid(1000), 1000, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.input(dust, GregoriusDrugworksMaterials.SalAAlcohol132, 1);
        builder.input(dust, GregoriusDrugworksMaterials.PyridiniumDichromate, 3);
        builder.input(dust, GregoriusDrugworksMaterials.SodiumAcetate, 3);
        builder.output(dust, GregoriusDrugworksMaterials.ChromiumOxide, 1);
        builder.output(dust, GregoriusDrugworksMaterials.PyridiniumSaltWaste, 1);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalAAldehyde14, 1, 7800, 0);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalAPdc132To14, 1, 2200, 0);
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_aldehyde_14_to_furyl_alcohol_15_and_16
        builder = chemicalPlantBuilder(IV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Thf.getFluid(1500), 2000, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.input(dust, GregoriusDrugworksMaterials.SalAAldehyde14, 1);
        builder.fluidInputs(GregoriusDrugworksMaterials.Material3Bromofuran.getFluid(4000), GregoriusDrugworksMaterials.TBuliInPentane.getFluid(5600));
        builder.output(dust, GregoriusDrugworksMaterials.LithiumBromide, 1);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalAFurylAlcohol15, 1, 4500, 0);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalAFurylAlcohol16, 1, 3500, 0);
        builder.chancedOutput(dust, GregoriusDrugworksMaterials.SalAFurylOrganicWaste14To15And16, 1, 2000, 0);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Isobutane.getFluid(4000), GregoriusDrugworksMaterials.Pentane.getFluid(5200));
        builder.duration(3600);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // sal_a_furyl_alcohol_16_recycling
        builder = chemicalPlantBuilder(IV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Dichloromethane.getFluid(1000), 1500, 0);
        GregoriusDrugworksChancedInputSupport.chancedCatalystItemInput(builder, OreDictUnifier.get(dust, GregoriusDrugworksMaterials.MolecularSieve4A, 1), 1000, 0);
        builder.input(dust, GregoriusDrugworksMaterials.SalAFurylAlcohol16, 1);
        builder.input(dust, GregoriusDrugworksMaterials.PyridiniumDichromate, 3);
        builder.input(dust, GregoriusDrugworksMaterials.SodiumAcetate, 3);
        builder.output(dust, GregoriusDrugworksMaterials.SalAAldehyde14, 1);
        builder.output(dust, GregoriusDrugworksMaterials.ChromiumOxide, 1);
        builder.output(dust, GregoriusDrugworksMaterials.PyridiniumSaltWaste, 1);
        builder.duration(3600);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // sal_a_furyl_alcohol_15_to_lactol_17
        builder = chemicalPlantBuilder(IV);
        GregoriusDrugworksChancedInputSupport.chancedCatalystItemInput(builder, OreDictUnifier.get(dust, GregoriusDrugworksMaterials.PToluenesulfonicAcid, 1), 500, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.input(dust, GregoriusDrugworksMaterials.SalAFurylAlcohol15, 1);
        builder.fluidInputs(GregoriusDrugworksMaterials.Material5AqueousAcetone.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.SalALactol17, 1);
        builder.fluidOutputs(Materials.Acetone.getFluid(900), Materials.Water.getFluid(100));
        builder.duration(3600);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // sal_a_lactol_17_to_alcohol_17_2
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Dichloromethane.getFluid(1000), 1500, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.input(dust, GregoriusDrugworksMaterials.SalALactol17, 1);
        builder.input(dust, GregoriusDrugworksMaterials.Ddq, 2);
        builder.fluidInputs(Materials.Water.getFluid(200));
        builder.output(dust, GregoriusDrugworksMaterials.SalAAlcohol172, 1);
        builder.output(dust, GregoriusDrugworksMaterials.DdqReduced, 2);
        builder.fluidOutputs(GregoriusDrugworksMaterials.SalAAnisoleWasteFluid17To172.getFluid(500));
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_alcohol_17_2_to_carboxylic_acid_17_3
        builder = chemicalPlantBuilder(IV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Dimethylformamide.getFluid(1000), 1500, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.input(dust, GregoriusDrugworksMaterials.SalAAlcohol172, 1);
        builder.input(dust, GregoriusDrugworksMaterials.SodiumAcetate, 3);
        builder.input(dust, GregoriusDrugworksMaterials.PyridiniumDichromate, 8);
        builder.fluidInputs(GregoriusDrugworksMaterials.Material2Methyl2Butene.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.SalACarboxylicAcid173, 1);
        builder.output(dust, GregoriusDrugworksMaterials.ChromiumOxide, 2);
        builder.output(dust, GregoriusDrugworksMaterials.PyridiniumSaltWaste, 2);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Butanone.getFluid(500));
        builder.duration(3600);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // sal_a_carboxylic_acid_17_3_to_2_deacetoxysalvinorin_a
        builder = chemicalPlantBuilder(IV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Dichloromethane.getFluid(1000), 1500, 0);
        GregoriusDrugworksChancedInputSupport.chancedCatalystItemInput(builder, OreDictUnifier.get(dust, GregoriusDrugworksMaterials.Dmap, 1), 500, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.input(dust, GregoriusDrugworksMaterials.SalACarboxylicAcid173, 1);
        builder.input(dust, GregoriusDrugworksMaterials.Dcc, 3);
        builder.fluidInputs(Materials.Methanol.getFluid(1000));
        ItemStack deacetoxysalvinorinA = GregoriusDrugworksUnificationHelper.get(dust,
                GregoriusDrugworksMaterials.Material2DeacetoxysalvinorinA, 1);
        if (deacetoxysalvinorinA.isEmpty()) {
            throw new IllegalStateException("Failed to resolve dust output for 2-Deacetoxysalvinorin A.");
        }
        builder.output(dust, GregoriusDrugworksMaterials.Dicyclohexylurea, 3);
        builder.outputs(deacetoxysalvinorinA);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Butanone.getFluid(500));
        builder.duration(3600);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // sal_a_2_deacetoxysalvinorin_a_to_tes_19
        builder = chemicalPlantBuilder(EV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Thf.getFluid(1000), 1500, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.input(dust, GregoriusDrugworksMaterials.Material2DeacetoxysalvinorinA, 1);
        builder.fluidInputs(GregoriusDrugworksMaterials.Nahmdsinthf.getFluid(3000), GregoriusDrugworksMaterials.Tescl.getFluid(6000));
        builder.output(dust, GregoriusDrugworksMaterials.SalATes19, 1);
        builder.output(dust, GregoriusDrugworksMaterials.SodiumChloride, 3);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Hmds.getFluid(1000));
        builder.duration(1800);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // sal_a_tes_19_to_2_epi_salvinorin_b
        builder = chemicalPlantBuilder(LuV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Thf.getFluid(1000), 1500, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.input(dust, GregoriusDrugworksMaterials.SalATes19, 1);
        builder.fluidInputs(GregoriusDrugworksMaterials.WashedCpbaToluene.getFluid(1000), Materials.Water.getFluid(100), Materials.Toluene.getFluid(500));
        ItemStack epiSalvinorinB = GregoriusDrugworksUnificationHelper.get(dust,
                GregoriusDrugworksMaterials.Material2EpiSalvinorinB, 1);
        if (epiSalvinorinB.isEmpty()) {
            throw new IllegalStateException("Failed to resolve dust output for 2-epi-Salvinorin B.");
        }
        builder.outputs(epiSalvinorinB);
        builder.fluidOutputs(Materials.Toluene.getFluid(850), Materials.Water.getFluid(150), GregoriusDrugworksMaterials.MchlorobenzoicAcid.getFluid(1000));
        builder.duration(3600);
        builder.EUt(VA[LuV]);
        builder.buildAndRegister();

        // 2_epi_salvinorin_b_to_salvinorin_a
        builder = chemicalPlantBuilder(UV);
        GregoriusDrugworksChancedInputSupport.chancedFluidInput(builder, GregoriusDrugworksMaterials.Dichloromethane.getFluid(1000), 1500, 0);
        builder = chemicalPlantAtmosphere(builder, Materials.Nitrogen.getFluid(250));
        builder.input(dust, GregoriusDrugworksMaterials.Material2EpiSalvinorinB, 1);
        builder.input(dust, GregoriusDrugworksMaterials.Triphenylphosphine, 10);
        builder.fluidInputs(GregoriusDrugworksMaterials.Diad.getFluid(10000), Materials.AceticAcid.getFluid(30000));
        builder.output(dust, GregoriusDrugworksMaterials.SalvinorinA, 1);
        builder.output(dust, GregoriusDrugworksMaterials.TriphenylphosphineOxide, 10);
        builder.output(dust, GregoriusDrugworksMaterials.SalAHydrazinedicarboxylateWaste, 10);
        builder.duration(4800);
        builder.EUt(VA[UV]);
        builder.buildAndRegister();

        // stabilized_carrier_solution_warmed
        builder = RecipeMaps.FLUID_HEATER_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.StabilizedCarrierSolution.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.StabilizedCarrierSolutionWarmed.getFluid(1000));
        builder.duration(200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // kappa_reset_ampoule
        builder = RecipeMaps.CANNER_RECIPES.recipeBuilder();
        builder.input(GregoriusDrugworksMetaItems.EMPTY_GLASS_AMPOULE, 1);
        builder.fluidInputs(GregoriusDrugworksMaterials.KappaResetConcentrate.getFluid(250));
        builder.output(GregoriusDrugworksMetaItems.KAPPA_RESET_AMPOULE, 1);
        builder.duration(300);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // empty_glass_ampoule
        builder = RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder();
        builder.input(Blocks.GLASS, 2);
        builder.input(pipeTinyFluid, Materials.Steel, 1);
        builder.output(GregoriusDrugworksMetaItems.EMPTY_GLASS_AMPOULE, 4);
        builder.circuitMeta(7);
        builder.duration(200);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();
    }
}
