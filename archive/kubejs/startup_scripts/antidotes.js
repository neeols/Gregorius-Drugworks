StartupEvents.registry("item", function (event) {

  event.create('empty_glass_ampoule')
    .displayName('Empty Glass Ampoule')
    .maxStackSize(64);

  function safeCall(builder, methodName, args) {
    try {
      if (builder && builder[methodName]) {
        builder[methodName].apply(builder, args || []);
      }
    } catch (e) {}
  }

  function makeInjector(id, displayName, tooltipLines, opts) {
    opts = opts || {};

    var maxStack = (opts.maxStackSize !== undefined) ? opts.maxStackSize : 16;
    var rarity   = (opts.rarity !== undefined) ? String(opts.rarity) : "uncommon";
    var anim     = (opts.useAnimation !== undefined) ? String(opts.useAnimation) : "bow";

    var it = event.create(id)
      .displayName(displayName)
      .maxStackSize(maxStack);

    safeCall(it, "rarity", [rarity]);

    safeCall(it, "useAnimation", [anim]);

    safeCall(it, "use", [function (level, player, hand) {
      return true;
    }]);

    safeCall(it, "tooltip", ["§7§oInjector / Ampoule"]);
    if (tooltipLines && tooltipLines.length) {
      for (var i = 0; i < tooltipLines.length; i++) {
        safeCall(it, "tooltip", [String(tooltipLines[i])]);
      }
    }

    if (opts.glow === true) safeCall(it, "glow", [true]);

    return it;
  }

  // Opioids: naloxone
  makeInjector(
    "kubejs:naloxone_autoinjector",
    "Naloxone Autoinjector",
    [
      "§8Inspired by: naloxone (opioid antagonist).",
      "§7In-game: cancels opioid-type trips."
    ],
    { rarity: "rare", useAnimation: "bow", maxStackSize: 16 }
  );

  // Benzodiazepines: flumazenil
  makeInjector(
    "kubejs:flumazenil_ampoule",
    "Flumazenil Ampoule",
    [
      "§8Inspired by: flumazenil (benzodiazepine antagonist).",
      "§7In-game: cancels sedative/benzo-type trips."
    ],
    { rarity: "rare", useAnimation: "bow", maxStackSize: 16 }
  );

  // Organophosphates / nerve agents: atropine + pralidoxime (2-PAM)
  makeInjector(
    "kubejs:atropine_2pam_autoinjector",
    "Atropine + 2-PAM Autoinjector",
    [
      "§8Inspired by: atropine + pralidoxime (organophosphate poisoning).",
      "§7In-game: cancels cholinergic/nerve-toxin trips."
    ],
    { rarity: "epic", useAnimation: "bow", maxStackSize: 8 }
  );

  // Acetaminophen: N-acetylcysteine (NAC)
  makeInjector(
    "kubejs:nac_infusion",
    "N-Acetylcysteine Infusion",
    [
      "§8Inspired by: N-acetylcysteine (acetaminophen toxicity).",
      "§7In-game: cancels hepatotoxic trips."
    ],
    { rarity: "uncommon", useAnimation: "drink", maxStackSize: 16 }
  );

  // Toxic alcohols: fomepizole
  makeInjector(
    "kubejs:fomepizole_vial",
    "Fomepizole Vial",
    [
      "§8Inspired by: fomepizole (toxic alcohol antidote).",
      "§7In-game: cancels solvent/toxic-alcohol trips."
    ],
    { rarity: "rare", useAnimation: "bow", maxStackSize: 16 }
  );

  // Cyanide: hydroxocobalamin
  makeInjector(
    "kubejs:hydroxocobalamin_kit",
    "Hydroxocobalamin Kit",
    [
      "§8Inspired by: hydroxocobalamin (cyanide antidote).",
      "§7In-game: cancels cyanide-like trips."
    ],
    { rarity: "epic", useAnimation: "bow", maxStackSize: 8 }
  );

  // Anticoagulants: vitamin K (warfarin reversal) + protamine (heparin reversal)
  makeInjector(
    "kubejs:vitamin_k_ampoule",
    "Vitamin K Ampoule",
    [
      "§8Inspired by: vitamin K (warfarin reversal).",
      "§7In-game: cancels anticoagulant-type trips."
    ],
    { rarity: "uncommon", useAnimation: "bow", maxStackSize: 16 }
  );

  makeInjector(
    "kubejs:protamine_vial",
    "Protamine Vial",
    [
      "§8Inspired by: protamine (heparin reversal).",
      "§7In-game: cancels anticoagulant-type trips."
    ],
    { rarity: "uncommon", useAnimation: "bow", maxStackSize: 16 }
  );

  // Cardio tox: glucagon
  makeInjector(
    "kubejs:glucagon_injector",
    "Glucagon Injector",
    [
      "§8Inspired by: glucagon (used in some beta-blocker toxicity protocols).",
      "§7In-game: cancels beta-blocker-type trips."
    ],
    { rarity: "rare", useAnimation: "bow", maxStackSize: 16 }
  );

  // Digoxin: digoxin
  makeInjector(
    "kubejs:digoxin_fab",
    "Digoxin-Fab Ampoule",
    [
      "§8Inspired by: digoxin immune fab.",
      "§7In-game: cancels glycoside-type trips."
    ],
    { rarity: "epic", useAnimation: "bow", maxStackSize: 8 }
  );

  // Salvinorin A
  makeInjector(
    "kubejs:kappa_reset_ampoule",
    "Kappa Reset Ampoule",
    [
      "§8Inspired by: opioid receptor antagonism concepts (not a real salvinorin antidote).",
      "§7In-game: cancels salvinorin A trips."
    ],
    { rarity: "rare", useAnimation: "bow", maxStackSize: 16 }
  );
});