//priority: 1000000

global.id = (id) => `start:${id}`;

GTCEuStartupEvents.registry('gtceu:recipe_type', event => {
  event.create('distillation_unit')
    .category('multiblock')
    .setEUIO('in')
    .setProgressBar(GuiTextures.PROGRESS_BAR_DISTILLATION_TOWER, FillDirection.LEFT_TO_RIGHT)
    .setSlotOverlay(false, false, GuiTextures.MOLECULAR_OVERLAY_1)
    .setMaxIOSize(2, 6, 2, 9)
    .setSound(GTSoundEntries.MOTOR);
});