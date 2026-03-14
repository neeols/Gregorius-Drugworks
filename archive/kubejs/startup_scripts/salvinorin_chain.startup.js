//priority: 1000000

const $DustProperty = Java.loadClass('com.gregtechceu.gtceu.api.data.chemical.material.properties.DustProperty');
const $FluidProperty = Java.loadClass('com.gregtechceu.gtceu.api.data.chemical.material.properties.FluidProperty');
const $FluidBuilder = Java.loadClass('com.gregtechceu.gtceu.api.fluids.FluidBuilder');
const $FluidStorageKeys = Java.loadClass('com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys');

let addFluid = (mat, key) => {
    let prop = new $FluidProperty();
    prop.getStorage().enqueueRegistration(key, new $FluidBuilder());
    mat.setProperty(PropertyKey.FLUID, prop);
}

GTCEuStartupEvents.registry('gtceu:material', event => {

  GTMaterials.Chlorine.setProperty(PropertyKey.DUST, new $DustProperty());

  addFluid(GTMaterials.SodiumHydroxide, $FluidStorageKeys.LIQUID);


  // === Gases ===

  // Hydrogen Bromide (HBr)
  event.create('hydrogen_bromide')
    .gas()
    .components('1x hydrogen', '1x bromine')
    .color(0xDDE6FF);

  // Hydrogen Chloride (HCl)
  event.create('hydrogen_chloride')
    .dust()
    .gas()
    .components('1x hydrogen', '1x chlorine')
    .color(0xE6FFF4);

  // Hydrogen Fluoride (HF)
  event.create('hydrogen_fluoride')
    .gas()
    .components('1x hydrogen', '1x fluorine')
    .color(0xF2FFF0);

  // Ethylene Oxide (C2H4O1)
  event.create('ethylene_oxide')
    .gas()
    .components('2x carbon', '4x hydrogen', '1x oxygen')
    .color(0xEAF7FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Acetylene
  event.create('acetylene')
    .gas()
    .components('2x carbon', '2x hydrogen')
    .color(0xA3A683)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // chloroethane (C2H5Cl)
  event.create('chloroethane')
    .gas()
    .components('2x carbon', '5x hydrogen', '1x chlorine')
    .color(0xEAF6FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // === Liquids ===

  // 1,4-butanediol (C4H10O2)
  event.create('1_4_butanediol')
    .liquid()
    .components('4x carbon', '10x hydrogen', '2x oxygen')
    .color(0xE9F2FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);


  // 1-hexene (C6H12)
  event.create('1_hexene')
    .liquid()
    .components('6x carbon', '12x hydrogen')
    .color(0xE9F2FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // 2-ethyl-2-methyl-1,3-dioxolane (C6H12O2)
  event.create('2ethyl2methyl13dioxolane')
    .liquid()
    .components('6x carbon', '12x hydrogen', '2x oxygen')
    .color(0xF1FAFF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // p-Toluenesulfonic acid
  event.create('p_toluenesulfonic_acid')
    .dust()
    .components('1x carbon', '3x hydrogen', '6x carbon', '4x hydrogen', '1x sulfur', '3x oxygen', '1x hydrogen')
    .color(0x8A0000)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // 2-methyl-2-butene (C5H10)
  event.create('2methyl2butene')
    .liquid()
    .components('5x carbon', '10x hydrogen')
    .color(0xF7F7FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // 3-bromofuran (C4H3BrO)
  event.create('3_bromofuran')
    .liquid()
    .components('4x carbon', '3x hydrogen', '1x bromine', '1x oxygen')
    .color(0xFFE8C7)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // 4-methoxybenzyl chloride (C8H9ClO) [anisyl chloride]
  event.create('4_methoxybenzyl_chloride')
    .liquid()
    .components('8x carbon', '9x hydrogen', '1x chlorine', '1x oxygen')
    .color(0xFFF1D6)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // 5% Aqueous Acetone (MIXTURE)
  event.create('5_aqueous_acetone')
    .liquid()
    .color(0xEAF7FF);

  // Acetaldehyde (C2H4O)
  event.create('acetaldehyde')
    .liquid()
    .components('2x carbon', '4x hydrogen', '1x oxygen')
    .color(0xEFFFF9)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Acidic Wastewater (MIXTURE)
  event.create('acidic_wastewater')
    .liquid()
    .color(0xD9F0FF);

  // Aminopyridine (C5H6N2)
  event.create('aminopyridine')
    .liquid()
    .components('5x carbon', '6x hydrogen', '2x nitrogen')
    .color(0xFFF6DA)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Ammonium Chloride Solution (MIXTURE; NH4Cl(aq))
  event.create('ammonium_chloride_solution')
    .liquid()
    .color(0xEAF3FF);

  // Anisole (C7H8O)
  event.create('anisole')
    .liquid()
    .components('7x carbon', '8x hydrogen', '1x oxygen')
    .color(0xFFF2D0)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Borane (BH3)
  event.create('borane')
    .liquid()
    .components('1x boron', '3x hydrogen')
    .color(0xF6FFF2)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Borane THF 
  event.create('borane_thf')
    .liquid()
    .color(0xFFFFC5);

  // Boric Acid (H3BO3)
  event.create('boric_acid')
    .liquid()
    .components('3x hydrogen', '1x boron', '3x oxygen')
    .color(0xF2FFF7)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Brine
  event.create('brine')
    .liquid()
    .color(0xCFE7FF);

  // Butanol (C4H10O)
  event.create('butanol')
    .liquid()
    .components('4x carbon', '10x hydrogen', '1x oxygen')
    .color(0xF6FFF9)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Butanone / MEK (C4H8O)
  event.create('butanone')
    .liquid()
    .components('4x carbon', '8x hydrogen', '1x oxygen')
    .color(0xF4FBFF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Butyl Chloride (C4H9Cl)
  event.create('butyl_chloride')
    .liquid()
    .components('4x carbon', '9x hydrogen', '1x chlorine')
    .color(0xFFF0E6)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Chloroacetic Acid (C2H3ClO2)
  event.create('chloroacetic_acid')
    .liquid()
    .components('2x carbon', '3x hydrogen', '1x chlorine', '2x oxygen')
    .color(0xE5FFF2)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Chloromethylsilane
  event.create('chloromethylsilane')
    .liquid()
    .color(0xE9E9FF);

  // Chlorotrimethylsilane (C3H9ClSi)
  event.create('chlorotrimethylsilane')
    .liquid()
    .components('3x carbon', '9x hydrogen', '1x chlorine', '1x silicon')
    .color(0xE8F0FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // mCPBA in toluene (MIXTURE)
  event.create('cpba_toluene')
    .liquid()
    .color(0xFFF2D9);

  // washed mCPBA in toluene (MIXTURE)
  event.create('washed_cpba_toluene')
    .liquid()
    .color(0x8A8274);

  // Crude Pyridine (MIXTURE)
  event.create('crude_pyridine')
    .liquid()
    .color(0xFFF0C2);

  // Crude Pyridine Solution (MIXTURE)
  event.create('crude_pyridine_solution')
    .liquid()
    .color(0xF5F1D8);

  // Cyclohexylamine (C6H13N)
  event.create('cyclohexylamine')
    .liquid()
    .components('6x carbon', '13x hydrogen', '1x nitrogen')
    .color(0xFFF3D4)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // DIAD (C8H16N2O4)
  event.create('diad')
    .liquid()
    .components('8x carbon', '16x hydrogen', '2x nitrogen', '4x oxygen')
    .color(0xFFE9CE)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Dichloromethane (CH2Cl2)
  event.create('dichloromethane')
    .liquid()
    .components('1x carbon', '2x hydrogen', '2x chlorine')
    .color(0xE7F4FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Diethyl Ether (C4H10O)
  event.create('diethyl_ether')
    .liquid()
    .components('4x carbon', '10x hydrogen', '1x oxygen')
    .color(0xF0FBFF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Ethyl Acetate (C4H8O2)
  event.create('ethyl_acetate')
    .liquid()
    .components('4x carbon', '8x hydrogen', '2x oxygen')
    .color(0xF4FFF7)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Dimethylformamide (H7NO)
  event.create('dimethylformamide')
    .liquid()
    .components('3x carbon', '7x hydrogen', '1x nitrogen', '1x oxygen')
    .color(0xD6CC81)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Lithium Bromide (LiBr)
  event.create('lithium_bromide')
    .dust()
    .components('1x lithium', '1x bromine')
    .color(0xD6CC81);

  // Ethyl Chloride (C2H5Cl)
  event.create('ethyl_chloride')
    .liquid()
    .components('2x carbon', '5x hydrogen', '1x chlorine')
    .color(0xEDF6FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Ethyl Iodoacetate (C4H7IO2)
  event.create('ethyl_iodoacetate')
    .liquid()
    .components('4x carbon', '7x hydrogen', '1x iodine', '2x oxygen')
    .color(0xFFE7D0)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Ethylene Glycol (C2H6O2)
  event.create('ethylene_glycol')
    .liquid()
    .components('2x carbon', '6x hydrogen', '2x oxygen')
    .color(0xEAF7FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Furan (C4H4O)
  event.create('furan')
    .liquid()
    .components('4x carbon', '4x hydrogen', '1x oxygen')
    .color(0xFFF0CC)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Furfural (C5H4O2)
  event.create('furfural')
    .liquid()
    .components('5x carbon', '4x hydrogen', '2x oxygen')
    .color(0xD18A3A)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Hemicellulose Slurry (MIXTURE)
  event.create('hemicellulose_slurry')
    .liquid()
    .color(0xC8A66B);

  // Hexane (C6H14)
  event.create('hexane')
    .liquid()
    .components('6x carbon', '14x hydrogen')
    .color(0xF2F7FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // HMDS (C6H19NSi2)
  event.create('hmds')
    .liquid()
    .components('6x carbon', '19x hydrogen', '1x nitrogen', '2x silicon')
    .color(0xE8F0FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Iodoacetic Acid (C2H3IO2)
  event.create('iodoacetic_acid')
    .liquid()
    .components('2x carbon', '3x hydrogen', '1x iodine', '2x oxygen')
    .color(0xFFEAD6)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Isobutane (C4H10)
  event.create('isobutane')
    .liquid()
    .components('4x carbon', '10x hydrogen')
    .color(0xF6F7FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Isobutanol (C4H10O)
  event.create('isobutanol')
    .liquid()
    .components('4x carbon', '10x hydrogen', '1x oxygen')
    .color(0xF4FFFA)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Isobutylene (C4H8)
  event.create('isobutylene')
    .liquid()
    .components('4x carbon', '8x hydrogen')
    .color(0xF3F6FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Isopropanol (C3H8O)
  event.create('isopropanol')
    .liquid()
    .components('3x carbon', '8x hydrogen', '1x oxygen')
    .color(0xEAF8FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // m-Chlorobenzoic acid (C7H5ClO2)
  event.create('mchlorobenzoic_acid')
    .liquid()
    .components('7x carbon', '5x hydrogen', '1x chlorine', '2x oxygen')
    .color(0xE9FFD8)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // m-Chloroperoxybenzoic acid (C7H5ClO3)
  event.create('mchloroperoxybenzoic_acid')
    .liquid()
    .components('7x carbon', '5x hydrogen', '1x chlorine', '3x oxygen')
    .color(0xB5B5B5)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // p-Methoxybenzyl alcohol / Anisyl alcohol (C8H10O2)
  event.create('methoxybenzyl_alcohol')
    .liquid()
    .components('8x carbon', '10x hydrogen', '2x oxygen')
    .color(0xFFF2D6)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Methyl bromide (CH3Br)
  event.create('methylbromide')
    .liquid()
    .components('1x carbon', '3x hydrogen', '1x bromine')
    .color(0xEEF4FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Methyl chloride (CH3Cl)
  event.create('methylchloride')
    .liquid()
    .components('1x carbon', '3x hydrogen', '1x chlorine')
    .color(0xEEF8FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // NaHMDS (C6H18NNaSi2)
  event.create('nahmds')
    .liquid()
    .components('6x carbon', '18x hydrogen', '1x nitrogen', '1x sodium', '2x silicon')
    .color(0xDDE8FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

    // THF (C4H8O)
  event.create('thf')
    .liquid()
    .components('4x carbon', '8x hydrogen', '1x oxygen')
    .color(0xEAF6FF);

  // NaHMDS in THF (MIXTURE)
  event.create('nahmdsinthf')
    .liquid()
    .color(0xDCEAFF)
    .components('1x nahmds', '1x thf')
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Pentane (C5H12)
  event.create('pentane')
    .liquid()
    .components('5x carbon', '12x hydrogen')
    .color(0xF4F7FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Pentose Solution (MIXTURE)
  event.create('pentose_solution')
    .liquid()
    .color(0xD8B36C)
    

  // Phosphorus Trichloride (PCl3)
  event.create('phosphorus_trichloride')
    .liquid()
    .components('1x phosphorus', '3x chlorine')
    .color(0xFFF2CC);

  // Pyridine (C5H5N)
  event.create('pyridine')
    .liquid()
    .components('5x carbon', '5x hydrogen', '1x nitrogen')
    .color(0xFFF0B8)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Pyridine Organic Residue (MIXTURE)
  event.create('pyridine_organic_residue')
    .liquid()
    .color(0xB07A2A);

  // Pyridine Residue (MIXTURE)
  event.create('pyridine_residue')
    .liquid()
    .color(0x8C5E1E);

  // Pyridine Salt Waste Slurry (MIXTURE)
  event.create('pyridine_salt_waste_slurry')
    .liquid()
    .color(0xB2B2B2);

  // Pyridine Treated Water (MIXTURE)
  event.create('pyridine_treated_water')
    .liquid()
    .color(0xCFE9FF);

  // Pyridine Waste Brine (MIXTURE)
  event.create('pyridine_waste_brine')
    .liquid()
    .color(0x9FBAD1);

  // Pyridine Waste Sludge (MIXTURE)
  event.create('pyridine_waste_sludge')
    .liquid()
    .color(0x5A4A36);

  // Pyridine Waste Water (MIXTURE)
  event.create('pyridine_waste_water')
    .liquid()
    .color(0xA8CBE6);

  // Pyridine Water Azeotrope (MIXTURE)
  event.create('pyridine_water_azeotrope')
    .liquid()
    .color(0xDCEFFF);

  // === Salvinorin A line fluids ===
  event.create('sal_a_waste_water_e5_to_e6_and_7').liquid().color(0x291F1E);
  event.create('sal_a_heavy_organic_residue_e5_to_e6_and_7').liquid().color(0x3E2A18);
  event.create('sal_a_organic_residue_e5_to_e6_and_7').liquid().color(0xB37A2B);
  event.create('sal_a_treated_water_e5_to_e6_and_7').liquid().color(0x5B3B1A);
  event.create('sal_a_waste_sludge_e5_to_e6_and_7').liquid().color(0x2C1F15);
  event.create('sal_a_anisole_waste_fluid_17_to_17_2').liquid().color(0xB37A2B);
  event.create('sal_a_crude_organics_10_2_to_11').liquid().color(0x7A4D1B);
  event.create('sal_a_acidic_acqueous_layer_d6_to_d8').liquid().color(0xBEE6FF);
  event.create('sal_a_mixed_salts_solution_d8_to_b9').liquid().color(0xBFC7D1);
  event.create('sal_a_mixed_salts_solution_10_2_to_11').liquid().color(0xBFC7D1);
  event.create('sal_a_crude_organic_residue_d8_to_b9').liquid().color(0x5B3B1A);
  event.create('sal_a_mother_liquor_e4_to_a5').liquid().color(0xC7B37A);
  event.create('sal_a_heavy_organic_residue_14_to_15_and_16').liquid().color(0x3E2A18);
  event.create('sal_a_enone_precursor').liquid().color(0xE0B36A);
  event.create('sal_a_heavy_aromatic_residue').liquid().color(0x2C1F15);
  event.create('sal_a_heavy_aromatic_residue_17_to_17_2').liquid().color(0x2C1F15);
  event.create('sal_a_terpene_like_ketone_core').liquid().color(0xD39A4B);
  event.create('sal_a_triphenyl_like_aromatics').liquid().color(0x4A341E);
  event.create('sal_a_contaminated_waste_water_d8_to_b9').liquid().color(0x4A341E);

  // Silicon Tetrachloride (SiCl4)
  event.create('silicon_tetrachloride')
    .liquid()
    .components('1x silicon', '4x chlorine')
    .color(0xEEF4FF);

  // Sodium Aluminate Solution (MIXTURE)
  event.create('sodium_aluminate_solution')
    .liquid()
    .color(0xD6E9FF);

  // Sodium Bromide Brine (MIXTURE)
  event.create('sodium_bromide_brine')
    .liquid()
    .color(0xBBD7FF);

  // Sodium Dichromate Solution (MIXTURE; Na2Cr2O7)
  event.create('sodium_dichromate_solution')
    .liquid()
    .color(0xFF8A1F);

  // Sodium Hydroxide Solution (MIXTURE; NaOH(aq))
  event.create('sodium_hydroxide_solution')
    .liquid()
    .color(0xDDF2FF);

  // Sodium Methoxide (CH3ONa)
  event.create('sodium_methoxide')
    .liquid()
    .components('1x carbon', '3x hydrogen', '1x oxygen', '1x sodium')
    .color(0xEAF7FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Sodium Silicate Solution (MIXTURE)
  event.create('sodium_silicate_solution')
    .liquid()
    .color(0xCFE8FF);

  // Sulfuryl Chloride (SO2Cl2)
  event.create('sulfuryl_chloride')
    .liquid()
    .components('1x sulfur', '2x oxygen', '2x chlorine')
    .color(0xFFF0BF);

  // T-BuLi in Pentane (MIXTURE)
  event.create('t_buli_in_pentane')
    .liquid()
    .color(0xF2F2FF);

  // TBSCl (C6H15ClSi)
  event.create('tbscl')
    .liquid()
    .components('6x carbon', '15x hydrogen', '1x chlorine', '1x silicon')
    .color(0xE2ECFF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // tert-Butyl chloride (C4H9Cl)
  event.create('tertbutylchloride')
    .liquid()
    .components('4x carbon', '9x hydrogen', '1x chlorine')
    .color(0xFFF0E6)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // tert-Butyldimethylsilanol (C6H16OSi)
  event.create('tertbutyldimethylsilanol')
    .liquid()
    .components('6x carbon', '16x hydrogen', '1x oxygen', '1x silicon')
    .color(0xE8F0FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // TESCl (C6H15ClSi)
  event.create('tescl')
    .liquid()
    .components('6x carbon', '15x hydrogen', '1x chlorine', '1x silicon')
    .color(0xE2ECFF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);



  // Thionyl Chloride (SOCl2)
  event.create('thionyl_chloride')
    .liquid()
    .components('1x sulfur', '1x oxygen', '2x chlorine')
    .color(0xFFF0B3)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Tributylamine (C12H27N)
  event.create('tributylamine')
    .liquid()
    .components('12x carbon', '27x hydrogen', '1x nitrogen')
    .color(0xFFF2C4)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Triethylamine (C6H15N)
  event.create('triethylamine')
    .liquid()
    .components('6x carbon', '15x hydrogen', '1x nitrogen')
    .color(0xFFF2D0)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Triethylsilanol (C6H16OSi)
  event.create('triethylsilanol')
    .liquid()
    .components('6x carbon', '16x hydrogen', '1x oxygen', '1x silicon')
    .color(0xE8F0FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Zeolite A Gel (MIXTURE)
  event.create('zeolite_a_gel')
    .liquid()
    .color(0xC7D6E6);

  // Sodium Bicarbonate Solution (MIXTURE; NaHCO3(aq))
  event.create('sodium_bicarbonate_solution')
    .liquid()
    .color(0xD8EEFF);

  // stabilized carrier solution
  event.create('stabilized_carrier_solution')
    .liquid()
    .components('2x water', '1x ethanol', '1x glycerol')
    .color(0xBFE8FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // stabilized carrier solution warmed
  event.create('stabilized_carrier_solution_warmed')
    .liquid()
    .components('2x water', '1x ethanol', '1x glycerol')
    .color(0x88A5B5)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // kappa reset concentrate
  event.create('kappa_reset_concentrate')
    .liquid()
    .components('1x stabilized_carrier_solution', '1x activated_carbon', '1x sodium_bicarbonate')
    .color(0x9C6BFF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // === Solids ===

  // 2-deacetoxysalvinorin A
  event.create('2_deacetoxysalvinorin_a')
    .dust()
    .color(0xE7D2B5).iconSet(GTMaterialIconSet.DULL);

  // 2-epi-Salvinorin B
  event.create('2_epi_salvinorin_b')
    .dust()
    .color(0xE4CFAF).iconSet(GTMaterialIconSet.DULL);

  // Pyridine Hydrochloride
  event.create('pyridine_hydrochloride')
    .dust()
    .components('5x carbon', '6x hydrogen', '1x nitrogen', '1x chlorine')
    .color(0xDCEFFF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);
  
  // Alumina (Al2O3)
  event.create('alumina')
    .dust()
    .components('2x aluminium', '3x oxygen')
    .color(0xF3F3F3).iconSet(GTMaterialIconSet.DULL);

  // Potassium Fluoride (KF)
  event.create('potassium_fluoride')
    .dust()
    .components('1x potassium', '1x fluorine')
    .color(0xC9DEF5);

  // chromium oxide (CrO)
  event.create('chromium_oxide')
    .dust()
    .components('1x chromium', '1x oxygen')
    .color(0xC9DEF5);


  // Aluminium Hydroxide (Al(OH)3)
  event.create('aluminium_hydroxide')
    .dust()
    .components('1x aluminium', '3x oxygen', '3x hydrogen')
    .color(0xFAFAFA).iconSet(GTMaterialIconSet.DULL);

  // Calcium Sulfate (CaSO4)
  event.create('calcium_sulfate')
    .dust()
    .components('1x calcium', '1x sulfur', '4x oxygen')
    .color(0xF4F4F4).iconSet(GTMaterialIconSet.DULL);

  // Cellulose Pulp ((C6H10O5)n)
  event.create('cellulose_pulp')
    .dust()
    .components('6x carbon', '10x hydrogen', '5x oxygen')
    .color(0xE8D9B8).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Lignin Residue
  event.create('lignin_residue')
    .dust()
    .color(0x8F7E64).iconSet(GTMaterialIconSet.DULL);

  // 1,4-benzoquinone
  event.create('1_4_benzoquinone')
    .dust().liquid()
    .components('6x carbon', '4x hydrogen', '2x oxygen')
    .color(0xE5FF00)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // lithium hydride (LiH)
  event.create('lithium_hydride')
    .dust()
    .components('1x lithium', '1x hydrogen')
    .color(0x8F9184).iconSet(GTMaterialIconSet.DULL);

  // lithium aluminium hydride (LiAlH4)
  event.create('lithium_aluminium_hydride')
    .dust()
    .components('1x lithium', '1x aluminium', '4x hydrogen')
    .color(0xffffff).iconSet(GTMaterialIconSet.DULL);

  // DCC (C13H22N2)
  event.create('dcc')
    .dust()
    .components('13x carbon', '22x hydrogen', '2x nitrogen')
    .color(0xEFE5D0).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // DDQ (C8Cl2N2O2)
  event.create('ddq')
    .dust()
    .components('8x carbon', '2x chlorine', '2x nitrogen', '2x oxygen')
    .color(0xB98A1A).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // DDQ Reduced (approx DDQH2)
  event.create('ddq_reduced')
    .dust()
    .components('8x carbon', '2x chlorine', '2x nitrogen', '2x oxygen', '2x hydrogen')
    .color(0xC9A64A).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Dicyclohexylurea (C13H24N2O)
  event.create('dicyclohexylurea')
    .dust()
    .components('13x carbon', '24x hydrogen', '2x nitrogen', '1x oxygen')
    .color(0xF2EFE6).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // pyridinium chlorochromate ([C₅H₅NH]⁺[CrO₃Cl]⁻)
  event.create('pyridinium_chlorochromate')
    .dust()
    .components('5x carbon', '5x hydrogen', '1x nitrogen', '1x hydrogen', '1x chlorine', '1x chromium', '3x oxygen')
    .color(0xFC6A03).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // pyridinium dichromate
  event.create('pyridinium_dichromate')
    .dust()
    .color(0xFF4D00).iconSet(GTMaterialIconSet.DULL);

  // pyridinium salt waste
  event.create('pyridinium_salt_waste')
    .dust()
    .color(0x521D00).iconSet(GTMaterialIconSet.DULL);

  // DMAP (C7H10N2)
  event.create('dmap')
    .dust()
    .components('7x carbon', '10x hydrogen', '2x nitrogen')
    .color(0xF0E6C8).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Humins (MIXTURE)
  event.create('humins')
    .dust()
    .color(0x3A2A1E).iconSet(GTMaterialIconSet.DULL);

  // Methyl Triphenyl Phosphonium Bromide
  event.create('methyl_triphenyl_phosphonium_bromide')
    .dust()
    .components('19x carbon', '18x hydrogen', '1x bromine', '1x phosphorus')
    .color(0x3A2A1E).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // process intermediates
  event.create('sal_a_hydrazinedicarboxylate_waste').dust().color(0xB9B9B9).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_alcohol_10_2').dust().color(0xE8D7B9).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_alcohol_11_2').dust().color(0xE8D7B9).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_alcohol_13_2').dust().color(0xE8D7B9).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_alcohol_17_2').dust().color(0xE8D7B9).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_alcohol_5').dust().color(0xE8D7B9).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_aldehyde_14').dust().color(0xD9B37C).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_aldehyde_base_waste_11_3_to_12').dust().color(0x8C6A3A).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_reduced_intermediate_slurry_b9_to_d10').dust().color(0x6C5A46).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_bisaldehyde_11_3').dust().color(0xD4B07A).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_bisaldehyde_12').dust().color(0xD4B07A).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_bisolefin_9').dust().color(0xC9A35A).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_carboxylic_acid_17_3').dust().color(0xE6D6B6).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_chromium_oxidation_organic_waste_11_2_to_11_3').dust().color(0x5C3A1E).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_silylation_organic_waste_b9_to_d10').dust().color(0x4A3524).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_silylation_organic_waste_d10_to_10_2').dust().color(0x4A3524).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_diketone_8').dust().color(0xE0B06B).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_diol_10').dust().color(0xE8D7B9).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_dioxolane_6').dust().color(0xE9DCC8).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_organic_waste_10_2_to_11').dust().color(0x4A3A2C).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_organic_waste_e5_to_e6_and_7').dust().color(0x4A3A2C).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_enone_4').dust().color(0xD9A24A).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_enone_5').dust().color(0xD9A24A).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_ester_7').dust().color(0xE6C38A).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_exomethylene_11').dust().color(0xD6B37C).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_furyl_alcohol_15').dust().color(0xC18B3C).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_furyl_alcohol_16').dust().color(0xC18B3C).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_furyl_organic_waste_14_to_15_and_16').dust().color(0x5A3D1E).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_hydroboration_organic_waste_11_to_11_2').dust().color(0x5A3D1E).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_ketal_13').dust().color(0xE6CFA8).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_lactol_17').dust().color(0xE6CFA8).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_organoborane_intermediate_11_to_11_2').dust().color(0xC9C9C9).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_pdc_13_2_to_14').dust().color(0x7A5528).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_tbs_10_2').dust().color(0xDDE8FF).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_tes_19').dust().color(0xDDE8FF).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_spiro_ketone_carboxylate_intermediate').dust().color(0x3F4F47).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_wittig_organic_waste_d8_to_b9').dust().color(0x003018).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_mixed_salts_10_2_to_11').dust().color(0xBD7A1C).iconSet(GTMaterialIconSet.DULL);
  event.create('sal_a_mixed_salts_d8_to_b9').dust().color(0xBD7A1C).iconSet(GTMaterialIconSet.DULL);

  // Salvinorin A (C23H28O8)
  event.create('salvinorin_a')
    .dust()
    .components('23x carbon', '28x hydrogen', '8x oxygen')
    .color(0xEADBC2).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Silica (SiO2)
  event.create('silica')
    .dust()
    .components('1x silicon', '2x oxygen')
    .color(0xF7F7F7).iconSet(GTMaterialIconSet.DULL);

  // Molecular Sieve 4A
  event.create('molecular_sieve_4a')
    .dust()
    .color(0x000000).iconSet(GTMaterialIconSet.DULL);

  // Magnesium Oxide
  event.create('magnesium_oxide')
    .dust()
    .components('1x magnesium', '1x oxygen')
    .color(0xFFFFFF).iconSet(GTMaterialIconSet.DULL);

  // Vanadium Pentoxide
  event.create('vanadium_pentoxide')
    .dust()
    .components('2x vanadium', '5x oxygen')
    .color(0x755413).iconSet(GTMaterialIconSet.DULL);

  // Sodium Acetate (C2H3NaO2)
  event.create('sodium_acetate')
    .dust()
    .components('2x carbon', '3x hydrogen', '1x sodium', '2x oxygen')
    .color(0xF7F7F7).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Sodium Borate (Na2B4O7)
  event.create('sodium_borate')
    .dust()
    .components('2x sodium', '4x boron', '7x oxygen')
    .color(0xF0F0F0).iconSet(GTMaterialIconSet.DULL)

    // Sodium Bromide (NaBr)
  event.create('sodium_bromide')
    .dust()
    .components('1x sodium', '1x bromine')
    .color(0xF7F2EA).iconSet(GTMaterialIconSet.DULL);

  // Sodium Chloride (NaCl)
  event.create('sodium_chloride')
    .dust()
    .components('1x sodium', '1x chlorine')
    .color(0xF8F8F8).iconSet(GTMaterialIconSet.DULL);

  // Sodium Hydride (NaH)
  event.create('sodium_hydride')
    .dust()
    .components('1x sodium', '1x hydrogen')
    .color(0xCFCFCF).iconSet(GTMaterialIconSet.DULL);

  // Sodium Sulfate (Na2SO4)
  event.create('sodium_sulfate')
    .dust()
    .components('2x sodium', '1x sulfur', '4x oxygen')
    .color(0xF4F4F4).iconSet(GTMaterialIconSet.DULL);

  // Sodium Sulfate Hydrated (Na2SO4·10H2O)
  event.create('sodium_sulfate_hydrated')
    .dust()
    .components('2x sodium', '1x sulfur', '14x oxygen', '20x hydrogen')
    .color(0xEAF3FF).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // TBACl (C16H36ClN)
  event.create('tbacl')
    .dust()
    .components('16x carbon', '36x hydrogen', '1x chlorine', '1x nitrogen')
    .color(0xF2F2F2).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // TBAF (C16H36FN)
  event.create('tbaf')
    .dust()
    .components('16x carbon', '36x hydrogen', '1x fluorine', '1x nitrogen')
    .color(0xF2F2F2).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // TBSF (C6H15FSi)
  event.create('tbsf')
    .dust()
    .components('6x carbon', '15x hydrogen', '1x fluorine', '1x silicon')
    .color(0xDDE8FF).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Tetrabutylammonium Chloride (C16H36ClN)
  event.create('tetrabutylammonium_chloride')
    .dust()
    .components('16x carbon', '36x hydrogen', '1x chlorine', '1x nitrogen')
    .color(0xF2F2F2).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Triethylammonium Chloride (C6H16ClN)
  event.create('triethylammonium_chloride')
    .dust()
    .components('6x carbon', '16x hydrogen', '1x chlorine', '1x nitrogen')
    .color(0xF2F2F2).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Triphenylphosphine (C18H15P)
  event.create('triphenylphosphine')
    .dust()
    .components('18x carbon', '15x hydrogen', '1x phosphorus')
    .color(0xF1F1F1).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Triphenylphosphine Oxide (C18H15OP)
  event.create('triphenylphosphine_oxide')
    .dust()
    .components('18x carbon', '15x hydrogen', '1x oxygen', '1x phosphorus')
    .color(0xF6F6F6).iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  // Lithium Iodide
  event.create('lithium_iodide')
    .dust()
    .components('1x lithium', '1x iodine')
    .color(0x800C00).iconSet(GTMaterialIconSet.DULL);

  // Calcium Carbide
  event.create('calcium_carbide')
    .dust()
    .components('1x calcium', '2x carbon')
    .color(0x757575).iconSet(GTMaterialIconSet.DULL);

})

