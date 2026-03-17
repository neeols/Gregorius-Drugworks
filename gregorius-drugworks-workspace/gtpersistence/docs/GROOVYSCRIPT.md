# GroovyScript API

Gregorius Drugworks Persistence exposes a GroovyScript compatibility plugin when the optional `GroovyScript` mod is installed alongside `gregoriusdrugworkspersistence`.

If `GroovyScript` is not installed, nothing breaks. The addon still runs normally; you just do not get the `mods.gdw` / `mods.gdwpersistence` scripting bridge.

## Quick Start

- Plugin aliases: `mods.gdw` and `mods.gdwpersistence`
- Put Gregorius scripts in `<instance>/groovy/preInit/`
- Register new pill or vape items in `preInit`
- Keep item ids fully qualified when referring to existing items, for example `gregoriusdrugworkspersistence:kappa_reset_ampoule`
- `modelTexture(...)` expects the texture file resource location, for example `gregoriusdrugworkspersistence:textures/item/sample_vape.png`

The repo includes a copy-paste showcase script here:

- [`examples/groovy/preInit/gregorius_drugworks_showcase.groovy`](../examples/groovy/preInit/gregorius_drugworks_showcase.groovy)

## Why `preInit` Matters

The Groovy bridge can register new pills and vapes, but Forge item registration closes early in startup. Because of that, scripted item creation is intentionally limited to GroovyScript `preInit`.

If you try to register a new Gregorius item later, the bridge throws an error on purpose:

> Gregorius scripted items must be registered during GroovyScript preInit scripts.

For data-only work, later stages can still be useful, but keeping all Gregorius registrations in `preInit` is the safest pattern.

## Common Imports

Most scripts only need a few shared enums:

```groovy
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCategory
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCompatibility
import com.wurtzitane.gregoriusdrugworks.common.visual.VisualColorMode
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationUsePhase
```

You can then grab the bridge like this:

```groovy
def gdw = mods.gdw
```

## Exposed Sections

### `mods.gdw.pills`

Registers new pill items backed by the custom pill animation and trip hooks.

```groovy
gdw.pills.builder('showcase_pill')
    .useDurationTicks(24)
    .lockCamera(false)
    .tripHookEnabled(true)
    .rarity('RARE')
    .finishSound('gregoriusdrugworkspersistence:pill_gulp')
    .modelTexture('gregoriusdrugworkspersistence:textures/item/salvinorin_a_pill.png')
    .register()
```

Useful builder methods:

- `maxStackSize(int)`
- `useDurationTicks(int)`
- `arcHeight(float)`
- `launchForward(float)`
- `mouthOffsetY(float)`
- `spinXPerTick(float)`, `spinYPerTick(float)`, `spinZPerTick(float)`
- `lockCamera(boolean)`
- `tripHookEnabled(boolean)`
- `rarity(EnumRarity or String)`
- `finishSound(ResourceLocation or String)`
- `modelTexture(ResourceLocation or String)`

Required fields in practice:

- `finishSound(...)`
- `modelTexture(...)`

### `mods.gdw.vapes`

Registers custom inhale items with server-driven phases, particles, remainders, lingering smoke, and per-phase actions.

```groovy
def handler = gdw.vapes.effectHandler()
    .onPhase(InhalationUsePhase.INHALE_COMPLETE, gdw.vapes.applyPotionEffect('minecraft:speed', 100, 0, false, true))
    .onPhase(InhalationUsePhase.USE_FINISH, gdw.vapes.executeTriggerBundle('showcase_bundle'))
    .build()

gdw.vapes.builder('showcase_vape')
    .maxUses(6)
    .localCameraNudge(false)
    .modelTexture('gregoriusdrugworkspersistence:textures/item/sample_vape.png')
    .effectHandler(handler)
    .register()
```

Useful builder methods:

