package org.jupiterhub.pipu.chat.repository;

import org.jupiterhub.pipu.chat.record.Chat;
import org.jupiterhub.pipu.chat.record.Message;

import java.util.List;

public interface ChatRepository {
    List<Chat> getChats();

    List<Chat> getChatsByUserId(String userId);

    Chat saveChat(Chat chat);   // or update

    Chat saveMessage(String chatId, Message message);

    Chat updateMessage(String chatId, String messageId, String message);

    List<Message> getMessages(String chatId, int offset, int limit);

    void deleteChat(Message message);
}
