package org.jupiterhub.pipu.chat.util;

import org.jupiterhub.pipu.chat.record.Chat;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.EncodeException;
import java.io.StringReader;

public class JsonChatUtil {
    public static Chat decode(String json) throws DecodeException {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            JsonObject parsed = reader.readObject();

            return new Chat(parsed.get("id").toString(),
                    parsed.getString("message"),
                    parsed.getString("from"),
                    parsed.getString("to"),
                    parsed.getJsonNumber("timestamp").longValue());
        }
    }

    public static String encode(Chat chat) throws EncodeException {
        return Json.createObjectBuilder()
                .add("id", chat.id())
                .add("timestamp", chat.timestamp())
                .add("from", chat.from())
                .add("to", chat.to())
                .add("message", chat.message())
                .build()
                .toString();
    }
}
