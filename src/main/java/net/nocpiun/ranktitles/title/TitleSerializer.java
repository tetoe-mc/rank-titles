package net.nocpiun.ranktitles.title;

import com.google.gson.*;
import net.minecraft.server.network.ServerPlayerEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TitleSerializer implements JsonSerializer<Title> {
    @Override
    public JsonElement serialize(Title src, Type typeOfSrc, JsonSerializationContext ctx) {
        final JsonObject json = new JsonObject();
        json.addProperty("id", src.getId());
        json.addProperty("title", src.getTitleString());

        ArrayList<String> uuidList = src.getPlayerUUIDList();
        JsonArray playersArr = new JsonArray(uuidList.size());
        for(String uuid : uuidList) {
            playersArr.add(uuid);
        }
        json.add("players", playersArr);

        return json;
    }
}
