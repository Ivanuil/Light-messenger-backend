package edu.example.light_messenger.service;

import edu.example.light_messenger.model.ChatModel;
import edu.example.light_messenger.model.UserModel;
import edu.example.light_messenger.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public ChatModel findOrCreateChat(UserModel user1, UserModel user2) {
        var chatOpt = chatRepository.findChatForUsers(user1, user2);
        return chatOpt.orElseGet(() ->
                chatRepository.save(new ChatModel(0L, user1, user2, null)));
    }

    public Set<String> findCompanionsForUser(UserModel user) {
        var chatSet = chatRepository.findChatsForUser(user);
        Set<String> companionSet = new HashSet<>();
        for (ChatModel chat : chatSet) {
            if (chat.getUser1().equals(user))
                companionSet.add(chat.getUser2().getUsername());
            else
                companionSet.add(chat.getUser1().getUsername());
        }
        return companionSet;
    }

}
