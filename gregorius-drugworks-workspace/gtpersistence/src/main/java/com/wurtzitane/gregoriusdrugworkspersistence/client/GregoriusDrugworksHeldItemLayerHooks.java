package com.wurtzitane.gregoriusdrugworkspersistence.client;

import com.wurtzitane.gregoriusdrugworkspersistence.inhalation.client.GregoriusDrugworksInhalationClientHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.medical.client.GregoriusDrugworksApplicatorClientHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.pill.client.GregoriusDrugworksPillClientHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public final class GregoriusDrugworksHeldItemLayerHooks {

    private static final Field LAYER_RENDERERS_FIELD = ReflectionHelper.findField(
            RenderLivingBase.class,
            "layerRenderers",
            "field_177097_h"
    );

    private static boolean initialised = false;
    private static boolean installed = false;

    private GregoriusDrugworksHeldItemLayerHooks() {
    }

    public static void preInit() {
        if (initialised) {
            return;
        }
        initialised = true;

        MinecraftForge.EVENT_BUS.register(new GregoriusDrugworksHeldItemLayerHooks());
        tryInstall();
    }

    public static boolean shouldSuppressHeldItems(EntityLivingBase entity) {
        if (!(entity instanceof EntityPlayer)) {
            return false;
        }

        int entityId = entity.getEntityId();
        return GregoriusDrugworksPillClientHooks.hasActiveSequence(entityId)
                || GregoriusDrugworksInhalationClientHooks.hasActiveSequence(entityId)
                || GregoriusDrugworksApplicatorClientHooks.hasActiveSequence(entityId);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !installed) {
            tryInstall();
        }
    }

    private static void tryInstall() {
        if (installed) {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft == null || minecraft.getRenderManager() == null) {
            return;
        }

        Map<String, RenderPlayer> skinMap = minecraft.getRenderManager().getSkinMap();
        if (skinMap == null || skinMap.isEmpty()) {
            return;
        }

        for (RenderPlayer renderPlayer : skinMap.values()) {
            replaceHeldItemLayer(renderPlayer);
        }

        installed = true;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void replaceHeldItemLayer(RenderPlayer renderPlayer) {
        try {
            List layerRenderers = (List) LAYER_RENDERERS_FIELD.get(renderPlayer);
            for (int i = 0; i < layerRenderers.size(); i++) {
                Object layer = layerRenderers.get(i);
                if (layer instanceof ConditionalHeldItemLayer) {
                    return;
                }
                if (layer instanceof LayerHeldItem) {
                    layerRenderers.set(i, new ConditionalHeldItemLayer(renderPlayer));
                    return;
                }
            }

            layerRenderers.add(new ConditionalHeldItemLayer(renderPlayer));
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException("Unable to install Gregorius held-item suppression layer", exception);
        }
    }

    private static final class ConditionalHeldItemLayer extends LayerHeldItem {

        private ConditionalHeldItemLayer(RenderLivingBase<?> livingEntityRendererIn) {
            super(livingEntityRendererIn);
        }

        @Override
        public void doRenderLayer(
                EntityLivingBase entitylivingbaseIn,
                float limbSwing,
                float limbSwingAmount,
                float partialTicks,
                float ageInTicks,
                float netHeadYaw,
                float headPitch,
                float scale
        ) {
            if (shouldSuppressHeldItems(entitylivingbaseIn)) {
                return;
            }

            super.doRenderLayer(
                    entitylivingbaseIn,
                    limbSwing,
                    limbSwingAmount,
                    partialTicks,
                    ageInTicks,
                    netHeadYaw,
                    headPitch,
                    scale
            );
        }
    }
}
