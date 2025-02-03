package net.nocpiun.ranktitles.utils;

import net.nocpiun.ranktitles.RankTitles;
import net.nocpiun.ranktitles.RankTitlesPlugin;
import net.nocpiun.ranktitles.title.Title;
import org.jetbrains.annotations.NotNull;

public class Utils {
    public static String getPlayerTitlesPrefix(String uuid) {
        RankTitlesPlugin plugin = RankTitles.plugin;
        StringBuilder titleString = new StringBuilder();
        int amount = 0;

        for(Title title : plugin.getTitles()) {
            if(title.hasPlayer(uuid)) {
                titleString.append("[").append(title.getTitleString()).append("&r]");
                amount++;
            }
        }
        if(amount != 0) titleString.append(" ");

        return titleString.toString();
    }

    public static <O> boolean checkProperty(@NotNull O obj, String propertyName) {
        try {
            Class<?> type = obj.getClass();
            type.getDeclaredField(propertyName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}
