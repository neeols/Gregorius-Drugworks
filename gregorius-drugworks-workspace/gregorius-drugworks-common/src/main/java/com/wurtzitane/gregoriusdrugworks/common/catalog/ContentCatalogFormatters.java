package com.wurtzitane.gregoriusdrugworks.common.catalog;

public final class ContentCatalogFormatters {

    private ContentCatalogFormatters() {
    }

    public static String describeEntry(ContentCatalogEntry<?> entry) {
        return entry.getId()
                + " | family=" + entry.getFamily().name().toLowerCase()
                + " | sample=" + entry.isSample()
                + " | key=" + entry.getTranslationKey();
    }
}