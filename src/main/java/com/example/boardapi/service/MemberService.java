package com.example.boardapi.service;

import com.example.boardapi.entity.Member;
import com.example.boardapi.enums.Role;
import com.example.boardapi.exception.ErrorCode;
import com.example.boardapi.repository.MemberRepository;
import com.example.boardapi.request.member.ReqSignup;
import com.example.boardapi.response.ResComResult;
import com.example.boardapi.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /*회원가입 처리*/
    @Transactional
    public ResComResult memberJoin(ReqSignup req){
        CommonUtils.throwCustomExceptionIf(memberRepository.existsByEmail(req.getEmail()), ErrorCode.DUPLICATE_EMAIL);

        Member member = Member.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .name(req.getName())
                .role(Role.USER.getRoleType())
                .build();

        memberRepository.save(member);
        return CommonUtils.successResponseNoData(member.getMemberSid(),"회원가입 성공",ErrorCode.INSERT_OPERATION_FAILED);
    }
}
