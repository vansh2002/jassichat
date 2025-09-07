package com.example.chatti.repository;

import com.example.chatti.entity.ChattyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChattyRepository extends JpaRepository<ChattyEntity, Long> {

    Optional<ChattyEntity> findByUsername(String username);

    Optional<ChattyEntity> findByEmail(String email);
}
