package net.nocpiun.ranktitles.utils;

import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.nocpiun.ranktitles.RankTitles;
import net.nocpiun.ranktitles.RankTitlesPlugin;
import net.nocpiun.ranktitles.title.Title;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

    public static void refreshPlayerList(MinecraftServer server) {
        PlayerManager playerManager = server.getPlayerManager();
        playerManager.sendToAll(new PlayerListS2CPacket(EnumSet.of(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME), playerManager.getPlayerList()));
    }
}
