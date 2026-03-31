package com.wurtzitane.gregoriusdrugworkspersistence.blotter;

import com.google.common.collect.ImmutableList;
import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Startup registry for printable blotter source images and generated composite textures.
 *
 * @author wurtzitane
 */
public final class BlotterPrintableRegistry {

    public static final String SOURCE_PATH = "assets/" + Tags.MOD_ID + "/textures/blotter_images";

    private static final File GENERATED_ROOT = new File(Loader.instance().getConfigDir(),
            Tags.MOD_ID + "/generated/assets/" + Tags.MOD_ID);
    private static final File GENERATED_TEXTURE_ROOT = new File(GENERATED_ROOT, "textures/item/generated");
    private static final File GENERATED_NORMALIZED_ROOT = new File(GENERATED_ROOT, "textures/blotter_images/normalized");
    private static final File GENERATED_INDEX_FILE = new File(GENERATED_ROOT, "blotter_print_index.txt");

    private static final String BASE_BLOTTER_TEXTURE = "/assets/" + Tags.MOD_ID + "/textures/item/blotter_paper.png";
    private static final String BASE_SINGLE_TAB_TEXTURE = "/assets/" + Tags.MOD_ID + "/textures/item/single_tab.png";

    private static final List<Entry> ENTRIES = new ArrayList<>();
    private static final Map<String, Entry> LOOKUP = new HashMap<>();
    private static final List<String> SKIPPED = new ArrayList<>();

    private static boolean bootstrapped;

    private BlotterPrintableRegistry() {
    }

