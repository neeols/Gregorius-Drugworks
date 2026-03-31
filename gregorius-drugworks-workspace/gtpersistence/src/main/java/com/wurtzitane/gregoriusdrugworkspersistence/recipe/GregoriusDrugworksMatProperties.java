package com.wurtzitane.gregoriusdrugworkspersistence.recipe;

import gregtech.api.fluids.FluidBuilder;
import gregtech.api.fluids.store.FluidStorageKeys;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.properties.DustProperty;
import gregtech.api.unification.material.properties.FluidProperty;
import gregtech.api.unification.material.properties.PropertyKey;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static gregtech.api.unification.material.info.MaterialFlags.DISABLE_DECOMPOSITION;
import static gregtech.api.unification.material.info.MaterialFlags.GENERATE_FOIL;
import static gregtech.api.unification.material.info.MaterialFlags.GENERATE_PLATE;

public final class GregoriusDrugworksMatProperties {
    private GregoriusDrugworksMatProperties() {}

    public static void flagChanges() {
        Materials.Niobium.addFlags(GENERATE_PLATE, GENERATE_FOIL);
        disableCustomAutoDecomposition(
                GregoriusDrugworksMaterials.MaleicAnhydride,
                GregoriusDrugworksMaterials.MaleicAcid,
                GregoriusDrugworksMaterials.SuccinicAcid,
                GregoriusDrugworksMaterials.AluminiumNitrate,
                GregoriusDrugworksMaterials.TitaniumTrichloride,
                GregoriusDrugworksMaterials.AmmoniumAcetate,
                GregoriusDrugworksMaterials.AmmoniumCarbonate,
                GregoriusDrugworksMaterials.MagnesiumSulfate,
                GregoriusDrugworksMaterials.Material4Bromoindole,
                GregoriusDrugworksMaterials.Diethylamine,
                GregoriusDrugworksMaterials.CopperOxide,
                GregoriusDrugworksMaterials.Copper2Chloride,
                GregoriusDrugworksMaterials.Palladium2Chloride,
                GregoriusDrugworksMaterials.CalciumCarbonate,
                GregoriusDrugworksMaterials.Material5Ethyl2Methylpyridine,
                GregoriusDrugworksMaterials.CupricNitrate,
                GregoriusDrugworksMaterials.IsocinchomeronicAcid,
                GregoriusDrugworksMaterials.LithiumChloride,
                GregoriusDrugworksMaterials.Material5AqueousAcetone,
                GregoriusDrugworksMaterials.AcidicWastewater,
                GregoriusDrugworksMaterials.AmmoniumChlorideSolution,
                GregoriusDrugworksMaterials.BoraneThf,
                GregoriusDrugworksMaterials.Brine,
                GregoriusDrugworksMaterials.Chloromethylsilane,
                GregoriusDrugworksMaterials.CpbaToluene,
                GregoriusDrugworksMaterials.WashedCpbaToluene,
                GregoriusDrugworksMaterials.CrudePyridine,
                GregoriusDrugworksMaterials.CrudePyridineSolution,
                GregoriusDrugworksMaterials.HemicelluloseSlurry,
                GregoriusDrugworksMaterials.PentoseSolution,
                GregoriusDrugworksMaterials.PyridineOrganicResidue,
                GregoriusDrugworksMaterials.PyridineResidue,
                GregoriusDrugworksMaterials.PyridineSaltWasteSlurry,
                GregoriusDrugworksMaterials.PyridineTreatedWater,
                GregoriusDrugworksMaterials.PyridineWasteBrine,
                GregoriusDrugworksMaterials.PyridineWasteSludge,
                GregoriusDrugworksMaterials.PyridineWasteWater,
                GregoriusDrugworksMaterials.PyridineWaterAzeotrope,
                GregoriusDrugworksMaterials.SalAWasteWaterE5ToE6And7,
                GregoriusDrugworksMaterials.SalAHeavyOrganicResidueE5ToE6And7,
                GregoriusDrugworksMaterials.SalAOrganicResidueE5ToE6And7,
                GregoriusDrugworksMaterials.SalATreatedWaterE5ToE6And7,
                GregoriusDrugworksMaterials.SalAWasteSludgeE5ToE6And7,
                GregoriusDrugworksMaterials.SalAAnisoleWasteFluid17To172,
                GregoriusDrugworksMaterials.SalACrudeOrganics102To11,
                GregoriusDrugworksMaterials.SalAAcidicAcqueousLayerD6ToD8,
                GregoriusDrugworksMaterials.SalAMixedSaltsSolutionD8ToB9,
                GregoriusDrugworksMaterials.SalAMixedSaltsSolution102To11,
                GregoriusDrugworksMaterials.SalACrudeOrganicResidueD8ToB9,
                GregoriusDrugworksMaterials.SalAMotherLiquorE4ToA5,
                GregoriusDrugworksMaterials.SalAHeavyOrganicResidue14To15And16,
                GregoriusDrugworksMaterials.SalAEnonePrecursor,
                GregoriusDrugworksMaterials.SalAHeavyAromaticResidue,
                GregoriusDrugworksMaterials.SalAHeavyAromaticResidue17To172,
                GregoriusDrugworksMaterials.SalATerpeneLikeKetoneCore,
                GregoriusDrugworksMaterials.SalATriphenylLikeAromatics,
                GregoriusDrugworksMaterials.SalAContaminatedWasteWaterD8ToB9,
                GregoriusDrugworksMaterials.SodiumAluminateSolution,
                GregoriusDrugworksMaterials.SodiumBromideBrine,
                GregoriusDrugworksMaterials.SodiumDichromateSolution,
                GregoriusDrugworksMaterials.SodiumHydroxideSolution,
                GregoriusDrugworksMaterials.SodiumSilicateSolution,
                GregoriusDrugworksMaterials.TBuliInPentane,
                GregoriusDrugworksMaterials.ZeoliteAGel,
                GregoriusDrugworksMaterials.SodiumBicarbonateSolution,
                GregoriusDrugworksMaterials.Material2DeacetoxysalvinorinA,
                GregoriusDrugworksMaterials.Material2EpiSalvinorinB,
                GregoriusDrugworksMaterials.LigninResidue,
                GregoriusDrugworksMaterials.PyridiniumDichromate,
                GregoriusDrugworksMaterials.PyridiniumSaltWaste,
                GregoriusDrugworksMaterials.Humins,
                GregoriusDrugworksMaterials.SalAHydrazinedicarboxylateWaste,
                GregoriusDrugworksMaterials.SalAAlcohol102,
                GregoriusDrugworksMaterials.SalAAlcohol112,
                GregoriusDrugworksMaterials.SalAAlcohol132,
                GregoriusDrugworksMaterials.SalAAlcohol172,
                GregoriusDrugworksMaterials.SalAAlcohol5,
                GregoriusDrugworksMaterials.SalAAldehyde14,
                GregoriusDrugworksMaterials.SalAAldehydeBaseWaste113To12,
                GregoriusDrugworksMaterials.SalAReducedIntermediateSlurryB9ToD10,
                GregoriusDrugworksMaterials.SalABisaldehyde113,
                GregoriusDrugworksMaterials.SalABisaldehyde12,
                GregoriusDrugworksMaterials.SalABisolefin9,
                GregoriusDrugworksMaterials.SalACarboxylicAcid173,
                GregoriusDrugworksMaterials.SalAChromiumOxidationOrganicWaste112To113,
                GregoriusDrugworksMaterials.SalASilylationOrganicWasteB9ToD10,
                GregoriusDrugworksMaterials.SalASilylationOrganicWasteD10To102,
                GregoriusDrugworksMaterials.SalADiketone8,
                GregoriusDrugworksMaterials.SalADiol10,
                GregoriusDrugworksMaterials.SalADioxolane6,
                GregoriusDrugworksMaterials.SalAOrganicWaste102To11,
                GregoriusDrugworksMaterials.SalAOrganicWasteE5ToE6And7,
                GregoriusDrugworksMaterials.SalAEnone4,
                GregoriusDrugworksMaterials.SalAEnone5,
                GregoriusDrugworksMaterials.SalAEster7,
                GregoriusDrugworksMaterials.SalAExomethylene11,
                GregoriusDrugworksMaterials.SalAFurylAlcohol15,
                GregoriusDrugworksMaterials.SalAFurylAlcohol16,
                GregoriusDrugworksMaterials.SalAFurylOrganicWaste14To15And16,
                GregoriusDrugworksMaterials.SalAHydroborationOrganicWaste11To112,
                GregoriusDrugworksMaterials.SalAKetal13,
                GregoriusDrugworksMaterials.SalALactol17,
                GregoriusDrugworksMaterials.SalAOrganoboraneIntermediate11To112,
                GregoriusDrugworksMaterials.SalAPdc132To14,
                GregoriusDrugworksMaterials.SalATbs102,
                GregoriusDrugworksMaterials.SalATes19,
                GregoriusDrugworksMaterials.SalASpiroKetoneCarboxylateIntermediate,
                GregoriusDrugworksMaterials.SalAWittigOrganicWasteD8ToB9,
                GregoriusDrugworksMaterials.SalAMixedSalts102To11,
                GregoriusDrugworksMaterials.SalAMixedSaltsD8ToB9,
                GregoriusDrugworksMaterials.MolecularSieve4A
        );
        disableAllAddonAutoDecomposition();
    }

