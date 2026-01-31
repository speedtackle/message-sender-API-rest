package com.message.api.controller;

import com.message.api.dtos.Login;
import com.message.api.dtos.Sessao;
import com.message.api.model.User;
import com.message.api.repository.UserRepository;
import com.message.api.security.JWTCreator;
import com.message.api.security.JWTObject;
import com.message.api.security.JWTProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class LoginController {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository repository;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Sessao logar(@RequestBody Login login) {

        User user = repository.findByUsername(login.getUsername());

        if (user == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        if (!encoder.matches(login.getPassword(), user.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        JWTObject jwtObject = new JWTObject();
        jwtObject.setSubject(user.getUsername()); // ✅ CRÍTICO
        jwtObject.setIssuedAt(new Date());
        jwtObject.setExpiration(
                new Date(System.currentTimeMillis() + JWTProperties.EXPIRATION)
        );
        jwtObject.setRoles(user.getRoles());

        Sessao sessao = new Sessao();
        sessao.setLogin(user.getUsername());
        sessao.setToken(
                JWTCreator.create(
                        JWTProperties.PREFIX,
                        JWTProperties.KEY,
                        jwtObject
                )
        );

        return sessao;
    }
}
