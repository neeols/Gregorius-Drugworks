package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GregoriusDrugworksSounds {

    private static final List<SoundEvent> REGISTERED_SOUNDS = new ArrayList<>();
    private static boolean registered = false;

    public static SoundEvent ANTIDOTE_INJECT;

    private GregoriusDrugworksSounds() {
    }

    public static void register() {
        if (registered) {
            return;
        }
        registered = true;

        ANTIDOTE_INJECT = create("antidote_inject");

        for (SoundEvent soundEvent : REGISTERED_SOUNDS) {
            if (!ForgeRegistries.SOUND_EVENTS.containsKey(soundEvent.getRegistryName())) {
                ForgeRegistries.SOUND_EVENTS.register(soundEvent);
            }
        }
    }

    public static List<SoundEvent> getRegisteredSounds() {
        return Collections.unmodifiableList(REGISTERED_SOUNDS);
    }

    private static SoundEvent create(String name) {
        SoundEvent soundEvent = new SoundEvent(GregoriusDrugworksUtil.makeName(name));
        soundEvent.setRegistryName(GregoriusDrugworksUtil.makeName(name));
        REGISTERED_SOUNDS.add(soundEvent);
        return soundEvent;
    }
}