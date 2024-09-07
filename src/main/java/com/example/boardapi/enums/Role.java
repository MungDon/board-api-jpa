package com.example.boardapi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("USER","회원"),
    ADMIN("ADMIN","관리자");

    private final String roleType;
    private final String roleName;
}
