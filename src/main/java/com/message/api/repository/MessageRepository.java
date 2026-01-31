package com.message.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.message.api.model.Message;

public interface MessageRepository extends JpaRepository<Message, Integer>{

} 