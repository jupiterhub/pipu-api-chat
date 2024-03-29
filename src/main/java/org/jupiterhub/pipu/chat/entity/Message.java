package org.jupiterhub.pipu.chat.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@RegisterForReflection
@Builder
public class Message {

    private String messageId;
    private String chatId;
    private List<String> people;

    private String from;

    private String to;
    private String message;

    private Long sentTimestamp; // created
    private Long readTimestamp;
    private Long deliveredTimestamp;

    public Message() {
        people = new ArrayList<>();
    }

    public Message(String message, String from, String to) {
        this();
        this.message = message;
        this.from = from;
        this.to = to;
    }

    public Message(String messageId, String chatId, List<String> people, String from, String to, String message,
                   Long sentTimestamp, Long readTimestamp, Long deliveredTimestamp) {
        this.messageId = messageId;
        this.chatId = chatId;
        this.people = people;
        this.from = from;
        this.to = to;
        this.message = message;
        this.sentTimestamp = sentTimestamp;
        this.readTimestamp = readTimestamp;
        this.deliveredTimestamp = deliveredTimestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<String> getPeople() {
        return people;
    }

    public void setPeople(List<String> people) {
        this.people = people;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getSentTimestamp() {
        return sentTimestamp;
    }

    public void setSentTimestamp(Long sentTimestamp) {
        this.sentTimestamp = sentTimestamp;
    }

    public Long getReadTimestamp() {
        return readTimestamp;
    }

    public void setReadTimestamp(Long readTimestamp) {
        this.readTimestamp = readTimestamp;
    }

    public Long getDeliveredTimestamp() {
        return deliveredTimestamp;
    }

    public void setDeliveredTimestamp(Long deliveredTimestamp) {
        this.deliveredTimestamp = deliveredTimestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", chatId='" + chatId + '\'' +
                ", people=" + people +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                ", sentTimestamp=" + sentTimestamp +
                ", readTimestamp=" + readTimestamp +
                ", deliveredTimestamp=" + deliveredTimestamp +
                '}';
    }
}
