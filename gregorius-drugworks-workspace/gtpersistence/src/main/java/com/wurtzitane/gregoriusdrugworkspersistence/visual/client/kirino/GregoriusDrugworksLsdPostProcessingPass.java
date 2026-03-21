package com.wurtzitane.gregoriusdrugworkspersistence.visual.client.kirino;

import com.cleanroommc.kirino.engine.render.core.camera.Camera;
import com.cleanroommc.kirino.engine.render.core.pipeline.Renderer;
import com.cleanroommc.kirino.engine.render.core.pipeline.post.subpasses.AbstractPostProcessingPass;
import com.cleanroommc.kirino.engine.render.core.pipeline.state.PipelineStateObject;
import com.cleanroommc.kirino.engine.resource.ResourceSlot;
import com.cleanroommc.kirino.gl.shader.ShaderProgram;
import com.cleanroommc.kirino.gl.vao.VAO;
import com.wurtzitane.gregoriusdrugworkspersistence.visual.client.GregoriusDrugworksVisualClientHooks;
import com.wurtzitane.gregoriusdrugworkspersistence.visual.client.KirinoTripVisualState;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL20;

public final class GregoriusDrugworksLsdPostProcessingPass extends AbstractPostProcessingPass {

    public GregoriusDrugworksLsdPostProcessingPass(
            ResourceSlot<Renderer> renderer,
            PipelineStateObject pso,
            ResourceSlot<VAO> fullscreenTriangleVao
    ) {
        super(renderer, pso, fullscreenTriangleVao);
    }

    @Override
    protected void updateShaderProgram(ShaderProgram shaderProgram, Camera camera, Object payload) {
        super.updateShaderProgram(shaderProgram, camera, payload);

        Minecraft minecraft = Minecraft.getMinecraft();
        float partialTicks = minecraft.getRenderPartialTicks();
        KirinoTripVisualState state = GregoriusDrugworksVisualClientHooks.getLsdKirinoState(partialTicks);

        setFloat(shaderProgram, "gdwAge", state == null ? 0.0F : state.getAge());
        setFloat(shaderProgram, "gdwProgress", state == null ? 0.0F : state.getProgress());
        setFloat(shaderProgram, "gdwIntensity", state == null ? 0.0F : state.getIntensity());
        setFloat(shaderProgram, "gdwPrism", state == null ? 0.0F : state.getPrism());
        setFloat(shaderProgram, "gdwTunnel", state == null ? 0.0F : state.getTunnel());
        setFloat(shaderProgram, "gdwRibbon", state == null ? 0.0F : state.getRibbon());
        setFloat(shaderProgram, "gdwWobble", state == null ? 0.0F : state.getWobble());
        setVec2(shaderProgram, "gdwResolution", minecraft.displayWidth, minecraft.displayHeight);
    }

    private static void setFloat(ShaderProgram shaderProgram, String uniformName, float value) {
        int location = GL20.glGetUniformLocation(shaderProgram.getProgramID(), uniformName);
        if (location >= 0) {
            GL20.glUniform1f(location, value);
        }
    }

    private static void setVec2(ShaderProgram shaderProgram, String uniformName, float x, float y) {
        int location = GL20.glGetUniformLocation(shaderProgram.getProgramID(), uniformName);
        if (location >= 0) {
            GL20.glUniform2f(location, x, y);
        }
    }
}