- `sample(boolean)`
- `rarity(EnumRarity or String)`
- `totalUseTicks(int)`
- `raiseTicks(int)`
- `inhaleStartTick(int)`, `inhaleEndTick(int)`
- `holdTicks(int)`
- `exhaleStartTick(int)`, `exhaleEndTick(int)`
- `finishTicks(int)`
- `maxUses(int)`
- `durabilityLossMode(DurabilityLossMode or String)`
- `fixedLoss(int)`
- `randomLossRange(int, int)`
- `minimumCompletionRatio(float)`
- `consumeOnInterrupt(boolean)`
- `cooldownTicks(int)`
- `useCustomRenderer(boolean)`
- `localCameraNudge(boolean)`
- `glowRange(float, float)`
- `startSound(...)`, `inhaleSound(...)`, `exhaleSound(...)`, `finishSound(...)`, `exhaustedSound(...)`
- `modelTexture(...)`
- `inhaleParticle(InhalationParticleSpec)`
- `exhaleParticle(InhalationParticleSpec)`
- `perUseRemainder(InhalationRemainderSpec)`
- `exhaustedRemainder(InhalationRemainderSpec)`
- `effectHandler(InhalationEffectHandler)`
- `lingering(InhalationLingeringSpec)`

Helpers exposed on the section:

- `effectHandler()` returns a per-phase action builder
- `particle(...)` creates an `InhalationParticleSpec`
- `remainder(...)` creates an `InhalationRemainderSpec`
- `lingering()` returns a lingering plume builder
- `applyPotionEffect(...)`
- `forwardTripItemUse(itemId)`
- `executeTriggerBundle(triggerBundleId)`
- `startVisualProfile(profileId, durationTicks)`

Current bridge notes:

- `particle(...)` still uses legacy parameter names in the Groovy bridge. The last four numeric arguments map to `speed`, `forwardBias`, `upwardBias`, and `weight` in the underlying runtime spec.
- `remainder(itemId, chance, replaceSourceStack)` also keeps a legacy parameter name. The third boolean currently maps to the runtime's `dropIfInventoryFull` flag. On exhausted vapes, the first guaranteed remainder is also used to replace the spent stack in-hand.

### `mods.gdw.visualProfiles`

Defines client visual profiles that can be started from trips, payloads, or vape phase actions.

```groovy
gdw.visualProfiles.builder('showcase_focus', 'Showcase Focus')
    .colorMode(VisualColorMode.PULSE)
    .tintArgb(0x55A7FFD8 as int)
    .pulseSpeed(0.12F)
    .vignetteStrength(0.18F)
    .prismSeparation(0.08F)
    .tunnelStrength(0.12F)
    .ribbonIntensity(0.10F)
    .ribbonDensity(2)
    .scanlineStrength(0.05F)
    .rollDrift(0.03F)
    .afterimageStrength(0.05F)
    .register()
```

Useful builder methods:

- `colorMode(VisualColorMode or String)`
- `tintArgb(int)`
- `pulseSpeed(float)`
- `pulseAmplitude(float)`
- `flashFrequency(float)`
- `flashIntensity(float)`
- `yawDrift(float)`
- `pitchDrift(float)`
- `wobbleSpeed(float)`
- `wobbleAmplitude(float)`
- `fovPulseAmount(float)`
- `fovPulseSpeed(float)`
- `particleDensity(int)`
- `vignetteStrength(float)`
- `prismSeparation(float)`
- `tunnelStrength(float)`
- `ribbonIntensity(float)`
- `ribbonDensity(int)`
- `scanlineStrength(float)`
- `rollDrift(float)`
- `afterimageStrength(float)`
- `localOnly(boolean)`
- `startSoundId(String)`

The new visual fields are all client-side tuning knobs. They let scripts describe stronger local overlays without adding extra network payload:

- `vignetteStrength` darkens and tints the screen edges
- `prismSeparation` adds offset color blooms / prism ghosts
- `tunnelStrength` drives expanding ring and tunnel geometry
- `ribbonIntensity` and `ribbonDensity` control moving band overlays
- `scanlineStrength` controls animated scanline sweeps
- `rollDrift` adds eased camera roll during the profile
- `afterimageStrength` layers lingering ghost halos behind the main tint

### `mods.gdw.triggerBundles`

Groups reusable actions that can be fired from payloads or inhale phases.

```groovy
gdw.triggerBundles.builder('showcase_bundle')
    .playWorldSound('minecraft:entity.evocation_illager.prepare_summon')
    .startVisualProfile('showcase_focus', 200)
    .register()
```

Useful builder methods:

- `action(TriggerActionDefinition)`
- `forwardTripItemUse(itemId)`
- `startVisualProfile(profileId, durationTicks)`
- `playWorldSound(soundId)`

The raw `action(TriggerActionType, primaryId, intValue)` helper is also available if you want to build definitions manually.

