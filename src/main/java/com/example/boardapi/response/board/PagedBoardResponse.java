package com.example.boardapi.response.board;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PagedBoardResponse {

    private int currentPage;        // 현재 페이지

    private int startPage;          // 시작 페이지

    private int endPage;            // 끝 페이지

    private List<BoardData> boardList;  // 실제 boardList

        @AllArgsConstructor(access = AccessLevel.PRIVATE)// 이너 클래스이기 때문에 외부에서 생성 할 수 없도록 설정 (캡슐화,불변성)
        @Getter
        @Builder
        public static class BoardData{ // 이너 클래스

            private Long boardSid;

            private String title;

            private String content;

            private String memberName;

            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 바인딩된 값을 정의한것과 같이 포맷(폼데이터 일 경우)
            private LocalDateTime createdDate;               // json 일 시에는 @jsonFormat 이용가능하다고 함.

            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            private LocalDateTime modifiedDate;
        }
}
