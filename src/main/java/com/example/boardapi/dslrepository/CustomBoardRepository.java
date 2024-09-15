package com.example.boardapi.dslrepository;

import com.example.boardapi.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomBoardRepository {
    Page<Board> findAllByDeleteYn(String deleteYn, Pageable page);
}
