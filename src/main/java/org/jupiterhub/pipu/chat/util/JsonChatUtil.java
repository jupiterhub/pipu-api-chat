package org.jupiterhub.pipu.chat.util;

import org.jupiterhub.pipu.chat.record.Message;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.EncodeException;
import java.io.StringReader;

public class JsonChatUtil {
    public static Message decode(String json) throws DecodeException {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            JsonObject parsed = reader.readObject();

            return new Message(
                    parsed.getString("id"),
                    parsed.getString("message"),
                    parsed.getString("from"),
                    parsed.getString("to"),
                    parsed.getJsonNumber("timestamp").longValue());
        }
    }

    public static String encode(Message message) throws EncodeException {
        return Json.createObjectBuilder()
                .add("timestamp", message.timestamp())
                .add("from", message.from())
                .add("to", message.to())
                .add("message", message.message())
                .add("id", message.id())
                .build()
                .toString();
    }
}
