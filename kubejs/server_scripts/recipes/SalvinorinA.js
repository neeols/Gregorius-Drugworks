ServerEvents.recipes(event => {
    const id = global.id;

    const gt = () => event.recipes.gtceu

    const chemicalReactor      = (rid) => gt().chemical_reactor(rid)
    const largeChemicalReactor = (rid) => gt().large_chemical_reactor(rid)
    const electricBlastFurnace = (rid) => gt().electric_blast_furnace(rid)
    const extractor            = (rid) => gt().extractor(rid)
    const distillationUnit     = (rid) => gt().distillation_unit(rid)
    const centrifuge           = (rid) => gt().centrifuge(rid)
    const mixer                = (rid) => gt().mixer(rid)
    const pyrolysisChamber     = (rid) => gt().pyrolysis_chamber(rid)
    const electrolyzer         = (rid) => gt().electrolyzer(rid)
    const thermalCentrifuge    = (rid) => gt().thermal_centrifuge(rid)
    const chemicalPlant        = (rid) => gt().chemical_plant(rid)
    const fluidHeater          = (rid) => gt().fluid_heater(rid);
    const canner               = (rid) => gt().canner(rid);
    const assembler            = (rid) => gt().assembler(rid);

    var CRtype = [largeChemicalReactor, chemicalReactor]
    CRtype.forEach(CR=>{
        CR(id('sal_a_terpene_like_ketone_core'))
        .inputFluids('gtceu:isoprene 4000', 'gtceu:acetone 1000')
        .chancedFluidInput('gtceu:sulfuric_acid 100', 500, 0)
        .outputFluids('gtceu:sal_a_terpene_like_ketone_core 1000', 'minecraft:water 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);
        
        CR(id('sal_a_enone_precursor'))
        .inputFluids('gtceu:sal_a_terpene_like_ketone_core 1000')
        .chancedInput('1x gcyr:aluminium_trichloride_dust', 500, 0)
        .outputFluids('gtceu:sal_a_enone_precursor 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('sal_a_enone_4'))
        .inputFluids('gtceu:sal_a_enone_precursor 1000', 'gtceu:oxygen 2000')
        .outputFluids('minecraft:water 1000')
        .itemOutputs('1x gtceu:sal_a_enone_4_dust')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('crude_pyridine_synthesis'))
        .inputFluids('gtceu:acetaldehyde 2000', 'gtceu:formaldehyde 1000', 'gtceu:ammonia 1000')
        .outputFluids('gtceu:crude_pyridine 1000', 'minecraft:water 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('pyridinium_chlorochromate'))
        .inputFluids('gtceu:hydrogen_chloride 1000', 'gtceu:pyridine 1000')
        .itemInputs('1x gtceu:chromium_trioxide_dust')
        .outputFluids('minecraft:water 1000')
        .itemOutputs('1x gtceu:pyridinium_chlorochromate_dust')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('chloroacetic_acid_synthesis'))
        .inputFluids('gtceu:acetic_acid 1000', 'gtceu:chlorine 1000')
        .outputFluids('gtceu:hydrogen_chloride 1000', 'gtceu:chloroacetic_acid 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('iodoacetic_acid_synthesis'))
        .inputFluids('gtceu:acetic_acid 1000')
        .itemInputs('1x gtceu:potassium_iodide_dust')
        .outputFluids('gtceu:iodoacetic_acid 1000')
        .itemOutputs('1x gcyr:potassium_chloride_dust')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('acetylene'))
        .inputFluids('minecraft:water 1000')
        .itemInputs('1x gtceu:calcium_carbide_dust')
        .outputFluids('gtceu:acetylene 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('aluminium_chloride'))
        .inputFluids('gtceu:chlorine 3000')
        .itemInputs('1x gtceu:aluminium_dust')
        .itemOutputs('1x gcyr:aluminium_trichloride_dust')
        .duration(800)
        .EUt(GTValues.V[GTValues.HV]);

        CR(id('methylbromide'))
        .inputFluids('gtceu:methane 1000', 'gtceu:bromine 1000')
        .outputFluids('gtceu:methylbromide 1000', 'gtceu:hydrogen_bromide 1000')
        .duration(800)
        .EUt(GTValues.V[GTValues.HV]);

        CR(id('methylchloride'))
        .inputFluids('gtceu:methane 1000', 'gtceu:chlorine 1000')
        .circuit(4)
        .outputFluids('gtceu:methylchloride 1000', 'gtceu:hydrogen_chloride 1000')
        .duration(800)
        .EUt(GTValues.V[GTValues.HV]);

        CR(id('hmds_synthesis'))
        .inputFluids('gtceu:chlorotrimethylsilane 2000', 'gtceu:ammonia 1000')
        .outputFluids('gtceu:hmds 1000', 'gtceu:hydrogen_chloride 2000')
        .duration(900)
        .EUt(GTValues.V[GTValues.HV]);

        CR(id('nahmds_synthesis'))
        .inputFluids('gtceu:hmds 1000')
        .itemInputs('1x gtceu:sodium_hydride_dust')
        .outputFluids('gtceu:hydrogen 1000', 'gtceu:nahmds 1000')
        .duration(900)
        .EUt(GTValues.V[GTValues.HV]);

        CR(id('methyl_triphenyl_phosphonium_bromide_synthesis'))
        .inputFluids('gtceu:methylbromide 1000')
        .itemInputs('1x gtceu:triphenylphosphine_dust')
        .itemOutputs('1x gtceu:methyl_triphenyl_phosphonium_bromide_dust')
        .duration(800)
        .EUt(GTValues.V[GTValues.HV]);

        CR(id('chloroethane_synthesis'))
        .inputFluids('gtceu:ethylene 1000', 'gtceu:hydrogen_chloride 1000')
        .outputFluids('gtceu:chloroethane 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('diethyl_ether_synthesis'))
        .inputFluids('gtceu:chloroethane 1000', 'gtceu:ethanol 1000')
        .itemInputs('1x gtceu:sodium_hydroxide_dust')
        .outputFluids('gtceu:diethyl_ether 1000', 'minecraft:water 1000')
        .itemOutputs('2x gtceu:salt_dust')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('lithium_hydride_synthesis'))
        .inputFluids('gtceu:hydrogen 2000')
        .itemInputs('1x gtceu:lithium_dust')
        .itemOutputs('1x gtceu:lithium_hydride_dust')
        .duration(1600)
        .EUt(GTValues.V[GTValues.EV]);

        CR(id('lithium_aluminium_hydride_synthesis'))
        .itemInputs('4x gtceu:lithium_hydride_dust', '1x gcyr:aluminium_trichloride_dust')
        .itemOutputs('1x gtceu:lithium_aluminium_hydride_dust', '3x gtceu:lithium_chloride_dust')
 
        .duration(1600)
        .EUt(GTValues.V[GTValues.EV]);

        CR(id('methylchloride_synthesis'))
        .inputFluids('gtceu:methanol 1000', 'gtceu:hydrogen_chloride 1000')
        .outputFluids('gtceu:methylchloride 1000', 'minecraft:water 1000')
        .duration(800)
        .EUt(GTValues.V[GTValues.HV]);

        CR(id('isobutylene_synthesis'))
        .inputFluids('gtceu:methanol 1000', 'gtceu:propene 1000')
        .outputFluids('gtceu:isobutylene 1000', 'minecraft:water 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('isobutanol_synthesis'))
        .inputFluids('gtceu:isobutylene 1000', 'minecraft:water 1000')
        .outputFluids('gtceu:isobutanol 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('ethyl_chloride_synthesis'))
        .inputFluids('gtceu:ethanol 1000', 'gtceu:hydrogen_chloride 1000')
        .outputFluids('gtceu:ethyl_chloride 1000', 'minecraft:water 1000')
        .duration(800)
        .EUt(GTValues.V[GTValues.HV]);

        CR(id('triethylamine_synthesis'))
        .inputFluids('gtceu:ethyl_chloride 1000', 'gtceu:ammonia 1000')
        .outputFluids('gtceu:hydrogen_chloride 3000', 'gtceu:triethylamine 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('hydrogen_fluoride_synthesis'))
        .inputFluids('gtceu:sulfuric_acid 1000')
        .itemInputs('1x gcyr:fluorite_dust')
        .outputFluids('gtceu:hydrogen_fluoride 2000')
        .itemOutputs('1x gtceu:calcium_sulfate_dust')
        .duration(400)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('potassium_fluoride_synthesis'))
        .inputFluids('gtceu:hydrogen_fluoride 1000')
        .itemInputs('1x gtceu:potassium_hydroxide_dust')
        .outputFluids('minecraft:water 1000')
        .itemOutputs('1x gtceu:potassium_fluoride_dust')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('butyl_chloride_synthesis'))
        .inputFluids('gtceu:butanol 1000', 'gtceu:hydrogen_chloride 1000')
        .circuit(5)
        .outputFluids('minecraft:water 1000', 'gtceu:butyl_chloride 1000')
        .duration(800)
        .EUt(GTValues.V[GTValues.HV]);

        CR(id('tributylamine_synthesis'))
        .inputFluids('gtceu:butyl_chloride 3000', 'gtceu:ammonia 1000')
        .outputFluids('gtceu:hydrogen_chloride 3000', 'gtceu:tributylamine 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('tbacl_synthesis'))
        .inputFluids('gtceu:tributylamine 1000', 'gtceu:butyl_chloride 1000')
        .itemOutputs('1x gtceu:tbacl_dust')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('tbaf_synthesis'))
        .chancedFluidInput('gtceu:thf 500', 1000, 0)
        .itemInputs('1x gtceu:potassium_fluoride_dust', '1x gtceu:tbacl_dust')
        .itemOutputs('1x gcyr:potassium_chloride_dust', '1x gtceu:tbaf_dust')
        .duration(900)
        .EUt(GTValues.V[GTValues.HV]);
    
        CR(id('tbsf_recycle'))
        .inputFluids('minecraft:water 1000')
        .itemInputs('1x gtceu:tbsf_dust')
        .outputFluids('gtceu:hydrogen_fluoride 1000', 'gtceu:tertbutyldimethylsilanol 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('anisole_synthesis'))
        .inputFluids('gtceu:phenol 1000', 'gtceu:methanol 1000')
        .outputFluids('minecraft:water 1000', 'gtceu:anisole 1000')
        .circuit(2)
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('boric_acid_synthesis'))
        .inputFluids('gtceu:hydrochloric_acid 2000')
        .itemInputs('1x gtceu:sodium_borate_dust')
        .outputFluids('gtceu:brine 2000', 'gtceu:boric_acid 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('sodium_acetate_synthesis'))
        .inputFluids('gtceu:acetic_acid 1000')
        .itemInputs('1x gtceu:sodium_hydroxide_dust')
        .outputFluids('minecraft:water 1000')
        .itemOutputs('1x gtceu:sodium_acetate_dust')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('sodium_methoxide_synthesis'))
        .inputFluids('gtceu:methanol 1000')
        .itemInputs('1x gtceu:sodium_dust')
        .outputFluids('gtceu:sodium_methoxide 1000', 'gtceu:hydrogen 1000')
        .circuit(4)
        .duration(1200)
        .EUt(GTValues.V[GTValues.EV]);

        CR(id('ethylene_oxide_synthesis'))
        .inputFluids('gtceu:ethylene 1000', 'gtceu:oxygen 1000')
        .chancedInput('1x gtceu:silver_dust', 500, 0)
        .outputFluids('gtceu:ethylene_oxide 1000')
        .circuit(4)
        .duration(1200)
        .EUt(GTValues.V[GTValues.EV]);

        CR(id('ethylene_glycol_synthesis'))
        .inputFluids('gtceu:ethylene_oxide 1000', 'minecraft:water 1000')
        .outputFluids('gtceu:ethylene_glycol 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('ethylene_to_acetaldehyde_oxidation'))
        .chancedInput('1x gtceu:palladium_dust', 500, 0)
        .inputFluids('gtceu:ethylene 1000', 'gtceu:oxygen 1000', 'minecraft:water 1000')
        .outputFluids('gtceu:acetaldehyde 1000')
        .duration(1200)
        .EUt(GTValues.V[GTValues.EV]);

        CR(id('carbon_monoxide_to_acetaldehyde'))
        .chancedInput('1x gtceu:rhodium_dust', 500, 0)
        .inputFluids('gtceu:carbon_monoxide 2000', 'gtceu:hydrogen 4000', 'minecraft:water 1000')
        .outputFluids('gtceu:acetaldehyde 1000', 'minecraft:water 1000')
        .duration(1200)
        .EUt(GTValues.V[GTValues.EV]);

        CR(id('acetaldehyde_to_butanol'))
        .inputFluids('gtceu:acetaldehyde 2000', 'gtceu:hydrogen 2000')
        .outputFluids('gtceu:butanol 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('butanol_to_butanone'))
        .inputFluids('gtceu:butanol 1000', 'gtceu:oxygen 1000')
        .outputFluids('gtceu:butanone 1000', 'minecraft:water 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('furfural_to_furan'))
        .inputFluids('gtceu:furfural 1000', 'gtceu:hydrogen 1000')
        .outputFluids('gtceu:furan 1000', 'minecraft:water 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('furan_to_3_bromofuran'))
        .inputFluids('gtceu:furan 1000', 'gtceu:bromine 1000')
        .outputFluids('gtceu:3_bromofuran 1000', 'gtceu:hydrogen_bromide 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('tertbutylchloride_synthesis'))
        .inputFluids('gtceu:isobutylene 1000', 'gtceu:hydrogen_chloride 1000')
        .outputFluids('gtceu:tertbutylchloride 1000')
        .duration(800)
        .EUt(GTValues.V[GTValues.HV]);

        CR(id('ddq_unreduction'))
        .inputFluids('gtceu:oxygen 500')
        .itemInputs('1x gtceu:ddq_reduced_dust')
        .outputFluids('minecraft:water 250')
        .itemOutputs('1x gtceu:ddq_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.EV]);

        CR(id('vanadium_pentoxide_synthesis'))
        .inputFluids('gtceu:oxygen 5000')
        .itemInputs('1x gtceu:vanadium_dust')
        .itemOutputs('1x gtceu:vanadium_pentoxide_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.EV]);

        CR(id('2methyl2butene_from_isobutanol'))
        .chancedFluidInput('gtceu:sulfuric_acid 100', 500, 0)
        .inputFluids('gtceu:isobutanol 1000')
        .outputFluids('gtceu:2methyl2butene 1000', 'minecraft:water 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('cyclohexylamine_synthesis'))
        .inputFluids('gtceu:benzene 1000', 'gtceu:hydrogen 6000', 'gtceu:ammonia 1000')
        .outputFluids('gtceu:cyclohexylamine 1000')
        .circuit(10)
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

        CR(id('dicyclohexylurea_synthesis'))
        .inputFluids('gtceu:cyclohexylamine 1000', 'gtceu:carbon_dioxide 1000')
        .outputFluids('minecraft:water 1000')
        .itemOutputs('1x gtceu:dicyclohexylurea_dust')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);
    })

    electricBlastFurnace(id('aluminium_chloride_dust'))
        .inputFluids('gtceu:chlorine 1000')
        .itemInputs('1x gtceu:aluminium_dust')
        .itemOutputs('1x gcyr:aluminium_trichloride_dust')
        .circuit(3)
        .duration(4200)
        .EUt(GTValues.V[GTValues.EV]);

    electricBlastFurnace(id('calcium_carbide_dust'))
        .itemInputs('1x gtceu:quicklime_dust', '1x gtceu:coke_dust')
        .itemOutputs('1x gtceu:calcium_carbide_dust')
        .circuit(3)
        .duration(4200)
        .EUt(GTValues.V[GTValues.EV]);


    electricBlastFurnace(id('hydrogen_chloride_dust'))
        .inputFluids('gtceu:hydrogen 1000')
        .itemInputs('1x gtceu:chlorine_dust')
        .itemOutputs('1x gtceu:hydrogen_chloride_dust')
        .duration(4200)
        .EUt(GTValues.V[GTValues.EV]);

    electricBlastFurnace(id('chromium_ingot_from_chromium_trioxide_1'))
        .itemInputs('1x gtceu:chromium_trioxide_dust', '3x gtceu:carbon_dust')
        .itemOutputs('2x gtceu:chromium_ingot')
        .outputFluids('gtceu:carbon_monoxide 3000')
        .duration(4200)
        .EUt(GTValues.V[GTValues.EV]);

    electricBlastFurnace(id('chromium_ingot_from_chromium_trioxide_2'))
        .itemInputs('1x gtceu:chromium_trioxide_dust', '2x gtceu:coke_dust')
        .itemOutputs('2x gtceu:chromium_ingot')
        .outputFluids('gtceu:carbon_monoxide 3000')
        .duration(4200)
        .EUt(GTValues.V[GTValues.EV]);

    extractor(id('hydrogen_chloride'))
        .itemInputs('1x gtceu:hydrogen_chloride_dust')
        .outputFluids('gtceu:hydrogen_chloride 1000')
        .duration(400)
        .EUt(GTValues.V[GTValues.MV]);

    distillationUnit(id('crude_pyridine_distillation'))
        .inputFluids('gtceu:crude_pyridine 1000')
        .outputFluids('gtceu:pyridine 800', 'gtceu:pyridine_residue 200')
        .duration(2400)
        .EUt(GTValues.V[GTValues.HV]);

    centrifuge(id('pyridine_residue_centrifuge'))
        .inputFluids('gtceu:pyridine_residue 100')
        .outputFluids('gtceu:pyridine_waste_water 200')
        .itemOutputs('1x gtceu:carbon_dust')
        .duration(300)
        .EUt(GTValues.V[GTValues.MV]);

    event.recipes.gtceu.chemical_reactor(id('dichloromethane'))
        .inputFluids('gtceu:chloromethane 1000', 'gtceu:chlorine 1000')
        .outputFluids('gtceu:hydrogen_chloride 1000', 'gtceu:dichloromethane 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.MV]);

    largeChemicalReactor(id('ethyl_iodoacetate_synthesis'))
        .chancedFluidInput('gtceu:sulfuric_acid 250', 2000, 0)
        .inputFluids('gtceu:iodoacetic_acid 1000', 'gtceu:ethanol 1000')
        .outputFluids('gtceu:ethyl_iodoacetate 1000', 'minecraft:water 1000')
        .chancedFluidOutput('gtceu:ethyl_acetate 50', 500, 0)
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('butanediol_synthesis'))
        .inputFluids('gtceu:acetylene 1000', 'gtceu:formaldehyde 2000', 'gtceu:hydrogen 2000')
        .chancedInput('1x gtceu:copper_dust', 500, 0)
        .chancedInput('1x gtceu:nickel_dust', 500, 0)
        .outputFluids('gtceu:1_4_butanediol 1000')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('thf_synthesis'))
        .chancedFluidInput('gtceu:sulfuric_acid 250', 2000, 0)
        .inputFluids('gtceu:1_4_butanediol 1000')
        .outputFluids('gtceu:thf 1000', 'minecraft:water 1000')
        .chancedFluidOutput('gtceu:butene 50', 1000, 0)
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    mixer(id('nahmds_in_thf'))
        .inputFluids('gtceu:nahmds 1000', 'gtceu:thf 1000')
        .outputFluids('gtceu:nahmdsinthf 2000')
        .duration(200)
        .EUt(GTValues.V[GTValues.LV]);

    largeChemicalReactor(id('butene_oligomerisation_to_1_hexene'))
        .inputFluids('gtceu:butene 2000')
        .chancedInput('1x gtceu:nickel_dust', 500, 0)
        .circuit(1)
        .outputFluids('gtceu:1_hexene 1000', 'gtceu:ethylene 1000')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('hexane_hydrogenation'))
        .inputFluids('gtceu:1_hexene 1000', 'gtceu:hydrogen 2000')
        .chancedInput('1x gtceu:nickel_dust', 500, 0)
        .outputFluids('gtceu:hexane 1000')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('2methyl2butene_isomerisation'))
        .inputFluids('gtceu:butene 1000')
        .chancedInput('1x gcyr:aluminium_trichloride_dust', 500, 0)
        .circuit(2)
        .outputFluids('gtceu:2methyl2butene 1000')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('ethyl_acetate_to_ethyl_iodoacetate'))
        .chancedInput('1x gtceu:iodine_dust', 5000, 0)
        .inputFluids('gtceu:ethyl_acetate 1000', 'gtceu:oxygen 1000')
        .chancedInput('1x gtceu:phosphorus_dust', 500, 0)
        .outputFluids('gtceu:ethyl_iodoacetate 1000', 'minecraft:water 1000')
        .chancedFluidOutput('gtceu:butene 50', 1000, 0)
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('chlorotrimethylsilane'))
        .inputFluids('gtceu:methylchloride 3000', 'gtceu:chlorine 1000')
        .itemInputs('1x gtceu:silicon_dust')
        .outputFluids('gtceu:chlorotrimethylsilane 1000', 'gtceu:hydrogen 500')
        .chancedFluidOutput('gtceu:silicon_tetrachloride 250', 500, 200)
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    mixer(id('brine_mixer'))
        .inputFluids('minecraft:water 1000')
        .itemInputs('1x gtceu:salt_dust')
        .outputFluids('gtceu:brine 1000')
        .duration(200)
        .EUt(GTValues.V[GTValues.LV]);

    mixer(id('ammonium_chloride_solution_mixer'))
        .inputFluids('minecraft:water 1000')
        .itemInputs('1x gtceu:ammonium_chloride_dust')
        .outputFluids('gtceu:ammonium_chloride_solution 1000')
        .duration(200)
        .EUt(GTValues.V[GTValues.LV]);

    largeChemicalReactor(id('triphenylphosphine_synthesis'))
        .inputFluids('gtceu:benzene 3000', 'gtceu:phosphorus_trichloride 1000', 'gtceu:hydrogen 3000')
        .outputFluids('gtceu:hydrogen_chloride 3000')
        .itemOutputs('1x gtceu:triphenylphosphine_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('tertbutyldimethylsilanol_synthesis'))
        .inputFluids('gtceu:methylchloride 2000', 'gtceu:isobutylene 1000', 'minecraft:water 1000')
        .itemInputs('1x gtceu:silicon_dust')
        .outputFluids('gtceu:tertbutyldimethylsilanol 1000', 'gtceu:hydrogen_chloride 2000')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('tbscl_synthesis'))
        .inputFluids('gtceu:thionyl_chloride 1000', 'gtceu:tertbutyldimethylsilanol 1000')
        .outputFluids('gtceu:tbscl 1000', 'gtceu:sulfur_dioxide 1000', 'gtceu:hydrogen_chloride 1000')
        .duration(1800)
        .EUt(GTValues.V[GTValues.EV]);

    largeChemicalReactor(id('aminopyridine_synthesis'))
        .inputFluids('gtceu:pyridine 1000', 'gtceu:ammonia 1000', 'gtceu:hydrogen 1000')
        .outputFluids('gtceu:aminopyridine 1000', 'minecraft:water 1000')
        .circuit(1)
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('dmap_synthesis'))
        .inputFluids('gtceu:aminopyridine 1000', 'gtceu:methylchloride 2000')
        .itemOutputs('1x gtceu:dmap_dust')
        .outputFluids('gtceu:hydrogen_chloride 2000')
        .circuit(1)
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('methoxybenzyl_alcohol_synthesis'))
        .inputFluids('gtceu:anisole 1000', 'gtceu:hydrogen 1000', 'gtceu:formaldehyde 1000')
        .outputFluids('gtceu:methoxybenzyl_alcohol 1000')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('4_methoxybenzyl_chloride_synthesis'))
        .inputFluids('gtceu:methoxybenzyl_alcohol 1000', 'gtceu:thionyl_chloride 1000')
        .outputFluids('gtceu:sulfur_dioxide 1000', 'gtceu:hydrogen_chloride 1000', 'gtceu:4_methoxybenzyl_chloride 1000')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    pyrolysisChamber(id('borax_to_sodium_borate'))
        .itemInputs('1x gtceu:borax_dust')
        .outputFluids('gtceu:steam 10000')
        .itemOutputs('1x gtceu:sodium_borate_dust')
        .duration(3000)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('borane_synthesis'))
        .itemInputs('3x gtceu:magnesium_dust')
        .inputFluids('gtceu:hydrogen 6000', 'gtceu:boric_acid 1000')
        .outputFluids('gtceu:borane 1000', 'minecraft:water 3000')
        .itemOutputs('3x gtceu:magnesium_oxide_dust')
        .duration(1800)
        .EUt(GTValues.V[GTValues.EV]);

    mixer(id('borane_thf_mixer'))
        .inputFluids('gtceu:borane 1000', 'gtceu:thf 1000')
        .outputFluids('gtceu:borane_thf 2000')
        .duration(200)
        .EUt(GTValues.V[GTValues.LV]);

    largeChemicalReactor(id('sodium_dichromate_solution_lcr'))
        .itemInputs('2x gtceu:chromium_trioxide_dust', '2x gtceu:sodium_hydroxide_dust')
        .inputFluids('minecraft:water 1000')
        .outputFluids('gtceu:sodium_dichromate_solution 2000')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('pyridinium_dichromate_synthesis'))
        .inputFluids('gtceu:sodium_dichromate_solution 1000', 'gtceu:pyridine 1000', 'gtceu:sulfuric_acid 1000')
        .outputFluids('gtceu:brine 2000')
        .itemOutputs('2x gtceu:pyridinium_dichromate_dust')
        .circuit(3)
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('sodium_aluminate_solution_lcr'))
        .inputFluids('minecraft:water 1000')
        .itemInputs('1x gtceu:alumina_dust', '2x gtceu:sodium_hydroxide_dust')
        .outputFluids('gtceu:sodium_aluminate_solution 1000')
        .circuit(7)
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('sodium_silicate_solution_lcr'))
        .inputFluids('minecraft:water 1000')
        .itemInputs('1x gtceu:silicon_dioxide_dust', '2x gtceu:sodium_hydroxide_dust')
        .outputFluids('gtceu:sodium_silicate_solution 1000')
        .circuit(7)
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    mixer(id('zeolite_a_gel_mixing'))
        .inputFluids('gtceu:sodium_silicate_solution 1000', 'gtceu:sodium_aluminate_solution 1000', 'minecraft:water 2000')
        .outputFluids('gtceu:zeolite_a_gel 2000')
        .circuit(7)
        .duration(200)
        .EUt(GTValues.V[GTValues.HV]);

    electricBlastFurnace(id('zeolite_a_gel_to_molecular_sieve_4a_in_ebf'))
        .inputFluids('gtceu:zeolite_a_gel 1000')
        .outputFluids('gtceu:steam 1000')
        .itemOutputs('1x gtceu:molecular_sieve_4a_dust')
        .circuit(7)
        .duration(4200)
        .EUt(GTValues.V[GTValues.EV]);

    largeChemicalReactor(id('p_toluenesulfonic_acid_synthesis'))
        .inputFluids('minecraft:water 1000', 'gtceu:sulfur_trioxide 1000', 'gtceu:toluene 1000')
        .itemOutputs('1x gtceu:p_toluenesulfonic_acid_dust')
        .circuit(2)
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('2ethyl2methyl13dioxolane_synthesis'))
        .inputFluids('gtceu:butanone 1000', 'gtceu:ethylene_glycol 1000')
        .chancedInput('1x gtceu:p_toluenesulfonic_acid_dust', 500, 0)
        .outputFluids('minecraft:water 1000', 'gtceu:2ethyl2methyl13dioxolane 1000')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    // fractionating chemical reactor for now LCR
    largeChemicalReactor(id('biomass_fractioning'))
        .inputFluids('gtceu:biomass 4000', 'gtceu:steam 4000')
        .outputFluids('gtceu:hemicellulose_slurry 2000')
        .itemOutputs('2x gtceu:cellulose_pulp_dust', '1x gtceu:lignin_residue_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    pyrolysisChamber(id('lignin_residue_pyrolysis'))
        .itemInputs('1x gtceu:lignin_residue_dust')
        .outputFluids('gtceu:phenol 300', 'gtceu:benzene 200', 'gtceu:toluene 200')
        .itemOutputs('1x gtceu:carbon_dust')
        .duration(3000)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('pentose_solution'))
        .inputFluids('gtceu:hemicellulose_slurry 1000', 'gtceu:diluted_sulfuric_acid 250')
        .outputFluids('gtceu:pentose_solution 750', 'gtceu:acidic_wastewater 1000')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('acidic_wastewater_processing'))
        .inputFluids('gtceu:acidic_wastewater 1000')
        .itemInputs('1x gtceu:calcium_hydroxide_dust')
        .outputFluids('minecraft:water 1000')
        .itemOutputs('1x gtceu:gypsum_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    distillationUnit(id('pentose_solution_distillation'))
        .inputFluids('gtceu:pentose_solution 1500')
        .outputFluids('minecraft:water 900', 'gtceu:furfural 600')
        .itemOutputs('1x gtceu:humins_dust')
        .duration(2400)
        .EUt(GTValues.V[GTValues.HV]);

    pyrolysisChamber(id('humins_pyrolysis'))
        .itemInputs('1x gtceu:humins_dust')
        .outputFluids('gtceu:furfural 200', 'gtceu:carbon_monoxide 500', 'gtceu:hydrogen 500')
        .itemOutputs('1x gtceu:carbon_dust')
        .duration(3000)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('t_buli_in_pentane_synthesis'))
        .inputFluids('gtceu:pentane 1000', 'gtceu:tertbutylchloride 1000')
        .itemInputs('1x gtceu:lithium_dust')
        .outputFluids('gtceu:t_buli_in_pentane 1000')
        .itemOutputs('1x gtceu:lithium_chloride_dust')
        .duration(2400)
        .EUt(GTValues.V[GTValues.EV]);

    mixer(id('5_percent_aqueous_acetone_mixer'))
        .inputFluids('gtceu:acetone 950', 'minecraft:water 50')
        .outputFluids('gtceu:5_aqueous_acetone 1000')
        .circuit(5)
        .duration(200)
        .EUt(GTValues.V[GTValues.LV]);

    largeChemicalReactor(id('1_4_benzoquinone_synthesis'))
        .chancedInput('1x gtceu:vanadium_pentoxide_dust', 500, 0)
        .inputFluids('gtceu:benzene 1000', 'gtceu:oxygen 2000')
        .outputFluids('minecraft:water 1000')
        .itemOutputs('1x gtceu:1_4_benzoquinone_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    extractor(id('1_4_benzoquinone_extract'))
        .itemInputs('1x gtceu:1_4_benzoquinone_dust')
        .outputFluids('gtceu:1_4_benzoquinone 1000')
        .duration(400)
        .EUt(GTValues.V[GTValues.MV]);

    largeChemicalReactor(id('ddq_synthesis'))
        .chancedInput('1x gtceu:copper_dust', 500, 0)
        .inputFluids('gtceu:1_4_benzoquinone 1000', 'gtceu:hydrogen_cyanide 2000', 'gtceu:chlorine 1000')
        .outputFluids('gtceu:hydrogen 1000')
        .itemOutputs('1x gtceu:ddq_dust')
        .duration(1800)
        .EUt(GTValues.V[GTValues.EV]);

    largeChemicalReactor(id('dcc_synthesis'))
        .inputFluids('gtceu:thionyl_chloride 500')
        .itemInputs('1x gtceu:dicyclohexylurea_dust')
        .outputFluids('gtceu:sulfur_dioxide 500', 'gtceu:hydrogen_chloride 500')
        .itemOutputs('1x gtceu:dcc_dust')
        .duration(1800)
        .EUt(GTValues.V[GTValues.EV]);
        
    largeChemicalReactor(id('triethylsilanol_synthesis'))
        .inputFluids('gtceu:ethyl_chloride 3000', 'minecraft:water 1000')
        .itemInputs('1x gtceu:silicon_dust')
        .outputFluids('gtceu:hydrogen_chloride 3000', 'gtceu:triethylsilanol 1000')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('tescl_synthesis'))
        .inputFluids('gtceu:triethylsilanol 1000', 'gtceu:thionyl_chloride 1000')
        .outputFluids('gtceu:tescl 1000', 'gtceu:sulfur_dioxide 1000', 'gtceu:hydrogen_chloride 1000')
        .duration(1800)
        .EUt(GTValues.V[GTValues.EV]);

    largeChemicalReactor(id('mchlorobenzoic_acid_synthesis'))
        .inputFluids('gtceu:chlorobenzene 1000', 'gtceu:oxygen 2000', 'minecraft:water 1000')
        .outputFluids('gtceu:mchlorobenzoic_acid 1000')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('mchloroperoxybenzoic_acid_synthesis'))
        .inputFluids('gtceu:mchlorobenzoic_acid 1000', 'gtceu:hydrogen_peroxide 1000')
        .outputFluids('gtceu:mchloroperoxybenzoic_acid 1000', 'minecraft:water 1000')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    mixer(id('cpba_in_toluene_mixer'))
        .inputFluids('gtceu:mchloroperoxybenzoic_acid 1000', 'gtceu:toluene 1000')
        .outputFluids('gtceu:cpba_toluene 1000')
        .duration(200)
        .EUt(GTValues.V[GTValues.MV]);

    mixer(id('sodium_bicarbonate_solution_mixer'))
        .inputFluids('minecraft:water 1000')
        .itemInputs('1x gtceu:sodium_bicarbonate_dust')
        .outputFluids('gtceu:sodium_bicarbonate_solution 1000')
        .duration(200)
        .EUt(GTValues.V[GTValues.LV]);

    largeChemicalReactor(id('washed_cpba_in_toluene'))
        .inputFluids('gtceu:cpba_toluene 1000', 'gtceu:sodium_bicarbonate_solution 250')
        .outputFluids('gtceu:washed_cpba_toluene 1000', 'minecraft:water 250', 'gtceu:carbon_dioxide 250')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('diad_synthesis'))
        .inputFluids('gtceu:isopropanol 2000', 'gtceu:carbon_dioxide 2000', 'gtceu:ammonia 1000', 'gtceu:oxygen 2000')
        .outputFluids('gtceu:diad 1000', 'minecraft:water 3000')
        .duration(1800)
        .EUt(GTValues.V[GTValues.EV]);

    largeChemicalReactor(id('thionyl_chloride_synthesis'))
        .inputFluids('gtceu:chlorine 1000', 'gtceu:sulfur_dioxide 1000')
        .outputFluids('gtceu:thionyl_chloride 1000')
        .chancedFluidOutput('gtceu:hydrogen_chloride 250', 1000, 0)
        .chancedFluidOutput('gtceu:sulfuryl_chloride 100', 500, 0)
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    // Byproducts processing
    largeChemicalReactor(id('sal_a_mother_liquor_processing'))
        .inputFluids('gtceu:sal_a_mother_liquor_e4_to_a5 15', 'minecraft:water 250')
        .itemInputs('1x gtceu:ammonium_chloride_dust')
        .outputFluids('gtceu:ammonia 1000', 'gtceu:salt_water 250')
        .itemOutputs('1x gcyr:potassium_chloride_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('pyridine_hydrochloride_processing'))
        .inputFluids('minecraft:water 1000')
        .itemInputs('1x gtceu:pyridine_hydrochloride_dust', '1x gtceu:sodium_hydroxide_dust')
        .outputFluids('gtceu:crude_pyridine_solution 2000')
        .itemOutputs('1x gtceu:sodium_chloride_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    distillationUnit(id('crude_pyridine_solution_distillation'))
        .inputFluids('gtceu:crude_pyridine_solution 1000')
        .outputFluids('gtceu:pyridine 400', 'gtceu:pyridine_water_azeotrope 200', 'gtceu:pyridine_organic_residue 50', 'gtceu:pyridine_salt_waste_slurry 350')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('pyridine_water_azeotrope_processing'))
        .inputFluids('gtceu:pyridine_water_azeotrope 400')
        .itemInputs('1x gtceu:quicklime_dust')
        .outputFluids('gtceu:pyridine 180', 'gtceu:pyridine_waste_water 250')
        .itemOutputs('1x gtceu:calcium_hydroxide_dust', '1x gtceu:tiny_salt_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    centrifuge(id('pyridine_organic_residue_processing'))
        .inputFluids('gtceu:pyridine_organic_residue 100')
        .outputFluids('gtceu:pyridine_waste_water 50')
        .itemOutputs('1x gtceu:carbon_dust', '1x gtceu:tiny_salt_dust')
        .duration(300)
        .EUt(GTValues.V[GTValues.MV]);

    // Settling Tank Recipe (LCR for now)
    largeChemicalReactor(id('settle_pyridine_salt_waste_slurry_and_pyridine_waste_water'))
        .inputFluids('gtceu:pyridine_salt_waste_slurry 700', 'gtceu:pyridine_waste_water 250')
        .outputFluids('gtceu:pyridine_waste_brine 600', 'gtceu:pyridine_waste_sludge 200', 'gtceu:pyridine_treated_water 150')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    electrolyzer(id('pyridine_waste_brine_processing'))
        .inputFluids('gtceu:pyridine_waste_brine 400')
        .outputFluids('gtceu:chlorine 300', 'gtceu:hydrogen 300')
        .itemOutputs('1x gtceu:sodium_hydroxide_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.MV]);

    thermalCentrifuge(id('pyridine_waste_sludge_processing'))
        .inputFluids('gtceu:pyridine_waste_sludge 200')
        .outputFluids('gtceu:pyridine_waste_water 100')
        .itemOutputs('1x gtceu:small_salt_dust', '1x gtceu:tiny_chromium_trioxide_dust')
        .duration(300)
        .EUt(GTValues.V[GTValues.MV]);

    distillationUnit(id('pyridine_treated_water_processing'))
        .inputFluids('gtceu:pyridine_treated_water 150', 'gtceu:pyridine_waste_water 100')
        .outputFluids('gtceu:distilled_water 200')
        .itemOutputs('1x gtceu:tiny_salt_dust')
        .duration(2400)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('sal_a_ester_7_to_spiro_ketone_carboxylate_intermediate'))
        .inputFluids('minecraft:water 1000')
        .itemInputs('1x gtceu:sal_a_ester_7_dust', '1x gtceu:sodium_hydroxide_dust')
        .outputFluids('gtceu:ethanol 1000')
        .itemOutputs('1x gtceu:sal_a_spiro_ketone_carboxylate_intermediate_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    pyrolysisChamber(id('sal_a_spiro_ketone_carboxylate_intermediate_to_enone_5'))
        .itemInputs('1x gtceu:sal_a_spiro_ketone_carboxylate_intermediate_dust')
        .outputFluids('gtceu:carbon_dioxide 1000')
        .itemOutputs('1x gtceu:sal_a_enone_5_dust')
        .duration(2400)
        .EUt(GTValues.V[GTValues.HV]);

    pyrolysisChamber(id('sal_a_organic_waste_e5_to_e6_and_7_processing'))
        .itemInputs('1x gtceu:sal_a_organic_waste_e5_to_e6_and_7_dust')
        .outputFluids('gtceu:hydrogen 250', 'gtceu:methane 250', 'gtceu:ammonia 100')
        .itemOutputs('1x gtceu:carbon_dust')
        .duration(3000)
        .EUt(GTValues.V[GTValues.HV]);    

    // Settling Tank Recipe (LCR for now)
    largeChemicalReactor(id('settle_sal_a_waste_water_e5_to_e6_and_7'))
        .inputFluids('gtceu:sal_a_waste_water_e5_to_e6_and_7 1000')
        .outputFluids('gtceu:sal_a_organic_residue_e5_to_e6_and_7 100', 'gtceu:sal_a_waste_sludge_e5_to_e6_and_7 200', 'gtceu:sal_a_treated_water_e5_to_e6_and_7 700')
        .duration(3000)
        .EUt(GTValues.V[GTValues.HV]);

    distillationUnit(id('sal_a_organic_residue_e5_to_e6_and_7_processing'))
        .inputFluids('gtceu:sal_a_organic_residue_e5_to_e6_and_7 1000')
        .outputFluids('gtceu:ethyl_acetate 400', 'gtceu:hexane 400', 'gtceu:thf 100', 'gtceu:sal_a_heavy_organic_residue_e5_to_e6_and_7 100')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    pyrolysisChamber(id('sal_a_heavy_organic_residue_e5_to_e6_and_7_processing'))
        .inputFluids('gtceu:sal_a_heavy_organic_residue_e5_to_e6_and_7 100')
        .outputFluids('gtceu:methane 50', 'gtceu:sal_a_waste_water_e5_to_e6_and_7 400')
        .itemOutputs('1x gtceu:tiny_carbon_dust')
        .duration(2400)
        .EUt(GTValues.V[GTValues.HV]);

    thermalCentrifuge(id('sal_a_waste_sludge_e5_to_e6_and_7_processing'))
        .inputFluids('gtceu:sal_a_waste_sludge_e5_to_e6_and_7 200')
        .itemOutputs('1x gtceu:tiny_sodium_sulfate_dust', '1x gtceu:tiny_lithium_chloride_dust', '1x gtceu:small_salt_dust')
        .duration(300)
        .EUt(GTValues.V[GTValues.MV]);

    pyrolysisChamber(id('sal_a_treated_water_e5_to_e6_and_7_processing'))
        .inputFluids('gtceu:sal_a_treated_water_e5_to_e6_and_7 700')
        .outputFluids('gtceu:distilled_water 650')
        .itemOutputs('1x gtceu:tiny_salt_dust')
        .duration(3000)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('sal_a_acidic_acqueous_layer_d6_to_d8_processing'))
        .inputFluids('gtceu:sal_a_acidic_acqueous_layer_d6_to_d8 1200', 'minecraft:water 300')
        .itemInputs('1x gtceu:sodium_bicarbonate_dust')
        .outputFluids('gtceu:carbon_dioxide 1000', 'gtceu:brine 1500')
        .chancedOutput('1x gtceu:sodium_chloride_dust', 2500, 0)
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('triphenylphosphine_oxide_to_triphenylphosphine'))
        .chancedFluidInput('gtceu:thionyl_chloride 250', 8000, 0)
        .inputFluids('gtceu:hydrogen 2000')
        .itemInputs('1x gtceu:triphenylphosphine_oxide_dust')
        .outputFluids('gtceu:sulfur_dioxide 250', 'gtceu:hydrogen_chloride 500')
        .itemOutputs('1x gtceu:triphenylphosphine_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('pyridinium_salt_waste_recycle'))
        .itemInputs('1x gtceu:pyridinium_salt_waste_dust', '2x gtceu:sodium_hydroxide_dust')
        .inputFluids('minecraft:water 1000')
        .outputFluids('gtceu:brine 1000', 'gtceu:pyridine 1000')
        .itemOutputs('1x gtceu:salt_dust')
        .chancedOutput('1x gtceu:tiny_chromium_oxide_dust', 1000, 0)
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    pyrolysisChamber(id('sulfuryl_chloride_pyrolysis'))
        .inputFluids('gtceu:sulfuryl_chloride 1000')
        .outputFluids('gtceu:sulfur_dioxide 1000', 'gtceu:chlorine 1000')
        .duration(3000)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('sal_a_wittig_organic_waste_d8_to_b9_processing'))
        .itemInputs('1x gtceu:sal_a_wittig_organic_waste_d8_to_b9_dust')
        .outputFluids('gtceu:hmds 100')
        .chancedOutput('1x gtceu:sodium_bromide_dust', 5000, 0)
        .chancedOutput('1x gtceu:triphenylphosphine_oxide_dust', 5000, 0)
        .itemOutputs('1x gtceu:carbon_dust')
        .duration(1200)
 
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('sal_a_crude_organic_residue_d8_to_b9_processing'))
        .itemInputs('1x gtceu:sodium_sulfate_dust')
        .chancedFluidInput('gtceu:ethyl_acetate 500', 1000, 0)
        .inputFluids('gtceu:sal_a_crude_organic_residue_d8_to_b9 200', 'gtceu:ammonium_chloride_solution 500', 'gtceu:brine 500')
        .outputFluids('gtceu:sal_a_contaminated_waste_water_d8_to_b9 1000')
        .itemOutputs('1x gtceu:sal_a_bisolefin_9_dust', '1x gtceu:sodium_sulfate_hydrated_dust', '1x gtceu:sal_a_mixed_salts_d8_to_b9_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);
    
    electricBlastFurnace(id('sodium_sulfate_dehydration'))
        .itemInputs('1x gtceu:sodium_sulfate_hydrated_dust')
        .outputFluids('gtceu:steam 1000')
        .itemOutputs('1x gtceu:sodium_sulfate_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    mixer(id('sal_a_mixed_salts_solution_d8_to_b9_mixer'))
        .inputFluids('minecraft:water 1000')
        .itemInputs('1x gtceu:sal_a_mixed_salts_d8_to_b9_dust')
        .outputFluids('gtceu:sal_a_mixed_salts_solution_d8_to_b9 1000')
        .duration(300)
        .EUt(GTValues.V[GTValues.LV]);

    distillationUnit(id('sal_a_contaminated_waste_water_d8_to_b9_distillation'))
        .inputFluids('gtceu:sal_a_contaminated_waste_water_d8_to_b9 1000')
        .outputFluids('minecraft:water 800', 'gtceu:ammonia 100')
        .itemOutputs('1x gtceu:ammonium_chloride_dust', '1x gtceu:sodium_chloride_dust', '1x gtceu:sal_a_mixed_salts_d8_to_b9_dust')
        .duration(2400)
        .EUt(GTValues.V[GTValues.HV]);

    electrolyzer(id('sal_a_mixed_salts_solution_d8_to_b9_electrolysis'))
        .inputFluids('gtceu:sal_a_mixed_salts_solution_d8_to_b9 1000')
        .outputFluids('minecraft:water 500', 'gtceu:hydrogen 500', 'gtceu:chlorine 500', 'gtceu:bromine 500')
        .itemOutputs('1x gtceu:lithium_dust', '1x gtceu:sodium_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    mixer(id('sodium_bromide_brine_mixer'))
        .inputFluids('minecraft:water 1000')
        .itemInputs('1x gtceu:sodium_bromide_dust')
        .outputFluids('gtceu:sodium_bromide_brine 1000')
        .duration(200)
        .EUt(GTValues.V[GTValues.LV]);

    electrolyzer(id('sodium_bromide_brine_electrolysis'))
        .inputFluids('gtceu:sodium_bromide_brine 1000')
        .outputFluids('gtceu:bromine 500', 'gtceu:hydrogen 500')
        .itemOutputs('1x gtceu:sodium_hydroxide_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    centrifuge(id('sal_a_silylation_organic_waste_b9_to_d10_centrifuge'))
        .itemInputs('1x gtceu:sal_a_silylation_organic_waste_b9_to_d10_dust')
        .outputFluids('gtceu:hydrogen 250', 'gtceu:methane 250')
        .itemOutputs('1x gtceu:carbon_dust')
        .duration(300)
        .EUt(GTValues.V[GTValues.MV]);

    centrifuge(id('sal_a_silylation_organic_waste_d10_to_10_2_centrifuge'))
        .itemInputs('1x gtceu:sal_a_silylation_organic_waste_d10_to_10_2_dust')
        .itemOutputs('1x gtceu:carbon_dust', '2x gtceu:tiny_silica_dust')
        .chancedOutput('1x gtceu:triethylammonium_chloride_dust', 2500, 0)
        .chancedFluidOutput('gtceu:tbscl 50', 1000, 0)
        .duration(300)
        .EUt(GTValues.V[GTValues.MV]);
        
    largeChemicalReactor(id('triethylammonium_chloride_to_triethylamine'))
        .notConsumableFluid('minecraft:water 1000')
        .itemInputs('1x gtceu:sodium_hydroxide_dust', '1x gtceu:triethylammonium_chloride_dust')
        .outputFluids('gtceu:triethylamine 1000')
        .itemOutputs('1x gtceu:sodium_chloride_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    mixer(id('sal_a_mixed_salts_solution_10_2_to_11_mixer'))
        .inputFluids('minecraft:water 1000')
        .itemInputs('1x gtceu:sal_a_mixed_salts_10_2_to_11_dust')
        .outputFluids('gtceu:sal_a_mixed_salts_solution_10_2_to_11 1000')
        .duration(200)
        .EUt(GTValues.V[GTValues.LV]);

    electrolyzer(id('sal_a_mixed_salts_solution_10_2_to_11_electrolysis'))
        .inputFluids('gtceu:sal_a_mixed_salts_solution_10_2_to_11 1000')
        .outputFluids('gtceu:chlorine 500', 'gtceu:hydrogen 500', 'gtceu:sodium_hydroxide 1000')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);

    pyrolysisChamber(id('sal_a_organic_waste_10_2_to_11_pyrolysis'))
        .itemInputs('1x gtceu:sal_a_organic_waste_10_2_to_11_dust')
        .outputFluids('gtceu:toluene 250', 'gtceu:methanol 250', 'gtceu:hydrogen 250')
        .itemOutputs('1x gtceu:tiny_carbon_dust')
        .duration(3000)
        .EUt(GTValues.V[GTValues.HV]);
        
    pyrolysisChamber(id('sal_a_hydroboration_organic_waste_11_to_11_2_pyrolysis'))
        .itemInputs('1x gtceu:sal_a_hydroboration_organic_waste_11_to_11_2_dust')
        .outputFluids('gtceu:hydrogen 250', 'gtceu:methane 250')
        .itemOutputs('1x gtceu:carbon_dust')
        .duration(3000)
        .EUt(GTValues.V[GTValues.HV]);

    largeChemicalReactor(id('sal_a_aldehyde_base_waste_11_3_to_12_processing'))
        .itemInputs('1x gtceu:sal_a_aldehyde_base_waste_11_3_to_12_dust')
        .inputFluids('gtceu:oxygen 1000')
        .outputFluids('gtceu:carbon_dioxide 1000', 'minecraft:water 500')
        .itemOutputs('1x gtceu:tiny_ash_dust')
        .duration(1200)
        .EUt(GTValues.V[GTValues.HV]);
    
    centrifuge(id('sal_a_pdc_13_2_to_14_centrifuge'))
        .itemInputs('1x gtceu:sal_a_pdc_13_2_to_14_dust')
        .outputFluids('gtceu:sal_a_triphenyl_like_aromatics 1000')
        .itemOutputs('1x gtceu:carbon_dust')
        .duration(300)
        .EUt(GTValues.V[GTValues.MV]);

    distillationUnit(id('sal_a_furyl_organic_waste_14_to_15_and_16_distillation'))
        .itemInputs('1x gtceu:sal_a_furyl_organic_waste_14_to_15_and_16_dust')
        .outputFluids('gtceu:3_bromofuran 250', 'gtceu:furan 250', 'gtceu:sal_a_heavy_organic_residue_14_to_15_and_16 500')
        .duration(2400)
        .EUt(GTValues.V[GTValues.HV]);

    pyrolysisChamber(id('sal_a_heavy_organic_residue_14_to_15_and_16_pyrolysis'))
        .inputFluids('gtceu:sal_a_heavy_organic_residue_14_to_15_and_16 500')
        .outputFluids('gtceu:carbon_monoxide 500', 'gtceu:hydrogen 500', 'gtceu:methane 250')
        .itemOutputs('1x gtceu:carbon_dust')
        .duration(3000)
        .EUt(GTValues.V[GTValues.HV]);

    distillationUnit(id('sal_a_anisole_waste_fluid_17_to_17_2_distillation'))
        .inputFluids('gtceu:sal_a_anisole_waste_fluid_17_to_17_2 500')
        .outputFluids('gtceu:anisole 400', 'gtceu:toluene 300', 'gtceu:sal_a_heavy_aromatic_residue_17_to_17_2 300')
        .duration(2400)
        .EUt(GTValues.V[GTValues.HV]);

    pyrolysisChamber(id('sal_a_heavy_aromatic_residue_17_to_17_2_pyrolysis'))
        .inputFluids('gtceu:sal_a_heavy_aromatic_residue_17_to_17_2 300')
        .outputFluids('gtceu:carbon_monoxide 300', 'gtceu:hydrogen 300')
        .itemOutputs('1x gtceu:carbon_dust')
        .duration(3000)
        .EUt(GTValues.V[GTValues.HV]);

    pyrolysisChamber(id('sal_a_hydrazinedicarboxylate_waste_pyrolysis'))
        .itemInputs('1x gtceu:sal_a_hydrazinedicarboxylate_waste_dust')
        .outputFluids('gtceu:carbon_dioxide 1000', 'gtceu:nitrogen 500', 'gtceu:ammonia 500', 'gtceu:methanol 250')
        .itemOutputs('1x gtceu:tiny_carbon_dust')
        .duration(3000)
        .EUt(GTValues.V[GTValues.HV]);

    // Large chemical Plant Recipes (LCR for now)
    chemicalPlant(id('sal_a_enone_4_to_alcohol_5'))
        .chancedFluidInput('gtceu:methanol 1000', 1000, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .itemInputs('1x gtceu:potassium_hydroxide_dust', '1x gtceu:sal_a_enone_4_dust')
        .outputFluids('gtceu:sal_a_mother_liquor_e4_to_a5 15')
        .chancedOutput('1x gtceu:tiny_potassium_carbonate_dust', 1000, 0)
        .chancedOutput('1x gtceu:small_dark_ash_dust', 500, 0)
        .itemOutputs('1x gtceu:sal_a_alcohol_5_dust')
        .duration(2400)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_alcohol_5_to_enone_5'))
        .chancedFluidInput('gtceu:dichloromethane 1000', 2000, 0)
        .itemInputs('1x gtceu:pyridinium_chlorochromate_dust', '1x gtceu:sal_a_alcohol_5_dust')
        .itemOutputs('1x gtceu:sal_a_enone_5_dust', '1x gtceu:chromium_trioxide_dust', '1x gtceu:pyridine_hydrochloride_dust')
        .duration(2400)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_enone_5_to_dioxolane_6'))
        .notConsumableFluid('gtceu:nitrogen 250')
        .itemInputs('3x gtceu:lithium_dust', '1x gtceu:sal_a_enone_5_dust')
        .inputFluids('gtceu:ethyl_iodoacetate 1000', 'gtceu:ammonia 1000', 'gtceu:thf 500')
        .outputFluids('gtceu:ammonia 1000', 'gtceu:sal_a_waste_water_e5_to_e6_and_7 1500')
        .chancedOutput('1x gtceu:sal_a_dioxolane_6_dust', 5100, 0)
        .chancedOutput('1x gtceu:sal_a_ester_7_dust', 2100, 0)
        .chancedOutput('1x gtceu:sal_a_organic_waste_e5_to_e6_and_7_dust', 2800, 0)
        .itemOutputs('1x gtceu:lithium_iodide_dust', '1x gtceu:lithium_chloride_dust')
        .duration(2400)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_dioxolane_6_to_diketone_8'))
        .chancedFluidInput('gtceu:ethanol 1000', 2000, 0)
        .inputFluids('gtceu:hydrochloric_acid 1000', 'minecraft:water 1000')
        .itemInputs('1x gtceu:sal_a_dioxolane_6_dust')
        .outputFluids('gtceu:ethylene_glycol 1000', 'gtceu:sal_a_acidic_acqueous_layer_d6_to_d8 1200')
        .itemOutputs('1x gtceu:sal_a_diketone_8_dust')
        .duration(2400)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_diketone_8_to_bisolefin_9'))
        .chancedFluidInput('gtceu:thf 1000', 2000, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .itemInputs('6x gtceu:methyl_triphenyl_phosphonium_bromide_dust', '1x gtceu:sal_a_diketone_8_dust')
        .inputFluids('gtceu:nahmdsinthf 5000')
        .outputFluids('gtceu:sal_a_crude_organic_residue_d8_to_b9 200')
        .chancedOutput('1x gtceu:sal_a_bisolefin_9_dust', 8500, 0)
        .chancedOutput('1x gtceu:sal_a_wittig_organic_waste_d8_to_b9_dust', 1500, 0)
        .itemOutputs('2x gtceu:triphenylphosphine_oxide_dust', '6x gtceu:sodium_bromide_dust')
        .duration(2400)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_bisolefin_9_to_reduced_intermediate_slurry_b9_to_d10'))
        .chancedFluidInput('gtceu:diethyl_ether 1000', 1500, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .itemInputs('1x gtceu:sal_a_bisolefin_9_dust', '1x gtceu:lithium_aluminium_hydride_dust')
        .outputFluids('gtceu:hydrogen 500')
        .itemOutputs('1x gtceu:sal_a_reduced_intermediate_slurry_b9_to_d10_dust')
        .duration(2400)
        .EUt(GTValues.V[GTValues.EV]);

    centrifuge(id('sal_a_11_2_to_11_3_chromium_oxidation_organic_waste_to_triphenyl_like_aromatics_centrifuge'))
        .itemInputs('1x gtceu:sal_a_chromium_oxidation_organic_waste_11_2_to_11_3_dust')
        .outputFluids('gtceu:sal_a_triphenyl_like_aromatics 1000')
        .itemOutputs('1x gtceu:carbon_dust')
        .duration(300)
        .EUt(GTValues.V[GTValues.MV]);

    distillationUnit(id('sal_a_triphenyl_like_aromatics_distillation'))
        .inputFluids('gtceu:sal_a_triphenyl_like_aromatics 1000')
        .outputFluids('gtceu:benzene 300', 'gtceu:toluene 300', 'gtceu:chlorobenzene 100', 'gtceu:sal_a_heavy_aromatic_residue 300')
        .duration(2400)
        .EUt(GTValues.V[GTValues.HV]);

    pyrolysisChamber(id('sal_a_heavy_aromatic_residue_pyrolysis'))
        .inputFluids('gtceu:sal_a_heavy_aromatic_residue 300')
        .outputFluids('gtceu:methane 300', 'gtceu:hydrogen 200', 'gtceu:carbon_monoxide 200')
        .itemOutputs('1x gtceu:carbon_dust')
        .duration(3000)
        .EUt(GTValues.V[GTValues.HV]);


    chemicalPlant(id('sal_a_reduced_intermediate_slurry_b9_to_d10_reduction'))
        .chancedFluidInput('gtceu:diethyl_ether 300', 2000, 0)
        .inputFluids('minecraft:water 500', 'gtceu:ammonium_chloride_solution 1000')
        .itemInputs('1x gtceu:sal_a_reduced_intermediate_slurry_b9_to_d10_dust', '1x gtceu:sodium_sulfate_dust')
        .outputFluids('gtceu:hydrogen 500', 'gtceu:ammonia 500')
        .itemOutputs('1x gtceu:sodium_sulfate_hydrated_dust', '2x gtceu:aluminium_hydroxide_dust', '1x gtceu:lithium_chloride_dust', '1x gtceu:ammonium_chloride_dust')
        .chancedOutput('1x gtceu:sal_a_diol_10_dust', 5700, 0)
        .chancedOutput('1x gtceu:sal_a_silylation_organic_waste_b9_to_d10_dust', 4300, 0)
        .duration(2400)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_diol_10_to_tbs_10_2'))
        .chancedFluidInput('gtceu:dichloromethane 1000', 1000, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .chancedInput('1x gtceu:dmap_dust', 500, 0)
        .chancedFluidInput('gtceu:tbscl 2000', 1250, 0)
        .inputFluids('gtceu:triethylamine 1200')
        .itemInputs('1x gtceu:sal_a_diol_10_dust')
        .itemOutputs('1x gtceu:triethylammonium_chloride_dust')
        .chancedOutput('1x gtceu:sal_a_tbs_10_2_dust', 9900, 0)
        .chancedOutput('1x gtceu:sal_a_silylation_organic_waste_d10_to_10_2_dust', 100, 0)
        .duration(1800)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_tbs_10_2_to_alcohol_10_2'))
        .chancedFluidInput('gtceu:thf 500', 2000, 0)
        .itemInputs('1x gtceu:sal_a_tbs_10_2_dust', '1x gtceu:tbaf_dust')
        .itemOutputs('1x gtceu:tbsf_dust', '1x gtceu:sal_a_alcohol_10_2_dust')
        .duration(1800)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_alcohol_10_2_to_exomethylene_11'))
        .chancedFluidInput('gtceu:dimethylformamide 1000', 1500, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .inputFluids('gtceu:4_methoxybenzyl_chloride 1200')
        .itemInputs('1x gtceu:sal_a_alcohol_10_2_dust', '3x gtceu:sodium_hydride_dust')
        .itemOutputs('1x gtceu:sodium_chloride_dust')
        .chancedOutput('1x gtceu:sal_a_exomethylene_11_dust', 9400, 0)
        .chancedOutput('1x gtceu:sal_a_organic_waste_10_2_to_11_dust', 600, 0)
        .outputFluids('gtceu:sal_a_crude_organics_10_2_to_11 150', 'gtceu:hydrogen 3000')
        .duration(2400)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_crude_organics_10_2_to_11_to_mixed_salts'))
        .chancedFluidInput('gtceu:ethyl_acetate 500', 1000, 0)
        .inputFluids('gtceu:ammonium_chloride_solution 1000', 'gtceu:brine 500', 'gtceu:sal_a_crude_organics_10_2_to_11 150')
        .itemInputs('1x gtceu:sodium_sulfate_dust')
        .itemOutputs('1x gtceu:sodium_sulfate_hydrated_dust', '1x gtceu:ammonium_chloride_dust', '1x gtceu:sodium_chloride_dust', '1x gtceu:sal_a_mixed_salts_10_2_to_11_dust')
        .outputFluids('minecraft:water 900')
        .duration(1800)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_exomethylene_11_to_organoborane_intermediate_11_to_11_2'))
        .chancedFluidInput('gtceu:thf 1000', 1000, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .inputFluids('gtceu:borane_thf 5000')
        .itemInputs('1x gtceu:sal_a_exomethylene_11_dust')
        .itemOutputs('1x gtceu:sal_a_organoborane_intermediate_11_to_11_2_dust')
        .duration(3000)
        .EUt(GTValues.V[GTValues.IV]);

    chemicalPlant(id('sal_a_11_to_11_2_organoborane_intermediate_to_alcohol_11_2'))
        .inputFluids('gtceu:sodium_hydroxide_solution 40000', 'gtceu:hydrogen_peroxide 40000', 'gtceu:hydrochloric_acid 1000', 'minecraft:water 2000')
        .itemInputs('1x gtceu:sal_a_organoborane_intermediate_11_to_11_2_dust')
        .itemOutputs('1x gtceu:sodium_chloride_dust', 'gtceu:sodium_borate_dust')
        .outputFluids('gtceu:oxygen 1000', 'minecraft:water 3000')
        .chancedOutput('1x gtceu:sal_a_alcohol_11_2_dust', 9500, 0)
        .chancedOutput('1x gtceu:sal_a_hydroboration_organic_waste_11_to_11_2_dust', 500, 0)
        .duration(3000)
        .EUt(GTValues.V[GTValues.IV]);

    chemicalPlant(id('sal_a_alcohol_11_2_to_bisaldehyde_11_3'))
        .chancedInput('1x gtceu:molecular_sieve_4a_dust', 1000, 0)
        .chancedInput('6x gtceu:sodium_acetate_dust', 9250, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .inputFluids('gtceu:dichloromethane 1000')
        .itemInputs('1x gtceu:sal_a_alcohol_11_2_dust', '6x gtceu:pyridinium_dichromate_dust')
        .itemOutputs('1x gtceu:chromium_oxide_dust', '2x gtceu:pyridinium_salt_waste_dust')
        .chancedOutput('1x gtceu:sal_a_bisaldehyde_11_3_dust', 8700, 0)
        .chancedOutput('1x gtceu:sal_a_chromium_oxidation_organic_waste_11_2_to_11_3_dust', 1300, 0)
        .duration(2400)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_bisaldehyde_11_3_to_bisaldehyde_12'))
        .chancedFluidInput('gtceu:methanol 1000', 8500, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .inputFluids('gtceu:sodium_methoxide 3000')
        .itemInputs('1x gtceu:sal_a_bisaldehyde_11_3_dust')
        .itemOutputs('1x gtceu:chromium_oxide_dust')
        .chancedOutput('1x gtceu:sal_a_bisaldehyde_12_dust', 7300, 0)
        .chancedOutput('1x gtceu:sal_a_aldehyde_base_waste_11_3_to_12_dust', 2700, 0)
        .duration(2400)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_bisaldehyde_12_to_ketal_13'))
        .chancedInput('1x gtceu:p_toluenesulfonic_acid_dust', 500, 0)
        .chancedFluidInput('gtceu:2ethyl2methyl13dioxolane 1000', 1000, 0)
        .chancedFluidInput('gtceu:ethylene_glycol 2000', 4000, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .itemInputs('1x gtceu:sal_a_bisaldehyde_12_dust')
        .itemOutputs('1x gtceu:sal_a_ketal_13_dust')
        .outputFluids('minecraft:water 2000')
        .duration(2400)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_ketal_13_to_alcohol_13_2'))
        .chancedFluidInput('gtceu:thf 1000', 1000, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .itemInputs('1x gtceu:sal_a_ketal_13_dust', '2x gtceu:tbaf_dust')
        .itemOutputs('1x gtceu:sal_a_alcohol_13_2_dust', '1x gtceu:tbsf_dust', '1x gtceu:tbacl_dust')
        .outputFluids('minecraft:water 2000')
        .duration(2400)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_alcohol_13_2_to_aldehyde_14'))
        .chancedInput('1x gtceu:molecular_sieve_4a_dust', 1000, 0)
        .chancedFluidInput('gtceu:dichloromethane 1000', 1000, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .itemInputs('1x gtceu:sal_a_alcohol_13_2_dust', '3x gtceu:pyridinium_dichromate_dust', '3x gtceu:sodium_acetate_dust')
        .itemOutputs('1x gtceu:chromium_oxide_dust', '1x gtceu:pyridinium_salt_waste_dust')
        .chancedOutput('1x gtceu:sal_a_aldehyde_14_dust', 7800, 0)
        .chancedOutput('1x gtceu:sal_a_pdc_13_2_to_14_dust', 2200, 0)
        .duration(2400)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_aldehyde_14_to_furyl_alcohol_15_and_16'))
        .chancedFluidInput('gtceu:thf 1500', 2000, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .itemInputs('1x gtceu:sal_a_aldehyde_14_dust')
        .inputFluids('gtceu:3_bromofuran 4000', 'gtceu:t_buli_in_pentane 5600')
        .itemOutputs('1x gtceu:lithium_bromide_dust')
        .chancedOutput('1x gtceu:sal_a_furyl_alcohol_15_dust', 4500, 0)
        .chancedOutput('1x gtceu:sal_a_furyl_alcohol_16_dust', 3500, 0)
        .chancedOutput('1x gtceu:sal_a_furyl_organic_waste_14_to_15_and_16_dust', 2000, 0)
        .outputFluids('gtceu:isobutane 4000', 'gtceu:pentane 5200')
        .duration(3600)
        .EUt(GTValues.V[GTValues.IV]);

    chemicalPlant(id('sal_a_furyl_alcohol_16_recycling'))
        .chancedFluidInput('gtceu:dichloromethane 1000', 1500, 0)
        .chancedInput('1x gtceu:molecular_sieve_4a_dust', 1000, 0)
        .itemInputs('1x gtceu:sal_a_furyl_alcohol_16_dust', '3x gtceu:pyridinium_dichromate_dust', '3x gtceu:sodium_acetate_dust')
        .itemOutputs('1x gtceu:sal_a_aldehyde_14_dust', '1x gtceu:chromium_oxide_dust', '1x gtceu:pyridinium_salt_waste_dust')
        .duration(3600)
        .EUt(GTValues.V[GTValues.IV]);

    chemicalPlant(id('sal_a_furyl_alcohol_15_to_lactol_17'))
        .chancedInput('1x gtceu:p_toluenesulfonic_acid_dust', 500, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .itemInputs('1x gtceu:sal_a_furyl_alcohol_15_dust')
        .inputFluids('gtceu:5_aqueous_acetone 1000')
        .itemOutputs('1x gtceu:sal_a_lactol_17_dust')
        .outputFluids('gtceu:acetone 900', 'minecraft:water 100')
        .duration(3600)
        .EUt(GTValues.V[GTValues.IV]);

    chemicalPlant(id('sal_a_lactol_17_to_alcohol_17_2'))
        .chancedFluidInput('gtceu:dichloromethane 1000', 1500, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .itemInputs('1x gtceu:sal_a_lactol_17_dust', '2x gtceu:ddq_dust')
        .inputFluids('minecraft:water 200')
        .itemOutputs('1x gtceu:sal_a_alcohol_17_2_dust', '2x gtceu:ddq_reduced_dust')
        .outputFluids('gtceu:sal_a_anisole_waste_fluid_17_to_17_2 500')
        .duration(2400)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_alcohol_17_2_to_carboxylic_acid_17_3'))
        .chancedFluidInput('gtceu:dimethylformamide 1000', 1500, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .itemInputs('1x gtceu:sal_a_alcohol_17_2_dust', '3x gtceu:sodium_acetate_dust', '8x gtceu:pyridinium_dichromate_dust')
        .inputFluids('gtceu:2methyl2butene 1000')
        .itemOutputs('1x gtceu:sal_a_carboxylic_acid_17_3_dust', '2x gtceu:chromium_oxide_dust', '2x gtceu:pyridinium_salt_waste_dust')
        .outputFluids('gtceu:butanone 500')
        .duration(3600)
        .EUt(GTValues.V[GTValues.IV]);

    chemicalPlant(id('sal_a_carboxylic_acid_17_3_to_2_deacetoxysalvinorin_a'))
        .chancedFluidInput('gtceu:dichloromethane 1000', 1500, 0)
        .chancedInput('1x gtceu:dmap_dust', 500, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .itemInputs('1x gtceu:sal_a_carboxylic_acid_17_3_dust', '3x gtceu:dcc_dust')
        .inputFluids('gtceu:methanol 1000')
        .itemOutputs('1x gtceu:2_deacetoxysalvinorin_a_dust', '3x gtceu:dicyclohexylurea_dust')
        .outputFluids('gtceu:butanone 500')
        .duration(3600)
        .EUt(GTValues.V[GTValues.IV]);

    chemicalPlant(id('sal_a_2_deacetoxysalvinorin_a_to_tes_19'))
        .chancedFluidInput('gtceu:thf 1000', 1500, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .itemInputs('1x gtceu:2_deacetoxysalvinorin_a_dust')
        .inputFluids('gtceu:nahmdsinthf 3000', 'gtceu:tescl 6000')
        .itemOutputs('1x gtceu:sal_a_tes_19_dust', '3x gtceu:sodium_chloride_dust')
        .outputFluids('gtceu:hmds 1000')
        .duration(1800)
        .EUt(GTValues.V[GTValues.EV]);

    chemicalPlant(id('sal_a_tes_19_to_2_epi_salvinorin_b'))
        .chancedFluidInput('gtceu:thf 1000', 1500, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .itemInputs('1x gtceu:sal_a_tes_19_dust')
        .inputFluids('gtceu:washed_cpba_toluene 1000', 'minecraft:water 100', 'gtceu:toluene 500')
        .itemOutputs('1x gtceu:2_epi_salvinorin_b_dust')
        .outputFluids('gtceu:toluene 850', 'minecraft:water 150', 'gtceu:mchlorobenzoic_acid 1000')
        .duration(3600)
        .EUt(GTValues.V[GTValues.LuV]);

    chemicalPlant(id('2_epi_salvinorin_b_to_salvinorin_a'))
        .chancedFluidInput('gtceu:dichloromethane 1000', 1500, 0)
        .notConsumableFluid('gtceu:nitrogen 250')
        .itemInputs('1x gtceu:2_epi_salvinorin_b_dust', '10x gtceu:triphenylphosphine_dust')
        .inputFluids('gtceu:diad 10000', 'gtceu:acetic_acid 30000')
        .itemOutputs('1x gtceu:salvinorin_a_dust', '10x gtceu:triphenylphosphine_oxide_dust', '10x gtceu:sal_a_hydrazinedicarboxylate_waste_dust')
        .duration(4800)
        .EUt(GTValues.V[GTValues.UV]);

    // antidote

    assembler(id('empty_glass_ampoule'))
        .itemInputs('2x minecraft:glass', '1x gtceu:steel_tiny_fluid_pipe')
        .itemOutputs('4x kubejs:empty_glass_ampoule')
        .circuit(7)
        .duration(200)
        .EUt(GTValues.V[GTValues.MV]);

    chemicalReactor(id('stabilized_carrier_solution'))
        .inputFluids('minecraft:water 1000', 'gtceu:ethanol 500', 'gtceu:glycerol 250')
        .outputFluids('gtceu:stabilized_carrier_solution 1000')
        .duration(400)
        .EUt(GTValues.V[GTValues.HV]);

    fluidHeater(id('stabilized_carrier_solution_warmed'))
        .inputFluids('gtceu:stabilized_carrier_solution 1000')
        .outputFluids('gtceu:stabilized_carrier_solution_warmed 1000')
        .duration(200)
        .EUt(GTValues.V[GTValues.HV]);

    chemicalReactor(id('kappa_reset_concentrate'))
        .itemInputs('1x minecraft:glowstone_dust', '1x gtceu:activated_carbon_dust', '1x gtceu:sodium_bicarbonate_dust')
        .inputFluids('gtceu:stabilized_carrier_solution_warmed 1000')
        .outputFluids('gtceu:kappa_reset_concentrate 1000')
        .duration(600)
        .EUt(GTValues.V[GTValues.EV]);

    canner(id('kappa_reset_ampoule'))
        .itemInputs('1x kubejs:empty_glass_ampoule')
        .inputFluids('gtceu:kappa_reset_concentrate 250')
        .itemOutputs('1x kubejs:kappa_reset_ampoule')
        .duration(300)
        .EUt(GTValues.V[GTValues.EV]);
});    




