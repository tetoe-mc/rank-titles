package net.nocpiun.ranktitles.title;

import com.google.gson.*;
import net.minecraft.server.network.ServerPlayerEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TitleDeserializer implements JsonDeserializer<Title> {
    @Override
    public Title deserialize(JsonElement jsonElem, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException {
        final JsonObject json = jsonElem.getAsJsonObject();

        ArrayList<String> uuidList = new ArrayList<>();
        JsonArray playersArr = json.getAsJsonArray("players");
        playersArr.forEach((item) -> {
            uuidList.add(item.getAsString());
        });

        return new Title(json.get("id").getAsString(), json.get("title").getAsString(), uuidList);
    }
}
