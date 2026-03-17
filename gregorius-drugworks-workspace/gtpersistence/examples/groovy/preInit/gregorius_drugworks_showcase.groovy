import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCategory
import com.wurtzitane.gregoriusdrugworks.common.payload.PayloadCompatibility
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.InhalationUsePhase

/*
 * Gregorius Drugworks Persistence GroovyScript showcase.
 *
 * Copy this file into:
 *   <instance>/groovy/preInit/gregorius_drugworks_showcase.groovy
 *
 * GroovyScript is optional.
 * If the GroovyScript mod is not installed, Gregorius Drugworks Persistence still works;
 * you just will not have the `mods.gdw` / `mods.gdwpersistence` bridge available.
 *
 * This example shows:
 * - visual profiles
 * - trigger bundles
 * - custom pill registration
 * - custom trips and antidote routing
 * - custom vape registration with per-phase effects
 * - applicator payloads and payload sources
 *
 * Important:
 * New scripted pills and vapes must be registered in preInit.
 */

def gdw = mods.gdw
final int SECOND = 20

// ---------------------------------------------------------------------------
// 1. Visual profile: reusable camera / screen-side effect profile.
// ---------------------------------------------------------------------------
gdw.visualProfiles.builder('showcase_focus', 'Showcase Focus')
    .colorMode('PULSE')
    .tintArgb(0x55A7FFD8 as int)
    .pulseSpeed(0.12F)
    .pulseAmplitude(0.14F)
    .wobbleSpeed(0.08F)
    .wobbleAmplitude(0.018F)
    .fovPulseAmount(0.015F)
    .fovPulseSpeed(0.09F)
    .particleDensity(1)
    .startSoundId('minecraft:block.note.chime')
    .register()

// ---------------------------------------------------------------------------
// 2. Trigger bundle: reusable action packet you can fire from payloads or vapes.
// ---------------------------------------------------------------------------
gdw.triggerBundles.builder('showcase_focus_bundle')
    .playWorldSound('minecraft:entity.evocation_illager.prepare_summon')
    .startVisualProfile('showcase_focus', 10 * SECOND)
    .register()

// ---------------------------------------------------------------------------
// 3. Applicator payload + source.
//
// Payloads describe what an applicator does after delivery.
// Payload sources map some real item id to that payload so loading code can
// resolve it. This example uses sugar just to keep the demo dependency-free.
// In a real pack you would usually point at your own ampoule / vial / cartridge.
// ---------------------------------------------------------------------------
gdw.payloads.builder(
        'showcase_stimulant_payload',
        PayloadCategory.UTILITY,
        PayloadCompatibility.APPLICATOR,
        'item.gregoriusdrugworkspersistence.showcase_stimulant_payload'
    )
    .defaultCharges(1)
    .visualProfile('showcase_focus', 10 * SECOND)
    .triggerBundleId('showcase_focus_bundle')
    .register()

gdw.payloadSources.builder('minecraft:sugar', 'showcase_stimulant_payload')
    .consumed(true)
    .register()

// You can create a loaded applicator stack for rewards, loot, or other scripts.
// This does not automatically give it to anyone; it just produces an ItemStack.
def showcaseApplicator = gdw.applicators.createLoaded('showcase_stimulant_payload', 1)
assert gdw.applicators.hasPayload(showcaseApplicator)
// println(gdw.applicators.describe(showcaseApplicator))

// ---------------------------------------------------------------------------
// 4. Pill item + staged trip.
//
// The trip is keyed to the pill's full registry id. Because the pill keeps
// `tripHookEnabled(true)`, using it will automatically forward into TripHooks.
// ---------------------------------------------------------------------------
gdw.pills.builder('showcase_salvia_pill')
    .maxStackSize(16)
    .useDurationTicks(24)
    .arcHeight(1.00F)
    .launchForward(0.24F)
    .mouthOffsetY(-0.06F)
    .spinXPerTick(8.0F)
    .spinYPerTick(12.0F)
    .spinZPerTick(4.0F)
    .lockCamera(false)
    .tripHookEnabled(true)
    .rarity('RARE')
    .finishSound('gregoriusdrugworkspersistence:pill_gulp')
    .modelTexture('gregoriusdrugworkspersistence:textures/item/salvinorin_a_pill.png')
    .register()

gdw.trips.registerTrip(
    gdw.trips.trip('gregoriusdrugworkspersistence:showcase_salvia_pill')
        .stage(
            gdw.trips.stage(0)
                .message('The first wave is light and clean.', 'gray')
                .effect(gdw.trips.effect('minecraft:speed', 6, 0, false))
                .particle(gdw.trips.particle('minecraft:spell_mob', 4, 0.20D, 0.01D))
                .build()
        )
        .stage(
            gdw.trips.stage(7)
                .message('Peripheral detail starts to bend.', 'light_purple')
                .particle(gdw.trips.particle('minecraft:portal', 8, 0.35D, 0.03D))
                .build()
        )
        .stage(
            gdw.trips.stage(18)
                .message('The room exhales with you.', 'aqua')
                .effect(gdw.trips.effect('minecraft:night_vision', 8, 0, true))
                .particle(gdw.trips.particle('minecraft:cloud', 10, 0.40D, 0.02D))
                .build()
        )
        .build()
)

