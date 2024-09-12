package com.example.boardapi.service;

import com.example.boardapi.entity.Board;
import com.example.boardapi.entity.Member;
import com.example.boardapi.enums.DeleteYn;
import com.example.boardapi.exception.ErrorCode;
import com.example.boardapi.repository.BoardRepository;
import com.example.boardapi.repository.ImageRepository;
import com.example.boardapi.request.board.ReqBoardAdd;
import com.example.boardapi.response.ResComResult;
import com.example.boardapi.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ImageService imageService;

    @Transactional
    public ResComResult boardAdd(ReqBoardAdd req, Member member){
        Board board = Board.builder()
                .title(req.getTitle())
                .member(member)
                .deleteYn(DeleteYn.D.getCode())
                .build();
         String processContent = imageService.processContentWithImages(board,req.getContent());

         board.saveProcessedContent(processContent);
         return CommonUtils.successResponseNoData(board.getBoardSid(),"글이 등록되었습니다.", ErrorCode.INSERT_OPERATION_FAILED);
    }
}
