
const GTMaterials = Java.loadClass('com.gregtechceu.gtceu.common.data.GTMaterials')
const GTMaterialFlags = Java.loadClass('com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags')

GTCEuStartupEvents.materialModification(event => {
  GTMaterials.Niobium.addFlags(GTMaterialFlags.GENERATE_PLATE)
  GTMaterials.Kanthal.addFlags(GTMaterialFlags.GENERATE_FOIL)
})

StartupEvents.registry('item', event => {
  event.create('heatproof_alloy_mesh')
    .displayName('Heatproof Alloy Mesh')
    .maxStackSize(64);

  event.create('fine_stainless_mesh')
    .displayName('Fine Stainless Mesh')
    .maxStackSize(64);

  event.create('ceramic_filter')
    .displayName('Ceramic Filter')
    .maxStackSize(64);

  event.create('carbon_nanotubes')
    .displayName('Carbon Nanotubes')
    .maxStackSize(64);
});
GTCEuStartupEvents.registry('gtceu:material', event => {

  event.create('ceramic_fiber')
    .dust()
    .components('1x alumina', '2x silicon_dioxide')
    .color(0xEDE9E3)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.GENERATE_PLATE, GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('high_temperature_insulation')
    .dust()
    .components('1x ceramic_fiber', '1x silicon_dioxide')
    .color(0xC8C1B8)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('hexafluoropropylene')
    .gas()
    .components('3x carbon', '6x fluorine')
    .color(0xCFEFFF)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('perfluoroalkoxy_polymer_slurry')
    .liquid()
    .components('5x carbon', '12x fluorine', '1x oxygen')
    .color(0xE6F2FF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('fluoropolymer_structural_film')
    .polymer()
    .components('5x carbon', '12x fluorine', '1x oxygen')
    .color(0xF7FBFF)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('fluoropolymer_residue')
    .liquid()
    .components('3x carbon', '6x fluorine')
    .color(0xB6C6D6)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('phthalic_anhydride')
    .dust()
    .components('8x carbon', '4x hydrogen', '3x oxygen')
    .color(0xF1E6D6)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('trimellitic_anhydride')
    .dust()
    .components('9x carbon', '4x hydrogen', '5x oxygen')
    .color(0xE8D7C6)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('pyromellitic_dianhydride')
    .dust()
    .components('10x carbon', '2x hydrogen', '6x oxygen')
    .color(0xE6D2B8)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('oxydianiline')
    .dust()
    .components('12x carbon', '12x hydrogen', '2x nitrogen', '1x oxygen')
    .color(0xE9DAB8)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('m_phenylenediamine')
    .dust()
    .components('6x carbon', '8x hydrogen', '2x nitrogen')
    .color(0xD8C7A0)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('bisphenol_a_dianhydride')
    .dust()
    .components('31x carbon', '18x hydrogen', '8x oxygen')
    .color(0xD7C1A3)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('polyetherimide')
    .liquid()
    .components('37x carbon', '22x hydrogen', '2x nitrogen', '6x oxygen')
    .color(0xC79B43)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('polyimide_solution')
    .liquid()
    .components('22x carbon', '10x hydrogen', '2x nitrogen', '5x oxygen')
    .color(0xA06A2F)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('microporous_polyimide_membrane')
    .polymer()
    .components('22x carbon', '10x hydrogen', '2x nitrogen', '5x oxygen')
    .color(0xC9D2DA)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('polysiloxane')
    .liquid()
    .components('2x carbon', '6x hydrogen', '1x oxygen', '1x silicon')
    .color(0xDDE7EF)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('iron_pentacarbonyl')
    .liquid()
    .components('1x iron', '5x carbon', '5x oxygen')
    .color(0xC9D4DC);

  event.create('as_grown_multiwalled_carbon_nanotubes')
    .dust()
    .components('1x carbon')
    .color(0x141414)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('purified_multiwalled_carbon_nanotubes')
    .dust()
    .components('1x carbon')
    .color(0x101010)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('m_dinitrobenzene')
    .dust()
    .components('6x carbon', '4x hydrogen', '2x nitrogen', '4x oxygen')
    .color(0xD9C86A)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('p_chloronitrobenzene')
    .dust()
    .components('6x carbon', '4x hydrogen', '1x chlorine', '1x nitrogen', '2x oxygen')
    .color(0xD8C05E)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('p_nitrophenol')
    .dust()
    .components('6x carbon', '5x hydrogen', '1x nitrogen', '3x oxygen')
    .color(0xC9B04F)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);
  
  event.create('dinitrodiphenyl_ether_4_4')
    .dust()
    .components('12x carbon', '8x hydrogen', '2x nitrogen', '5x oxygen')
    .color(0xC9B36B)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('phenolic_resin')
    .polymer()
    .components('7x carbon', '6x hydrogen', '1x oxygen')
    .color(0x6B3C22)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('carbon_matrix_composite')
    .dust()
    .components('1x carbon', '1x graphite')
    .color(0x1C1C1C)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('molten_volcanic_glass')
    .liquid()
    .components('2x silicon_dioxide', '1x alumina')
    .color(0x2A202B)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('obsidian_ceramic_composite')
    .dust()
    .components('2x silicon_dioxide', '1x alumina')
    .color(0x3B303E)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('aluminosilicate_gel')
    .dust()
    .components('1x aluminum', '1x silicon', '4x oxygen', '2x hydrogen')
    .color(0xC8D0C8)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('zeolite_catalyst_matrix')
    .dust()
    .components('1x sodium', '1x aluminum', '1x silicon', '4x oxygen', '2x hydrogen')
    .color(0xB9C7B7)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

  event.create('catalytic_cracking_matrix')
    .dust()
    .components('1x zeolite_catalyst_matrix', '1x vanadium_pentoxide', '1x alumina')
    .color(0x879A86)
    .iconSet(GTMaterialIconSet.DULL)
    .flags(GTMaterialFlags.DISABLE_DECOMPOSITION);

});