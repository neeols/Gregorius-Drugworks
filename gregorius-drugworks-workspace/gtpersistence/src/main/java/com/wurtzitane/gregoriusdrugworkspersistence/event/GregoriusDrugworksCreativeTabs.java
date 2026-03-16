package com.wurtzitane.gregoriusdrugworkspersistence.event;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public final class GregoriusDrugworksCreativeTabs {

    private GregoriusDrugworksCreativeTabs() {
    }

    public static CreativeTabs MAIN;

    public static void preInit() {
        if (MAIN != null) {
            return;
        }

        MAIN = new CreativeTabs("gregoriusdrugworkspersistence.main") {
            @Nonnull
            @Override
            public ItemStack createIcon() {
                if (GregoriusDrugworksMetaItems.KAPPA_RESET_AMPOULE != null) {
                    return new ItemStack(GregoriusDrugworksMetaItems.KAPPA_RESET_AMPOULE);
                }
                if (GregoriusDrugworksItems.CARBON_NANOTUBES != null) {
                    return new ItemStack(GregoriusDrugworksItems.CARBON_NANOTUBES);
                }
                if (GregoriusDrugworksBlocks.CARBONIZED_REACTOR_CASING != null) {
                    return new ItemStack(GregoriusDrugworksBlocks.CARBONIZED_REACTOR_CASING);
                }
                return ItemStack.EMPTY;
            }
        };
    }
}