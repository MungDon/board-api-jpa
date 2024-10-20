package com.example.boardapi.controller;

import com.example.boardapi.entity.Member;
import com.example.boardapi.request.board.ReqBoardAdd;
import com.example.boardapi.request.board.ReqBoardUpdate;
import com.example.boardapi.response.ResComResult;
import com.example.boardapi.service.BoardService;
import com.example.boardapi.util.AuthUserData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("")
    public ResponseEntity<ResComResult> boardList(@PageableDefault(page = 0, size = 10, sort = "boardSid",direction = Sort.Direction.DESC) Pageable page){
        ResComResult result = boardService.boardList(page);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }


    @GetMapping("/detail/{boardSid}")
    public ResponseEntity<ResComResult> boardDetail(@PathVariable(value = "boardSid")Long boardSid){
        ResComResult result = boardService.boardDetail(boardSid);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @PostMapping("/add")
    public ResponseEntity<ResComResult> boardAdd(@Valid @RequestBody ReqBoardAdd req, @AuthUserData Member member) {
        ResComResult result = boardService.boardAdd(req, member);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }
    @PutMapping("/update")
    public ResponseEntity<ResComResult> boardUpdate(ReqBoardUpdate req){
        ResComResult result = boardService.boardUpdate(req);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @DeleteMapping("/delete/{boardSid}")
    public ResponseEntity<ResComResult> boardDelete(@PathVariable(value = "boardSid")Long boardSid){
        ResComResult result = boardService.boardDelete(boardSid);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }
}
