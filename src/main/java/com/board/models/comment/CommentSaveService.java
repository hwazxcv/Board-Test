package com.board.models.comment;

import com.board.commons.MemberUtil;
import com.board.controllers.comments.CommentForm;
import com.board.controllers.comments.CommentFormValidator;
import com.board.entities.BoardData;
import com.board.entities.CommentData;
import com.board.models.board.BoardDataNotFoundException;
import com.board.repositories.BoardDataRepository;
import com.board.repositories.CommentDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

@Service
@RequiredArgsConstructor
public class CommentSaveService {

    private final CommentDataRepository commentDataRepository;
    private final BoardDataRepository boardDataRepository;
    private final CommentInfoService commentInfoService;

    private final CommentFormValidator commentFormValidator;
    private final MemberUtil memberUtil;
    private final PasswordEncoder passwordEncoder;

    public void save(CommentForm form , Errors errors ){
        commentFormValidator.validate(form, errors);
        if(errors.hasErrors()){
            return;
        }

        CommentData commentData =null;
        Long seq = form.getSeq();
        if(seq == null){ //추가 -- 게시글 번호, 회원 정보는 바뀌면 안되는 데이터
            commentData = new CommentData();
            Long boardDataSeq = form.getBoardDataSeq();
            BoardData boardData = boardDataRepository.findById(boardDataSeq).orElseThrow(BoardDataNotFoundException::new);
            commentData.setBoardData(boardData);
            commentData.setMember(memberUtil.getMember());
        }else { // 수정
            commentData = commentDataRepository.findById(seq).orElseThrow(CommentNotFoundException::new);
        }
        commentData.setPoster(form.getPoster());
        commentData.setContent(form.getContent());

        String guestPw = form.getGuestPw();
        if(StringUtils.hasText(guestPw)){
            commentData.setGuestPw(passwordEncoder.encode(guestPw));
        }
        save(commentData);
    }


    public void save(CommentData comment){

        commentDataRepository.saveAndFlush(comment);

        //총 댓글 개수 업데이트
        Long boardDataSeq = comment.getBoardData().getSeq();
        commentInfoService.updateCommentCnt(boardDataSeq);
    }
}
