package com.wurtzitane.gregoriusdrugworkspersistence.event;

import com.wurtzitane.gregoriusdrugworkspersistence.client.GregoriusDrugworksHeldItemLayerHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.debug.GregoriusDrugworksDebug;
import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.client.GregoriusDrugworksInhalationClientHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.medical.client.GregoriusDrugworksApplicatorClientHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.payload.PayloadCarrierTooltipHelper;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.GregoriusDrugworksPayloadPills;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.GregoriusDrugworksPillColors;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.client.GregoriusDrugworksPillClientHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.visual.client.GregoriusDrugworksVisualClientHooks;
import net.minecraft.client.Minecraft;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientProxy {

    private static final int TOOLTIP_WRAP_WIDTH = 220;

    public static void earlyPreInit() {
        GregoriusDrugworksDebug.setEnabled(true);
        GregoriusDrugworksTextures.preInit();
    }

    public static void latePreInit() {
        GregoriusDrugworksHeldItemLayerHooks.preInit();
        GregoriusDrugworksPillClientHooks.preInit();
        GregoriusDrugworksInhalationClientHooks.preInit();
        GregoriusDrugworksApplicatorClientHooks.preInit();
        GregoriusDrugworksVisualClientHooks.preInit();
    }

    public static void postInit() {
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        GregoriusDrugworksItems.registerModels();
        GregoriusDrugworksFluids.registerFluidBlockModels();
        GregoriusDrugworksMetaBlocks.registerModels();
        com.wurtzitane.gregoriusdrugworkspersistence.medical.GregoriusDrugworksMedicalApplicators.registerModels();
    }

    @SubscribeEvent
    public static void registerFluidModels(TextureStitchEvent.Pre event) {
        GregoriusDrugworksFluids.registerFluidModels(event);
    }

    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item event) {
        event.getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            if (tintIndex == 1) {
                return GregoriusDrugworksPillColors.resolveRgb(
                        GregoriusDrugworksPayloadPills.getLeftColorId(stack),
                        0xEDEDED
                );
            }
            if (tintIndex == 2) {
                return GregoriusDrugworksPillColors.resolveRgb(
                        GregoriusDrugworksPayloadPills.getRightColorId(stack),
                        0xEDEDED
                );
            }
            return 0xFFFFFF;
        }, GregoriusDrugworksMetaItems.PILL);
    }

    @SubscribeEvent
    public static void addTooltipNormal(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) {
            return;
        }

        PayloadCarrierTooltipHelper.appendTooltip(stack, event.getToolTip());

        ResourceLocation registryName = stack.getItem().getRegistryName();
        String path = registryName == null ? "" : registryName.getPath();
        MaterialStack materialStack = OreDictUnifier.getMaterial(stack);

        if (isSalvinorinChainStack(stack, path)) {
            addWrappedLocalizedTooltip(event.getToolTip(), TextFormatting.LIGHT_PURPLE,
                    "tooltip.gregoriusdrugworkspersistence.salvinorin_chain");
        }

        String roleTooltipKey = null;
        if ("fluoropolymer_fractionation_casing".equals(path) ||
                "polyetherimide_thermal_casing".equals(path) ||
                "molecular_membrane_casing".equals(path) ||
                "polysiloxane_vapor_control_casing".equals(path)) {
            roleTooltipKey = "tooltip.gregoriusdrugworkspersistence.distillation_unit_role";
        } else if ("carbonized_reactor_casing".equals(path) ||
                "obsidian_forged_thermal_casing".equals(path) ||
                "thermocrack_matrix_casing".equals(path)) {
            roleTooltipKey = "tooltip.gregoriusdrugworkspersistence.pyrolysis_chamber_role";
        }

        if (roleTooltipKey != null) {
            addWrappedLocalizedTooltip(event.getToolTip(), TextFormatting.BLUE, roleTooltipKey);
        }

        String descriptionKey = null;
        if ("fluoropolymer_fractionation_casing".equals(path)) {
            descriptionKey = "tooltip.gregoriusdrugworkspersistence.fluoropolymer_fractionation_casing.desc";
        } else if ("polyetherimide_thermal_casing".equals(path)) {
            descriptionKey = "tooltip.gregoriusdrugworkspersistence.polyetherimide_thermal_casing.desc";
        } else if ("molecular_membrane_casing".equals(path)) {
            descriptionKey = "tooltip.gregoriusdrugworkspersistence.molecular_membrane_casing.desc";
        } else if ("polysiloxane_vapor_control_casing".equals(path)) {
            descriptionKey = "tooltip.gregoriusdrugworkspersistence.polysiloxane_vapor_control_casing.desc";
        } else if ("carbonized_reactor_casing".equals(path)) {
            descriptionKey = "tooltip.gregoriusdrugworkspersistence.carbonized_reactor_casing.desc";
        } else if ("obsidian_forged_thermal_casing".equals(path)) {
            descriptionKey = "tooltip.gregoriusdrugworkspersistence.obsidian_forged_thermal_casing.desc";
        } else if ("thermocrack_matrix_casing".equals(path)) {
            descriptionKey = "tooltip.gregoriusdrugworkspersistence.thermocrack_matrix_casing.desc";
        }

        if (descriptionKey != null) {
            addWrappedLocalizedTooltip(event.getToolTip(), TextFormatting.DARK_GRAY, descriptionKey);
        }

        if (path.startsWith("chemplant_machine_casing_t")) {
            addWrappedLocalizedTooltip(event.getToolTip(), TextFormatting.BLUE,
                    "tooltip.gregoriusdrugworkspersistence.chemplant_machine_casing.desc");
        } else if (path.startsWith("chemplant_pipe_casing_t")) {
            addWrappedLocalizedTooltip(event.getToolTip(), TextFormatting.BLUE,
                    "tooltip.gregoriusdrugworkspersistence.chemplant_pipe_casing.desc");
        }

        if (materialStack != null && materialStack.material != null) {
            String materialTooltipKey = materialStack.material.getUnlocalizedName() + ".tooltip";
            if (I18n.hasKey(materialTooltipKey)) {
                addWrappedLocalizedTooltip(event.getToolTip(), TextFormatting.GRAY, materialTooltipKey);
            }
        }
    }

    private static boolean isSalvinorinChainStack(ItemStack stack, String path) {
        if (path.startsWith("sal_a_")) {
            return true;
        }
        MaterialStack materialStack = OreDictUnifier.getMaterial(stack);
        return materialStack != null && materialStack.material != null &&
                materialStack.material.getName().startsWith("sal_a_");
    }

    private static void addWrappedLocalizedTooltip(List<String> tooltip, TextFormatting color, String key) {
        if (!I18n.hasKey(key)) {
            return;
        }
        addWrappedTooltip(tooltip, color + I18n.format(key));
    }

    private static void addWrappedTooltip(List<String> tooltip, String line) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft == null || minecraft.fontRenderer == null) {
            tooltip.add(line);
            return;
        }
        List<String> wrapped = minecraft.fontRenderer.listFormattedStringToWidth(line, TOOLTIP_WRAP_WIDTH);
        if (wrapped.isEmpty()) {
            tooltip.add(line);
            return;
        }
        tooltip.addAll(wrapped);
    }
}
