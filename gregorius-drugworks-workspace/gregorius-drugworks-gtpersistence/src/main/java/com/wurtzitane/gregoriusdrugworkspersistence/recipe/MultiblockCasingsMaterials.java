package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;

import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;
import static com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials.resLoc;

public final class MultiblockCasingsMaterials {
    private MultiblockCasingsMaterials() {}

    public static void init() {
        GregoriusDrugworksMaterials.CeramicFiber = new Material.Builder(32000, resLoc("ceramic_fiber"))
                .dust()
                .color(0xEDE9E3)
                .iconSet(DULL)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .components(GregoriusDrugworksMaterials.Alumina, 1, GregoriusDrugworksMaterials.SiliconDioxide, 2)
                .build();

        GregoriusDrugworksMaterials.HighTemperatureInsulation = new Material.Builder(32001, resLoc("high_temperature_insulation"))
                .dust()
                .color(0xC8C1B8)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(GregoriusDrugworksMaterials.CeramicFiber, 1, GregoriusDrugworksMaterials.SiliconDioxide, 1)
                .build();

        GregoriusDrugworksMaterials.Hexafluoropropylene = new Material.Builder(32002, resLoc("hexafluoropropylene"))
                .gas()
                .color(0xCFEFFF)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 3, Materials.Fluorine, 6)
                .build();

        GregoriusDrugworksMaterials.PerfluoroalkoxyPolymerSlurry = new Material.Builder(32003, resLoc("perfluoroalkoxy_polymer_slurry"))
                .liquid()
                .color(0xE6F2FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 5, Materials.Fluorine, 12, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.FluoropolymerStructuralFilm = new Material.Builder(32004, resLoc("fluoropolymer_structural_film"))
                .polymer()
                .color(0xF7FBFF)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 5, Materials.Fluorine, 12, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.FluoropolymerResidue = new Material.Builder(32005, resLoc("fluoropolymer_residue"))
                .liquid()
                .color(0xB6C6D6)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 3, Materials.Fluorine, 6)
                .build();