### `mods.gdw.trips`

Registers staged effects keyed to an item id. Pills can trigger these automatically through `tripHookEnabled(true)`, and vapes or trigger bundles can forward item use into the same system.

```groovy
gdw.trips.registerTrip(
    gdw.trips.trip('gregoriusdrugworkspersistence:showcase_salvia_pill')
        .stage(
            gdw.trips.stage(0)
                .message('The edges soften.', 'gray')
                .triggerSpec(gdw.trips.stageEnter('showcase_focus_bundle'))
                .effect(gdw.trips.effect('minecraft:speed', 6, 0, false))
                .build()
        )
        .build()
)
```

Main entry points:

- `trip(itemId)`
- `stage(atSeconds)`
- `antidote(itemId)`
- `triggerSpec()`
- `stageEnter(triggerBundleId)`
- `stageTick(triggerBundleId, intervalTicks)`
- `stageExit(triggerBundleId)`
- `effect(effectId, seconds, amplifier, hideParticles)`
- `particle(particleId, count, spread, speed)`
- `sound(soundId, volume, pitch)`
- `registerTrip(TripDefinition)`
- `registerAntidote(AntidoteDefinition)`
- `allowAntidoteForTrip(antidoteItemId, tripItemId)`

Trip stages use the common Java builder, so you can wire stage trigger bundles directly through `TripStage.Builder.triggerSpec(...)`. The helpers above are just convenience constructors for the trigger spec:

```groovy
gdw.trips.stage(18)
    .message('The room exhales with you.', 'aqua')
    .triggerSpec(
        gdw.trips.triggerSpec()
            .onEnter('showcase_focus_bundle')
            .onTick('showcase_focus_bundle', 80)
            .build()
    )
    .build()
```

### `mods.gdw.payloads`

Registers delivery payload definitions. Applicators are the primary current use case, but the compatibility enum is broader and leaves room for additional delivery paths.

For the built-in medical flow, think of payloads as abstract effects and payload sources as the real physical ingredients. A vial or ampoule becomes useful once it is mapped through `payloadSources` and loaded into an applicator stack.

```groovy
gdw.payloads.builder(
        'showcase_stimulant_payload',
        PayloadCategory.UTILITY,
        PayloadCompatibility.APPLICATOR,
        'item.gregoriusdrugworkspersistence.showcase_stimulant_payload'
    )
    .defaultCharges(1)
    .visualProfile('showcase_focus', 200)
    .triggerBundleId('showcase_bundle')
    .register()
```

Useful builder methods:

- `defaultCharges(int)`
- `chargePolicy(PayloadChargePolicy or String)`
- `flag(PayloadBehaviorFlag or String)`
- `forwardItemId(itemId)`
- `visualProfile(profileId, durationTicks)`
- `visualProfileId(profileId)`
- `defaultVisualDurationTicks(int)`
- `triggerBundleId(bundleId)`

### `mods.gdw.payloadSources`

Maps a real source item to a payload id so the loading system knows what an applicator should receive.

```groovy
gdw.payloadSources.builder('minecraft:sugar', 'showcase_stimulant_payload')
    .consumed(true)
    .register()
```

Useful builder methods:

- `chargesOverride(int)`
- `consumed(boolean)`
- `remainderItemId(itemId)`

### `mods.gdw.applicators`

Helpers for working with the existing syringe/applicator item stack format.

Main entry points:

- `load(applicatorStack, payloadId)`
- `load(applicatorStack, payloadId, chargesOverride)`
- `load(applicatorStack, payloadId, chargesOverride, payloadData)`
- `createLoaded(payloadId)`
- `createLoaded(payloadId, chargesOverride)`
- `createLoaded(payloadId, chargesOverride, payloadData)`
- `clear(applicatorStack)`
- `hasPayload(applicatorStack)`
- `describe(applicatorStack)`

This is useful for quest rewards, loot scripts, or hand-built stacks created by other Groovy integrations.

## Practical Tips

- Prefer `mods.gdw` in examples. It is short and maps cleanly to the mod's aliases.
- Use full registry ids when referencing existing items, trips, antidotes, and payload sources.
- Reuse built-in textures and sounds while prototyping. The showcase script does this on purpose so it works with the assets already in this repo.
- If a scripted pill or vape fails to register, check that the script is in `preInit` and that your item id is unique.
