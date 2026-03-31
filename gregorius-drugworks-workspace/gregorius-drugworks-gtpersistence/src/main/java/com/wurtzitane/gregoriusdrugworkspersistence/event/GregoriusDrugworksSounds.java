package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class GregoriusDrugworksSounds {

    private static boolean registered = false;

    public static SoundEvent ANTIDOTE_INJECT;
    public static SoundEvent PILL_GULP;
    public static SoundEvent SNORT;

    public static SoundEvent INHALATION_START;
    public static SoundEvent INHALATION_INHALE;
    public static SoundEvent INHALATION_EXHALE;
    public static SoundEvent INHALATION_FINISH;
    public static SoundEvent INHALATION_EXHAUSTED;

    public static SoundEvent APPLICATOR_START;
    public static SoundEvent APPLICATOR_FINISH;
    public static SoundEvent APPLICATOR_FAIL;

    private GregoriusDrugworksSounds() {
    }

    public static void register() {
        if (registered) {
            return;
        }
        registered = true;
        MinecraftForge.EVENT_BUS.register(new GregoriusDrugworksSounds());
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        ANTIDOTE_INJECT = register(event, "antidote_inject");
        PILL_GULP = register(event, "pill_gulp");
        SNORT = register(event, "snort");

        INHALATION_START = register(event, "inhalation_start");
        INHALATION_INHALE = register(event, "inhalation_inhale");
        INHALATION_EXHALE = register(event, "inhalation_exhale");
        INHALATION_FINISH = register(event, "inhalation_finish");
        INHALATION_EXHAUSTED = register(event, "inhalation_exhausted");

        APPLICATOR_START = register(event, "applicator_start");
        APPLICATOR_FINISH = register(event, "applicator_finish");
        APPLICATOR_FAIL = register(event, "applicator_fail");
    }

    private static SoundEvent register(RegistryEvent.Register<SoundEvent> event, String name) {
        SoundEvent soundEvent = new SoundEvent(GregoriusDrugworksUtil.makeName(name));
        soundEvent.setRegistryName(GregoriusDrugworksUtil.makeName(name));
        event.getRegistry().register(soundEvent);
        return soundEvent;
    }
}
