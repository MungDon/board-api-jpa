package com.example.boardapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // JPA Auditing 기능을 활성화하여 엔티티의 생성일 및 수정일 자동 관리
public class BoardApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardApiApplication.class, args);
    }

}
