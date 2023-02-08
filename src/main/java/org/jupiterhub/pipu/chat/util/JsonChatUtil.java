package org.jupiterhub.pipu.chat.util;

import org.jupiterhub.pipu.chat.record.Chat;
import org.jupiterhub.pipu.chat.record.Message;
import org.jupiterhub.pipu.chat.record.MessageTimestamp;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.websocket.EncodeException;
import java.io.StringReader;
import java.util.function.Function;

public class JsonChatUtil {
    public static Message decode(String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            JsonObject parsed = reader.readObject();

            return new Message(
                    parsed.getString("id"),
                    parsed.getString("message"),
                    parsed.getString("from"),
                    parsed.getString("to"));
        }
    }

    public static String encode(Message message) throws EncodeException {
        return Json.createObjectBuilder()
                .add("timestamp", Json.createObjectBuilder()
                        .add("sent", message.timestamp().sent())
                        .add("delivered", message.timestamp().delivered())
                        .add("read", message.timestamp().read())
                        .build())
                .add("from", message.from())
                .add("to", message.to())
                .add("message", message.message())
                .add("id", message.id())
                .build()
                .toString();
    }

    public static Chat decodeChat(String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            JsonObject parsed = reader.readObject();

            return new Chat(
                    parsed.getString(Chat.FIELD_ID),
                    parsed.getJsonArray(Chat.FIELD_PEOPLE).getValuesAs(jsonValue -> jsonValue.toString()),
                    parsed.getJsonArray(Chat.FIELD_MESSAGES).stream().map(JsonValue::asJsonObject)
                            .map(asMessage())
                            .toList(),
                    parsed.getJsonNumber(Chat.FIELD_CREATED).longValue());
        }
    }

    private static Function<JsonObject, Message> asMessage() {
        return jsonObject -> {
            JsonObject timestamp = jsonObject.getJsonObject(Message.FIELD_TIMESTAMP);
            return new Message(
                    jsonObject.getString(Message.FIELD_ID),
                    jsonObject.getString(Message.FIELD_MESSAGE),
                    jsonObject.getString(Message.FIELD_FROM),
                    jsonObject.getString(Message.FIELD_TO),
                    new MessageTimestamp(
                            timestamp.getJsonNumber(MessageTimestamp.FIELD_SENT).longValue(),
                            timestamp.getJsonNumber(MessageTimestamp.FIELD_DELIVERED) != null ? timestamp.getJsonNumber(MessageTimestamp.FIELD_DELIVERED).longValue() : null,
                            timestamp.getJsonNumber(MessageTimestamp.FIELD_READ) != null ? timestamp.getJsonNumber(MessageTimestamp.FIELD_READ).longValue() : null
                            )
                    );
        };
    }
}
