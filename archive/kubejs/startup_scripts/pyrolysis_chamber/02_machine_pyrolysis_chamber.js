var $CoilWorkable = Java.loadClass(
  "com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine"
);

GTCEuStartupEvents.registry("gtceu:machine", function (event) {
  event.create("pyrolysis_chamber", "multiblock")
    .rotationState(RotationState.NON_Y_AXIS)
    .machine(function (holder) { return new $CoilWorkable(holder); })
    .recipeType("pyrolysis_chamber")
    .appearanceBlock(function () { return Block.getBlock("kubejs:carbonized_reactor_casing"); })
    .pattern(function (definition) {
      var p = FactoryBlockPattern.start();
      
      p.aisle(
        "CCC CCC",
        "BBB AAA",
        "BBB ADA",
        "    ADA",
        "    AAA"
      );

      p.aisle(
        "CCC CCC",
        "BHBBBBA",
        "BBB DED",
        "    DED",
        "    AAA"
      );

      p.aisle(
        "CCC CCC",
        "B~B AAA",
        "BBB ADA",
        "    ADA",
        "    AAA"
      );

      p.where("~", Predicates.controller(Predicates.blocks(definition.get())));

      p.where("H", Predicates.heatingCoils());

      p.where("C", Predicates.blocks("kubejs:obsidian_forged_thermal_casing"));

      p.where("A", Predicates.blocks("kubejs:thermocrack_matrix_casing"));

      p.where("D", Predicates.blocks("gtceu:laminated_glass"));

      p.where("E", Predicates.blocks("gtceu:borosilicate_glass_block"));

      p.where("B",
        Predicates.blocks("kubejs:carbonized_reactor_casing")
          .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setMaxGlobalLimited(3).setPreviewCount(1))
          .or(Predicates.abilities(PartAbility.EXPORT_ITEMS).setMaxGlobalLimited(6).setPreviewCount(1))
          .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(3).setPreviewCount(1))
          .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setMaxGlobalLimited(6).setPreviewCount(1))
          .or(Predicates.abilities(PartAbility.INPUT_ENERGY).setMaxGlobalLimited(1).setPreviewCount(1))
          .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1).setPreviewCount(1))
      );

      p.where(" ", Predicates.air());

      return p.build();
    })
    .workableCasingModel(
      "kubejs:block/carbonized_reactor_casing",
      "gtceu:block/multiblock/blast_furnace"
    );
});