StartupEvents.registry('block', event => {

  event.create('carbonized_reactor_casing')
    .displayName('Carbonized Reactor Casing')
    .soundType('metal')
    .hardness(6).resistance(10)
    .tagBlock('mineable/pickaxe')
    .tagBlock('forge:mineable/wrench')
    .requiresTool(true);

  event.create('obsidian_forged_thermal_casing', 'gtceu:active')
    .simple('kubejs:block/obsidian_forged_thermal_casing')
    .displayName('Obsidian-Forged Thermal Casing')
    .soundType('stone')
    .hardness(8).resistance(20)
    .tagBlock('mineable/pickaxe')
    .tagBlock('forge:mineable/wrench')
    .requiresTool(true);

  event.create('thermocrack_matrix_casing', 'gtceu:active')
    .simple('kubejs:block/thermocrack_matrix_casing')
    .displayName('Thermocrack Matrix Casing')
    .soundType('metal')
    .hardness(7).resistance(15)
    .tagBlock('mineable/pickaxe')
    .tagBlock('forge:mineable/wrench')
    .requiresTool(true);


});