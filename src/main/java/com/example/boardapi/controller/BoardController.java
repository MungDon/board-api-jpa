package com.example.boardapi.controller;

import com.example.boardapi.entity.Member;
import com.example.boardapi.request.board.ReqBoardAdd;
import com.example.boardapi.response.ResComResult;
import com.example.boardapi.service.BoardService;
import com.example.boardapi.util.AuthUserData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/add")
    public ResponseEntity<ResComResult> boardAdd(@Valid ReqBoardAdd req, @AuthUserData Member member){
        ResComResult result = boardService.boardAdd(req,member);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }
}