        GregoriusDrugworksMaterials.PhthalicAnhydride = new Material.Builder(32006, resLoc("phthalic_anhydride"))
                .dust()
                .color(0xF1E6D6)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 8, Materials.Hydrogen, 4, Materials.Oxygen, 3)
                .build();

        GregoriusDrugworksMaterials.TrimelliticAnhydride = new Material.Builder(32007, resLoc("trimellitic_anhydride"))
                .dust()
                .color(0xE8D7C6)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 9, Materials.Hydrogen, 4, Materials.Oxygen, 5)
                .build();

        GregoriusDrugworksMaterials.PyromelliticDianhydride = new Material.Builder(32008, resLoc("pyromellitic_dianhydride"))
                .dust()
                .color(0xE6D2B8)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 10, Materials.Hydrogen, 2, Materials.Oxygen, 6)
                .build();

        GregoriusDrugworksMaterials.Oxydianiline = new Material.Builder(32009, resLoc("oxydianiline"))
                .dust()
                .color(0xE9DAB8)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 12, Materials.Hydrogen, 12, Materials.Nitrogen, 2, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.MPhenylenediamine = new Material.Builder(32010, resLoc("m_phenylenediamine"))
                .dust()
                .color(0xD8C7A0)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 8, Materials.Nitrogen, 2)
                .build();

        GregoriusDrugworksMaterials.BisphenolADianhydride = new Material.Builder(32011, resLoc("bisphenol_a_dianhydride"))
                .dust()
                .color(0xD7C1A3)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 31, Materials.Hydrogen, 18, Materials.Oxygen, 8)
                .build();

        GregoriusDrugworksMaterials.Polyetherimide = new Material.Builder(32012, resLoc("polyetherimide"))
                .liquid()
                .color(0xC79B43)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 37, Materials.Hydrogen, 22, Materials.Nitrogen, 2, Materials.Oxygen, 6)
                .build();

        GregoriusDrugworksMaterials.PolyimideSolution = new Material.Builder(32013, resLoc("polyimide_solution"))
                .liquid()
                .color(0xA06A2F)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 22, Materials.Hydrogen, 10, Materials.Nitrogen, 2, Materials.Oxygen, 5)
                .build();

        GregoriusDrugworksMaterials.MicroporousPolyimideMembrane = new Material.Builder(32014, resLoc("microporous_polyimide_membrane"))
                .polymer()
                .color(0xC9D2DA)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 22, Materials.Hydrogen, 10, Materials.Nitrogen, 2, Materials.Oxygen, 5)
                .build();

        GregoriusDrugworksMaterials.Polysiloxane = new Material.Builder(32015, resLoc("polysiloxane"))
                .liquid()
                .color(0xDDE7EF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 2, Materials.Hydrogen, 6, Materials.Oxygen, 1, Materials.Silicon, 1)
                .build();

        GregoriusDrugworksMaterials.IronPentacarbonyl = new Material.Builder(32016, resLoc("iron_pentacarbonyl"))
                .liquid()
                .color(0xC9D4DC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Iron, 1, Materials.Carbon, 5, Materials.Oxygen, 5)
                .build();

        GregoriusDrugworksMaterials.AsGrownMultiwalledCarbonNanotubes = new Material.Builder(32017, resLoc("as_grown_multiwalled_carbon_nanotubes"))
                .dust()
                .color(0x141414)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 1)
                .build();

        GregoriusDrugworksMaterials.PurifiedMultiwalledCarbonNanotubes = new Material.Builder(32018, resLoc("purified_multiwalled_carbon_nanotubes"))
                .dust()
                .color(0x101010)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 1)
                .build();

        GregoriusDrugworksMaterials.MDinitrobenzene = new Material.Builder(32019, resLoc("m_dinitrobenzene"))
                .dust()
                .color(0xD9C86A)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 4, Materials.Nitrogen, 2, Materials.Oxygen, 4)
                .build();

        GregoriusDrugworksMaterials.PChloronitrobenzene = new Material.Builder(32020, resLoc("p_chloronitrobenzene"))
                .dust()
                .color(0xD8C05E)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 4, Materials.Chlorine, 1, Materials.Nitrogen, 1, Materials.Oxygen, 2)
                .build();

        GregoriusDrugworksMaterials.PNitrophenol = new Material.Builder(32021, resLoc("p_nitrophenol"))
                .dust()
                .color(0xC9B04F)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 6, Materials.Hydrogen, 5, Materials.Nitrogen, 1, Materials.Oxygen, 3)
                .build();

        GregoriusDrugworksMaterials.DinitrodiphenylEther44 = new Material.Builder(32022, resLoc("dinitrodiphenyl_ether_4_4"))
                .dust()
                .color(0xC9B36B)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 12, Materials.Hydrogen, 8, Materials.Nitrogen, 2, Materials.Oxygen, 5)
                .build();

        GregoriusDrugworksMaterials.PhenolicResin = new Material.Builder(32023, resLoc("phenolic_resin"))
                .polymer()
                .color(0x6B3C22)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 7, Materials.Hydrogen, 6, Materials.Oxygen, 1)
                .build();

        GregoriusDrugworksMaterials.CarbonMatrixComposite = new Material.Builder(32024, resLoc("carbon_matrix_composite"))
                .dust()
                .color(0x1C1C1C)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Carbon, 1, Materials.Graphite, 1)
                .build();

        GregoriusDrugworksMaterials.MoltenVolcanicGlass = new Material.Builder(32025, resLoc("molten_volcanic_glass"))
                .liquid()
                .color(0x2A202B)
                .flags(DISABLE_DECOMPOSITION)
                .components(GregoriusDrugworksMaterials.SiliconDioxide, 2, GregoriusDrugworksMaterials.Alumina, 1)
                .build();

        GregoriusDrugworksMaterials.ObsidianCeramicComposite = new Material.Builder(32026, resLoc("obsidian_ceramic_composite"))
                .dust()
                .color(0x3B303E)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(GregoriusDrugworksMaterials.SiliconDioxide, 2, GregoriusDrugworksMaterials.Alumina, 1)
                .build();

        GregoriusDrugworksMaterials.AluminosilicateGel = new Material.Builder(32027, resLoc("aluminosilicate_gel"))
                .dust()
                .color(0xC8D0C8)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Aluminium, 1, Materials.Silicon, 1, Materials.Oxygen, 4, Materials.Hydrogen, 2)
                .build();

        GregoriusDrugworksMaterials.ZeoliteCatalystMatrix = new Material.Builder(32028, resLoc("zeolite_catalyst_matrix"))
                .dust()
                .color(0xB9C7B7)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Materials.Sodium, 1, Materials.Aluminium, 1, Materials.Silicon, 1, Materials.Oxygen, 4, Materials.Hydrogen, 2)
                .build();

        GregoriusDrugworksMaterials.CatalyticCrackingMatrix = new Material.Builder(32029, resLoc("catalytic_cracking_matrix"))
                .dust()
                .color(0x879A86)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(GregoriusDrugworksMaterials.ZeoliteCatalystMatrix, 1, GregoriusDrugworksMaterials.VanadiumPentoxide, 1, GregoriusDrugworksMaterials.Alumina, 1)
                .build();
    }
}
