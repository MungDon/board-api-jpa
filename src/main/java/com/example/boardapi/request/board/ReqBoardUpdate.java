package com.example.boardapi.request.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqBoardUpdate {

    @NotNull(message = "게시글 시퀀스 번호가 누락되었습니다.")
    private Long boardSid;

    @NotBlank(message = "제목은 필수 입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입니다.")
    private String content;

}
