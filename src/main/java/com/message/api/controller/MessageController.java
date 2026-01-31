package com.message.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.message.api.model.Message;
import com.message.api.model.User;
import com.message.api.repository.UserRepository;
import com.message.api.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService service;
    @Autowired
    private UserRepository userRepository;
    @PostMapping
    public void postMessage(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Message message){
        User user = userRepository.findByUsername(userDetails.getUsername());
        service.writeMessage(user, message);
    }
}