// Reuse an existing medical item as the antidote for the showcase trip.
gdw.trips.registerAntidote(
    gdw.trips.antidote('gregoriusdrugworkspersistence:kappa_reset_ampoule')
        .cancelMessage('The spiral collapses into a clean line.', 'aqua')
        .sound(gdw.trips.sound('gregoriusdrugworkspersistence:antidote_inject', 1.0F, 1.0F))
        .particles(gdw.trips.particle('minecraft:cloud', 12, 0.22D, 0.01D))
        .build()
)
gdw.trips.allowAntidoteForTrip(
    'gregoriusdrugworkspersistence:kappa_reset_ampoule',
    'gregoriusdrugworkspersistence:showcase_salvia_pill'
)

// ---------------------------------------------------------------------------
// 5. Vape item with per-phase actions.
//
// This example mirrors the current inhale system:
// - particles on inhale and exhale
// - optional lingering plume
// - a guaranteed `used_vape` remainder when exhausted
// - a speed hit on inhale complete
// - a visual trigger bundle on finish
//
// Note:
// `gdw.vapes.particle(...)` still exposes older parameter names in the bridge.
// The final four numeric values map to:
//   speed, forwardBias, upwardBias, weight
// ---------------------------------------------------------------------------
def showcaseVapeHandler = gdw.vapes.effectHandler()
    .onPhase(
        InhalationUsePhase.INHALE_COMPLETE,
        gdw.vapes.applyPotionEffect('minecraft:speed', 5 * SECOND, 0, false, true)
    )
    .onPhase(
        InhalationUsePhase.USE_FINISH,
        gdw.vapes.executeTriggerBundle('showcase_focus_bundle')
    )
    .onPhase(
        InhalationUsePhase.USE_FINISH,
        gdw.vapes.startVisualProfile('showcase_focus', 8 * SECOND)
    )
    // You can also bridge directly into the trip system like this:
    // .onPhase(InhalationUsePhase.USE_FINISH, gdw.vapes.forwardTripItemUse('gregoriusdrugworkspersistence:showcase_salvia_pill'))
    .build()

gdw.vapes.builder('showcase_focus_vape')
    .sample(true)
    .rarity('UNCOMMON')
    .totalUseTicks(48)
    .raiseTicks(6)
    .inhaleStartTick(6)
    .inhaleEndTick(18)
    .holdTicks(6)
    .exhaleStartTick(22)
    .exhaleEndTick(40)
    .finishTicks(48)
    .maxUses(6)
    .durabilityLossMode('FIXED')
    .fixedLoss(1)
    .minimumCompletionRatio(0.55F)
    .consumeOnInterrupt(false)
    .cooldownTicks(4)
    .useCustomRenderer(true)
    .localCameraNudge(false)
    .glowRange(0.30F, 1.00F)
    .startSound('gregoriusdrugworkspersistence:inhalation_start')
    .inhaleSound('gregoriusdrugworkspersistence:inhalation_inhale')
    .exhaleSound('gregoriusdrugworkspersistence:inhalation_exhale')
    .finishSound('gregoriusdrugworkspersistence:inhalation_finish')
    .exhaustedSound('gregoriusdrugworkspersistence:inhalation_exhausted')
    .modelTexture('gregoriusdrugworkspersistence:textures/item/sample_vape.png')
    .inhaleParticle(gdw.vapes.particle('cloud', 1, 0.01D, 0.01D, 0.01D, 0.005D, 0.08D, 0.03D, 1))
    .inhaleParticle(gdw.vapes.particle('smoke_normal', 2, 0.015D, 0.015D, 0.015D, 0.008D, 0.22D, 0.06D, 2))
    .exhaleParticle(gdw.vapes.particle('cloud', 3, 0.05D, 0.03D, 0.05D, 0.020D, 0.20D, 0.05D, 1))
    .exhaleParticle(gdw.vapes.particle('smoke_normal', 5, 0.04D, 0.025D, 0.04D, 0.010D, 0.38D, 0.08D, 2))
    .lingering(
        gdw.vapes.lingering()
            .startDelayTicks(2)
            .durationTicks(28)
            .cadenceTicks(2)
            .attachedTicks(4)
            .initialCount(4)
            .finalCount(1)
            .initialSpread(0.03D)
            .finalSpread(0.12D)
            .initialSpeed(0.010D)
            .finalSpeed(0.003D)
            .forwardDrift(0.020D)
            .upwardDrift(0.040D)
            .addParticle(gdw.vapes.particle('cloud', 2, 0.02D, 0.02D, 0.02D, 0.004D, 0.06D, 0.04D, 3))
            .addParticle(gdw.vapes.particle('smoke_normal', 1, 0.01D, 0.01D, 0.01D, 0.002D, 0.12D, 0.07D, 1))
            .build()
    )
    .exhaustedRemainder(gdw.vapes.remainder('gregoriusdrugworkspersistence:used_vape', 1.0F, true))
    .effectHandler(showcaseVapeHandler)
    .register()
