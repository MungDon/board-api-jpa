package com.example.boardapi.controller;

import com.example.boardapi.entity.Member;
import com.example.boardapi.request.board.ReqBoardAdd;
import com.example.boardapi.response.ResComResult;
import com.example.boardapi.service.BoardService;
import com.example.boardapi.util.AuthUserData;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("")
    public ResponseEntity<ResComResult> boardList(@PageableDefault(page = 1, sort = "board_sid")Pageable page){
        ResComResult result = boardService.boardList(page);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }


    @GetMapping("/detail/{boardSid}")
    public ResponseEntity<ResComResult> boardDetail(@PathVariable(value = "boardSid")Long boardSid){
        ResComResult result = boardService.boardDetail(boardSid);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @PostMapping("/add")
    public ResponseEntity<ResComResult> boardAdd(@Valid ReqBoardAdd req, @AuthUserData Member member) {
        ResComResult result = boardService.boardAdd(req, member);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }
    @PutMapping("/update")
    public ResponseEntity<ResComResult> boardUpdate(ReqBoardUpdate req){
        ResComResult result = boardService.boardUpdate(req);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }
}
