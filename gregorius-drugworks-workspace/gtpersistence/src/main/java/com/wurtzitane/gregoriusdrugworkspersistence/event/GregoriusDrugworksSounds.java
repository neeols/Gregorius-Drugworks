package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class GregoriusDrugworksSounds {

    public static SoundEvent ANTIDOTE_INJECT;
    public static SoundEvent PILL_GULP;

    private GregoriusDrugworksSounds() {
    }

    public static void register() {
        // kept for compatibility with your current preInit call
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        ANTIDOTE_INJECT = register(event, "antidote_inject");
        PILL_GULP = register(event, "pill_gulp");
    }

    private static SoundEvent register(RegistryEvent.Register<SoundEvent> event, String name) {
        SoundEvent soundEvent = new SoundEvent(GregoriusDrugworksUtil.makeName(name));
        soundEvent.setRegistryName(GregoriusDrugworksUtil.makeName(name));
        event.getRegistry().register(soundEvent);
        return soundEvent;
    }
}