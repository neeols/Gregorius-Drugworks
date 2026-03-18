package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;

import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;
import static com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials.resLoc;

public final class SalvinorinChainMaterials {
    private SalvinorinChainMaterials() {}

    public static void init() {
        GregoriusDrugworksMaterials.HydrogenBromide = new Material.Builder(32030, resLoc("hydrogen_bromide"))
                .gas()
                .color(0xDDE6FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Hydrogen, 1, Materials.Bromine, 1)
                .build();

        GregoriusDrugworksMaterials.HydrogenChloride = new Material.Builder(32031, resLoc("hydrogen_chloride"))
                .dust()
                .gas()
                .color(0xE6FFF4)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Hydrogen, 1, Materials.Chlorine, 1)
                .build();

        GregoriusDrugworksMaterials.HydrogenFluoride = new Material.Builder(32032, resLoc("hydrogen_fluoride"))
                .gas()
                .color(0xF2FFF0)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Hydrogen, 1, Materials.Fluorine, 1)
                .build();

        GregoriusDrugworksMaterials.EthyleneOxide = new Material.Builder(32033, resLoc("ethylene_oxide"))
                .gas()
                .color(0xEAF7FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 2, Materials.Hydrogen, 4, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.Acetylene = new Material.Builder(32034, resLoc("acetylene"))
                .gas()
                .color(0xA3A683)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 2, Materials.Hydrogen, 2)
                .build();

        GregoriusDrugworksMaterials.Chloroethane = new Material.Builder(32035, resLoc("chloroethane"))
                .gas()
                .color(0xEAF6FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 2, Materials.Hydrogen, 5, Materials.Chlorine, 1)
                .build();

        GregoriusDrugworksMaterials.Material14Butanediol = new Material.Builder(32036, resLoc("1_4_butanediol"))
                .liquid()
                .color(0xE9F2FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 10, Materials.Oxygen, 2)
                .build();

        GregoriusDrugworksMaterials.Material1Hexene = new Material.Builder(32037, resLoc("1_hexene"))
                .liquid()
                .color(0xE9F2FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 12)
                .build();

        GregoriusDrugworksMaterials.Material2Ethyl2Methyl13Dioxolane = new Material.Builder(32038, resLoc("2_ethyl_2_methyl_13_dioxolane"))
                .liquid()
                .color(0xF1FAFF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 12, Materials.Oxygen, 2)
                .build();

        GregoriusDrugworksMaterials.PToluenesulfonicAcid = new Material.Builder(32039, resLoc("p_toluenesulfonic_acid"))
                .dust()
                .color(0x8A0000)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 1, Materials.Hydrogen, 3, Materials.Carbon, 6, Materials.Hydrogen, 4, Materials.Sulfur, 1, Materials.Oxygen, 3, Materials.Hydrogen, 1)
                .build();

        GregoriusDrugworksMaterials.Material2Methyl2Butene = new Material.Builder(32040, resLoc("2_methyl_2_butene"))
                .liquid()
                .color(0xF7F7FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 5, Materials.Hydrogen, 10)
                .build();

        GregoriusDrugworksMaterials.Material3Bromofuran = new Material.Builder(32041, resLoc("3_bromofuran"))
                .liquid()
                .color(0xFFE8C7)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 3, Materials.Bromine, 1, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.Material4MethoxybenzylChloride = new Material.Builder(32042, resLoc("4_methoxybenzyl_chloride"))
                .liquid()
                .color(0xFFF1D6)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 8, Materials.Hydrogen, 9, Materials.Chlorine, 1, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.Material5AqueousAcetone = new Material.Builder(32043, resLoc("5_aqueous_acetone"))
                .liquid()
                .color(0xEAF7FF)
                .build();

        GregoriusDrugworksMaterials.Acetaldehyde = new Material.Builder(32044, resLoc("acetaldehyde"))
                .liquid()
                .color(0xEFFFF9)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 2, Materials.Hydrogen, 4, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.AcidicWastewater = new Material.Builder(32045, resLoc("acidic_wastewater"))
                .liquid()
                .color(0xD9F0FF)
                .build();

        GregoriusDrugworksMaterials.Aminopyridine = new Material.Builder(32046, resLoc("aminopyridine"))
                .liquid()
                .color(0xFFF6DA)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 5, Materials.Hydrogen, 6, Materials.Nitrogen, 2)
                .build();

        GregoriusDrugworksMaterials.AmmoniumChlorideSolution = new Material.Builder(32047, resLoc("ammonium_chloride_solution"))
                .liquid()
                .color(0xEAF3FF)
                .build();

        GregoriusDrugworksMaterials.Anisole = new Material.Builder(32048, resLoc("anisole"))
                .liquid()
                .color(0xFFF2D0)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 7, Materials.Hydrogen, 8, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.Borane = new Material.Builder(32049, resLoc("borane"))
                .liquid()
                .color(0xF6FFF2)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Boron, 1, Materials.Hydrogen, 3)
                .build();

        GregoriusDrugworksMaterials.BoraneThf = new Material.Builder(32050, resLoc("borane_thf"))
                .liquid()
                .color(0xFFFFC5)
                .build();

        GregoriusDrugworksMaterials.BoricAcid = new Material.Builder(32051, resLoc("boric_acid"))
                .liquid()
                .color(0xF2FFF7)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Hydrogen, 3, Materials.Boron, 1, Materials.Oxygen, 3)
                .build();

        GregoriusDrugworksMaterials.Brine = new Material.Builder(32052, resLoc("brine"))
                .liquid()
                .color(0xCFE7FF)
                .build();

        GregoriusDrugworksMaterials.Butanol = new Material.Builder(32053, resLoc("butanol"))
                .liquid()
                .color(0xF6FFF9)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 10, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.Butanone = new Material.Builder(32054, resLoc("butanone"))
                .liquid()
                .color(0xF4FBFF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 8, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.ButylChloride = new Material.Builder(32055, resLoc("butyl_chloride"))
                .liquid()
                .color(0xFFF0E6)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 9, Materials.Chlorine, 1)
                .build();

        GregoriusDrugworksMaterials.ChloroaceticAcid = new Material.Builder(32056, resLoc("chloroacetic_acid"))
                .liquid()
                .color(0xE5FFF2)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 2, Materials.Hydrogen, 3, Materials.Chlorine, 1, Materials.Oxygen, 2)
                .build();

        GregoriusDrugworksMaterials.Chloromethylsilane = new Material.Builder(32057, resLoc("chloromethylsilane"))
                .liquid()
                .color(0xE9E9FF)
                .build();

        GregoriusDrugworksMaterials.Chlorotrimethylsilane = new Material.Builder(32058, resLoc("chlorotrimethylsilane"))
                .liquid()
                .color(0xE8F0FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 3, Materials.Hydrogen, 9, Materials.Chlorine, 1, Materials.Silicon, 1)
                .build();

        GregoriusDrugworksMaterials.CpbaToluene = new Material.Builder(32059, resLoc("cpba_toluene"))
                .liquid()
                .color(0xFFF2D9)
                .build();

        GregoriusDrugworksMaterials.WashedCpbaToluene = new Material.Builder(32060, resLoc("washed_cpba_toluene"))
                .liquid()
                .color(0x8A8274)
                .build();

        GregoriusDrugworksMaterials.CrudePyridine = new Material.Builder(32061, resLoc("crude_pyridine"))
                .liquid()
                .color(0xFFF0C2)
                .build();

        GregoriusDrugworksMaterials.CrudePyridineSolution = new Material.Builder(32062, resLoc("crude_pyridine_solution"))
                .liquid()
                .color(0xF5F1D8)
                .build();

        GregoriusDrugworksMaterials.Cyclohexylamine = new Material.Builder(32063, resLoc("cyclohexylamine"))
                .liquid()
                .color(0xFFF3D4)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 13, Materials.Nitrogen, 1)
                .build();

        GregoriusDrugworksMaterials.Diad = new Material.Builder(32064, resLoc("diad"))
                .liquid()
                .color(0xFFE9CE)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 8, Materials.Hydrogen, 16, Materials.Nitrogen, 2, Materials.Oxygen, 4)
                .build();

        GregoriusDrugworksMaterials.Dichloromethane = new Material.Builder(32065, resLoc("dichloromethane"))
                .liquid()
                .color(0xE7F4FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 1, Materials.Hydrogen, 2, Materials.Chlorine, 2)
                .build();

        GregoriusDrugworksMaterials.DiethylEther = new Material.Builder(32066, resLoc("diethyl_ether"))
                .liquid()
                .color(0xF0FBFF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 10, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.EthylAcetate = new Material.Builder(32067, resLoc("ethyl_acetate"))
                .liquid()
                .color(0xF4FFF7)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 8, Materials.Oxygen, 2)
                .build();

        GregoriusDrugworksMaterials.Dimethylformamide = new Material.Builder(32068, resLoc("dimethylformamide"))
                .liquid()
                .color(0xD6CC81)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 3, Materials.Hydrogen, 7, Materials.Nitrogen, 1, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.LithiumBromide = new Material.Builder(32069, resLoc("lithium_bromide"))
                .dust()
                .color(0xD6CC81)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Lithium, 1, Materials.Bromine, 1)
                .build();

        GregoriusDrugworksMaterials.EthylChloride = new Material.Builder(32070, resLoc("ethyl_chloride"))
                .liquid()
                .color(0xEDF6FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 2, Materials.Hydrogen, 5, Materials.Chlorine, 1)
                .build();

        GregoriusDrugworksMaterials.EthylIodoacetate = new Material.Builder(32071, resLoc("ethyl_iodoacetate"))
                .liquid()
                .color(0xFFE7D0)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 7, Materials.Iodine, 1, Materials.Oxygen, 2)
                .build();

        GregoriusDrugworksMaterials.EthyleneGlycol = new Material.Builder(32072, resLoc("ethylene_glycol"))
                .liquid()
                .color(0xEAF7FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 2, Materials.Hydrogen, 6, Materials.Oxygen, 2)
                .build();

        GregoriusDrugworksMaterials.Furan = new Material.Builder(32073, resLoc("furan"))
                .liquid()
                .color(0xFFF0CC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 4, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.Furfural = new Material.Builder(32074, resLoc("furfural"))
                .liquid()
                .color(0xD18A3A)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 5, Materials.Hydrogen, 4, Materials.Oxygen, 2)
                .build();

        GregoriusDrugworksMaterials.HemicelluloseSlurry = new Material.Builder(32075, resLoc("hemicellulose_slurry"))
                .liquid()
                .color(0xC8A66B)
                .build();

        GregoriusDrugworksMaterials.Hexane = new Material.Builder(32076, resLoc("hexane"))
                .liquid()
                .color(0xF2F7FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 14)
                .build();

        GregoriusDrugworksMaterials.Hmds = new Material.Builder(32077, resLoc("hmds"))
                .liquid()
                .color(0xE8F0FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 19, Materials.Nitrogen, 1, Materials.Silicon, 2)
                .build();

        GregoriusDrugworksMaterials.IodoaceticAcid = new Material.Builder(32078, resLoc("iodoacetic_acid"))
                .liquid()
                .color(0xFFEAD6)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 2, Materials.Hydrogen, 3, Materials.Iodine, 1, Materials.Oxygen, 2)
                .build();

        GregoriusDrugworksMaterials.Isobutane = new Material.Builder(32079, resLoc("isobutane"))
                .liquid()
                .color(0xF6F7FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 10)
                .build();

        GregoriusDrugworksMaterials.Isobutanol = new Material.Builder(32080, resLoc("isobutanol"))
                .liquid()
                .color(0xF4FFFA)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 10, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.Isobutylene = new Material.Builder(32081, resLoc("isobutylene"))
                .liquid()
                .color(0xF3F6FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 8)
                .build();

        GregoriusDrugworksMaterials.Isopropanol = new Material.Builder(32082, resLoc("isopropanol"))
                .liquid()
                .color(0xEAF8FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 3, Materials.Hydrogen, 8, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.MchlorobenzoicAcid = new Material.Builder(32083, resLoc("mchlorobenzoic_acid"))
                .liquid()
                .color(0xE9FFD8)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 7, Materials.Hydrogen, 5, Materials.Chlorine, 1, Materials.Oxygen, 2)
                .build();

        GregoriusDrugworksMaterials.MchloroperoxybenzoicAcid = new Material.Builder(32084, resLoc("mchloroperoxybenzoic_acid"))
                .liquid()
                .color(0xB5B5B5)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 7, Materials.Hydrogen, 5, Materials.Chlorine, 1, Materials.Oxygen, 3)
                .build();

        GregoriusDrugworksMaterials.MethoxybenzylAlcohol = new Material.Builder(32085, resLoc("methoxybenzyl_alcohol"))
                .liquid()
                .color(0xFFF2D6)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 8, Materials.Hydrogen, 10, Materials.Oxygen, 2)
                .build();

        GregoriusDrugworksMaterials.Methylbromide = new Material.Builder(32086, resLoc("methylbromide"))
                .liquid()
                .color(0xEEF4FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 1, Materials.Hydrogen, 3, Materials.Bromine, 1)
                .build();

        GregoriusDrugworksMaterials.Methylchloride = new Material.Builder(32087, resLoc("methylchloride"))
                .liquid()
                .color(0xEEF8FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 1, Materials.Hydrogen, 3, Materials.Chlorine, 1)
                .build();

        GregoriusDrugworksMaterials.Nahmds = new Material.Builder(32088, resLoc("nahmds"))
                .liquid()
                .color(0xDDE8FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 18, Materials.Nitrogen, 1, Materials.Sodium, 1, Materials.Silicon, 2)
                .build();

        GregoriusDrugworksMaterials.Thf = new Material.Builder(32089, resLoc("thf"))
                .liquid()
                .color(0xEAF6FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 8, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.Nahmdsinthf = new Material.Builder(32090, resLoc("nahmdsinthf"))
                .liquid()
                .color(0xDCEAFF)
                .flags(DISABLE_DECOMPOSITION)
                .components(GregoriusDrugworksMaterials.Nahmds, 1, GregoriusDrugworksMaterials.Thf, 1)
                .build();

        GregoriusDrugworksMaterials.Pentane = new Material.Builder(32091, resLoc("pentane"))
                .liquid()
                .color(0xF4F7FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 5, Materials.Hydrogen, 12)
                .build();

        GregoriusDrugworksMaterials.PentoseSolution = new Material.Builder(32092, resLoc("pentose_solution"))
                .liquid()
                .color(0xD8B36C)
                .build();

        GregoriusDrugworksMaterials.PhosphorusTrichloride = new Material.Builder(32093, resLoc("phosphorus_trichloride"))
                .liquid()
                .color(0xFFF2CC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Phosphorus, 1, Materials.Chlorine, 3)
                .build();

        GregoriusDrugworksMaterials.Pyridine = new Material.Builder(32094, resLoc("pyridine"))
                .liquid()
                .color(0xFFF0B8)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 5, Materials.Hydrogen, 5, Materials.Nitrogen, 1)
                .build();

        GregoriusDrugworksMaterials.PyridineOrganicResidue = new Material.Builder(32095, resLoc("pyridine_organic_residue"))
                .liquid()
                .color(0xB07A2A)
                .build();

        GregoriusDrugworksMaterials.PyridineResidue = new Material.Builder(32096, resLoc("pyridine_residue"))
                .liquid()
                .color(0x8C5E1E)
                .build();

        GregoriusDrugworksMaterials.PyridineSaltWasteSlurry = new Material.Builder(32097, resLoc("pyridine_salt_waste_slurry"))
                .liquid()
                .color(0xB2B2B2)
                .build();

        GregoriusDrugworksMaterials.PyridineTreatedWater = new Material.Builder(32098, resLoc("pyridine_treated_water"))
                .liquid()
                .color(0xCFE9FF)
                .build();

        GregoriusDrugworksMaterials.PyridineWasteBrine = new Material.Builder(32099, resLoc("pyridine_waste_brine"))
                .liquid()
                .color(0x9FBAD1)
                .build();

        GregoriusDrugworksMaterials.PyridineWasteSludge = new Material.Builder(32100, resLoc("pyridine_waste_sludge"))
                .liquid()
                .color(0x5A4A36)
                .build();

        GregoriusDrugworksMaterials.PyridineWasteWater = new Material.Builder(32101, resLoc("pyridine_waste_water"))
                .liquid()
                .color(0xA8CBE6)
                .build();

        GregoriusDrugworksMaterials.PyridineWaterAzeotrope = new Material.Builder(32102, resLoc("pyridine_water_azeotrope"))
                .liquid()
                .color(0xDCEFFF)
                .build();

        GregoriusDrugworksMaterials.SalAWasteWaterE5ToE6And7 = new Material.Builder(32103, resLoc("sal_a_waste_water_e_5_to_e_6_and_7"))
                .liquid()
                .color(0x291F1E)
                .build();

        GregoriusDrugworksMaterials.SalAHeavyOrganicResidueE5ToE6And7 = new Material.Builder(32104, resLoc("sal_a_heavy_organic_residue_e_5_to_e_6_and_7"))
                .liquid()
                .color(0x3E2A18)
                .build();

        GregoriusDrugworksMaterials.SalAOrganicResidueE5ToE6And7 = new Material.Builder(32105, resLoc("sal_a_organic_residue_e_5_to_e_6_and_7"))
                .liquid()
                .color(0xB37A2B)
                .build();

        GregoriusDrugworksMaterials.SalATreatedWaterE5ToE6And7 = new Material.Builder(32106, resLoc("sal_a_treated_water_e_5_to_e_6_and_7"))
                .liquid()
                .color(0x5B3B1A)
                .build();

        GregoriusDrugworksMaterials.SalAWasteSludgeE5ToE6And7 = new Material.Builder(32107, resLoc("sal_a_waste_sludge_e_5_to_e_6_and_7"))
                .liquid()
                .color(0x2C1F15)
                .build();

        GregoriusDrugworksMaterials.SalAAnisoleWasteFluid17To172 = new Material.Builder(32108, resLoc("sal_a_anisole_waste_fluid_17_to_17_2"))
                .liquid()
                .color(0xB37A2B)
                .build();

        GregoriusDrugworksMaterials.SalACrudeOrganics102To11 = new Material.Builder(32109, resLoc("sal_a_crude_organics_10_2_to_11"))
                .liquid()
                .color(0x7A4D1B)
                .build();

        GregoriusDrugworksMaterials.SalAAcidicAcqueousLayerD6ToD8 = new Material.Builder(32110, resLoc("sal_a_acidic_acqueous_layer_d_6_to_d_8"))
                .liquid()
                .color(0xBEE6FF)
                .build();

        GregoriusDrugworksMaterials.SalAMixedSaltsSolutionD8ToB9 = new Material.Builder(32111, resLoc("sal_a_mixed_salts_solution_d_8_to_b_9"))
                .liquid()
                .color(0xBFC7D1)
                .build();

        GregoriusDrugworksMaterials.SalAMixedSaltsSolution102To11 = new Material.Builder(32112, resLoc("sal_a_mixed_salts_solution_10_2_to_11"))
                .liquid()
                .color(0xBFC7D1)
                .build();

        GregoriusDrugworksMaterials.SalACrudeOrganicResidueD8ToB9 = new Material.Builder(32113, resLoc("sal_a_crude_organic_residue_d_8_to_b_9"))
                .liquid()
                .color(0x5B3B1A)
                .build();

        GregoriusDrugworksMaterials.SalAMotherLiquorE4ToA5 = new Material.Builder(32114, resLoc("sal_a_mother_liquor_e_4_to_a_5"))
                .liquid()
                .color(0xC7B37A)
                .build();

        GregoriusDrugworksMaterials.SalAHeavyOrganicResidue14To15And16 = new Material.Builder(32115, resLoc("sal_a_heavy_organic_residue_14_to_15_and_16"))
                .liquid()
                .color(0x3E2A18)
                .build();

        GregoriusDrugworksMaterials.SalAEnonePrecursor = new Material.Builder(32116, resLoc("sal_a_enone_precursor"))
                .liquid()
                .color(0xE0B36A)
                .build();

        GregoriusDrugworksMaterials.SalAHeavyAromaticResidue = new Material.Builder(32117, resLoc("sal_a_heavy_aromatic_residue"))
                .liquid()
                .color(0x2C1F15)
                .build();

        GregoriusDrugworksMaterials.SalAHeavyAromaticResidue17To172 = new Material.Builder(32118, resLoc("sal_a_heavy_aromatic_residue_17_to_17_2"))
                .liquid()
                .color(0x2C1F15)
                .build();

        GregoriusDrugworksMaterials.SalATerpeneLikeKetoneCore = new Material.Builder(32119, resLoc("sal_a_terpene_like_ketone_core"))
                .liquid()
                .color(0xD39A4B)
                .build();

        GregoriusDrugworksMaterials.SalATriphenylLikeAromatics = new Material.Builder(32120, resLoc("sal_a_triphenyl_like_aromatics"))
                .liquid()
                .color(0x4A341E)
                .build();

        GregoriusDrugworksMaterials.SalAContaminatedWasteWaterD8ToB9 = new Material.Builder(32121, resLoc("sal_a_contaminated_waste_water_d_8_to_b_9"))
                .liquid()
                .color(0x4A341E)
                .build();

        GregoriusDrugworksMaterials.SiliconTetrachloride = new Material.Builder(32122, resLoc("silicon_tetrachloride"))
                .liquid()
                .color(0xEEF4FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Silicon, 1, Materials.Chlorine, 4)
                .build();

        GregoriusDrugworksMaterials.SodiumAluminateSolution = new Material.Builder(32123, resLoc("sodium_aluminate_solution"))
                .liquid()
                .color(0xD6E9FF)
                .build();

        GregoriusDrugworksMaterials.SodiumBromideBrine = new Material.Builder(32124, resLoc("sodium_bromide_brine"))
                .liquid()
                .color(0xBBD7FF)
                .build();

        GregoriusDrugworksMaterials.SodiumDichromateSolution = new Material.Builder(32125, resLoc("sodium_dichromate_solution"))
                .liquid()
                .color(0xFF8A1F)
                .build();

        GregoriusDrugworksMaterials.SodiumHydroxideSolution = new Material.Builder(32126, resLoc("sodium_hydroxide_solution"))
                .liquid()
                .color(0xDDF2FF)
                .build();

        GregoriusDrugworksMaterials.SodiumMethoxide = new Material.Builder(32127, resLoc("sodium_methoxide"))
                .liquid()
                .color(0xEAF7FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 1, Materials.Hydrogen, 3, Materials.Oxygen, 1, Materials.Sodium, 1)
                .build();

        GregoriusDrugworksMaterials.SodiumSilicateSolution = new Material.Builder(32128, resLoc("sodium_silicate_solution"))
                .liquid()
                .color(0xCFE8FF)
                .build();

        GregoriusDrugworksMaterials.SulfurylChloride = new Material.Builder(32129, resLoc("sulfuryl_chloride"))
                .liquid()
                .color(0xFFF0BF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Sulfur, 1, Materials.Oxygen, 2, Materials.Chlorine, 2)
                .build();

        GregoriusDrugworksMaterials.TBuliInPentane = new Material.Builder(32130, resLoc("t_buli_in_pentane"))
                .liquid()
                .color(0xF2F2FF)
                .build();

        GregoriusDrugworksMaterials.Tbscl = new Material.Builder(32131, resLoc("tbscl"))
                .liquid()
                .color(0xE2ECFF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 15, Materials.Chlorine, 1, Materials.Silicon, 1)
                .build();

        GregoriusDrugworksMaterials.Tertbutylchloride = new Material.Builder(32132, resLoc("tertbutylchloride"))
                .liquid()
                .color(0xFFF0E6)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 4, Materials.Hydrogen, 9, Materials.Chlorine, 1)
                .build();

        GregoriusDrugworksMaterials.Tertbutyldimethylsilanol = new Material.Builder(32133, resLoc("tertbutyldimethylsilanol"))
                .liquid()
                .color(0xE8F0FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 16, Materials.Oxygen, 1, Materials.Silicon, 1)
                .build();

        GregoriusDrugworksMaterials.Tescl = new Material.Builder(32134, resLoc("tescl"))
                .liquid()
                .color(0xE2ECFF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 15, Materials.Chlorine, 1, Materials.Silicon, 1)
                .build();

        GregoriusDrugworksMaterials.ThionylChloride = new Material.Builder(32135, resLoc("thionyl_chloride"))
                .liquid()
                .color(0xFFF0B3)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Sulfur, 1, Materials.Oxygen, 1, Materials.Chlorine, 2)
                .build();

        GregoriusDrugworksMaterials.Tributylamine = new Material.Builder(32136, resLoc("tributylamine"))
                .liquid()
                .color(0xFFF2C4)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 12, Materials.Hydrogen, 27, Materials.Nitrogen, 1)
                .build();

        GregoriusDrugworksMaterials.Triethylamine = new Material.Builder(32137, resLoc("triethylamine"))
                .liquid()
                .color(0xFFF2D0)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 15, Materials.Nitrogen, 1)
                .build();

        GregoriusDrugworksMaterials.Triethylsilanol = new Material.Builder(32138, resLoc("triethylsilanol"))
                .liquid()
                .color(0xE8F0FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 16, Materials.Oxygen, 1, Materials.Silicon, 1)
                .build();

        GregoriusDrugworksMaterials.ZeoliteAGel = new Material.Builder(32139, resLoc("zeolite_a_gel"))
                .liquid()
                .color(0xC7D6E6)
                .build();

        GregoriusDrugworksMaterials.SodiumBicarbonateSolution = new Material.Builder(32140, resLoc("sodium_bicarbonate_solution"))
                .liquid()
                .color(0xD8EEFF)
                .build();

        GregoriusDrugworksMaterials.StabilizedCarrierSolution = new Material.Builder(32141, resLoc("stabilized_carrier_solution"))
                .liquid()
                .color(0xBFE8FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Water, 2, Materials.Ethanol, 1, Materials.Glycerol, 1)
                .build();

        GregoriusDrugworksMaterials.StabilizedCarrierSolutionWarmed = new Material.Builder(32142, resLoc("stabilized_carrier_solution_warmed"))
                .liquid()
                .color(0x88A5B5)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Water, 2, Materials.Ethanol, 1, Materials.Glycerol, 1)
                .build();

        GregoriusDrugworksMaterials.KappaResetConcentrate = new Material.Builder(32143, resLoc("kappa_reset_concentrate"))
                .liquid()
                .color(0x9C6BFF)
                .flags(DISABLE_DECOMPOSITION)
                .components(GregoriusDrugworksMaterials.StabilizedCarrierSolution, 1, GregoriusDrugworksMaterials.ActivatedCarbon, 1, Materials.SodiumBicarbonate, 1)
                .build();

        GregoriusDrugworksMaterials.Material2DeacetoxysalvinorinA = new Material.Builder(32144, resLoc("2_deacetoxysalvinorin_a"))
                .dust()
                .color(0xE7D2B5)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.Material2EpiSalvinorinB = new Material.Builder(32145, resLoc("2_epi_salvinorin_b"))
                .dust()
                .color(0xE4CFAF)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.PyridineHydrochloride = new Material.Builder(32146, resLoc("pyridine_hydrochloride"))
                .dust()
                .color(0xDCEFFF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 5, Materials.Hydrogen, 6, Materials.Nitrogen, 1, Materials.Chlorine, 1)
                .build();

        GregoriusDrugworksMaterials.Alumina = new Material.Builder(32147, resLoc("alumina"))
                .dust()
                .color(0xF3F3F3)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Aluminium, 2, Materials.Oxygen, 3)
                .build();

        GregoriusDrugworksMaterials.PotassiumFluoride = new Material.Builder(32148, resLoc("potassium_fluoride"))
                .dust()
                .color(0xC9DEF5)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Potassium, 1, Materials.Fluorine, 1)
                .build();

        GregoriusDrugworksMaterials.ChromiumOxide = new Material.Builder(32149, resLoc("chromium_oxide"))
                .dust()
                .color(0xC9DEF5)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Chrome, 1, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.AluminiumHydroxide = new Material.Builder(32150, resLoc("aluminium_hydroxide"))
                .dust()
                .color(0xFAFAFA)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Aluminium, 1, Materials.Oxygen, 3, Materials.Hydrogen, 3)
                .build();

        GregoriusDrugworksMaterials.CalciumSulfate = new Material.Builder(32151, resLoc("calcium_sulfate"))
                .dust()
                .color(0xF4F4F4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Calcium, 1, Materials.Sulfur, 1, Materials.Oxygen, 4)
                .build();

        GregoriusDrugworksMaterials.CellulosePulp = new Material.Builder(32152, resLoc("cellulose_pulp"))
                .dust()
                .color(0xE8D9B8)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 10, Materials.Oxygen, 5)
                .build();

        GregoriusDrugworksMaterials.LigninResidue = new Material.Builder(32153, resLoc("lignin_residue"))
                .dust()
                .color(0x8F7E64)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.Material14Benzoquinone = new Material.Builder(32154, resLoc("1_4_benzoquinone"))
                .dust()
                .liquid()
                .color(0xE5FF00)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 4, Materials.Oxygen, 2)
                .build();

        GregoriusDrugworksMaterials.LithiumHydride = new Material.Builder(32155, resLoc("lithium_hydride"))
                .dust()
                .color(0x8F9184)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Lithium, 1, Materials.Hydrogen, 1)
                .build();

        GregoriusDrugworksMaterials.LithiumAluminiumHydride = new Material.Builder(32156, resLoc("lithium_aluminium_hydride"))
                .dust()
                .color(0xFFFFFF)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Lithium, 1, Materials.Aluminium, 1, Materials.Hydrogen, 4)
                .build();

        GregoriusDrugworksMaterials.Dcc = new Material.Builder(32157, resLoc("dcc"))
                .dust()
                .color(0xEFE5D0)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 13, Materials.Hydrogen, 22, Materials.Nitrogen, 2)
                .build();

        GregoriusDrugworksMaterials.Ddq = new Material.Builder(32158, resLoc("ddq"))
                .dust()
                .color(0xB98A1A)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 8, Materials.Chlorine, 2, Materials.Nitrogen, 2, Materials.Oxygen, 2)
                .build();

        GregoriusDrugworksMaterials.DdqReduced = new Material.Builder(32159, resLoc("ddq_reduced"))
                .dust()
                .color(0xC9A64A)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 8, Materials.Chlorine, 2, Materials.Nitrogen, 2, Materials.Oxygen, 2, Materials.Hydrogen, 2)
                .build();

        GregoriusDrugworksMaterials.Dicyclohexylurea = new Material.Builder(32160, resLoc("dicyclohexylurea"))
                .dust()
                .color(0xF2EFE6)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 13, Materials.Hydrogen, 24, Materials.Nitrogen, 2, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.PyridiniumChlorochromate = new Material.Builder(32161, resLoc("pyridinium_chlorochromate"))
                .dust()
                .color(0xFC6A03)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 5, Materials.Hydrogen, 5, Materials.Nitrogen, 1, Materials.Hydrogen, 1, Materials.Chlorine, 1, Materials.Chrome, 1, Materials.Oxygen, 3)
                .build();

        GregoriusDrugworksMaterials.PyridiniumDichromate = new Material.Builder(32162, resLoc("pyridinium_dichromate"))
                .dust()
                .color(0xFF4D00)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.PyridiniumSaltWaste = new Material.Builder(32163, resLoc("pyridinium_salt_waste"))
                .dust()
                .color(0x521D00)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.Dmap = new Material.Builder(32164, resLoc("dmap"))
                .dust()
                .color(0xF0E6C8)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 7, Materials.Hydrogen, 10, Materials.Nitrogen, 2)
                .build();

        GregoriusDrugworksMaterials.Humins = new Material.Builder(32165, resLoc("humins"))
                .dust()
                .color(0x3A2A1E)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.MethylTriphenylPhosphoniumBromide = new Material.Builder(32166, resLoc("methyl_triphenyl_phosphonium_bromide"))
                .dust()
                .color(0x3A2A1E)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 19, Materials.Hydrogen, 18, Materials.Bromine, 1, Materials.Phosphorus, 1)
                .build();

        GregoriusDrugworksMaterials.SalAHydrazinedicarboxylateWaste = new Material.Builder(32167, resLoc("sal_a_hydrazinedicarboxylate_waste"))
                .dust()
                .color(0xB9B9B9)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAAlcohol102 = new Material.Builder(32168, resLoc("sal_a_alcohol_10_2"))
                .dust()
                .color(0xE8D7B9)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAAlcohol112 = new Material.Builder(32169, resLoc("sal_a_alcohol_11_2"))
                .dust()
                .color(0xE8D7B9)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAAlcohol132 = new Material.Builder(32170, resLoc("sal_a_alcohol_13_2"))
                .dust()
                .color(0xE8D7B9)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAAlcohol172 = new Material.Builder(32171, resLoc("sal_a_alcohol_17_2"))
                .dust()
                .color(0xE8D7B9)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAAlcohol5 = new Material.Builder(32172, resLoc("sal_a_alcohol_5"))
                .dust()
                .color(0xE8D7B9)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAAldehyde14 = new Material.Builder(32173, resLoc("sal_a_aldehyde_14"))
                .dust()
                .color(0xD9B37C)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAAldehydeBaseWaste113To12 = new Material.Builder(32174, resLoc("sal_a_aldehyde_base_waste_11_3_to_12"))
                .dust()
                .color(0x8C6A3A)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAReducedIntermediateSlurryB9ToD10 = new Material.Builder(32175, resLoc("sal_a_reduced_intermediate_slurry_b_9_to_d_10"))
                .dust()
                .color(0x6C5A46)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalABisaldehyde113 = new Material.Builder(32176, resLoc("sal_a_bisaldehyde_11_3"))
                .dust()
                .color(0xD4B07A)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalABisaldehyde12 = new Material.Builder(32177, resLoc("sal_a_bisaldehyde_12"))
                .dust()
                .color(0xD4B07A)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalABisolefin9 = new Material.Builder(32178, resLoc("sal_a_bisolefin_9"))
                .dust()
                .color(0xC9A35A)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalACarboxylicAcid173 = new Material.Builder(32179, resLoc("sal_a_carboxylic_acid_17_3"))
                .dust()
                .color(0xE6D6B6)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAChromiumOxidationOrganicWaste112To113 = new Material.Builder(32180, resLoc("sal_a_chromium_oxidation_organic_waste_11_2_to_11_3"))
                .dust()
                .color(0x5C3A1E)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalASilylationOrganicWasteB9ToD10 = new Material.Builder(32181, resLoc("sal_a_silylation_organic_waste_b_9_to_d_10"))
                .dust()
                .color(0x4A3524)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalASilylationOrganicWasteD10To102 = new Material.Builder(32182, resLoc("sal_a_silylation_organic_waste_d_10_to_10_2"))
                .dust()
                .color(0x4A3524)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalADiketone8 = new Material.Builder(32183, resLoc("sal_a_diketone_8"))
                .dust()
                .color(0xE0B06B)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalADiol10 = new Material.Builder(32184, resLoc("sal_a_diol_10"))
                .dust()
                .color(0xE8D7B9)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalADioxolane6 = new Material.Builder(32185, resLoc("sal_a_dioxolane_6"))
                .dust()
                .color(0xE9DCC8)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAOrganicWaste102To11 = new Material.Builder(32186, resLoc("sal_a_organic_waste_10_2_to_11"))
                .dust()
                .color(0x4A3A2C)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAOrganicWasteE5ToE6And7 = new Material.Builder(32187, resLoc("sal_a_organic_waste_e_5_to_e_6_and_7"))
                .dust()
                .color(0x4A3A2C)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAEnone4 = new Material.Builder(32188, resLoc("sal_a_enone_4"))
                .dust()
                .color(0xD9A24A)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAEnone5 = new Material.Builder(32189, resLoc("sal_a_enone_5"))
                .dust()
                .color(0xD9A24A)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAEster7 = new Material.Builder(32190, resLoc("sal_a_ester_7"))
                .dust()
                .color(0xE6C38A)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAExomethylene11 = new Material.Builder(32191, resLoc("sal_a_exomethylene_11"))
                .dust()
                .color(0xD6B37C)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAFurylAlcohol15 = new Material.Builder(32192, resLoc("sal_a_furyl_alcohol_15"))
                .dust()
                .color(0xC18B3C)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAFurylAlcohol16 = new Material.Builder(32193, resLoc("sal_a_furyl_alcohol_16"))
                .dust()
                .color(0xC18B3C)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAFurylOrganicWaste14To15And16 = new Material.Builder(32194, resLoc("sal_a_furyl_organic_waste_14_to_15_and_16"))
                .dust()
                .color(0x5A3D1E)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAHydroborationOrganicWaste11To112 = new Material.Builder(32195, resLoc("sal_a_hydroboration_organic_waste_11_to_11_2"))
                .dust()
                .color(0x5A3D1E)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAKetal13 = new Material.Builder(32196, resLoc("sal_a_ketal_13"))
                .dust()
                .color(0xE6CFA8)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalALactol17 = new Material.Builder(32197, resLoc("sal_a_lactol_17"))
                .dust()
                .color(0xE6CFA8)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAOrganoboraneIntermediate11To112 = new Material.Builder(32198, resLoc("sal_a_organoborane_intermediate_11_to_11_2"))
                .dust()
                .color(0xC9C9C9)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAPdc132To14 = new Material.Builder(32199, resLoc("sal_a_pdc_13_2_to_14"))
                .dust()
                .color(0x7A5528)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalATbs102 = new Material.Builder(32200, resLoc("sal_a_tbs_10_2"))
                .dust()
                .color(0xDDE8FF)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalATes19 = new Material.Builder(32201, resLoc("sal_a_tes_19"))
                .dust()
                .color(0xDDE8FF)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalASpiroKetoneCarboxylateIntermediate = new Material.Builder(32202, resLoc("sal_a_spiro_ketone_carboxylate_intermediate"))
                .dust()
                .color(0x3F4F47)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAWittigOrganicWasteD8ToB9 = new Material.Builder(32203, resLoc("sal_a_wittig_organic_waste_d_8_to_b_9"))
                .dust()
                .color(0x003018)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAMixedSalts102To11 = new Material.Builder(32204, resLoc("sal_a_mixed_salts_10_2_to_11"))
                .dust()
                .color(0xBD7A1C)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalAMixedSaltsD8ToB9 = new Material.Builder(32205, resLoc("sal_a_mixed_salts_d_8_to_b_9"))
                .dust()
                .color(0xBD7A1C)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.SalvinorinA = new Material.Builder(32206, resLoc("salvinorin_a"))
                .dust()
                .color(0xEADBC2)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 23, Materials.Hydrogen, 28, Materials.Oxygen, 8)
                .build();

        GregoriusDrugworksMaterials.SiliconDioxide = new Material.Builder(32207, resLoc("silicon_dioxide"))
                .dust()
                .color(0xF7F7F7)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Silicon, 1, Materials.Oxygen, 2)
                .build();

        GregoriusDrugworksMaterials.MolecularSieve4A = new Material.Builder(32208, resLoc("molecular_sieve_4_a"))
                .dust()
                .color(0x000000)
                .iconSet(DULL)
                .build();

        GregoriusDrugworksMaterials.MagnesiumOxide = new Material.Builder(32209, resLoc("magnesium_oxide"))
                .dust()
                .color(0xFFFFFF)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Magnesium, 1, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.VanadiumPentoxide = new Material.Builder(32210, resLoc("vanadium_pentoxide"))
                .dust()
                .color(0x755413)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Vanadium, 2, Materials.Oxygen, 5)
                .build();

        GregoriusDrugworksMaterials.SodiumAcetate = new Material.Builder(32211, resLoc("sodium_acetate"))
                .dust()
                .color(0xF7F7F7)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 2, Materials.Hydrogen, 3, Materials.Sodium, 1, Materials.Oxygen, 2)
                .build();

        GregoriusDrugworksMaterials.SodiumBorate = new Material.Builder(32212, resLoc("sodium_borate"))
                .dust()
                .color(0xF0F0F0)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Sodium, 2, Materials.Boron, 4, Materials.Oxygen, 7)
                .build();

        GregoriusDrugworksMaterials.SodiumBromide = new Material.Builder(32213, resLoc("sodium_bromide"))
                .dust()
                .color(0xF7F2EA)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Sodium, 1, Materials.Bromine, 1)
                .build();

        GregoriusDrugworksMaterials.SodiumChloride = new Material.Builder(32214, resLoc("sodium_chloride"))
                .dust()
                .color(0xF8F8F8)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Sodium, 1, Materials.Chlorine, 1)
                .build();

        GregoriusDrugworksMaterials.SodiumHydride = new Material.Builder(32215, resLoc("sodium_hydride"))
                .dust()
                .color(0xCFCFCF)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Sodium, 1, Materials.Hydrogen, 1)
                .build();

        GregoriusDrugworksMaterials.SodiumSulfate = new Material.Builder(32216, resLoc("sodium_sulfate"))
                .dust()
                .color(0xF4F4F4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Sodium, 2, Materials.Sulfur, 1, Materials.Oxygen, 4)
                .build();

        GregoriusDrugworksMaterials.SodiumSulfateHydrated = new Material.Builder(32217, resLoc("sodium_sulfate_hydrated"))
                .dust()
                .color(0xEAF3FF)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Sodium, 2, Materials.Sulfur, 1, Materials.Oxygen, 14, Materials.Hydrogen, 20)
                .build();

        GregoriusDrugworksMaterials.Tbacl = new Material.Builder(32218, resLoc("tbacl"))
                .dust()
                .color(0xF2F2F2)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 16, Materials.Hydrogen, 36, Materials.Chlorine, 1, Materials.Nitrogen, 1)
                .build();

        GregoriusDrugworksMaterials.Tbaf = new Material.Builder(32219, resLoc("tbaf"))
                .dust()
                .color(0xF2F2F2)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 16, Materials.Hydrogen, 36, Materials.Fluorine, 1, Materials.Nitrogen, 1)
                .build();

        GregoriusDrugworksMaterials.Tbsf = new Material.Builder(32220, resLoc("tbsf"))
                .dust()
                .color(0xDDE8FF)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 15, Materials.Fluorine, 1, Materials.Silicon, 1)
                .build();

        GregoriusDrugworksMaterials.TetrabutylammoniumChloride = new Material.Builder(32221, resLoc("tetrabutylammonium_chloride"))
                .dust()
                .color(0xF2F2F2)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 16, Materials.Hydrogen, 36, Materials.Chlorine, 1, Materials.Nitrogen, 1)
                .build();

        GregoriusDrugworksMaterials.TriethylammoniumChloride = new Material.Builder(32222, resLoc("triethylammonium_chloride"))
                .dust()
                .color(0xF2F2F2)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 16, Materials.Chlorine, 1, Materials.Nitrogen, 1)
                .build();

        GregoriusDrugworksMaterials.Triphenylphosphine = new Material.Builder(32223, resLoc("triphenylphosphine"))
                .dust()
                .color(0xF1F1F1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 18, Materials.Hydrogen, 15, Materials.Phosphorus, 1)
                .build();

        GregoriusDrugworksMaterials.TriphenylphosphineOxide = new Material.Builder(32224, resLoc("triphenylphosphine_oxide"))
                .dust()
                .color(0xF6F6F6)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 18, Materials.Hydrogen, 15, Materials.Oxygen, 1, Materials.Phosphorus, 1)
                .build();

        GregoriusDrugworksMaterials.LithiumIodide = new Material.Builder(32225, resLoc("lithium_iodide"))
                .dust()
                .color(0x800C00)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Lithium, 1, Materials.Iodine, 1)
                .build();

        GregoriusDrugworksMaterials.CalciumCarbide = new Material.Builder(32226, resLoc("calcium_carbide"))
                .dust()
                .color(0x757575)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Calcium, 1, Materials.Carbon, 2)
                .build();
    }
}
