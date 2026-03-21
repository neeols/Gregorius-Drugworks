package com.wurtzitane.gregoriusdrugworkspersistence.worldgen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import com.wurtzitane.gregoriusdrugworkspersistence.recipe.GregoriusDrugworksMaterials;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialFlag;
import gregtech.api.unification.material.info.MaterialIconSet;
import gregtech.api.util.FileUtility;
import gregtech.api.worldgen.config.OreDepositDefinition;
import gregtech.api.worldgen.config.WorldGenRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Small helper for defining GDW ores in one place: the material itself and the GT-native
 * worldgen vein config it installs into the GregTech config folder.
 *
 * @author wurtzitane
 */
public final class GregoriusDrugworksOreBuilder {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final List<PendingOreVein> PENDING_ORE_VEINS = new ArrayList<>();
    private static boolean worldgenRegistered;

    private GregoriusDrugworksOreBuilder() {
    }

    public static Builder create(int id, String path) {
        return new Builder(id, path);
    }

    public static void registerPendingWorldgen() {
        if (worldgenRegistered) {
            return;
        }

        Path worldgenRoot = Loader.instance().getConfigDir().toPath()
                .resolve("gregtech")
                .resolve("worldgen");

        for (PendingOreVein pendingOreVein : PENDING_ORE_VEINS) {
            Path veinPath = worldgenRoot.resolve(FileUtility.slashToNativeSep(pendingOreVein.depositName));
            try {
                Files.createDirectories(veinPath.getParent());
                if (!Files.exists(veinPath)) {
                    Files.write(veinPath, GSON.toJson(pendingOreVein.config).getBytes(StandardCharsets.UTF_8),
                            StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
                }
            } catch (IOException exception) {
                throw new IllegalStateException(
                        "Failed to install Gregorius Drugworks ore vein definition " + pendingOreVein.depositName,
                        exception);
            }

            WorldGenRegistry.INSTANCE.addVeinDefinitions(new OreDepositDefinition(pendingOreVein.depositName));
        }

        worldgenRegistered = true;
    }

    public static final class Builder {

        private final Material.Builder materialBuilder;
        private final String materialPath;
        private LayeredVeinSpec layeredVeinSpec;
        private String formula;
        private boolean formatFormula;

        private Builder(int id, String path) {
            ResourceLocation resourceLocation = GregoriusDrugworksMaterials.resLoc(path);
            this.materialBuilder = new Material.Builder(id, resourceLocation);
            this.materialPath = extractResourcePath(resourceLocation);
        }

        public Builder dust() {
            materialBuilder.dust();
            return this;
        }

        public Builder dust(int harvestLevel) {
            materialBuilder.dust(harvestLevel);
            return this;
        }

        public Builder ore() {
            materialBuilder.ore();
            return this;
        }

        public Builder ore(int oreMultiplier, int byproductMultiplier) {
            materialBuilder.ore(oreMultiplier, byproductMultiplier);
            return this;
        }

        public Builder gem() {
            materialBuilder.gem();
            return this;
        }

        public Builder gem(int harvestLevel) {
            materialBuilder.gem(harvestLevel);
            return this;
        }

        public Builder color(int color) {
            materialBuilder.color(color);
            return this;
        }

        public Builder iconSet(MaterialIconSet iconSet) {
            materialBuilder.iconSet(iconSet);
            return this;
        }

        public Builder flags(MaterialFlag... flags) {
            materialBuilder.flags(flags);
            return this;
        }

        public Builder components(Object... components) {
            materialBuilder.components(components);
            return this;
        }

        public Builder formula(String formula, boolean withFormatting) {
            this.formula = formula;
            this.formatFormula = withFormatting;
            return this;
        }

        public Builder overworldLayeredVein(String nameKey, int weight, float density, int minHeight,
                                            int maxHeight, int minRadius, int maxRadius, Material between,
                                            Material sporadic) {
            return layeredVein("overworld", nameKey, weight, density, minHeight, maxHeight, minRadius, maxRadius,
                    between.getName(), sporadic.getName());
        }

        public Builder layeredVein(String dimensionFolder, String nameKey, int weight, float density,
                                   int minHeight, int maxHeight, int minRadius, int maxRadius, String between,
                                   String sporadic) {
            layeredVeinSpec = new LayeredVeinSpec(dimensionFolder, nameKey, weight, density, minHeight, maxHeight,
                    minRadius, maxRadius, materialPath, between, sporadic);
            return this;
        }

        public Material build() {
            Material material = materialBuilder.build();
            if (formula != null) {
                material.setFormula(formula, formatFormula);
            }
            if (layeredVeinSpec != null) {
                PENDING_ORE_VEINS.add(layeredVeinSpec.toPendingOreVein());
            }
            return material;
        }

        private String extractResourcePath(ResourceLocation resourceLocation) {
            String stringValue = resourceLocation.toString();
            int separatorIndex = stringValue.indexOf(':');
            return separatorIndex >= 0 ? stringValue.substring(separatorIndex + 1) : stringValue;
        }
    }

    private static final class LayeredVeinSpec {

        private final String dimensionFolder;
        private final String nameKey;
        private final int weight;
        private final float density;
        private final int minHeight;
        private final int maxHeight;
        private final int minRadius;
        private final int maxRadius;
        private final String oreMaterial;
        private final String between;
        private final String sporadic;

        private LayeredVeinSpec(String dimensionFolder, String nameKey, int weight, float density, int minHeight,
                                int maxHeight, int minRadius, int maxRadius, String oreMaterial, String between,
                                String sporadic) {
            this.dimensionFolder = Objects.requireNonNull(dimensionFolder, "dimensionFolder");
            this.nameKey = Objects.requireNonNull(nameKey, "nameKey");
            this.weight = weight;
            this.density = density;
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
            this.minRadius = minRadius;
            this.maxRadius = maxRadius;
            this.oreMaterial = Objects.requireNonNull(oreMaterial, "oreMaterial");
            this.between = Objects.requireNonNull(between, "between");
            this.sporadic = Objects.requireNonNull(sporadic, "sporadic");
        }

        private PendingOreVein toPendingOreVein() {
            String depositName = "vein/" + dimensionFolder + "/" + Tags.MOD_ID + "_" + oreMaterial + "_vein.json";

            JsonObject root = new JsonObject();
            root.addProperty("name", nameKey);
            root.addProperty("weight", weight);
            root.addProperty("density", density);
            root.addProperty("min_height", minHeight);
            root.addProperty("max_height", maxHeight);

            JsonObject populator = new JsonObject();
            populator.addProperty("type", "surface_rock");
            populator.addProperty("material", oreMaterial);
            root.add("vein_populator", populator);

            JsonObject generator = new JsonObject();
            generator.addProperty("type", "layered");
            JsonArray radius = new JsonArray();
            radius.add(minRadius);
            radius.add(maxRadius);
            generator.add("radius", radius);
            root.add("generator", generator);

            JsonObject filler = new JsonObject();
            filler.addProperty("type", "layered");
            JsonArray values = new JsonArray();
            values.add(singleOreLayer("primary", oreMaterial));
            values.add(singleOreLayer("secondary", oreMaterial));
            values.add(singleOreLayer("between", between));
            values.add(singleOreLayer("sporadic", sporadic));
            filler.add("values", values);
            root.add("filler", filler);

            return new PendingOreVein(depositName, root);
        }

        private JsonObject singleOreLayer(String layerName, String materialName) {
            JsonObject object = new JsonObject();
            object.addProperty(layerName, "ore:" + materialName);
            return object;
        }
    }

    private static final class PendingOreVein {

        private final String depositName;
        private final JsonObject config;

        private PendingOreVein(String depositName, JsonObject config) {
            this.depositName = depositName;
            this.config = config;
        }
    }
}
