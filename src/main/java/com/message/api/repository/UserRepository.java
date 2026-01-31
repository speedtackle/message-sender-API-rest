package com.message.api.repository;

import com.message.api.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT e FROM User e JOIN FETCH e.roles WHERE e.username= (:username)")
    public User findByUsername(@Param("username") String username);
    @Query("SELECT e FROM User e JOIN FETCH e.roles WHERE e.id= (:id)")
    public Optional<User> findById(@Param("id") Integer id);
    
    boolean existsByUsername(String username);
    
}
