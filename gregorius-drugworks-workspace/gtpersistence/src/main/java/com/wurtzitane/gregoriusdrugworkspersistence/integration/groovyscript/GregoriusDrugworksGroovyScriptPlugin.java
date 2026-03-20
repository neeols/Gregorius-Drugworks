package com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyPlugin;
import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;
import com.cleanroommc.groovyscript.compat.mods.ModPropertyContainer;
import com.cleanroommc.groovyscript.sandbox.expand.ExpansionHelper;
import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import gregtech.api.recipes.RecipeBuilder;
import net.minecraftforge.fml.common.Loader;

import java.util.Arrays;
import java.util.Collection;

public final class GregoriusDrugworksGroovyScriptPlugin implements GroovyPlugin {

    @Override
    public String getModId() {
        return Tags.MOD_ID;
    }

    @Override
    public String getContainerName() {
        return "Gregorius Drugworks Persistence";
    }

    @Override
    public boolean isLoaded() {
        return Loader.isModLoaded(Tags.MOD_ID);
    }

    @Override
    public Collection<String> getAliases() {
        return Arrays.asList("gdw", "gdwpersistence");
    }

    @Override
    public ModPropertyContainer createModPropertyContainer() {
        return new GregoriusDrugworksGroovyScriptContainer();
    }

    @Override
    public void onCompatLoaded(GroovyContainer<?> container) {
        ExpansionHelper.mixinMethod(RecipeBuilder.class, GregoriusDrugworksGroovyRecipeExpansions.class, "chancedItemInput");
        ExpansionHelper.mixinMethod(RecipeBuilder.class, GregoriusDrugworksGroovyRecipeExpansions.class, "chancedFluidInput");
        ExpansionHelper.mixinMethod(RecipeBuilder.class, GregoriusDrugworksGroovyRecipeExpansions.class, "chancedCatalystItemInput");
    }

    @Override
    public Priority getOverridePriority() {
        return Priority.NONE;
    }
}
