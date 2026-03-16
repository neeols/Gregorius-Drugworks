package com.wurtzitane.gregoriusdrugworks.common.catalog;

public final class ContentCatalogEntry<T> {

    private final String id;
    private final ContentFamily family;
    private final String translationKey;
    private final boolean sample;
    private final T payload;

    public ContentCatalogEntry(
            String id,
            ContentFamily family,
            String translationKey,
            boolean sample,
            T payload
    ) {
        this.id = id;
        this.family = family;
        this.translationKey = translationKey;
        this.sample = sample;
        this.payload = payload;
    }

    public String getId() {
        return id;
    }

    public ContentFamily getFamily() {
        return family;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public boolean isSample() {
        return sample;
    }

    public T getPayload() {
        return payload;
    }
}