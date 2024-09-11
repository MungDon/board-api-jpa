package com.example.boardapi.entity;


import jakarta.persistence.Column;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@SuperBuilder
public class BaseTimeEntity {

    //생성일 자동 
    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createDate;

    // 수정일 자동
    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
}
