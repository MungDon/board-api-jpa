package com.example.boardapi.controller;

import com.example.boardapi.request.member.ReqSignup;
import com.example.boardapi.response.ResComResult;
import com.example.boardapi.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /*회원가입 처리*/
    @PostMapping("/join")
    public ResponseEntity<ResComResult> memberJoin(@Valid @RequestBody ReqSignup req){
        ResComResult result = memberService.memberJoin(req);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

}
