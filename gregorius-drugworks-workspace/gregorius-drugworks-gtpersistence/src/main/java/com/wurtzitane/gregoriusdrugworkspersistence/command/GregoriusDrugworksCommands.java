package com.wurtzitane.gregoriusdrugworkspersistence.command;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

public final class GregoriusDrugworksCommands {

    private GregoriusDrugworksCommands() {
    }

    public static void register(FMLServerStartingEvent event) {
        if (!FMLLaunchHandler.isDeobfuscatedEnvironment()) {
            return;
        }
        event.registerServerCommand(new CommandGregoriusDrugworksDev());
    }
}
