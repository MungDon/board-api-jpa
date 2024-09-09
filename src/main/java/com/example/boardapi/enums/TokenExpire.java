package com.example.boardapi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenExpire {

    ACCESS_TOKEN(5*60*1000),
    REFRESH_TOKEN(7*24*60*60*1000);

    private final int expiredTime;
}
