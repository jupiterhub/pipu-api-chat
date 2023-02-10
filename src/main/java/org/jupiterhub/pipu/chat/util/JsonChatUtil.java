package org.jupiterhub.pipu.chat.util;

import org.jupiterhub.pipu.chat.entity.Message;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.StringReader;

public class JsonChatUtil {
    public static Message decode(String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            JsonObject parsed = reader.readObject();

            return new Message(
                    parsed.getString("messageId"),
                    parsed.getString("chatId"),
                    parsed.getJsonArray("people").stream().map(JsonValue::toString).toList(),
                    parsed.getString("from"),
                    parsed.getString("to"),
                    parsed.getString("message"),
                    parsed.getJsonNumber("sentTimestamp").longValue(),
                    parsed.getJsonNumber("deliveredTimestamp").longValue(),
                    parsed.getJsonNumber("readTimestamp").longValue());
        }
    }
}
