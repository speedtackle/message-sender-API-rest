package com.message.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.message.api.model.Message;
import com.message.api.model.User;
import com.message.api.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository repository;
    public void writeMessage(User user, Message message){
        message.setUser(user);
        message.setSender(user.getUsername());
        repository.save(message);
    }
}
