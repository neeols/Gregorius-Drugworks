package com.wurtzitane.gregoriusdrugworkspersistence.blotter;

/**
 * Printed blotter carrier kinds.
 *
 * @author wurtzitane
 */
public enum PrintableCarrierKind {

    BLOTTER_PAPER("blotter_paper", "blotter_paper", 64, 64),
    SINGLE_TAB("single_tab", "single_tab", 8, 8);

    private final String itemName;
    private final String generatedTextureFolder;
    private final int textureWidth;
    private final int textureHeight;

    PrintableCarrierKind(String itemName, String generatedTextureFolder, int textureWidth, int textureHeight) {
        this.itemName = itemName;
        this.generatedTextureFolder = generatedTextureFolder;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public String getItemName() {
        return itemName;
    }

    public String getGeneratedTextureFolder() {
        return generatedTextureFolder;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }
}
