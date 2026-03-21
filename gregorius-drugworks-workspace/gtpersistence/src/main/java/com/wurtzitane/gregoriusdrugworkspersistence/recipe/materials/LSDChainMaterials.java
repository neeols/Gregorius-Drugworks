package com.wurtzitane.gregoriusdrugworkspersistence.recipe.materials;

import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;

import static com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials.resLoc;
import static gregtech.api.unification.material.info.MaterialFlags.DISABLE_DECOMPOSITION;
import static gregtech.api.unification.material.info.MaterialFlags.GENERATE_PLATE;
import static gregtech.api.unification.material.info.MaterialIconSet.BRIGHT;
import static gregtech.api.unification.material.info.MaterialIconSet.DULL;
import static gregtech.api.unification.material.info.MaterialIconSet.FINE;
import static gregtech.api.unification.material.info.MaterialIconSet.SHINY;

public final class LSDChainMaterials {
    private LSDChainMaterials() {
    }

    public static void init() {
        GregoriusDrugworksMaterials.LSD = new Material.Builder(32243, resLoc("lsd"))
                .liquid().dust()
                .color(0xF2E1F3).iconSet(BRIGHT)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 20, Materials.Hydrogen, 25, Materials.Nitrogen, 3, Materials.Oxygen, 1)
                .build().setFormula("C20H25N3O", true);

