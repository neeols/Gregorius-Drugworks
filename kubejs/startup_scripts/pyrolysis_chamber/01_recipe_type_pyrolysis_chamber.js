//priority: 1000000

global.id = (id) => `start:${id}`;

GTCEuStartupEvents.registry('gtceu:recipe_type', event => {
  event.create('pyrolysis_chamber')
    .category('multiblock')
    .setEUIO('in')
    .setProgressBar(GuiTextures.PROGRESS_BAR_CRACKING, FillDirection.LEFT_TO_RIGHT)
    .setSlotOverlay(false, false, GuiTextures.HEATING_OVERLAY_1)
    .setMaxIOSize(3, 6, 3, 6)
    .setSound(GTSoundEntries.COMBUSTION);
});