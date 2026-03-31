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

import static gregtech.api.GTValues.*;
import static gregtech.api.unification.ore.OrePrefix.*;

public final class MultiblockCasingsRecipes {

    private MultiblockCasingsRecipes() {}

    public static void init() {
        RecipeBuilder builder;

        // trimellitic_anhydride
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.PhthalicAnhydride, 1);
        builder.fluidInputs(Materials.CarbonMonoxide.getFluid(1000), Materials.Oxygen.getFluid(2000));
        builder.output(dust, GregoriusDrugworksMaterials.TrimelliticAnhydride, 1);
        builder.duration(2600);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // pfa_slurry_polymerization
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.Hexafluoropropylene.getFluid(2000), Materials.Tetrafluoroethylene.getFluid(3000), Materials.Oxygen.getFluid(1000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.PerfluoroalkoxyPolymerSlurry.getFluid(5000));
        builder.duration(3600);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // m_dinitrobenzene
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Nitrobenzene.getFluid(1000), Materials.NitricAcid.getFluid(2000));
        builder.output(dust, GregoriusDrugworksMaterials.MDinitrobenzene, 1);
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.circuitMeta(1);
        builder.duration(2200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // bisphenol_a_dianhydride
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.PhthalicAnhydride, 2);
        builder.fluidInputs(Materials.Acetone.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.BisphenolADianhydride, 1);
        builder.duration(2600);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // pyromellitic_dianhydride
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.TrimelliticAnhydride, 2);
        builder.fluidInputs(Materials.Oxygen.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.PyromelliticDianhydride, 1);
        builder.duration(2200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // dinitrodiphenyl_ether_4_4
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.PChloronitrobenzene, 1);
        builder.input(dust, GregoriusDrugworksMaterials.PNitrophenol, 1);
        builder.input(dust, Materials.SodiumHydroxide, 1);
        builder.output(dust, GregoriusDrugworksMaterials.DinitrodiphenylEther44, 1);
        builder.output(dust, Materials.Salt, 1);
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.duration(2800);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // iron_pentacarbonyl
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, Materials.Iron, 1);
        builder.fluidInputs(Materials.CarbonMonoxide.getFluid(5000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.IronPentacarbonyl.getFluid(1000));
        builder.duration(2400);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // polysiloxane_polymer
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Dimethyldichlorosilane.getFluid(2000), Materials.Water.getFluid(2000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Polysiloxane.getFluid(1000), GregoriusDrugworksMaterials.HydrogenChloride.getFluid(2000));
        builder.duration(2000);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // phenolic_resin
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Phenol.getFluid(2000), GregoriusDrugworksMaterials.Formaldehyde.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.PhenolicResin, 2);
        builder.duration(2000);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // obsidian_ceramic_composite
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.MoltenVolcanicGlass.getFluid(3000));
        builder.input(dust, GregoriusDrugworksMaterials.Alumina, 1);
        builder.output(dust, GregoriusDrugworksMaterials.ObsidianCeramicComposite, 2);
        builder.duration(2000);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // aluminosilicate_gel
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.SodiumSilicateSolution.getFluid(1000), GregoriusDrugworksMaterials.SodiumAluminateSolution.getFluid(2000));
        builder.output(dust, GregoriusDrugworksMaterials.AluminosilicateGel, 3);
        builder.duration(2000);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // zeolite_catalyst_matrix
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.AluminosilicateGel, 2);
        builder.input(dust, Materials.SodiumHydroxide, 1);
        builder.output(dust, GregoriusDrugworksMaterials.ZeoliteCatalystMatrix, 1);
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.duration(2000);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // catalytic_cracking_matrix
        builder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.ZeoliteCatalystMatrix, 1);
        builder.input(dust, GregoriusDrugworksMaterials.VanadiumPentoxide, 1);
        builder.input(dust, GregoriusDrugworksMaterials.Alumina, 2);
        builder.output(dust, GregoriusDrugworksMaterials.CatalyticCrackingMatrix, 2);
        builder.duration(2400);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // phthalic_anhydride
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Benzene.getFluid(1000), Materials.Oxygen.getFluid(3000));
        builder.output(dust, GregoriusDrugworksMaterials.PhthalicAnhydride, 1);
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.circuitMeta(7);
        builder.duration(2400);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // ceramic_fiber
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.Alumina, 2);
        builder.input(dust, Materials.NetherQuartz, 1);
        builder.output(dust, GregoriusDrugworksMaterials.CeramicFiber, 3);
        builder.duration(1600);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // hfp_synthesis
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Propene.getFluid(3000), Materials.Fluorine.getFluid(6000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.Hexafluoropropylene.getFluid(1000), GregoriusDrugworksMaterials.HydrogenFluoride.getFluid(6000));
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // m_phenylenediamine
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.MDinitrobenzene, 1);
        builder.fluidInputs(Materials.Hydrogen.getFluid(6000));
        builder.output(dust, GregoriusDrugworksMaterials.MPhenylenediamine, 1);
        builder.fluidOutputs(Materials.Water.getFluid(4000));
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // polyetherimide
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.BisphenolADianhydride, 1);
        builder.input(dust, GregoriusDrugworksMaterials.MPhenylenediamine, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.Polyetherimide.getFluid(2000));
        builder.duration(3200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // p_chloronitrobenzene
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Chlorobenzene.getFluid(1000), Materials.NitricAcid.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.PChloronitrobenzene, 1);
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.circuitMeta(2);
        builder.duration(1800);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // p_nitrophenol
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(Materials.Phenol.getFluid(1000), Materials.NitricAcid.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.PNitrophenol, 1);
        builder.fluidOutputs(Materials.Water.getFluid(1000));
        builder.circuitMeta(2);
        builder.duration(1800);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // oxydianiline
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.DinitrodiphenylEther44, 1);
        builder.fluidInputs(Materials.Hydrogen.getFluid(12000));
        builder.output(dust, GregoriusDrugworksMaterials.Oxydianiline, 1);
        builder.fluidOutputs(Materials.Water.getFluid(4000));
        builder.duration(3200);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // polyimide_solution
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.PyromelliticDianhydride, 1);
        builder.input(dust, GregoriusDrugworksMaterials.Oxydianiline, 1);
        builder.fluidOutputs(GregoriusDrugworksMaterials.PolyimideSolution.getFluid(2000));
        builder.duration(2600);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // microporous_polyimide_membrane
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.PolyimideSolution.getFluid(2000));
        builder.input(GregoriusDrugworksItems.CARBON_NANOTUBES, 1);
        builder.output(dust, GregoriusDrugworksMaterials.MicroporousPolyimideMembrane, 2);
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // high_temperature_insulation
        builder = RecipeMaps.CHEMICAL_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.CeramicFiber, 2);
        builder.input(dust, GregoriusDrugworksMaterials.SiliconDioxide, 1);
        builder.output(dust, GregoriusDrugworksMaterials.HighTemperatureInsulation, 1);
        builder.duration(1600);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // pfa_slurry_fractionation
        builder = RecipeMaps.DISTILLATION_RECIPES.recipeBuilder();
        builder.fluidInputs(GregoriusDrugworksMaterials.PerfluoroalkoxyPolymerSlurry.getFluid(5000));
        builder.fluidOutputs(GregoriusDrugworksMaterials.FluoropolymerResidue.getFluid(2000));
        builder.output(dust, GregoriusDrugworksMaterials.FluoropolymerStructuralFilm, 3);
        builder.duration(2400);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // as_grown_multiwalled_carbon_nanotubes
        builder = RecipeMaps.PYROLYSE_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.CalciumCarbide, 1);
        builder.fluidInputs(GregoriusDrugworksMaterials.IronPentacarbonyl.getFluid(1000));
        builder.output(dust, GregoriusDrugworksMaterials.AsGrownMultiwalledCarbonNanotubes, 2);
        builder.fluidOutputs(Materials.Hydrogen.getFluid(6000));
        builder.duration(2600);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // carbon_matrix_composite
        builder = RecipeMaps.PYROLYSE_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.PhenolicResin, 2);
        builder.input(dust, Materials.Graphite, 2);
        builder.output(dust, GregoriusDrugworksMaterials.CarbonMatrixComposite, 3);
        builder.fluidOutputs(Materials.Hydrogen.getFluid(1000));
        builder.duration(3000);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // molten_volcanic_glass
        builder = RecipeMaps.PYROLYSE_RECIPES.recipeBuilder();
        builder.input(dust, Materials.Obsidian, 4);
        builder.input(dust, Materials.Basalt, 2);
        builder.fluidOutputs(GregoriusDrugworksMaterials.MoltenVolcanicGlass.getFluid(3000));
        builder.duration(2400);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // id2('chemical_plant_controller')
        builder = RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder();
        builder.input(MetaTileEntities.HULL[EV], 1);
        builder.input(GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T1, 2);
        builder.input(GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T1, 2);
        builder.input(pipeLargeFluid, Materials.StainlessSteel, 2);
        builder.input(MetaItems.ELECTRIC_PUMP_EV, 2);
        builder.input(MetaItems.ROBOT_ARM_EV, 1);
        builder.input(MetaItems.SENSOR_EV, 1);
        builder.input(MetaItems.FIELD_GENERATOR_EV, 1);
        builder.fluidInputs(Materials.SolderingAlloy.getFluid(576));
        builder.output(GregoriusDrugworksMetaTileEntities.CHEMICAL_PLANT, 1);
        builder.duration(1200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // id2('distillation_unit_controller')
        builder = RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder();
        builder.input(MetaTileEntities.HULL[IV], 1);
        builder.input(GregoriusDrugworksBlocks.FLUOROPOLYMER_FRACTIONATION_CASING, 2);
        builder.input(GregoriusDrugworksBlocks.MOLECULAR_MEMBRANE_CASING, 2);
        builder.input(pipeLargeFluid, Materials.Titanium, 2);
        builder.input(MetaItems.ELECTRIC_PUMP_IV, 2);
        builder.input(MetaItems.ROBOT_ARM_IV, 1);
        builder.input(MetaItems.SENSOR_IV, 1);
        builder.input(MetaItems.EMITTER_IV, 1);
        builder.fluidInputs(Materials.SolderingAlloy.getFluid(864));
        builder.output(GregoriusDrugworksMetaTileEntities.DISTILLATION_UNIT, 1);
        builder.duration(1600);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // id2('pyrolysis_chamber_controller')
        builder = RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder();
        builder.input(MetaTileEntities.HULL[LuV], 1);
        builder.input(GregoriusDrugworksBlocks.CARBONIZED_REACTOR_CASING, 2);
        builder.input(GregoriusDrugworksBlocks.OBSIDIAN_FORGED_THERMAL_CASING, 2);
        builder.input(GregoriusDrugworksBlocks.THERMOCRACK_MATRIX_CASING, 2);
        builder.input(pipeLargeFluid, Materials.TungstenSteel, 2);
        builder.input(MetaItems.ELECTRIC_PUMP_LuV, 2);
        builder.input(MetaItems.SENSOR_LuV, 1);
        builder.input(MetaItems.FIELD_GENERATOR_LuV, 1);
        builder.fluidInputs(Materials.SolderingAlloy.getFluid(1152));
        builder.output(GregoriusDrugworksMetaTileEntities.PYROLYSIS_CHAMBER, 1);
        builder.duration(2400);
        builder.EUt(VA[LuV]);
        builder.buildAndRegister();

        // fluoropolymer_fractionation_casing
        builder = RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.FluoropolymerStructuralFilm, 3);
        builder.input(plate, Materials.Titanium, 4);
        builder.input(dust, GregoriusDrugworksMaterials.CeramicFiber, 2);
        builder.output(GregoriusDrugworksBlocks.FLUOROPOLYMER_FRACTIONATION_CASING, 1);
        builder.duration(400);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // polyetherimide_thermal_casing
        builder = RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder();
        builder.input(plate, Materials.Niobium, 2);
        builder.input(wireFine, Materials.BorosilicateGlass, 2);
        builder.fluidInputs(GregoriusDrugworksMaterials.Polyetherimide.getFluid(2000));
        builder.output(GregoriusDrugworksBlocks.POLYETHERIMIDE_THERMAL_CASING, 1);
        builder.duration(400);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // fine_stainless_mesh
        builder = RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder();
        builder.input(foil, Materials.StainlessSteel, 4);
        builder.output(GregoriusDrugworksItems.FINE_STAINLESS_MESH, 1);
        builder.circuitMeta(7);
        builder.duration(200);
        builder.EUt(VA[LV]);
        builder.buildAndRegister();

        // molecular_membrane_casing
        builder = RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.MicroporousPolyimideMembrane, 2);
        builder.input(plate, Materials.Titanium, 2);
        builder.input(GregoriusDrugworksItems.FINE_STAINLESS_MESH, 1);
        builder.output(GregoriusDrugworksBlocks.MOLECULAR_MEMBRANE_CASING, 1);
        builder.duration(400);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // polysiloxane_vapor_control_casing
        builder = RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder();
        builder.input(plate, Materials.StainlessSteel, 2);
        builder.input(GregoriusDrugworksItems.CERAMIC_FILTER, 1);
        builder.fluidInputs(GregoriusDrugworksMaterials.Polysiloxane.getFluid(1000));
        builder.output(GregoriusDrugworksBlocks.POLYSILOXANE_VAPOR_CONTROL_CASING, 1);
        builder.duration(400);
        builder.EUt(VA[IV]);
        builder.buildAndRegister();

        // carbonized_reactor_casing
        builder = RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.CarbonMatrixComposite, 3);
        builder.input(plate, Materials.Tungsten, 2);
        builder.input(dust, GregoriusDrugworksMaterials.HighTemperatureInsulation, 1);
        builder.output(GregoriusDrugworksBlocks.CARBONIZED_REACTOR_CASING, 1);
        builder.duration(400);
        builder.EUt(VA[LuV]);
        builder.buildAndRegister();

        // obsidian_forged_thermal_casing
        builder = RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.ObsidianCeramicComposite, 2);
        builder.input(plate, Materials.HSSS, 2);
        builder.input(dust, GregoriusDrugworksMaterials.HighTemperatureInsulation, 1);
        builder.output(GregoriusDrugworksBlocks.OBSIDIAN_FORGED_THERMAL_CASING, 1);
        builder.duration(400);
        builder.EUt(VA[LuV]);
        builder.buildAndRegister();

        // heatproof_alloy_mesh
        builder = RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder();
        builder.input(foil, Materials.TungstenSteel, 2);
        builder.input(foil, Materials.Kanthal, 2);
        builder.output(GregoriusDrugworksItems.HEATPROOF_ALLOY_MESH, 1);
        builder.circuitMeta(7);
        builder.duration(300);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();

        // thermocrack_matrix_casing
        builder = RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.CatalyticCrackingMatrix, 2);
        builder.input(plate, Materials.TungstenSteel, 2);
        builder.input(GregoriusDrugworksItems.HEATPROOF_ALLOY_MESH, 1);
        builder.output(GregoriusDrugworksBlocks.THERMOCRACK_MATRIX_CASING, 1);
        builder.duration(400);
        builder.EUt(VA[LuV]);
        builder.buildAndRegister();

        // purified_multiwalled_carbon_nanotubes
        builder = RecipeMaps.THERMAL_CENTRIFUGE_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.AsGrownMultiwalledCarbonNanotubes, 2);
        builder.output(dust, GregoriusDrugworksMaterials.PurifiedMultiwalledCarbonNanotubes, 1);
        builder.output(dust, Materials.Carbon, 1);
        builder.duration(1800);
        builder.EUt(VA[MV]);
        builder.buildAndRegister();

        // carbon_nanotubes
        builder = RecipeMaps.ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.PurifiedMultiwalledCarbonNanotubes, 1);
        builder.output(GregoriusDrugworksItems.CARBON_NANOTUBES, 1);
        builder.output(dust, Materials.Iron, 2);
        builder.duration(1200);
        builder.EUt(VA[HV]);
        builder.buildAndRegister();

        // ceramic_filter
        builder = RecipeMaps.FORMING_PRESS_RECIPES.recipeBuilder();
        builder.input(dust, GregoriusDrugworksMaterials.CeramicFiber, 2);
        builder.input(dust, GregoriusDrugworksMaterials.Alumina, 1);
        builder.notConsumable(MetaItems.SHAPE_MOLD_CYLINDER);
        builder.output(GregoriusDrugworksItems.CERAMIC_FILTER, 1);
        builder.duration(1200);
        builder.EUt(VA[EV]);
        builder.buildAndRegister();
    }
}
