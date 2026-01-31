package com.message.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.message.api.model.User;
import com.message.api.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public void createUser(User user){
        String pass = user.getPassword();
        //criptografando senha antes de salvar o user no db
        user.setPassword(passwordEncoder.encode(pass));
        repository.save(user);
    }
}
