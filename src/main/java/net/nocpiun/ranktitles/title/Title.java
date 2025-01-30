package net.nocpiun.ranktitles.title;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.nocpiun.ranktitles.utils.Message;

import java.util.ArrayList;

public class Title {
    private final String id;
    private final String title; // not colorized
    private ArrayList<String> playerUUIDList;

    public Title(String id, String title) {
        this.id = id;
        this.title = title;
        playerUUIDList = new ArrayList<>();
    }

    public Title(String id, String title, ArrayList<String> playerUUIDList) {
        this.id = id;
        this.title = title;
        this.playerUUIDList = playerUUIDList;
    }

    public String getId() {
        return id;
    }

    public String getTitleString() {
        return title;
    }

    public ArrayList<String> getPlayerUUIDList() {
        return playerUUIDList;
    }

    public void addPlayer(ServerPlayerEntity player) {
        playerUUIDList.add(player.getUuidAsString());
    }

    public void removePlayer(ServerPlayerEntity player) {
        playerUUIDList.remove(player.getUuidAsString());
    }

    public boolean hasPlayer(String uuid) {
        return playerUUIDList.contains(uuid);
    }
}
