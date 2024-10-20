package com.example.boardapi.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
public class DSLConfig {

    @PersistenceContext // EntityManager 를 빈으로 주입받기 위해 사용하는 어노테이션
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory queryFactory(){// QueryDSL을 사용하여 쿼리를 작성하기 위한 JPAQueryFactory Bean 생성
        return new JPAQueryFactory(entityManager);
    }
}