    public static void preInit() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;
        rebuild();
    }

    public static Collection<Entry> all() {
        return Collections.unmodifiableList(ENTRIES);
    }

    public static Collection<String> getSkipped() {
        return Collections.unmodifiableList(SKIPPED);
    }

    @Nullable
    public static Entry resolve(String rawInput) {
        String key = normalizeLookupKey(rawInput);
        if (key.isEmpty()) {
            return null;
        }
        return LOOKUP.get(key);
    }

    @Nullable
    public static Entry findByVariantId(String variantId) {
        if (variantId == null || variantId.trim().isEmpty()) {
            return null;
        }
        for (Entry entry : ENTRIES) {
            if (entry.getVariantId().equals(variantId)) {
                return entry;
            }
        }
        return null;
    }

    public static File getGeneratedTextureFile(PrintableCarrierKind carrierKind, String variantId, int opacityPercent) {
        return new File(new File(GENERATED_TEXTURE_ROOT, carrierKind.getGeneratedTextureFolder()),
                variantId + "_" + formatOpacitySuffix(opacityPercent) + ".png");
    }

    public static ResourceLocation getGeneratedSpriteLocation(PrintableCarrierKind carrierKind, String variantId,
                                                              int opacityPercent) {
        return new ResourceLocation(Tags.MOD_ID, "item/generated/" + carrierKind.getGeneratedTextureFolder() + "/" +
                variantId + "_" + formatOpacitySuffix(opacityPercent));
    }

    private static void rebuild() {
        ENTRIES.clear();
        LOOKUP.clear();
        SKIPPED.clear();

        try {
            ensureDirectory(GENERATED_ROOT.toPath());
            resetGeneratedSubtree();
            List<RawSourceImage> rawSources = collectRawSourceImages();
            rawSources.sort(Comparator.comparing(RawSourceImage::getCanonicalPath));

            BufferedImage blotterBase = readRequiredImage(BASE_BLOTTER_TEXTURE);
            BufferedImage singleTabBase = readRequiredImage(BASE_SINGLE_TAB_TEXTURE);

            Map<String, Integer> variantCounts = new HashMap<>();
            for (RawSourceImage rawSource : rawSources) {
                String variantBase = sanitizeVariantBase(rawSource.getCanonicalPath());
                int collisionIndex = variantCounts.getOrDefault(variantBase, 0);
                variantCounts.put(variantBase, collisionIndex + 1);
                String variantId = collisionIndex == 0 ? variantBase : variantBase + "_" + collisionIndex;

                BufferedImage normalized = normalizeToSquare(rawSource.getImage(), 64);
                File normalizedFile = new File(GENERATED_NORMALIZED_ROOT, variantId + ".png");
                ensureDirectory(normalizedFile.toPath().getParent());
                writeImage(normalized, normalizedFile);

                Entry entry = new Entry(
                        variantId,
                        rawSource.getCanonicalPath(),
                        stripExtension(rawSource.getCanonicalPath()),
                        normalizedFile
                );
                ENTRIES.add(entry);

                generateCompositeSet(entry, normalized, blotterBase, singleTabBase);
            }

            rebuildLookupTable();
            writeIndexFile();
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to build blotter printable registry.", exception);
        }
    }

    private static void generateCompositeSet(Entry entry, BufferedImage normalized, BufferedImage blotterBase,
                                             BufferedImage singleTabBase) throws IOException {
        for (int opacity = 0; opacity <= 100; opacity++) {
            File blotterTarget = getGeneratedTextureFile(PrintableCarrierKind.BLOTTER_PAPER, entry.getVariantId(), opacity);
            ensureDirectory(blotterTarget.toPath().getParent());
            writeImage(composeBlotterTexture(blotterBase, normalized, opacity), blotterTarget);

            File singleTabTarget = getGeneratedTextureFile(PrintableCarrierKind.SINGLE_TAB, entry.getVariantId(), opacity);
            ensureDirectory(singleTabTarget.toPath().getParent());
            writeImage(composeSingleTabTexture(singleTabBase, normalized, opacity), singleTabTarget);
        }
    }

    private static void rebuildLookupTable() {
        Map<String, Integer> basenameCounts = new HashMap<>();
        for (Entry entry : ENTRIES) {
            basenameCounts.merge(extractBasename(entry.getSourcePath()), 1, Integer::sum);
            basenameCounts.merge(extractBasename(entry.getSourcePathWithoutExtension()), 1, Integer::sum);
        }

        for (Entry entry : ENTRIES) {
            registerLookup(entry.getSourcePath(), entry);
            registerLookup(entry.getSourcePathWithoutExtension(), entry);

            String basenameWithExtension = extractBasename(entry.getSourcePath());
            if (basenameCounts.getOrDefault(basenameWithExtension, 0) == 1) {
                registerLookup(basenameWithExtension, entry);
            }

            String basenameWithoutExtension = extractBasename(entry.getSourcePathWithoutExtension());
            if (basenameCounts.getOrDefault(basenameWithoutExtension, 0) == 1) {
                registerLookup(basenameWithoutExtension, entry);
            }
        }
    }

    private static void registerLookup(String rawKey, Entry entry) {
        String key = normalizeLookupKey(rawKey);
        if (!key.isEmpty()) {
            LOOKUP.put(key, entry);
        }
    }

    private static void writeIndexFile() throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("# Generated blotter print index");
        lines.add("# variantId|sourcePath");
        for (Entry entry : ENTRIES) {
            lines.add(entry.getVariantId() + "|" + entry.getSourcePath());
        }
        if (!SKIPPED.isEmpty()) {
            lines.add("");
            lines.add("# Skipped");
            lines.addAll(SKIPPED);
        }
        ensureDirectory(GENERATED_INDEX_FILE.toPath().getParent());
        Files.write(GENERATED_INDEX_FILE.toPath(), lines, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }

    private static List<RawSourceImage> collectRawSourceImages() throws IOException {
        Map<String, RawSourceImage> discovered = new LinkedHashMap<>();
        scanFromModContainer(discovered);
        if (discovered.isEmpty()) {
            scanFromClassLoader(discovered);
        }
        return new ArrayList<>(discovered.values());
    }

    private static void scanFromModContainer(Map<String, RawSourceImage> discovered) throws IOException {
        ModContainer container = Loader.instance().getIndexedModList().get(Tags.MOD_ID);
        if (container == null || container.getSource() == null) {
            return;
        }

        File source = container.getSource();
        if (source.isFile() && source.getName().toLowerCase(Locale.ROOT).endsWith(".jar")) {
            scanJarFile(source, discovered);
            return;
        }

        if (source.isDirectory()) {
            Path sourcePath = source.toPath();
            scanDirectoryIfPresent(sourcePath.resolve(SOURCE_PATH), discovered);
            scanDirectoryIfPresent(sourcePath.resolveSibling("resources").resolve("main").resolve(SOURCE_PATH), discovered);
            scanDirectoryIfPresent(sourcePath.resolveSibling("src").resolve("main").resolve("resources").resolve(SOURCE_PATH), discovered);
        }
    }

    private static void scanFromClassLoader(Map<String, RawSourceImage> discovered) throws IOException {
        java.util.Enumeration<URL> resources =
                BlotterPrintableRegistry.class.getClassLoader().getResources(SOURCE_PATH.replace('\\', '/'));
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            String protocol = url.getProtocol();
            if ("file".equalsIgnoreCase(protocol)) {
                try {
                    scanDirectoryIfPresent(new File(url.toURI()).toPath(), discovered);
                } catch (Exception ignored) {
                    // Best-effort fallback only.
                }
            } else if ("jar".equalsIgnoreCase(protocol)) {
                JarURLConnection connection = (JarURLConnection) url.openConnection();
                try (ZipFile zip = new ZipFile(new File(connection.getJarFileURL().toURI()))) {
                    scanZip(zip, discovered);
                } catch (Exception ignored) {
                    // Best-effort fallback only.
                }
            }
        }
    }

    private static void scanJarFile(File jarFile, Map<String, RawSourceImage> discovered) throws IOException {
        try (ZipFile zip = new ZipFile(jarFile)) {
            scanZip(zip, discovered);
        }
    }

    private static void scanZip(ZipFile zip, Map<String, RawSourceImage> discovered) throws IOException {
        List<? extends ZipEntry> entries = Collections.list(zip.entries());
        entries.sort(Comparator.comparing(ZipEntry::getName));
        for (ZipEntry zipEntry : entries) {
            if (zipEntry.isDirectory()) {
                continue;
            }
            String name = zipEntry.getName().replace('\\', '/');
            if (!name.startsWith(SOURCE_PATH + "/")) {
                continue;
            }
            String relativePath = name.substring((SOURCE_PATH + "/").length());
            try (InputStream inputStream = zip.getInputStream(zipEntry)) {
                addSourceImage(relativePath, inputStream, discovered);
            }
        }
    }

    private static void scanDirectoryIfPresent(Path root, Map<String, RawSourceImage> discovered) throws IOException {
        if (root == null || !Files.isDirectory(root)) {
            return;
        }
        try (java.util.stream.Stream<Path> stream = Files.walk(root)) {
            stream.filter(Files::isRegularFile)
                    .sorted()
                    .forEach(path -> {
                        Path relative = root.relativize(path);
                        try (InputStream inputStream = Files.newInputStream(path)) {
                            addSourceImage(relative.toString().replace('\\', '/'), inputStream, discovered);
                        } catch (IOException exception) {
                            SKIPPED.add(relative.toString().replace('\\', '/') + " | " + exception.getMessage());
                        }
                    });
        }
    }

    private static void addSourceImage(String relativePath, InputStream inputStream,
                                       Map<String, RawSourceImage> discovered) throws IOException {
        String canonicalPath = canonicalSourcePath(relativePath);
        if (canonicalPath.isEmpty() || discovered.containsKey(canonicalPath)) {
            return;
        }

        BufferedImage sourceImage = ImageIO.read(inputStream);
        if (sourceImage == null) {
            SKIPPED.add(relativePath + " | unsupported image format");
            return;
        }
        if (sourceImage.getWidth() <= 0 || sourceImage.getHeight() <= 0) {
            SKIPPED.add(relativePath + " | invalid image dimensions");
            return;
        }

        discovered.put(canonicalPath, new RawSourceImage(canonicalPath, sourceImage));
    }

    private static BufferedImage composeBlotterTexture(BufferedImage blotterBase, BufferedImage normalizedSource,
                                                       int opacityPercent) {
        BufferedImage canvas = scaleNearest(blotterBase, PrintableCarrierKind.BLOTTER_PAPER.getTextureWidth(),
                PrintableCarrierKind.BLOTTER_PAPER.getTextureHeight());
        alphaComposite(canvas, normalizedSource, 0, 0, BlotterPrintData.clampOpacity(opacityPercent) / 100.0F);
        return canvas;
    }

    private static BufferedImage composeSingleTabTexture(BufferedImage singleTabBase, BufferedImage normalizedSource,
                                                         int opacityPercent) {
        BufferedImage canvas = scaleNearest(singleTabBase,
                PrintableCarrierKind.SINGLE_TAB.getTextureWidth(),
                PrintableCarrierKind.SINGLE_TAB.getTextureHeight());
        BufferedImage reducedOverlay = scaleNearest(normalizedSource,
                PrintableCarrierKind.SINGLE_TAB.getTextureWidth(),
                PrintableCarrierKind.SINGLE_TAB.getTextureHeight());
        int x = (canvas.getWidth() - reducedOverlay.getWidth()) / 2;
        int y = (canvas.getHeight() - reducedOverlay.getHeight()) / 2;
        alphaComposite(canvas, reducedOverlay, x, y, BlotterPrintData.clampOpacity(opacityPercent) / 100.0F);
        return canvas;
    }

    private static void alphaComposite(BufferedImage destination, BufferedImage overlay, int startX, int startY,
                                       float opacityFactor) {
        for (int y = 0; y < overlay.getHeight(); y++) {
            for (int x = 0; x < overlay.getWidth(); x++) {
                int sourceArgb = overlay.getRGB(x, y);
                int sourceAlpha = (int) (((sourceArgb >>> 24) & 0xFF) * opacityFactor);
                if (sourceAlpha <= 0) {
                    continue;
                }

                int destinationArgb = destination.getRGB(startX + x, startY + y);
                destination.setRGB(startX + x, startY + y, blendArgb(destinationArgb, sourceArgb, sourceAlpha));
            }
        }
    }

    private static int blendArgb(int destinationArgb, int sourceArgb, int sourceAlphaOverride) {
        int srcA = sourceAlphaOverride;
        int dstA = (destinationArgb >>> 24) & 0xFF;
        int outA = srcA + dstA * (255 - srcA) / 255;
        if (outA <= 0) {
            return 0;
        }

        int srcR = (sourceArgb >>> 16) & 0xFF;
        int srcG = (sourceArgb >>> 8) & 0xFF;
        int srcB = sourceArgb & 0xFF;

        int dstR = (destinationArgb >>> 16) & 0xFF;
        int dstG = (destinationArgb >>> 8) & 0xFF;
        int dstB = destinationArgb & 0xFF;

        int outR = (srcR * srcA + dstR * dstA * (255 - srcA) / 255) / outA;
        int outG = (srcG * srcA + dstG * dstA * (255 - srcA) / 255) / outA;
        int outB = (srcB * srcA + dstB * dstA * (255 - srcA) / 255) / outA;

        return (outA & 0xFF) << 24 | (outR & 0xFF) << 16 | (outG & 0xFF) << 8 | (outB & 0xFF);
    }

    private static BufferedImage normalizeToSquare(BufferedImage source, int size) {
        int square = Math.max(source.getWidth(), source.getHeight());
        BufferedImage padded = new BufferedImage(square, square, BufferedImage.TYPE_INT_ARGB);
        int offsetX = (square - source.getWidth()) / 2;
        int offsetY = (square - source.getHeight()) / 2;
        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = 0; x < source.getWidth(); x++) {
                padded.setRGB(offsetX + x, offsetY + y, source.getRGB(x, y));
            }
        }
        if (square == size) {
            return padded;
        }
        return scaleNearest(padded, size, size);
    }

    private static BufferedImage scaleNearest(BufferedImage source, int width, int height) {
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = scaled.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        graphics.drawImage(source, 0, 0, width, height, null);
        graphics.dispose();
        return scaled;
    }

    private static BufferedImage copyImage(BufferedImage source) {
        BufferedImage copy = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = copy.createGraphics();
        graphics.drawImage(source, 0, 0, null);
        graphics.dispose();
        return copy;
    }

    private static BufferedImage readRequiredImage(String classpathLocation) throws IOException {
        try (InputStream inputStream = BlotterPrintableRegistry.class.getResourceAsStream(classpathLocation)) {
            if (inputStream == null) {
                throw new IOException("Missing required base texture: " + classpathLocation);
            }
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new IOException("Unable to decode required base texture: " + classpathLocation);
            }
            return image;
        }
    }

    private static void writeImage(BufferedImage image, File file) throws IOException {
        ensureDirectory(Objects.requireNonNull(file.toPath().getParent()));
        ImageIO.write(image, "png", file);
    }

    private static void resetGeneratedSubtree() throws IOException {
        deleteRecursively(GENERATED_TEXTURE_ROOT.toPath());
        deleteRecursively(GENERATED_NORMALIZED_ROOT.toPath());
        if (GENERATED_INDEX_FILE.exists()) {
            Files.delete(GENERATED_INDEX_FILE.toPath());
        }
    }

    private static void deleteRecursively(Path path) throws IOException {
        if (path == null || !Files.exists(path)) {
            return;
        }
        try (java.util.stream.Stream<Path> stream = Files.walk(path)) {
            stream.sorted(Comparator.reverseOrder()).forEach(candidate -> {
                try {
                    Files.deleteIfExists(candidate);
                } catch (IOException ignored) {
                    // Rebuilt every launch; best-effort cleanup is acceptable.
                }
            });
        }
    }

    private static void ensureDirectory(Path path) throws IOException {
        if (path != null) {
            Files.createDirectories(path);
        }
    }

    private static String canonicalSourcePath(String relativePath) {
        String value = relativePath == null ? "" : relativePath.trim().replace('\\', '/');
        while (value.startsWith("/")) {
            value = value.substring(1);
        }
        return value.toLowerCase(Locale.ROOT);
    }

    private static String normalizeLookupKey(String rawInput) {
        String value = rawInput == null ? "" : rawInput.trim().replace('\\', '/');
        while (value.startsWith("/")) {
            value = value.substring(1);
        }
        return value.toLowerCase(Locale.ROOT);
    }

    private static String stripExtension(String path) {
        int slashIndex = path.lastIndexOf('/');
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex > slashIndex) {
            return path.substring(0, dotIndex);
        }
        return path;
    }

    private static String extractBasename(String path) {
        int slashIndex = path.lastIndexOf('/');
        return slashIndex >= 0 ? path.substring(slashIndex + 1) : path;
    }

    private static String sanitizeVariantBase(String canonicalPath) {
        String noExtension = stripExtension(canonicalPath);
        String flattened = noExtension.replace('/', '_');
        StringBuilder builder = new StringBuilder("blotter_paper_");
        boolean lastUnderscore = true;
        for (int i = 0; i < flattened.length(); i++) {
            char character = flattened.charAt(i);
            boolean valid = (character >= 'a' && character <= 'z') || (character >= '0' && character <= '9');
            if (valid) {
                builder.append(character);
                lastUnderscore = false;
            } else if (!lastUnderscore) {
                builder.append('_');
                lastUnderscore = true;
            }
        }
        while (builder.length() > 0 && builder.charAt(builder.length() - 1) == '_') {
            builder.deleteCharAt(builder.length() - 1);
        }
        if (builder.length() == "blotter_paper_".length()) {
            builder.append("image");
        }
        return builder.toString();
    }

    private static String formatOpacitySuffix(int opacityPercent) {
        return String.format(Locale.ROOT, "%03d", BlotterPrintData.clampOpacity(opacityPercent));
    }

    public static final class Entry {
        private final String variantId;
        private final String sourcePath;
        private final String sourcePathWithoutExtension;
        private final File normalizedImageFile;

        private Entry(String variantId, String sourcePath, String sourcePathWithoutExtension, File normalizedImageFile) {
            this.variantId = variantId;
            this.sourcePath = sourcePath;
            this.sourcePathWithoutExtension = sourcePathWithoutExtension;
            this.normalizedImageFile = normalizedImageFile;
        }

        public String getVariantId() {
            return variantId;
        }

        public String getSourcePath() {
            return sourcePath;
        }

        public String getSourcePathWithoutExtension() {
            return sourcePathWithoutExtension;
        }

        public File getNormalizedImageFile() {
            return normalizedImageFile;
        }

        public Collection<String> getLookupKeys() {
            Set<String> keys = new TreeSet<>();
            keys.add(sourcePath);
            keys.add(sourcePathWithoutExtension);
            keys.add(extractBasename(sourcePath));
            keys.add(extractBasename(sourcePathWithoutExtension));
            return ImmutableList.copyOf(keys);
        }
    }

    private static final class RawSourceImage {
        private final String canonicalPath;
        private final BufferedImage image;

        private RawSourceImage(String canonicalPath, BufferedImage image) {
            this.canonicalPath = canonicalPath;
            this.image = image;
        }

        public String getCanonicalPath() {
            return canonicalPath;
        }

        public BufferedImage getImage() {
            return image;
        }
    }
}
