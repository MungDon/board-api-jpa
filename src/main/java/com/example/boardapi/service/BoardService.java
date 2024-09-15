package com.example.boardapi.service;

import com.example.boardapi.dslrepository.CustomBoardRepository;
import com.example.boardapi.entity.Board;
import com.example.boardapi.entity.Member;
import com.example.boardapi.enums.DeleteYn;
import com.example.boardapi.exception.CustomException;
import com.example.boardapi.exception.ErrorCode;
import com.example.boardapi.repository.BoardRepository;
import com.example.boardapi.request.board.ReqBoardAdd;
import com.example.boardapi.request.board.ReqBoardUpdate;
import com.example.boardapi.response.ResComResult;
import com.example.boardapi.response.board.ResBoardDetail;
import com.example.boardapi.response.board.PagedBoardResponse;
import com.example.boardapi.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ImageService imageService;
    private final CustomBoardRepository customBoardRepository;

    /* 게시글 추가 */
    @Transactional
    public ResComResult boardAdd(ReqBoardAdd req, Member member) {
        Board board = Board.builder()
                .title(req.getTitle())
                .member(member)
                .deleteYn(DeleteYn.D.getCode())
                .build();
        boardRepository.save(board);
        String processContent = imageService.processContentWithImages(board, req.getContent());

        board.saveProcessedContent(processContent);
        return CommonUtils.successResponseNoData(board.getBoardSid(), "글이 등록되었습니다.", ErrorCode.INSERT_OPERATION_FAILED);
    }

    /* 게시글 리스트 */
    @Transactional(readOnly = true)
    public ResComResult boardList(Pageable page) {
        Page<Board> pagingBoardList = customBoardRepository.findAllByDeleteYn(DeleteYn.D.getCode(), page);
        int currentPage = pagingBoardList.getPageable().getPageNumber() + 1;    // 현재페이지
        int startPage = Math.max(currentPage - 4, 1);   // 두 파라미터 값 중 더 큰 값을 반환
        int endPage = Math.min(currentPage + 5, pagingBoardList.getTotalPages());  // 두 파라미터 값 중 더 작은 값을 반환

        List<PagedBoardResponse.BoardData> boardList = pagingBoardList.stream()
                .map(boardData -> PagedBoardResponse.BoardData.builder()
                        .boardSid(boardData.getBoardSid())
                        .title(boardData.getTitle())
                        .content(boardData.getContent())
                        .memberName(boardData.getMember().getName())
                        .createdDate(boardData.getCreateDate())
                        .modifiedDate(boardData.getModifiedDate())
                        .build()
                ).toList();

        PagedBoardResponse boardResponse = PagedBoardResponse.builder()
                .currentPage(currentPage)
                .startPage(startPage)
                .endPage(endPage)
                .boardList(boardList)
                .build();

        return CommonUtils.successResponseHasData(1L, "게시글 리스트 조회", ErrorCode.SELECT_OPERATION_FAILED, boardResponse);
    }

    /*게시글 상세보기*/
    @Transactional(readOnly = true)
    public ResComResult boardDetail(Long boardSid) {
        Board board = boardRepository.findByBoardSid(boardSid).orElseThrow(() -> new CustomException(ErrorCode.BOARD_DATA_NOT_FOUND));
        ResBoardDetail boardDetail = ResBoardDetail.builder()
                .boardSid(board.getBoardSid())
                .title(board.getTitle())
                .content(board.getContent())
                .memberName(board.getMember().getName())
                .createdDate(board.getCreateDate())
                .modifiedDate(board.getModifiedDate())
                .build();
        return CommonUtils.successResponseHasData(board.getBoardSid(), "게시글 데이터 가져오기 성공", ErrorCode.SELECT_OPERATION_FAILED, boardDetail);
    }

    /* 게시글 수정 */
    @Transactional
    public ResComResult boardUpdate(ReqBoardUpdate req) {
        Board board = boardRepository.findById(req.getBoardSid()).orElseThrow(() -> new CustomException(ErrorCode.BOARD_DATA_NOT_FOUND));
        String processContent = imageService.processContentWithImages(board, req.getContent());

        req.setContent(processContent);
        board.updateBoard(req);
        return CommonUtils.successResponseNoData(board.getBoardSid(), "게시글이 수정되었습니다.", ErrorCode.UPDATE_OPERATION_FAILED);
    }

    /* 게시글 삭제 */
    @Transactional
    public ResComResult boardDelete(Long boardSid) {
        Board board = boardRepository.findById(boardSid).orElseThrow(() -> new CustomException(ErrorCode.BOARD_DATA_NOT_FOUND));
        boardRepository.delete(board);
        return CommonUtils.successResponseNoData(board.getBoardSid(), "게시글이 삭제되었습니다.", ErrorCode.DELETE_OPERATION_FAILED);
    }


}
