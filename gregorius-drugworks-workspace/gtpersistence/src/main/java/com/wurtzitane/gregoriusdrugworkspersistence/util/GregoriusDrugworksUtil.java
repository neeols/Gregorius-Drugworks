package com.wurtzitane.gregoriusdrugworkspersistence.util;

import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraft.util.ResourceLocation;

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

    public static ResourceLocation makeName(String name) {
        return new ResourceLocation(Tags.MOD_ID, name);
    }

    //public static ResourceLocation makeGroovyName(String name) {
    //    return new ResourceLocation(GroovyHelper.getPackId(), name);
    //}

}