        GregoriusDrugworksMaterials.AcidifiedSaltWater = new Material.Builder(32244, resLoc("acidified_salt_water"))
                .liquid()
                .color(0x516A8D).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.SaltWater, 1, Materials.SulfuricAcid, 1)
                .build().setFormula("(NaClH2O)(H2SO4)?", true);

        GregoriusDrugworksMaterials.TreatedSaltWater = new Material.Builder(32245, resLoc("treated_salt_water"))
                .liquid()
                .color(0x5D7F74).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.SaltWater, 1, Materials.Sulfur, 1, Materials.Chlorine, 1)
                .build().setFormula("NaClH2OSCl?", true);

        GregoriusDrugworksMaterials.HydrobromicAcid = new Material.Builder(32247, resLoc("hydrobromic_acid"))
                .liquid()
                .color(0xD0D6D8).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Hydrogen, 1, Materials.Bromine, 1)
                .build().setFormula("HBr", true);

        GregoriusDrugworksMaterials.ParaNitrotoluene = new Material.Builder(32248, resLoc("p_nitrotoluene"))
                .liquid()
                .color(0xE8D14A).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 7, Materials.Hydrogen, 7, Materials.Nitrogen, 1, Materials.Oxygen, 2)
                .build().setFormula("C6H4(CH3)(NO2)", true);

        GregoriusDrugworksMaterials.Material2Bromo4Nitrotoluene = new Material.Builder(32249, resLoc("2_bromo_4_nitrotoluene"))
                .dust()
                .color(0xC39A2D).iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 7, Materials.Hydrogen, 6, Materials.Bromine, 1, Materials.Nitrogen, 1, Materials.Oxygen, 2)
                .build().setFormula("C6H3Br(CH3)(NO2)", true);

        GregoriusDrugworksMaterials.Material2Bromo3Nitrotoluene = new Material.Builder(32250, resLoc("2_bromo_3_nitrotoluene"))
                .dust()
                .color(0x9F7C28).iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 7, Materials.Hydrogen, 6, Materials.Bromine, 1, Materials.Nitrogen, 1, Materials.Oxygen, 2)
                .build().setFormula("C6H3Br(CH3)(NO2)", true);

        GregoriusDrugworksMaterials.Material3Bromo4Methylaniline = new Material.Builder(32251, resLoc("3_bromo_4_methylaniline"))
                .dust()
                .color(0xB18D54).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 7, Materials.Hydrogen, 8, Materials.Bromine, 1, Materials.Nitrogen, 1)
                .build().setFormula("C6H3Br(CH3)(NH2)", true);

        GregoriusDrugworksMaterials.Material3Bromo4MethylbenzenediazoniumChloride = new Material.Builder(32252, resLoc("3_bromo_4_methylbenzenediazonium_chloride"))
                .dust()
                .color(0xD0A567).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 7, Materials.Hydrogen, 6, Materials.Bromine, 1, Materials.Nitrogen, 2, Materials.Chlorine, 1)
                .build().setFormula("C6H3Br(CH3)N2Cl", true);

        GregoriusDrugworksMaterials.Material2Bromotoluene = new Material.Builder(32253, resLoc("2_bromotoluene"))
                .liquid()
                .color(0xA46439).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 7, Materials.Hydrogen, 7, Materials.Bromine, 1)
                .build().setFormula("C6H4Br(CH3)", true);

        GregoriusDrugworksMaterials.MixedBromonitrotolueneIsomers = new Material.Builder(32254, resLoc("mixed_bromonitrotoluene_isomers"))
                .liquid()
                .color(0x8B6A2A).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(GregoriusDrugworksMaterials.Material2Bromo4Nitrotoluene, 3, GregoriusDrugworksMaterials.Material2Bromo3Nitrotoluene, 1)
                .build().setFormula("(C6H3Br(CH3)(NO2))4", true);

        GregoriusDrugworksMaterials.NickelOxide = new Material.Builder(32255, resLoc("nickel_oxide"))
                .dust()
                .color(0x5A8C5D).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Nickel, 1, Materials.Oxygen, 1)
                .build().setFormula("NiO", true);

        GregoriusDrugworksMaterials.MaterialH2SO4TreatedCarbon = new Material.Builder(32256, resLoc("sulfuric_acid_treated_carbon"))
                .dust()
                .color(0x3A342C).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 1, Materials.SulfuricAcid, 1)
                .build().setFormula("C(H2SO4)?", true);

        GregoriusDrugworksMaterials.MaleicAnhydride = new Material.Builder(32257, resLoc("maleic_anhydride"))
                .liquid().dust()
                .color(0xE8E2D1).iconSet(FINE)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 2, Materials.Oxygen, 3)
                .build().setFormula("C2H2(CO)2O", true);

        GregoriusDrugworksMaterials.MaleicAcid = new Material.Builder(32258, resLoc("maleic_acid"))
                .dust()
                .color(0xF0F0E2).iconSet(FINE)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 4, Materials.Oxygen, 4)
                .build().setFormula("HO2CCH=CHCO2H", true);

        GregoriusDrugworksMaterials.SuccinicAcid = new Material.Builder(32259, resLoc("succinic_acid"))
                .dust()
                .color(0xD9E5DE).iconSet(FINE)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 6, Materials.Oxygen, 4)
                .build().setFormula("HO2C(CH2)2CO2H", true);

        GregoriusDrugworksMaterials.AluminiumNitrate = new Material.Builder(32260, resLoc("aluminium_nitrate"))
                .dust()
                .color(0xDADFE8).iconSet(BRIGHT)
                .components(Materials.Aluminium, 1, Materials.Nitrogen, 3, Materials.Oxygen, 9)
                .build().setFormula("Al(NO3)3", true);

        GregoriusDrugworksMaterials.Pyrolidine = new Material.Builder(32261, resLoc("pyrolidine"))
                .liquid()
                .color(0xB6C8C8).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 9, Materials.Nitrogen, 1)
                .build().setFormula("(CH2)4NH", true);

        GregoriusDrugworksMaterials.DimethylformamideDimethylAcetal = new Material.Builder(32262, resLoc("dimethylformamide_dimethyl_acetal"))
                .liquid()
                .color(0xC5DBE4).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 5, Materials.Hydrogen, 13, Materials.Nitrogen, 1, Materials.Oxygen, 2)
                .build().setFormula("(CH3O)2CHN(CH3)2", true);

        GregoriusDrugworksMaterials.PyrolidineMixture = new Material.Builder(32263, resLoc("pyrolidine_mixture"))
                .liquid()
                .color(0x7A5A39).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(GregoriusDrugworksMaterials.Material2Bromo3Nitrotoluene, 1, GregoriusDrugworksMaterials.Pyrolidine, 1,
                        GregoriusDrugworksMaterials.DimethylformamideDimethylAcetal, 1)
                .build().setFormula("C15H21BrN2O2?", true);

        GregoriusDrugworksMaterials.DiisopropylEther = new Material.Builder(32266, resLoc("diisopropyl_ether"))
                .liquid()
                .color(0xE3F1FF).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 14, Materials.Oxygen, 1)
                .build().setFormula("[(CH3)2CH]2O", true);

        GregoriusDrugworksMaterials.SolvatedPyrolidineMixture = new Material.Builder(32264, resLoc("solvated_pyrolidine_mixture"))
                .liquid()
                .color(0x91806B).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .build().setFormula("C15H21BrN2O2(C6H14O)3?", true);

        GregoriusDrugworksMaterials.Isopropyl = new Material.Builder(32265, resLoc("isopropyl_alcohol"))
                .liquid()
                .color(0xD9FFFF).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 3, Materials.Hydrogen, 8, Materials.Oxygen, 1)
                .build().setFormula("(CH3)2CHOH", true);

        GregoriusDrugworksMaterials.NNDimethyl22Bromo6NitrophenylEthenylamine = new Material.Builder(32267, resLoc("n_n_dimethyl_2_2_bromo_6_nitrophenyl_ethenylamine"))
                .dust()
                .color(0x7B5331).iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 10, Materials.Hydrogen, 11, Materials.Bromine, 1, Materials.Nitrogen, 2, Materials.Oxygen, 2)
                .build().setFormula("C10H11BrN2O2", true);

        GregoriusDrugworksMaterials.TitaniumTrichloride = new Material.Builder(32268, resLoc("titanium_trichloride"))
                .dust()
                .color(0x8A63C7).iconSet(BRIGHT)
                .components(Materials.Titanium, 1, Materials.Chlorine, 3)
                .build().setFormula("TiCl3", true);

        GregoriusDrugworksMaterials.AmmoniumAcetate = new Material.Builder(32269, resLoc("ammonium_acetate"))
                .dust()
                .color(0xF6F2E8).iconSet(FINE)
                .components(Materials.Carbon, 2, Materials.Hydrogen, 7, Materials.Nitrogen, 1, Materials.Oxygen, 2)
                .build().setFormula("NH4C2H3O2", true);

        GregoriusDrugworksMaterials.AmmoniumCarbonate = new Material.Builder(32270, resLoc("ammonium_carbonate"))
                .dust()
                .color(0xEDECE3).iconSet(FINE)
                .components(Materials.Carbon, 1, Materials.Hydrogen, 8, Materials.Nitrogen, 2, Materials.Oxygen, 3)
                .build().setFormula("(NH4)2CO3", true);

        GregoriusDrugworksMaterials.TiCl3AmmoniumAcetateMixture = new Material.Builder(32271, resLoc("titanium_trichloride_ammonium_acetate_mixture"))
                .dust()
                .color(0x9A82BB).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(GregoriusDrugworksMaterials.TitaniumTrichloride, 1, GregoriusDrugworksMaterials.AmmoniumAcetate, 1)
                .build().setFormula("TiCl3(NH4C2H3O2)", true);

        GregoriusDrugworksMaterials.MagnesiumSulfate = new Material.Builder(32272, resLoc("magnesium_sulfate"))
                .dust()
                .color(0xE2E9F1).iconSet(FINE)
                .components(Materials.Magnesium, 1, Materials.Sulfur, 1, Materials.Oxygen, 4)
                .build().setFormula("MgSO4", true);



        GregoriusDrugworksMaterials.LSDOrganicMixture = new Material.Builder(32273, resLoc("lsd_organic_mixture"))
                .dust()
                .color(0x74605B).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(GregoriusDrugworksMaterials.TiCl3AmmoniumAcetateMixture, 1,
                        GregoriusDrugworksMaterials.NNDimethyl22Bromo6NitrophenylEthenylamine, 1, Materials.Methanol, 1)
                .build().setFormula("C10H11BrN2O2TiCl3?", true);

        GregoriusDrugworksMaterials.LSDCentrifugedOrganicMixture = new Material.Builder(32274, resLoc("lsd_centrifuged_organic_mixture"))
                .dust()
                .color(0x6A5D50).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(GregoriusDrugworksMaterials.LSDOrganicMixture, 1)
                .build().setFormula("C10H11BrN2O2TiCl3?", true);

        GregoriusDrugworksMaterials.LSDCombinedOrganicExtract = new Material.Builder(32275, resLoc("lsd_combined_organic_extract"))
                .dust()
                .color(0x7E6B54).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(GregoriusDrugworksMaterials.LSDCentrifugedOrganicMixture, 1, GregoriusDrugworksMaterials.DiisopropylEther, 1)
                .build().setFormula("C10H11BrN2O2(C6H14O)?", true);

        GregoriusDrugworksMaterials.Material4Bromoindole = new Material.Builder(32277, resLoc("4_bromoindole"))
                .liquid().dust()
                .color(0x6A4F2B).iconSet(SHINY)
                .components(Materials.Carbon, 8, Materials.Hydrogen, 6, Materials.Bromine, 1, Materials.Nitrogen, 1)
                .build().setFormula("C8H6BrN", true);

        GregoriusDrugworksMaterials.Material4BromoindoleContainingOil = new Material.Builder(32276, resLoc("4_bromoindole_containing_oil"))
                .liquid()
                .color(0x473726).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(GregoriusDrugworksMaterials.Material4Bromoindole, 1, GregoriusDrugworksMaterials.DiisopropylEther, 1)
                .build().setFormula("C8H6BrN(C6H14O)?", true);

        GregoriusDrugworksMaterials.ChromatographyPurifyingMixture = new Material.Builder(32278, resLoc("chromatography_purifying_mixture"))
                .liquid()
                .color(0xCED0B5).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .components(GregoriusDrugworksMaterials.Dichloromethane, 1, GregoriusDrugworksMaterials.Hexane, 1)
                .build().setFormula("CH2Cl2C6H14?", true);

        GregoriusDrugworksMaterials.LysergicAcid = new Material.Builder(32279, resLoc("lysergic_acid"))
                .dust()
                .color(0xD8D3C7).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 16, Materials.Hydrogen, 16, Materials.Nitrogen, 2, Materials.Oxygen, 2)
                .build().setFormula("C16H16N2O2", true);

        GregoriusDrugworksMaterials.Diethylamine = new Material.Builder(32280, resLoc("diethylamine"))
                .liquid()
                .color(0xD8F0F0).iconSet(FINE)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 11, Materials.Nitrogen, 1)
                .build().setFormula("(C2H5)2NH", true);

        GregoriusDrugworksMaterials.SyntheticLSD = new Material.Builder(32281, resLoc("synthetic_lsd"))
                .dust()
                .color(0xEEE6FB).iconSet(BRIGHT)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 20, Materials.Hydrogen, 25, Materials.Nitrogen, 3, Materials.Oxygen, 1)
                .build().setFormula("C20H25N3O", true);

        GregoriusDrugworksMaterials.CopperOxide = new Material.Builder(32282, resLoc("copper_oxide"))
                .dust()
                .color(0x3C2F2C).iconSet(DULL)
                .components(Materials.Copper, 1, Materials.Oxygen, 1)
                .build().setFormula("CuO", true);

        GregoriusDrugworksMaterials.Copper2Chloride = new Material.Builder(32283, resLoc("copper_ii_chloride"))
                .dust()
                .color(0x6B9273).iconSet(BRIGHT)
                .components(Materials.Copper, 1, Materials.Chlorine, 2)
                .build().setFormula("CuCl2", true);

        GregoriusDrugworksMaterials.Palladium2Chloride = new Material.Builder(32284, resLoc("palladium_ii_chloride"))
                .dust()
                .color(0xC6B88E).iconSet(BRIGHT)
                .components(Materials.Palladium, 1, Materials.Chlorine, 2)
                .build().setFormula("PdCl2", true);

        GregoriusDrugworksMaterials.PolymerizedAcetaldehyde = new Material.Builder(32285, resLoc("polymerized_acetaldehyde"))
                .dust()
                .color(0xE5E0D8).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .components(GregoriusDrugworksMaterials.Acetaldehyde, 3)
                .build().setFormula("(C2H4O)3", true);

        GregoriusDrugworksMaterials.CalciumCarbonate = new Material.Builder(32286, resLoc("calcium_carbonate"))
                .dust()
                .color(0xF3F2ED).iconSet(FINE)
                .components(Materials.Calcium, 1, Materials.Carbon, 1, Materials.Oxygen, 3)
                .build().setFormula("CaCO3", true);

        GregoriusDrugworksMaterials.Paraldehyde = new Material.Builder(32287, resLoc("paraldehyde"))
                .liquid()
                .color(0xD4E4E7).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 12, Materials.Oxygen, 3)
                .build().setFormula("(C2H4O)3", true);

        GregoriusDrugworksMaterials.Material5Ethyl2MethylpyridineCondensate = new Material.Builder(32288, resLoc("5_ethyl_2_methylpyridine_condensate"))
                .liquid()
                .color(0xA27753).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 8, Materials.Hydrogen, 11, Materials.Nitrogen, 1, Materials.Oxygen, 1)
                .build().setFormula("C8H11NO?", true);

        GregoriusDrugworksMaterials.Material5Ethyl2Methylpyridine = new Material.Builder(32289, resLoc("5_ethyl_2_methylpyridine"))
                .liquid()
                .color(0x91633F).iconSet(SHINY)
                .components(Materials.Carbon, 8, Materials.Hydrogen, 11, Materials.Nitrogen, 1)
                .build().setFormula("C8H11N", true);

        GregoriusDrugworksMaterials.CupricNitrate = new Material.Builder(32290, resLoc("cupric_nitrate"))
                .dust()
                .color(0x6EB8C2).iconSet(BRIGHT)
                .components(Materials.Copper, 1, Materials.Nitrogen, 2, Materials.Oxygen, 6)
                .build().setFormula("Cu(NO3)2", true);

        GregoriusDrugworksMaterials.CopperDiisocinchomeronate = new Material.Builder(32291, resLoc("copper_diisocinchomeronate"))
                .dust()
                .color(0x688291).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Copper, 1, Materials.Carbon, 14, Materials.Hydrogen, 8, Materials.Nitrogen, 2, Materials.Oxygen, 4)
                .build().setFormula("Cu(C7H4NO2)2", true);

        GregoriusDrugworksMaterials.IsocinchomeronicAcid = new Material.Builder(32292, resLoc("isocinchomeronic_acid"))
                .dust()
                .color(0xE8E6D7).iconSet(FINE)
                .components(Materials.Carbon, 7, Materials.Hydrogen, 5, Materials.Nitrogen, 1, Materials.Oxygen, 4)
                .build().setFormula("C5H3N(CO2H)2", true);

        GregoriusDrugworksMaterials.BromineVapor = new Material.Builder(32293, resLoc("bromine_vapor"))
                .gas()
                .color(0x260300)
                .components(Materials.Bromine, 2)
                .build().setFormula("MgSO4", true);

    }
}
