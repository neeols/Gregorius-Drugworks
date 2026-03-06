var $Workable = Java.loadClass(
  "com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine"
);

GTCEuStartupEvents.registry("gtceu:machine", function (event) {
  event.create("distillation_unit", "multiblock")
    .rotationState(RotationState.NON_Y_AXIS)
    .machine(function (holder) { return new $Workable(holder); })
    .recipeType("distillation_unit")
    .appearanceBlock(function () { return Block.getBlock("kubejs:fluoropolymer_fractionation_casing"); })
    .pattern(function (definition) {
      var p = FactoryBlockPattern.start();

      p.aisle(
        "    BBBB    ",
        "    DDDD    ",
        "    DDDD    ",
        "    D  D    ",
        "    D  D    ",
        "            ",
        "            "
      );

      p.aisle(
        "AA  BBBB  AA",
        "CCEEEEEEEECC",
        "CC  DBBD  CC",
        "FF        FF",
        "CC        CC",
        "CC        CC",
        "AA        AA"
      );

      p.aisle(
        "AA  BBBB  AA",
        "CC  D  D  CC",
        "CC  DDDD  CC",
        "FF        FF",
        "CC        CC",
        "CC        CC",
        "AA        AA"
      );

      p.aisle(
        "    BBBB    ",
        "    DD~D    ",
        "    DDDD    ",
        "            ",
        "            ",
        "            ",
        "            "
      );

      p.where("~", Predicates.controller(Predicates.blocks(definition.get())));
      p.where("A", Predicates.blocks("kubejs:polyetherimide_thermal_casing"));
      p.where("B", Predicates.blocks("gtceu:filter_casing"));
      p.where("C", Predicates.blocks("kubejs:polysiloxane_vapor_control_casing"));
      p.where("E", Predicates.blocks("gtceu:tungstensteel_pipe_casing"));
      p.where("F", Predicates.blocks("kubejs:molecular_membrane_casing"));

      p.where("D",
        Predicates.blocks("kubejs:fluoropolymer_fractionation_casing")
          .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setMaxGlobalLimited(2).setPreviewCount(1))
          .or(Predicates.abilities(PartAbility.EXPORT_ITEMS).setMaxGlobalLimited(6).setPreviewCount(1))
          .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(2).setPreviewCount(1))
          .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setMaxGlobalLimited(9).setPreviewCount(1))
          .or(Predicates.abilities(PartAbility.INPUT_ENERGY).setMaxGlobalLimited(1).setPreviewCount(1))
          .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1).setPreviewCount(1))
      );

      p.where(" ", Predicates.air());

      return p.build();
    })
    .workableCasingModel(
      "kubejs:block/fluoropolymer_fractionation_casing",
      "gtceu:block/multiblock/blast_furnace"
    );
});