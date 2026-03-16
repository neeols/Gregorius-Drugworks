package com.wurtzitane.gregoriusdrugworkspersistence.medical;

import com.wurtzitane.gregoriusdrugworks.common.catalog.ContentFamily;
import com.wurtzitane.gregoriusdrugworkspersistence.catalog.GregoriusDrugworksContentCatalogs;
import com.wurtzitane.gregoriusdrugworks.common.medical.ApplicatorUseProfile;
import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;

public final class GregoriusDrugworksMedicalApplicators {

    public static ItemMedicalApplicator MEDICAL_APPLICATOR;
    private static boolean bootstrapped = false;

    private GregoriusDrugworksMedicalApplicators() {
    }

    public static void preInit() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;

        GregoriusDrugworksApplicatorPayloads.preInit();

        MEDICAL_APPLICATOR = new ItemMedicalApplicator(
                "medical_applicator",
                new ApplicatorUseProfile(
                        18,
                        5,
                        6,
                        10,
                        14,
                        18
                ),
                false,
                GregoriusDrugworksUtil.makeName("applicator_start"),
                GregoriusDrugworksUtil.makeName("applicator_finish"),
                GregoriusDrugworksUtil.makeName("applicator_fail")
        );

        GregoriusDrugworksContentCatalogs.registerItem(MEDICAL_APPLICATOR, ContentFamily.APPLICATOR, true);
    }

    public static void register(IForgeRegistry<Item> registry) {
        preInit();
        registry.register(MEDICAL_APPLICATOR);
    }

    public static void registerModels() {
        preInit();
        ModelLoader.setCustomModelResourceLocation(
                MEDICAL_APPLICATOR,
                0,
                new ModelResourceLocation(MEDICAL_APPLICATOR.getRegistryName(), "inventory")
        );
    }
}