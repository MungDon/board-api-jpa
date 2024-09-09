package com.example.boardapi.repository;

import com.example.boardapi.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JWTRepository extends JpaRepository<Token,String> {

    @Query("SELECT T FROM Token T JOIN FETCH T.member WHERE T.refreshToken = :refreshToken")
    Optional<Token> findByRefreshToken(String refreshToken);
}
