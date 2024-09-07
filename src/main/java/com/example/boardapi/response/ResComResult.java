package com.example.boardapi.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResComResult{

    private boolean success;

    private int statusCode;

    private String message;

}
