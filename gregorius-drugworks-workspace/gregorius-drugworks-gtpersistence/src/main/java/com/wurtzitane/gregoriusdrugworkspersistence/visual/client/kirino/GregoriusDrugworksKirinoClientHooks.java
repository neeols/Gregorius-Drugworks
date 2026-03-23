package com.wurtzitane.gregoriusdrugworkspersistence.visual.client.kirino;

import com.cleanroommc.kirino.KirinoCommonCore;
import com.cleanroommc.kirino.engine.render.core.pipeline.post.event.PostProcessingRegistrationEvent;
import com.cleanroommc.kirino.engine.render.core.shader.event.ShaderRegistrationEvent;
import com.wurtzitane.gregoriusdrugworkspersistence.GTAddon;
import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Method;

@SideOnly(Side.CLIENT)
public final class GregoriusDrugworksKirinoClientHooks {

    private static final String GDW_LSD_FRAGMENT_SHADER = "gregoriusdrugworkspersistence:shaders/pp_lsd_distortion.frag";
    private static boolean initialised;

    private GregoriusDrugworksKirinoClientHooks() {
    }

    public static void preInit() {
        if (initialised) {
            return;
        }
        initialised = true;

        try {
            Method registerMethod = KirinoCommonCore.KIRINO_EVENT_BUS.getClass()
                    .getDeclaredMethod("register", Class.class, Object.class, Method.class, ModContainer.class);
            registerMethod.setAccessible(true);

            ModContainer owner = Loader.instance().activeModContainer();
            if (owner == null) {
                owner = Loader.instance().getIndexedModList().get(Tags.MOD_ID);
            }
            if (owner == null) {
                owner = Loader.instance().getMinecraftModContainer();
            }

            Method onShaderRegister = GregoriusDrugworksKirinoClientHooks.class
                    .getDeclaredMethod("onShaderRegister", ShaderRegistrationEvent.class);
            registerMethod.invoke(
                    KirinoCommonCore.KIRINO_EVENT_BUS,
                    ShaderRegistrationEvent.class,
                    GregoriusDrugworksKirinoClientHooks.class,
                    onShaderRegister,
                    owner
            );

            Method onPostProcessingRegister = GregoriusDrugworksKirinoClientHooks.class
                    .getDeclaredMethod("onPostProcessingRegister", PostProcessingRegistrationEvent.class);
            registerMethod.invoke(
                    KirinoCommonCore.KIRINO_EVENT_BUS,
                    PostProcessingRegistrationEvent.class,
                    GregoriusDrugworksKirinoClientHooks.class,
                    onPostProcessingRegister,
                    owner
            );

            GTAddon.LOGGER.info("Registered Gregorius Drugworks Kirino post-processing hooks.");
        } catch (Throwable throwable) {
            GTAddon.LOGGER.warn("Failed to register Gregorius Drugworks Kirino post-processing hooks.", throwable);
        }
    }

    @SubscribeEvent
    public static void onShaderRegister(ShaderRegistrationEvent event) {
        event.register(new ResourceLocation(GDW_LSD_FRAGMENT_SHADER));
    }

    @SubscribeEvent
    public static void onPostProcessingRegister(PostProcessingRegistrationEvent event) {
        event.register(
                "GDW LSD Distortion",
                new String[] { "forge:shaders/post_processing.vert", GDW_LSD_FRAGMENT_SHADER },
                GregoriusDrugworksLsdPostProcessingPass::new
        );
    }
}
