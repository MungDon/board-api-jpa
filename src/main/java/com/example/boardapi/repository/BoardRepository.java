package com.example.boardapi.repository;

import com.example.boardapi.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board,Long> {



    @Query("SELECT board FROM Board board JOIN FETCH board.member WHERE board.boardSid = :boardSid")
    Optional<Board> findByBoardSid(Long boardSid);
}
