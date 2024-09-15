package com.example.boardapi.dslrepository;

import com.example.boardapi.entity.Board;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.boardapi.entity.QBoard.board;
import static com.example.boardapi.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class CustomBoardRepositoryImpl implements CustomBoardRepository{

    private final JPAQueryFactory queryFactory;
    @Override
    public Page<Board> findAllByDeleteYn(String deleteYn, Pageable page) {

        QueryResults<Board> results = queryFactory
                .selectFrom(board)
                .leftJoin(board.member, member)
                .where(board.deleteYn.eq(deleteYn))
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetchResults();

        // QueryResults 에서 결과 리스트와 총 개수를 가져옴.
        List<Board> boards = results.getResults();
        long total = results.getTotal();

        // PageImpl을 사용하여 Page 객체를 반환함.
        return new PageImpl<>(boards, page, total);
    }
}
