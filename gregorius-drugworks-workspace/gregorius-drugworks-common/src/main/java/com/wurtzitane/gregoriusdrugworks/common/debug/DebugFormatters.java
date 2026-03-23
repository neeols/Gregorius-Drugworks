package com.wurtzitane.gregoriusdrugworks.common.debug;

public final class DebugFormatters {

    private DebugFormatters() {
    }

    public static String kv(String key, Object value) {
        return key + "=" + String.valueOf(value);
    }

    public static String join(String prefix, String... entries) {
        StringBuilder builder = new StringBuilder(prefix);
        for (int i = 0; i < entries.length; i++) {
            if (i == 0) {
                builder.append(" ");
            } else {
                builder.append(" | ");
            }
            builder.append(entries[i]);
        }
        return builder.toString();
    }
}