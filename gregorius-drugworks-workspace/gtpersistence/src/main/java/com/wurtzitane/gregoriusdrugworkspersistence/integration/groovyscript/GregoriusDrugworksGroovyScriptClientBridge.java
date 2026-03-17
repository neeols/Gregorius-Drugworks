package com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public final class GregoriusDrugworksGroovyScriptClientBridge {

    private GregoriusDrugworksGroovyScriptClientBridge() {
    }

    public static void registerModels() {
        for (Item item : GregoriusDrugworksGroovyScriptBridge.getScriptedItems()) {
            if (item.getRegistryName() == null) {
                throw new IllegalStateException("Registry name was null for scripted item model registration: " + item);
            }
            ModelLoader.setCustomModelResourceLocation(
                    item,
                    0,
                    new ModelResourceLocation(item.getRegistryName(), "inventory")
            );
        }
    }
}
