package com.example.boardapi.repository;

import com.example.boardapi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    /*회원 중복 체크*/
    Boolean existsByEmail(String email);

    /*회원 이메일 정보로 찾기(UserDetails 용도)*/
    Optional<Member> findByEmail(String email);
}
