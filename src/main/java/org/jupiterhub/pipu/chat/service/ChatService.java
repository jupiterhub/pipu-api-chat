package org.jupiterhub.pipu.chat.service;

import org.jupiterhub.pipu.chat.record.Chat;
import org.jupiterhub.pipu.chat.record.Message;
import org.jupiterhub.pipu.chat.record.MessageTimestamp;

import java.util.List;

public interface ChatService {

    List<Chat> getAllChats(int offset, int limit);
    Chat getChatById(String chatId);
    List<Chat> getChatsByUserId(String userId);
    Chat saveChat(Chat chat);
    Chat updateChat(Chat chat);
    void deleteChat(String chatId);
}
