package com.example.boardapi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeleteYn {

    Y("Y","삭제"),
    D("N","삭제안됨");

    private final String code;
    private final String name;
}
