StartupEvents.registry('block', event => {

  event.create('fluoropolymer_fractionation_casing')
    .displayName('Fluoropolymer Fractionation Casing')
    .soundType('metal')
    .hardness(5).resistance(8)
    .tagBlock('mineable/pickaxe')
    .tagBlock('forge:mineable/wrench')
    .requiresTool(true);

  event.create('polyetherimide_thermal_casing')
    .displayName('Polyetherimide Thermal Casing')
    .soundType('metal')
    .hardness(5).resistance(8)
    .tagBlock('mineable/pickaxe')
    .tagBlock('forge:mineable/wrench')
    .requiresTool(true);

  event.create('molecular_membrane_casing', 'gtceu:active')
    .simple('kubejs:block/molecular_membrane_casing')
    .displayName('Molecular Membrane Casing')
    .soundType('metal')
    .hardness(4).resistance(7)
    .tagBlock('mineable/pickaxe')
    .tagBlock('forge:mineable/wrench')
    .requiresTool(true);

  event.create('polysiloxane_vapor_control_casing')
    .displayName('Polysiloxane Vapor Control Casing')
    .soundType('metal')
    .hardness(5).resistance(8)
    .tagBlock('mineable/pickaxe')
    .tagBlock('forge:mineable/wrench')
    .requiresTool(true);

});