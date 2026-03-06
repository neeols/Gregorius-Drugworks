StartupEvents.registry('block', event => {
  for (let i = 0; i < 7; i++) {
    event.create(`chemplant_pipe_casing_t${i + 1}`)
      .displayName(`Chemical Plant Pipe Casing (Tier ${i + 1})`)
      .soundType('metal')
      .hardness(5).resistance(6)
      .tagBlock('mineable/pickaxe')
      .tagBlock('forge:mineable/wrench')
      .requiresTool(true);
  }

  for (let i = 0; i < 7; i++) {
    event.create(`chemplant_machine_casing_t${i + 1}`)
      .displayName(`Chemical Plant Machine Casing (Tier ${i + 1})`)
      .soundType('metal')
      .hardness(5).resistance(6)
      .tagBlock('mineable/pickaxe')
      .tagBlock('forge:mineable/wrench')
      .requiresTool(true);
  }
});