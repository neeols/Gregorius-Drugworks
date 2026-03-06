//priority: 1000000

global.id = (id) => `start:${id}`;

GTCEuStartupEvents.registry('gtceu:recipe_type', event => {
  event.create('chemical_plant')
    .category('multiblock')
    .setEUIO('in')
    .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT) // 
    .setSlotOverlay(false, false, GuiTextures.BEAKER_OVERLAY_1)
    .setMaxIOSize(6, 6, 6, 6)
    .setSound(GTSoundEntries.CHEMICAL)
});