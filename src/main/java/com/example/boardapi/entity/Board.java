package com.example.boardapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@SuperBuilder
@Getter
@Table(name = "tb_board")// 데이터베이스 테이블 명 설정
public class Board extends BaseTimeEntity{

    @Id
    @Column(name = "board_sid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardSid;

    private String title;

    private String content;
                                            // LAZY(지연로딩) : 실제로 필요한 시점까지 DB 에서 관련데이터를 로딩(조회) 하지않는다
                                        // 즉 쓸때 없이 join 을 하지않아 성능 향상에 도움
    @ManyToOne(fetch = FetchType.LAZY)  // 반대되는 의미에 EAGER(즉시로딩) 이있다

    @JoinColumn(name = "member_sid")    // 매핑할 외래키를 지정한다
    private Member member;              // 1:N 관계에서는 @ManyToOne 과 @JoinColumn 이 함께 있다면,
                                        // 해당 Entity 를 연관관계 주인으로 판단하고 해당 필드를 FK로 매핑한다.
    @Column(name="delete_yn")
    private String deleteYn;




}
