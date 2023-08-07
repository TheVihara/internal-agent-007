

package net.skydecade.utils;

public final class Colors {

    private static final char CHAR_FROM = '&';
    private static final char CHAR_TO = '\u00a7';

    private Colors() {
    }

    public static String of(String text) {
        if (text == null) return null;
        return text.replace(CHAR_FROM, CHAR_TO);
    }

}
