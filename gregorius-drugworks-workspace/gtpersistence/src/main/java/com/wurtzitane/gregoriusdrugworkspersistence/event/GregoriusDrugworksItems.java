package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript.GregoriusDrugworksGroovyScriptClientBridge;
import com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript.GregoriusDrugworksGroovyScriptBridge;
import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GregoriusDrugworksItems {

    private static final List<Item> REGISTERED_ITEMS = new ArrayList<>();
    private static boolean bootstrapped = false;

    public static Item HEATPROOF_ALLOY_MESH;
    public static Item FINE_STAINLESS_MESH;
    public static Item CERAMIC_FILTER;
    public static Item CARBON_NANOTUBES;
    public static Item USED_PAPER_FILTER;
    public static Item USED_VAPE;
    public static Item ASH_RESIDUE;

    private GregoriusDrugworksItems() {
    }

    public static void preInit() {
        bootstrap();
    }

    private static void bootstrap() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;
        REGISTERED_ITEMS.clear();

        HEATPROOF_ALLOY_MESH = createItem("heatproof_alloy_mesh", 64);
        FINE_STAINLESS_MESH = createItem("fine_stainless_mesh", 64);
        CERAMIC_FILTER = createItem("ceramic_filter", 64);
        CARBON_NANOTUBES = createItem("carbon_nanotubes", 64);
        USED_PAPER_FILTER = createItem("used_paper_filter", 64);
        USED_VAPE = createItem("used_vape", 1);
    }

    private static Item createItem(String name, int maxStackSize) {
        Item item = new Item();
        item.setRegistryName(GregoriusDrugworksUtil.makeName(name));
        item.setTranslationKey(Tags.MOD_ID + "." + name);
        item.setCreativeTab(GregoriusDrugworksCreativeTabs.MAIN);
        item.setMaxStackSize(maxStackSize);
        REGISTERED_ITEMS.add(item);
        return item;
    }

    public static void register(IForgeRegistry<Item> registry) {
        bootstrap();

        for (Item item : REGISTERED_ITEMS) {
            registry.register(item);
        }

        GregoriusDrugworksBlocks.registerItemBlocks(registry);
        GregoriusDrugworksMetaItems.register(registry);
        GregoriusDrugworksGroovyScriptBridge.registerItems(registry);
    }

    public static void registerModels() {
        bootstrap();

        for (Item item : REGISTERED_ITEMS) {
            if (item.getRegistryName() == null) {
                throw new IllegalStateException("Registry name was null for item model registration: " + item);
            }
            ModelLoader.setCustomModelResourceLocation(
                    item,
                    0,
                    new ModelResourceLocation(item.getRegistryName(), "inventory")
            );
        }

        GregoriusDrugworksBlocks.registerModels();
        GregoriusDrugworksMetaItems.registerModels();
        GregoriusDrugworksGroovyScriptClientBridge.registerModels();
    }

    public static List<Item> getRegisteredItems() {
        bootstrap();
        return Collections.unmodifiableList(REGISTERED_ITEMS);
    }
}
