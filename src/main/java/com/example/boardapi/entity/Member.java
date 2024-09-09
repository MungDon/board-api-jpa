package com.example.boardapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Entity
@Getter
@SuperBuilder
@Table(name = "tb_member") //테이블명 설정
public class Member extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_sid")
    private Long memberSid;

    private String email;

    private String name;

    private String password;

    private String role;

}
