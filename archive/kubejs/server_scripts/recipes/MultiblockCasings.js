const GTValues = Java.loadClass('com.gregtechceu.gtceu.api.GTValues');

ServerEvents.recipes(event => {
  const id = (path) => `kubejs:multiblock_casings/${path}`;
  const id2 = (path) => `kubejs:multiblock_controllers/${path}`;

  const largeChemicalReactor = (rid) => event.recipes.gtceu.large_chemical_reactor(rid);
  const chemicalReactor = (rid) => event.recipes.gtceu.chemical_reactor(rid);
  const distillationTower = (rid) => event.recipes.gtceu.distillation_tower(rid);
  const pyrolyseOven = (rid) => event.recipes.gtceu.pyrolyse_oven(rid);
  const assembler = (rid) => event.recipes.gtceu.assembler(rid);
  const thermalCentrifuge = (rid) => event.recipes.gtceu.thermal_centrifuge(rid);
  const electromagneticSeparator = (rid) => event.recipes.gtceu.electromagnetic_separator(rid);
  const formingPress = (rid) => event.recipes.gtceu.forming_press(rid);

  assembler(id2('chemical_plant_controller'))
    .itemInputs(
      '1x gtceu:ev_machine_hull',
      '2x kubejs:chemplant_pipe_casing_t1',
      '2x kubejs:chemplant_machine_casing_t1',
      '2x gtceu:stainless_steel_large_fluid_pipe',
      '2x gtceu:ev_electric_pump',
      '1x gtceu:ev_robot_arm',
      '1x gtceu:ev_sensor',
      '1x gtceu:ev_field_generator',
      Ingredient.of('#gtceu:circuits/ev').withCount(2)
    )
    .inputFluids('gtceu:soldering_alloy 576')
    .itemOutputs('1x gtceu:chemical_plant')
    .duration(1200)
    .EUt(GTValues.V[GTValues.EV]);

  assembler(id2('distillation_unit_controller'))
    .itemInputs(
      '1x gtceu:iv_machine_hull',
      '2x kubejs:fluoropolymer_fractionation_casing',
      '2x kubejs:molecular_membrane_casing',
      '2x gtceu:titanium_large_fluid_pipe',
      '2x gtceu:iv_electric_pump',
      '1x gtceu:iv_robot_arm',
      '1x gtceu:iv_sensor',
      '1x gtceu:iv_emitter',
      Ingredient.of('#gtceu:circuits/iv').withCount(2)
    )
    .inputFluids('gtceu:soldering_alloy 864')
    .itemOutputs('1x gtceu:distillation_unit')
    .duration(1600)
    .EUt(GTValues.V[GTValues.IV]);

  assembler(id2('pyrolysis_chamber_controller'))
    .itemInputs(
      '1x gtceu:luv_machine_hull',
      '2x kubejs:carbonized_reactor_casing',
      '2x kubejs:obsidian_forged_thermal_casing',
      '2x kubejs:thermocrack_matrix_casing',
      '2x gtceu:tungsten_steel_large_fluid_pipe',
      '2x gtceu:luv_electric_pump',
      '1x gtceu:luv_sensor',
      '1x gtceu:luv_field_generator',
      Ingredient.of('#gtceu:circuits/luv').withCount(2)
    )
    .inputFluids('gtceu:soldering_alloy 1152')
    .itemOutputs('1x gtceu:pyrolysis_chamber')
    .duration(2400)
    .EUt(GTValues.V[GTValues.LuV]);

  chemicalReactor(id('phthalic_anhydride'))
    .inputFluids('gtceu:benzene 1000', 'gtceu:oxygen 3000')
    .itemOutputs('1x gtceu:phthalic_anhydride_dust')
    .outputFluids('minecraft:water 1000')
    .circuit(7)
    .duration(2400)
    .EUt(GTValues.V[GTValues.MV]);

  largeChemicalReactor(id('trimellitic_anhydride'))
    .itemInputs('1x gtceu:phthalic_anhydride_dust')
    .inputFluids('gtceu:carbon_monoxide 1000', 'gtceu:oxygen 2000')
    .itemOutputs('1x gtceu:trimellitic_anhydride_dust')
    .duration(2600)
    .EUt(GTValues.V[GTValues.HV]);

  // FLUOROPOLYMER FRACTIONATION CASING

  chemicalReactor(id('ceramic_fiber'))
    .itemInputs('2x gtceu:alumina_dust', '1x gtceu:nether_quartz_dust')
    .itemOutputs('3x gtceu:ceramic_fiber_dust')
    .duration(1600)
    .EUt(GTValues.V[GTValues.HV]);

  chemicalReactor(id('hfp_synthesis'))
    .inputFluids('gtceu:propene 3000', 'gtceu:fluorine 6000')
    .outputFluids('gtceu:hexafluoropropylene 1000', 'gtceu:hydrogen_fluoride 6000')
    .duration(2400)
    .EUt(GTValues.V[GTValues.EV]);

  largeChemicalReactor(id('pfa_slurry_polymerization'))
    .inputFluids('gtceu:hexafluoropropylene 2000', 'gtceu:tetrafluoroethylene 3000', 'gtceu:oxygen 1000')
    .outputFluids('gtceu:perfluoroalkoxy_polymer_slurry 5000')
    .duration(3600)
    .EUt(GTValues.V[GTValues.EV]);

  distillationTower(id('pfa_slurry_fractionation'))
    .inputFluids('gtceu:perfluoroalkoxy_polymer_slurry 5000')
    .outputFluids('gtceu:fluoropolymer_residue 2000')
    .itemOutputs('3x gtceu:fluoropolymer_structural_film_dust')
    .duration(2400)
    .EUt(GTValues.V[GTValues.EV]);

  assembler(id('fluoropolymer_fractionation_casing'))
    .itemInputs('3x gtceu:fluoropolymer_structural_film_dust', '4x gtceu:titanium_plate', '2x gtceu:ceramic_fiber_dust')
    .itemOutputs('1x kubejs:fluoropolymer_fractionation_casing')
    .duration(400)
    .EUt(GTValues.V[GTValues.IV]);

  // POLYETHERIMIDE THERMAL CASING

  largeChemicalReactor(id('m_dinitrobenzene'))
    .inputFluids('gtceu:nitrobenzene 1000', 'gtceu:nitric_acid 2000')
    .itemOutputs('1x gtceu:m_dinitrobenzene_dust')
    .outputFluids('minecraft:water 1000')
    .circuit(1)
    .duration(2200)
    .EUt(GTValues.V[GTValues.EV]);

  chemicalReactor(id('m_phenylenediamine'))
    .itemInputs('1x gtceu:m_dinitrobenzene_dust')
    .inputFluids('gtceu:hydrogen 6000')
    .itemOutputs('1x gtceu:m_phenylenediamine_dust')
    .outputFluids('minecraft:water 4000')
    .duration(2400)
    .EUt(GTValues.V[GTValues.EV]);

  largeChemicalReactor(id('bisphenol_a_dianhydride'))
    .itemInputs('2x gtceu:phthalic_anhydride_dust')
    .inputFluids('gtceu:acetone 1000')
    .itemOutputs('1x gtceu:bisphenol_a_dianhydride_dust')
    .duration(2600)
    .EUt(GTValues.V[GTValues.IV]);

  chemicalReactor(id('polyetherimide'))
    .itemInputs('1x gtceu:bisphenol_a_dianhydride_dust', '1x gtceu:m_phenylenediamine_dust')
    .outputFluids('gtceu:polyetherimide 2000')
    .duration(3200)
    .EUt(GTValues.V[GTValues.EV]);

  assembler(id('polyetherimide_thermal_casing'))
    .itemInputs('2x gtceu:niobium_plate', '2x gtceu:fine_borosilicate_glass_wire')
    .inputFluids('gtceu:polyetherimide 2000')
    .itemOutputs('1x kubejs:polyetherimide_thermal_casing')
    .duration(400)
    .EUt(GTValues.V[GTValues.IV]);

  // MOLECULAR MEMBRANE CASING

  largeChemicalReactor(id('pyromellitic_dianhydride'))
    .itemInputs('2x gtceu:trimellitic_anhydride_dust')
    .inputFluids('gtceu:oxygen 1000')
    .itemOutputs('1x gtceu:pyromellitic_dianhydride_dust')
    .duration(2200)
    .EUt(GTValues.V[GTValues.EV]);

  chemicalReactor(id('p_chloronitrobenzene'))
    .inputFluids('gtceu:chlorobenzene 1000', 'gtceu:nitric_acid 1000')
    .itemOutputs('1x gtceu:p_chloronitrobenzene_dust')
    .outputFluids('minecraft:water 1000')
    .circuit(2)
    .duration(1800)
    .EUt(GTValues.V[GTValues.EV]);

  chemicalReactor(id('p_nitrophenol'))
    .inputFluids('gtceu:phenol 1000', 'gtceu:nitric_acid 1000')
    .itemOutputs('1x gtceu:p_nitrophenol_dust')
    .outputFluids('minecraft:water 1000')
    .circuit(2)
    .duration(1800)
    .EUt(GTValues.V[GTValues.EV]);

  largeChemicalReactor(id('dinitrodiphenyl_ether_4_4'))
    .itemInputs('1x gtceu:p_chloronitrobenzene_dust', '1x gtceu:p_nitrophenol_dust', '1x gtceu:sodium_hydroxide_dust')
    .itemOutputs('1x gtceu:dinitrodiphenyl_ether_4_4_dust', '1x gtceu:salt_dust')
    .outputFluids('minecraft:water 1000')
    .duration(2800)
    .EUt(GTValues.V[GTValues.IV]);

  chemicalReactor(id('oxydianiline'))
    .itemInputs('1x gtceu:dinitrodiphenyl_ether_4_4_dust')
    .inputFluids('gtceu:hydrogen 12000')
    .itemOutputs('1x gtceu:oxydianiline_dust')
    .outputFluids('minecraft:water 4000')
    .duration(3200)
    .EUt(GTValues.V[GTValues.IV]);

  chemicalReactor(id('polyimide_solution'))
    .itemInputs('1x gtceu:pyromellitic_dianhydride_dust', '1x gtceu:oxydianiline_dust')
    .outputFluids('gtceu:polyimide_solution 2000')
    .duration(2600)
    .EUt(GTValues.V[GTValues.EV]);

  largeChemicalReactor(id('iron_pentacarbonyl'))
    .itemInputs('1x gtceu:iron_dust')
    .inputFluids('gtceu:carbon_monoxide 5000')
    .outputFluids('gtceu:iron_pentacarbonyl 1000')
    .duration(2400)
    .EUt(GTValues.V[GTValues.HV]);

  pyrolyseOven(id('as_grown_multiwalled_carbon_nanotubes'))
    .itemInputs('1x gtceu:calcium_carbide_dust')
    .inputFluids('gtceu:iron_pentacarbonyl 1000')
    .itemOutputs('2x gtceu:as_grown_multiwalled_carbon_nanotubes_dust')
    .outputFluids('gtceu:hydrogen 6000')
    .duration(2600)
    .EUt(GTValues.V[GTValues.HV]);

  thermalCentrifuge(id('purified_multiwalled_carbon_nanotubes'))
    .itemInputs('2x gtceu:as_grown_multiwalled_carbon_nanotubes_dust')
    .itemOutputs('1x gtceu:purified_multiwalled_carbon_nanotubes_dust', '1x gtceu:carbon_dust')
    .duration(1800)
    .EUt(GTValues.V[GTValues.MV]);

  electromagneticSeparator(id('carbon_nanotubes'))
    .itemInputs('1x gtceu:purified_multiwalled_carbon_nanotubes_dust')
    .itemOutputs('1x kubejs:carbon_nanotubes', '2x gtceu:iron_dust')
    .duration(1200)
    .EUt(GTValues.V[GTValues.HV]);


  chemicalReactor(id('microporous_polyimide_membrane'))
    .inputFluids('gtceu:polyimide_solution 2000')
    .itemInputs('1x kubejs:carbon_nanotubes')
    .itemOutputs('2x gtceu:microporous_polyimide_membrane_dust')
    .duration(2400)
    .EUt(GTValues.V[GTValues.EV]);

  assembler(id('fine_stainless_mesh'))
    .itemInputs('4x gtceu:stainless_steel_foil')
    .itemOutputs('1x kubejs:fine_stainless_mesh')
    .circuit(7)
    .duration(200)
    .EUt(GTValues.V[GTValues.LV]);

  formingPress(id('ceramic_filter'))
    .itemInputs('2x gtceu:ceramic_fiber_dust', '1x gtceu:alumina_dust')
    .notConsumable('1x gtceu:cylinder_casting_mold')
    .itemOutputs('1x kubejs:ceramic_filter')
    .duration(1200)
    .EUt(GTValues.V[GTValues.EV]);

  assembler(id('molecular_membrane_casing'))
    .itemInputs('2x gtceu:microporous_polyimide_membrane_dust', '2x gtceu:titanium_plate', '1x kubejs:fine_stainless_mesh')
    .itemOutputs('1x kubejs:molecular_membrane_casing')
    .duration(400)
    .EUt(GTValues.V[GTValues.IV]);

  // POLYSILOXANE VAPOR CONTROL CASING

  largeChemicalReactor(id('polysiloxane_polymer'))
    .inputFluids('gtceu:dimethyldichlorosilane 2000', 'minecraft:water 2000')
    .outputFluids('gtceu:polysiloxane 1000', 'gtceu:hydrogen_chloride 2000')
    .duration(2000)
    .EUt(GTValues.V[GTValues.EV]);

  assembler(id('polysiloxane_vapor_control_casing'))
    .itemInputs('2x gtceu:stainless_steel_plate', '1x kubejs:ceramic_filter')
    .inputFluids('gtceu:polysiloxane 1000')
    .itemOutputs('1x kubejs:polysiloxane_vapor_control_casing')
    .duration(400)
    .EUt(GTValues.V[GTValues.IV]);

  // CARBONIZED REACTOR CASING

  largeChemicalReactor(id('phenolic_resin'))
    .inputFluids('gtceu:phenol 2000', 'gtceu:formaldehyde 1000')
    .itemOutputs('2x gtceu:phenolic_resin_dust')
    .duration(2000)
    .EUt(GTValues.V[GTValues.EV]);

  pyrolyseOven(id('carbon_matrix_composite'))
    .itemInputs('2x gtceu:phenolic_resin_dust', '2x gtceu:graphite_dust')
    .itemOutputs('3x gtceu:carbon_matrix_composite_dust')
    .outputFluids('gtceu:hydrogen 1000')
    .duration(3000)
    .EUt(GTValues.V[GTValues.IV]);

  chemicalReactor(id('high_temperature_insulation'))
    .itemInputs('2x gtceu:ceramic_fiber_dust','1x gtceu:silicon_dioxide_dust')
    .itemOutputs('1x gtceu:high_temperature_insulation_dust')
    .duration(1600)
    .EUt(GTValues.V[GTValues.EV]);

  assembler(id('carbonized_reactor_casing'))
    .itemInputs('3x gtceu:carbon_matrix_composite_dust', '2x gtceu:tungsten_plate', '1x gtceu:high_temperature_insulation_dust')
    .itemOutputs('1x kubejs:carbonized_reactor_casing')
    .duration(400)
    .EUt(GTValues.V[GTValues.LuV]);

  // OBSIDIAN-FORGED THERMAL CASING

  pyrolyseOven(id('molten_volcanic_glass'))
    .itemInputs('4x gtceu:obsidian_dust', '2x gtceu:basalt_dust')
    .outputFluids('gtceu:molten_volcanic_glass 3000')
    .duration(2400)
    .EUt(GTValues.V[GTValues.IV]);

  largeChemicalReactor(id('obsidian_ceramic_composite'))
    .inputFluids('gtceu:molten_volcanic_glass 3000')
    .itemInputs('1x gtceu:alumina_dust')
    .itemOutputs('2x gtceu:obsidian_ceramic_composite_dust')
    .duration(2000)
    .EUt(GTValues.V[GTValues.EV]);

  assembler(id('obsidian_forged_thermal_casing'))
    .itemInputs('2x gtceu:obsidian_ceramic_composite_dust', '2x gtceu:hsss_plate', '1x gtceu:high_temperature_insulation_dust')
    .itemOutputs('1x kubejs:obsidian_forged_thermal_casing')
    .duration(400)
    .EUt(GTValues.V[GTValues.LuV]);

  // THERMOCRACK MATRIX CASING

  largeChemicalReactor(id('aluminosilicate_gel'))
    .inputFluids('gtceu:sodium_silicate_solution 1000', 'gtceu:sodium_aluminate_solution 2000')
    .itemOutputs('3x gtceu:aluminosilicate_gel_dust')
    .duration(2000)
    .EUt(GTValues.V[GTValues.EV]);

  largeChemicalReactor(id('zeolite_catalyst_matrix'))
    .itemInputs('2x gtceu:aluminosilicate_gel_dust', '1x gtceu:sodium_hydroxide_dust')
    .itemOutputs('1x gtceu:zeolite_catalyst_matrix_dust')
    .outputFluids('minecraft:water 1000')
    .duration(2000)
    .EUt(GTValues.V[GTValues.EV]);

  largeChemicalReactor(id('catalytic_cracking_matrix'))
    .itemInputs('1x gtceu:zeolite_catalyst_matrix_dust', '1x gtceu:vanadium_pentoxide_dust', '2x gtceu:alumina_dust')
    .itemOutputs('2x gtceu:catalytic_cracking_matrix_dust')
    .duration(2400)
    .EUt(GTValues.V[GTValues.IV]);

  assembler(id('heatproof_alloy_mesh'))
    .itemInputs('2x gtceu:tungsten_steel_foil', '2x gtceu:kanthal_foil')
    .itemOutputs('1x kubejs:heatproof_alloy_mesh')
    .circuit(7)
    .duration(300)
    .EUt(GTValues.V[GTValues.EV]);

  assembler(id('thermocrack_matrix_casing'))
    .itemInputs('2x gtceu:catalytic_cracking_matrix_dust', '2x gtceu:tungsten_steel_plate', '1x kubejs:heatproof_alloy_mesh')
    .itemOutputs('1x kubejs:thermocrack_matrix_casing')
    .duration(400)
    .EUt(GTValues.V[GTValues.LuV]);

});