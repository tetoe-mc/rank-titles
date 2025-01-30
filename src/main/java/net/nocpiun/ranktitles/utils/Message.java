package net.nocpiun.ranktitles.utils;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class Message {
    private static String stringColorize(String str) {
        return str.replaceAll("&", "§");
    }

    public static Text formatted(String message) {
        return Text.of(stringColorize("[&eRank Titles&r] "+ message));
    }

    public static Text colorize(String message) {
        return Text.of(stringColorize(message));
    }

    public static MutableText colorizeMutable(String message) {
        return Text.literal(stringColorize(message));
    }
}
