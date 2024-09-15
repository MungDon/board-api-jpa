package com.example.boardapi.response.board;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResBoardDetail {

    private Long boardSid;

    private String title;

    private String content;

    private String memberName;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

}
