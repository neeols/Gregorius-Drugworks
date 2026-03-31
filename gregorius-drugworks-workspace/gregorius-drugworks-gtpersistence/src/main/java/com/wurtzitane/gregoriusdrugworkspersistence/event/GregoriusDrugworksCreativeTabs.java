package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksUnificationHelper;
import gregtech.api.recipes.FluidCellInput;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.creativetab.BaseCreativeTab;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class GregoriusDrugworksCreativeTabs {
    private static final OrePrefix[] MATERIAL_DISPLAY_PREFIXES = {
            OrePrefix.ore,
            OrePrefix.dust,
            OrePrefix.dustSmall,
            OrePrefix.dustTiny,
            OrePrefix.ingot,
            OrePrefix.nugget,
            OrePrefix.gem,
            OrePrefix.plate,
            OrePrefix.plateDouble,
            OrePrefix.plateDense,
            OrePrefix.foil,
            OrePrefix.stick,
            OrePrefix.stickLong,
            OrePrefix.bolt,
            OrePrefix.screw,
            OrePrefix.ring,
            OrePrefix.springSmall,
            OrePrefix.spring,
            OrePrefix.wireFine,
            OrePrefix.rotor,
            OrePrefix.gearSmall,
            OrePrefix.gear,
            OrePrefix.frameGt,
            OrePrefix.block
    };

    private GregoriusDrugworksCreativeTabs() {
    }

    public static CreativeTabs MAIN;
    public static CreativeTabs INDUSTRIAL;
    public static CreativeTabs MEDICAL;
    public static CreativeTabs MATERIALS;
    private static List<ItemStack> materialStacksCache;

    public static void preInit() {
        if (MAIN != null) {
            return;
        }
        materialStacksCache = null;

        MAIN = new BaseCreativeTab(Tags.MOD_ID + ".main",
                GregoriusDrugworksCreativeTabs::createMainIcon, false) {
            @Override
            public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> items) {
                Map<String, ItemStack> orderedStacks = new LinkedHashMap<>();
                addOverviewStacks(orderedStacks);
                addIndustrialStacks(orderedStacks);
                addMedicalStacks(orderedStacks);
                addRemainingRegisteredContent(orderedStacks);
                items.addAll(orderedStacks.values());
            }
        };

        INDUSTRIAL = new BaseCreativeTab(Tags.MOD_ID + ".industrial",
                GregoriusDrugworksCreativeTabs::createIndustrialIcon, false) {
            @Override
            public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> items) {
                Map<String, ItemStack> orderedStacks = new LinkedHashMap<>();
                addIndustrialStacks(orderedStacks);
                items.addAll(orderedStacks.values());
            }
        };

        MEDICAL = new BaseCreativeTab(Tags.MOD_ID + ".medical",
                GregoriusDrugworksCreativeTabs::createMedicalIcon, false) {
            @Override
            public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> items) {
                Map<String, ItemStack> orderedStacks = new LinkedHashMap<>();
                addMedicalStacks(orderedStacks);
                items.addAll(orderedStacks.values());
            }
        };

        MATERIALS = new BaseCreativeTab(Tags.MOD_ID + ".materials",
                GregoriusDrugworksCreativeTabs::createMaterialsIcon, true) {
            @Override
            public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> items) {
                for (ItemStack stack : getMaterialStacks()) {
                    items.add(stack.copy());
                }
            }
        };
    }

    private static ItemStack createMainIcon() {
        if (GregoriusDrugworksMetaItems.KAPPA_RESET_AMPOULE != null) {
            return new ItemStack(GregoriusDrugworksMetaItems.KAPPA_RESET_AMPOULE);
        }
        return createIndustrialIcon();
    }

    private static ItemStack createIndustrialIcon() {
        if (GregoriusDrugworksMetaTileEntities.CHEMICAL_PLANT != null) {
            return GregoriusDrugworksMetaTileEntities.CHEMICAL_PLANT.getStackForm();
        }
        if (GregoriusDrugworksBlocks.CARBONIZED_REACTOR_CASING != null) {
            return new ItemStack(GregoriusDrugworksBlocks.CARBONIZED_REACTOR_CASING);
        }
        if (GregoriusDrugworksItems.CARBON_NANOTUBES != null) {
            return new ItemStack(GregoriusDrugworksItems.CARBON_NANOTUBES);
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack createMedicalIcon() {
        if (GregoriusDrugworksMetaItems.NALOXONE_AUTOINJECTOR != null) {
            return new ItemStack(GregoriusDrugworksMetaItems.NALOXONE_AUTOINJECTOR);
        }
        if (GregoriusDrugworksMetaItems.KAPPA_RESET_AMPOULE != null) {
            return new ItemStack(GregoriusDrugworksMetaItems.KAPPA_RESET_AMPOULE);
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack createMaterialsIcon() {
        if (GregoriusDrugworksMaterials.SalvinorinA != null) {
            ItemStack dust = GregoriusDrugworksUnificationHelper.get(OrePrefix.dust,
                    GregoriusDrugworksMaterials.SalvinorinA);
            if (!dust.isEmpty()) {
                return dust;
            }
        }
        if (GregoriusDrugworksItems.CARBON_NANOTUBES != null) {
            return new ItemStack(GregoriusDrugworksItems.CARBON_NANOTUBES);
        }
        return ItemStack.EMPTY;
    }

    private static void addOverviewStacks(Map<String, ItemStack> orderedStacks) {
        addMachineStack(orderedStacks, GregoriusDrugworksMetaTileEntities.CHEMICAL_PLANT);
        addMachineStack(orderedStacks, GregoriusDrugworksMetaTileEntities.DISTILLATION_UNIT);
        addMachineStack(orderedStacks, GregoriusDrugworksMetaTileEntities.PYROLYSIS_CHAMBER);
        addMachineStack(orderedStacks, GregoriusDrugworksMetaTileEntities.BLOTTER_PRINTER);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T1);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.FLUOROPOLYMER_FRACTIONATION_CASING);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.THERMOCRACK_MATRIX_CASING);
        addItemStack(orderedStacks, GregoriusDrugworksItems.CARBON_NANOTUBES);
        addItemStack(orderedStacks, GregoriusDrugworksItems.HEATPROOF_ALLOY_MESH);
        addItemStack(orderedStacks, GregoriusDrugworksItems.FINE_STAINLESS_MESH);
        addItemStack(orderedStacks, GregoriusDrugworksItems.CERAMIC_FILTER);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.NALOXONE_AUTOINJECTOR);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.FLUMAZENIL_AMPOULE);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.ATROPINE_2PAM_AUTOINJECTOR);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.KAPPA_RESET_AMPOULE);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.PILL);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.SAMPLE_VAPE);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.CRYSTALMETH);
    }

    private static void addIndustrialStacks(Map<String, ItemStack> orderedStacks) {
        addMachineStack(orderedStacks, GregoriusDrugworksMetaTileEntities.CHEMICAL_PLANT);
        addMachineStack(orderedStacks, GregoriusDrugworksMetaTileEntities.DISTILLATION_UNIT);
        addMachineStack(orderedStacks, GregoriusDrugworksMetaTileEntities.PYROLYSIS_CHAMBER);
        addMachineStack(orderedStacks, GregoriusDrugworksMetaTileEntities.BLOTTER_PRINTER);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T1);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T2);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T3);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T4);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T5);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T6);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.CHEMPLANT_PIPE_CASING_T7);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T1);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T2);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T3);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T4);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T5);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T6);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.CHEMPLANT_MACHINE_CASING_T7);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.FLUOROPOLYMER_FRACTIONATION_CASING);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.POLYETHERIMIDE_THERMAL_CASING);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.MOLECULAR_MEMBRANE_CASING);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.POLYSILOXANE_VAPOR_CONTROL_CASING);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.CARBONIZED_REACTOR_CASING);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.OBSIDIAN_FORGED_THERMAL_CASING);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.THERMOCRACK_MATRIX_CASING);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.METHBLOCK);
        addBlockStack(orderedStacks, GregoriusDrugworksBlocks.COMPRESSEDMETHBLOCK);
        addItemStack(orderedStacks, GregoriusDrugworksItems.CARBON_NANOTUBES);
        addItemStack(orderedStacks, GregoriusDrugworksItems.HEATPROOF_ALLOY_MESH);
        addItemStack(orderedStacks, GregoriusDrugworksItems.FINE_STAINLESS_MESH);
        addItemStack(orderedStacks, GregoriusDrugworksItems.CERAMIC_FILTER);
        addItemStack(orderedStacks, GregoriusDrugworksItems.BLOTTER_PAPER_MOLD);
        addItemStack(orderedStacks, GregoriusDrugworksItems.BLOTTER_PAPER);
        addItemStack(orderedStacks, GregoriusDrugworksItems.SINGLE_TAB);
        addItemStack(orderedStacks, GregoriusDrugworksItems.USED_PAPER_FILTER);
        addItemStack(orderedStacks, GregoriusDrugworksItems.USED_VAPE);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.EMPTY_GLASS_AMPOULE);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.EMPTY_CAPSULE_PILL);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.MONTMORILLONITE_CLAY);
    }

    private static void addMedicalStacks(Map<String, ItemStack> orderedStacks) {
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.EMPTY_GLASS_AMPOULE);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.EMPTY_CAPSULE_PILL);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.PLUNGER);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.NEEDLE);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.PVC_GLOVE);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.SHAPE_GLOVE);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.NALOXONE_AUTOINJECTOR);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.FLUMAZENIL_AMPOULE);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.ATROPINE_2PAM_AUTOINJECTOR);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.NAC_INFUSION);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.FOMEPIZOLE_VIAL);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.SALVINORIN_A_VIAL);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.HYDROXOCOBALAMIN_KIT);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.VITAMIN_K_AMPOULE);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.PROTAMINE_VIAL);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.GLUCAGON_INJECTOR);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.DIGOXIN_FAB);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.KAPPA_RESET_AMPOULE);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.PILL);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.SAMPLE_VAPE);
        addItemStack(orderedStacks, GregoriusDrugworksMetaItems.CRYSTALMETH);
        addItemStack(orderedStacks, GregoriusDrugworksItems.BLOTTER_PAPER);
        addItemStack(orderedStacks, GregoriusDrugworksItems.SINGLE_TAB);
        addItemStack(orderedStacks, GregoriusDrugworksItems.USED_PAPER_FILTER);
        addItemStack(orderedStacks, GregoriusDrugworksItems.USED_VAPE);
    }

    private static void addRemainingRegisteredContent(Map<String, ItemStack> orderedStacks) {
        for (Item item : GregoriusDrugworksItems.getRegisteredItems()) {
            addUniqueStack(orderedStacks, new ItemStack(item));
        }
        for (Item item : GregoriusDrugworksMetaItems.getMetaItems()) {
            addUniqueStack(orderedStacks, new ItemStack(item));
        }
        for (Block block : GregoriusDrugworksBlocks.getBlocks()) {
            addUniqueStack(orderedStacks, new ItemStack(block));
        }
        addMachineStack(orderedStacks, GregoriusDrugworksMetaTileEntities.CHEMICAL_PLANT);
        addMachineStack(orderedStacks, GregoriusDrugworksMetaTileEntities.DISTILLATION_UNIT);
        addMachineStack(orderedStacks, GregoriusDrugworksMetaTileEntities.PYROLYSIS_CHAMBER);
        addMachineStack(orderedStacks, GregoriusDrugworksMetaTileEntities.BLOTTER_PRINTER);
    }

    private static void addItemStack(Map<String, ItemStack> orderedStacks, Item item) {
        if (item != null) {
            addUniqueStack(orderedStacks, new ItemStack(item));
        }
    }

    private static void addBlockStack(Map<String, ItemStack> orderedStacks, Block block) {
        if (block != null) {
            addUniqueStack(orderedStacks, new ItemStack(block));
        }
    }

    private static void addMachineStack(Map<String, ItemStack> orderedStacks, MetaTileEntity machine) {
        if (machine != null) {
            addUniqueStack(orderedStacks, machine.getStackForm());
        }
    }

    private static List<ItemStack> getMaterialStacks() {
        if (materialStacksCache != null) {
            return materialStacksCache;
        }
        List<Material> materials = getGregoriusDrugworksMaterials();
        if (materials.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        Map<String, ItemStack> orderedStacks = new LinkedHashMap<>();
        addOreStacks(orderedStacks, materials);
        addDustStacks(orderedStacks, materials);
        addFluidOnlyStacks(orderedStacks, materials);

        materialStacksCache = new ArrayList<>(orderedStacks.values());
        return materialStacksCache;
    }

    private static List<Material> getGregoriusDrugworksMaterials() {
        List<Material> materials = new ArrayList<>();
        for (Field field : GregoriusDrugworksMaterials.class.getFields()) {
            if (field.getType() != Material.class) {
                continue;
            }
            try {
                Object value = field.get(null);
                if (!(value instanceof Material)) {
                    continue;
                }
                Material material = (Material) value;
                if (Tags.MOD_ID.equals(material.getModid())) {
                    materials.add(material);
                }
            } catch (IllegalAccessException ignored) {
            }
        }
        materials.sort(Comparator.comparingInt(Material::getId));
        return materials;
    }

    private static void addOreStacks(Map<String, ItemStack> orderedStacks, List<Material> materials) {
        for (Material material : materials) {
            if (!material.hasProperty(PropertyKey.ORE)) {
                continue;
            }
            addGeneratedStacks(orderedStacks, material);
        }
    }

    private static void addDustStacks(Map<String, ItemStack> orderedStacks, List<Material> materials) {
        for (Material material : materials) {
            if (material.hasProperty(PropertyKey.ORE) || !material.hasProperty(PropertyKey.DUST)) {
                continue;
            }
            addGeneratedStacks(orderedStacks, material);
        }
    }

    private static void addFluidOnlyStacks(Map<String, ItemStack> orderedStacks, List<Material> materials) {
        for (Material material : materials) {
            if (material.hasProperty(PropertyKey.ORE) || material.hasProperty(PropertyKey.DUST) || !material.hasFluid()) {
                continue;
            }
            try {
                addUniqueStack(orderedStacks, FluidCellInput.getFilledCell(material.getFluid()));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    private static void addUniqueStack(Map<String, ItemStack> orderedStacks, ItemStack stack) {
        if (stack == null || stack.isEmpty() || stack.getItem().getRegistryName() == null) {
            return;
        }
        String key = stack.getItem().getRegistryName() + "|" + stack.getMetadata() + "|" +
                (stack.hasTagCompound() ? stack.getTagCompound().toString() : "");
        orderedStacks.putIfAbsent(key, stack.copy());
    }

    private static void addGeneratedStacks(Map<String, ItemStack> orderedStacks, Material material) {
        for (OrePrefix prefix : MATERIAL_DISPLAY_PREFIXES) {
            addUniqueStack(orderedStacks, GregoriusDrugworksUnificationHelper.get(prefix, material));
        }
    }
}
