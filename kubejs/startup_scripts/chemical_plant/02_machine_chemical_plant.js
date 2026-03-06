var $CoilWorkable = Java.loadClass(
  "com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine"
);

GTCEuStartupEvents.registry("gtceu:machine", function (event) {
  event.create("chemical_plant", "multiblock")
    .rotationState(RotationState.NON_Y_AXIS)
    .machine(function (holder) { return new $CoilWorkable(holder); })
    .recipeType("chemical_plant")
    .appearanceBlock(function () { return Block.getBlock("kubejs:chemplant_machine_casing_t1"); })
    .pattern(function (definition) {
      var p = FactoryBlockPattern.start();
      
      p.aisle(
        "CCCCCCC",
        "C     C",
        "C     C",
        "C     C",
        "C     C",
        "C     C",
        "CCCCCCC"
      );

      p.aisle(
        "CMMMMMC",
        " MMMMM ",
        "       ",
        "       ",
        "       ",
        " MMMMM ",
        "CCCCCCC"
      );

      p.aisle(
        "CMMMMMC",
        " MHHHM ",
        "  PPP  ",
        "  HHH  ",
        "  PPP  ",
        " MHHHM ",
        "CCCCCCC"
      );

      p.aisle(
        "CMMMMMC",
        " MHHHM ",
        "  PPP  ",
        "  H H  ",
        "  PPP  ",
        " MHHHM ",
        "CCCCCCC"
      );

      p.aisle(
        "CMMMMMC",
        " MHHHM ",
        "  PPP  ",
        "  HHH  ",
        "  PPP  ",
        " MHHHM ",
        "CCCCCCC"
      );

      p.aisle(
        "CMMMMMC",
        " MMMMM ",
        "       ",
        "       ",
        "       ",
        " MMMMM ",
        "CCCCCCC"
      );

      p.aisle(
        "CCC~CCC",
        "C     C",
        "C     C",
        "C     C",
        "C     C",
        "C     C",
        "CCCCCCC"
      );

      p.where("~", Predicates.controller(Predicates.blocks(definition.get())));
      p.where("H", Predicates.heatingCoils());
      p.where("M", Predicates.blocks("gtceu:mv_machine_casing"));
      p.where("P", Predicates.blocks("kubejs:chemplant_pipe_casing_t1"));

      p.where("C",
        Predicates.blocks("kubejs:chemplant_machine_casing_t1")
          .setMinGlobalLimited(86)
          .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setMaxGlobalLimited(6).setPreviewCount(1))
          .or(Predicates.abilities(PartAbility.EXPORT_ITEMS).setMaxGlobalLimited(6).setPreviewCount(1))
          .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(6).setPreviewCount(1))
          .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setMaxGlobalLimited(6).setPreviewCount(1))
          .or(Predicates.abilities(PartAbility.INPUT_ENERGY).setMaxGlobalLimited(1).setPreviewCount(1))
          .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1).setPreviewCount(1))
      );

      p.where(" ", Predicates.air());

      return p.build();
    })
    .workableCasingModel(
      "kubejs:block/chemplant_machine_casing_t1",
      "gtceu:block/multiblock/blast_furnace"
    );
});