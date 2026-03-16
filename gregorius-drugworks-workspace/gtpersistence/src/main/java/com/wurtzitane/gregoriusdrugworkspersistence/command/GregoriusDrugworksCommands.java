package com.wurtzitane.gregoriusdrugworkspersistence.command;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public final class GregoriusDrugworksCommands {

    private GregoriusDrugworksCommands() {
    }

    public static void register(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandGregoriusDrugworksDev());
    }
}