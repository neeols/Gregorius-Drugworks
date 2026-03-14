ItemEvents.tooltip(function (event) {
  event.addAdvanced(/sal_a_/, function (item, advanced, text) {
    text.add(Text.gray("(Salvinorin A Chain)"));
  });
});

ItemEvents.tooltip(function (event) {
  var rule = Text.gray(Text.translate("tooltip.kubejs.chemplant_tier_rule"));

  for (var t = 1; t <= 7; t++) {
    event.add("kubejs:chemplant_machine_casing_t" + t, rule);
    event.add("kubejs:chemplant_pipe_casing_t" + t, rule);
  }
});