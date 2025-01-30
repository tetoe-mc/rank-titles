package net.nocpiun.ranktitles.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.nocpiun.ranktitles.RankTitles;
import net.nocpiun.ranktitles.RankTitlesPlugin;
import net.nocpiun.ranktitles.title.Title;

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
}
