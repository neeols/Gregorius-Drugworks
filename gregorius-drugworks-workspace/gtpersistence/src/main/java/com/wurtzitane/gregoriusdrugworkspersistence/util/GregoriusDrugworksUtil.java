package com.wurtzitane.gregoriusdrugworkspersistence.util;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class GregoriusDrugworksUtil {

    public static boolean isClient() {
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }

    public static boolean isServer() {
        return FMLCommonHandler.instance().getEffectiveSide().isServer();
    }

    public static boolean isDedicatedServer() {
        return FMLCommonHandler.instance().getSide().isServer();
    }

}