    public static void propertyChanges() {
        if (!Materials.Chlorine.hasProperty(PropertyKey.DUST)) {
            Materials.Chlorine.setProperty(PropertyKey.DUST, new DustProperty());
        }
        if (!Materials.Bromine.hasProperty(PropertyKey.FLUID)) {
            Materials.Bromine.setProperty(PropertyKey.FLUID,
                    new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder().temperature(332)));
        } else {
            Materials.Bromine.getProperty(PropertyKey.FLUID)
                    .enqueueRegistration(FluidStorageKeys.LIQUID, new FluidBuilder().temperature(332));
        }
        if (!Materials.SodiumHydroxide.hasProperty(PropertyKey.FLUID)) {
            Materials.SodiumHydroxide.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        } else {
            Materials.SodiumHydroxide.getProperty(PropertyKey.FLUID).enqueueRegistration(FluidStorageKeys.LIQUID, new FluidBuilder());
        }
    }

    public static void miscChanges() {}

    private static void disableCustomAutoDecomposition(Material... materials) {
        for (Material material : materials) {
            if (material != null) {
                material.addFlags(DISABLE_DECOMPOSITION);
            }
        }
    }

    private static void disableAllAddonAutoDecomposition() {
        for (Field field : GregoriusDrugworksMaterials.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || field.getType() != Material.class) {
                continue;
            }
            try {
                Material material = (Material) field.get(null);
                if (material != null) {
                    material.addFlags(DISABLE_DECOMPOSITION);
                }
            } catch (IllegalAccessException exception) {
                throw new IllegalStateException("Failed to disable decomposition for addon material field "
                        + field.getName(), exception);
            }
        }
    }
}
