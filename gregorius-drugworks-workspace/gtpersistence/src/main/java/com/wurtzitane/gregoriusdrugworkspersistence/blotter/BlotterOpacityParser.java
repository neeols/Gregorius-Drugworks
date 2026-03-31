package com.wurtzitane.gregoriusdrugworkspersistence.blotter;

import javax.annotation.Nullable;

/**
 * Shared opacity parsing for blotter printing.
 *
 * @author wurtzitane
 */
public final class BlotterOpacityParser {

    private BlotterOpacityParser() {
    }

    public static ParseResult parse(@Nullable String rawValue) {
        String value = rawValue == null ? "" : rawValue.trim();
        if (value.isEmpty()) {
            return ParseResult.invalid();
        }
        if (value.endsWith("%")) {
            value = value.substring(0, value.length() - 1).trim();
        }
        if (value.isEmpty()) {
            return ParseResult.invalid();
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return ParseResult.invalid();
            }
        }
        try {
            int parsed = Integer.parseInt(value);
            if (parsed < 0 || parsed > 100) {
                return ParseResult.invalid();
            }
            return new ParseResult(true, parsed);
        } catch (NumberFormatException ignored) {
            return ParseResult.invalid();
        }
    }

    public static final class ParseResult {
        private static final ParseResult INVALID = new ParseResult(false, -1);

        private final boolean valid;
        private final int opacityPercent;

        private ParseResult(boolean valid, int opacityPercent) {
            this.valid = valid;
            this.opacityPercent = opacityPercent;
        }

        public static ParseResult invalid() {
            return INVALID;
        }

        public boolean isValid() {
            return valid;
        }

        public int getOpacityPercent() {
            return opacityPercent;
        }
    }
}
