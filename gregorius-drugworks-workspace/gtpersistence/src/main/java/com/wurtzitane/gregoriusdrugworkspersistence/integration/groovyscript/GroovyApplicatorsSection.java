package com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript;

import com.wurtzitane.gregoriusdrugworkspersistence.medical.GregoriusDrugworksApplicatorPayloads;
import com.wurtzitane.gregoriusdrugworkspersistence.medical.GregoriusDrugworksMedicalApplicators;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public final class GroovyApplicatorsSection extends AbstractGroovySection {

    public GroovyApplicatorsSection() {
        super("applicators", "applicator");
    }

    public boolean load(ItemStack applicatorStack, String payloadId) {
        return load(applicatorStack, payloadId, 0, null);
    }

    public boolean load(ItemStack applicatorStack, String payloadId, int chargesOverride) {
        return load(applicatorStack, payloadId, chargesOverride, null);
    }

    public boolean load(
            ItemStack applicatorStack,
            String payloadId,
            int chargesOverride,
            @Nullable NBTTagCompound payloadData
    ) {
        return GregoriusDrugworksGroovyScriptBridge.loadApplicatorPayload(applicatorStack, payloadId, chargesOverride, payloadData);
    }

    public ItemStack createLoaded(String payloadId) {
        return createLoaded(payloadId, 0, null);
    }

    public ItemStack createLoaded(String payloadId, int chargesOverride) {
        return createLoaded(payloadId, chargesOverride, null);
    }

    public ItemStack createLoaded(String payloadId, int chargesOverride, @Nullable NBTTagCompound payloadData) {
        return GregoriusDrugworksApplicatorPayloads.createLoadedApplicatorStack(
                GregoriusDrugworksMedicalApplicators.MEDICAL_APPLICATOR,
                payloadId,
                chargesOverride,
                payloadData
        );
    }

    public void clear(ItemStack applicatorStack) {
        GregoriusDrugworksApplicatorPayloads.clearPayload(applicatorStack);
    }

    public boolean hasPayload(ItemStack applicatorStack) {
        return GregoriusDrugworksApplicatorPayloads.hasPayload(applicatorStack);
    }

    public String describe(ItemStack applicatorStack) {
        return GregoriusDrugworksApplicatorPayloads.describeResolved(applicatorStack);
    }
}
