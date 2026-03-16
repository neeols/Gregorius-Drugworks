package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.util.GregoriusDrugworksUtil;
import net.minecraft.util.ResourceLocation;

public final class GregoriusDrugworksTextures {

    public static final String MODID = Tags.MOD_ID;

    public static final String[] CUBE_ALL_BLOCK_TEXTURES = {
            "carbonized_reactor_casing",
            "chemplant_machine_casing_t1",
            "chemplant_machine_casing_t2",
            "chemplant_machine_casing_t3",
            "chemplant_machine_casing_t4",
            "chemplant_machine_casing_t5",
            "chemplant_machine_casing_t6",
            "chemplant_machine_casing_t7",
            "fluoropolymer_fractionation_casing",
            "molecular_membrane_casing",
            "molecular_membrane_casing_active",
            "obsidian_forged_thermal_casing",
            "obsidian_forged_thermal_casing_active",
            "polyetherimide_thermal_casing",
            "polysiloxane_vapor_control_casing",
            "thermocrack_matrix_casing",
            "thermocrack_matrix_casing_active"
    };

    public static final String[] TOP_SIDE_BOTTOM_BLOCK_TEXTURES = {
            "chemplant_pipe_casing_t1",
            "chemplant_pipe_casing_t2",
            "chemplant_pipe_casing_t3",
            "chemplant_pipe_casing_t4",
            "chemplant_pipe_casing_t5",
            "chemplant_pipe_casing_t6",
            "chemplant_pipe_casing_t7"
    };

    public static final String[] ITEM_TEXTURES = {
            "heatproof_alloy_mesh",
            "fine_stainless_mesh",
            "ceramic_filter",
            "carbon_nanotubes",
            "empty_glass_ampoule",
            "naloxone_autoinjector",
            "flumazenil_ampoule",
            "atropine_2pam_autoinjector",
            "nac_infusion",
            "fomepizole_vial",
            "hydroxocobalamin_kit",
            "vitamin_k_ampoule",
            "protamine_vial",
            "glucagon_injector",
            "digoxin_fab",
            "kappa_reset_ampoule"
    };

    private GregoriusDrugworksTextures() {
    }

    public static void preInit() {
        // Deliberately lightweight.
        // This class centralises expected texture/model names so the rest of the code
        // can stay clean and future datagen / validation can use a single source of truth.
    }

    public static ResourceLocation blockTexture(String name) {
        return GregoriusDrugworksUtil.makeName("block/" + name);
    }

    public static ResourceLocation itemTexture(String name) {
        return GregoriusDrugworksUtil.makeName("item/" + name);
    }

    public static String modelBlockTexture(String name) {
        return MODID + ":block/" + name;
    }

    public static String modelItemTexture(String name) {
        return MODID + ":item/" + name;
    }
}