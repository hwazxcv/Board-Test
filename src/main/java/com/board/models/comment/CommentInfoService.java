package com.board.models.comment;

import com.board.commons.MemberUtil;
import com.board.commons.Utils;
import com.board.commons.exceptions.AlertBackException;
import com.board.controllers.comments.CommentForm;
import com.board.entities.BoardData;
import com.board.entities.CommentData;
import com.board.entities.Member;
import com.board.entities.QCommentData;
import com.board.models.board.BoardDataNotFoundException;
import com.board.models.board.RequiredPasswordCheckException;
import com.board.repositories.BoardDataRepository;
import com.board.repositories.CommentDataRepository;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CommentInfoService {

    private final CommentDataRepository commentDataRepository;
    private final EntityManager entityManager;
    private final BoardDataRepository boardDataRepository;
    private final MemberUtil memberUtil;
    private final HttpSession session;
    private final PasswordEncoder passwordEncoder;

    //개별 조회
    public CommentData get(Long seq){
        CommentData comment = commentDataRepository.findById(seq).orElseThrow(CommentNotFoundException::new);
        return comment;
    }

    public CommentForm getForm(Long seq){
        CommentData comment = get(seq);
        CommentForm form = new ModelMapper().map(comment,CommentForm.class);
        form.setBoardDataSeq(comment.getBoardData().getSeq());

        return form;
    }

    //목록 조회 -- 게시글 번호
    public List<CommentData> getList(Long boardDataSeq){
        QCommentData commentData = QCommentData.commentData;

        PathBuilder<CommentData> pathBuilder = new PathBuilder<>(CommentData.class , "commentData");

        List<CommentData> items = new JPAQueryFactory(entityManager)
                .select(commentData)
                .where(commentData.boardData.seq.eq(boardDataSeq)).leftJoin(commentData.member)
                .fetchJoin()
                .orderBy(new OrderSpecifier(Order.ASC,pathBuilder.get( "createdAt")))
                .fetch();

        return items;
    }

    //댓글 수 업데이트
    public void updateCommentCnt(Long seq){
       BoardData boardData = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new) ;
        boardData.setCommentCnt(commentDataRepository.getTotal(seq));

        boardDataRepository.flush();
    }
    public void isMine(Long seq){
        if(memberUtil.isAdmin()){
            return;
        }
        CommentData data= get(seq);
        Member commentMember = data.getMember();
        if(commentMember == null){ // 비회원 작성
            String key = "chk_comment_" + seq;
            if(session.getAttribute(key) == null){// 비회원 비밀번호 확인 전
                session.setAttribute("comment_seq" , seq);
                throw new RequiredPasswordCheckException();
            }
        }else{ // 로그인 상태 작성
            if(!memberUtil.isLogin() || commentMember.getUserNo().longValue() !=memberUtil.getMember().getUserNo().longValue()){
                throw new AlertBackException(Utils.getMessage("작성한_댓글만_수정_삭제_가가능합니다." , "error"), HttpStatus.BAD_REQUEST );
            }
        }
    }

    //수정 삭제 가능 여부 ( 버튼 노출 여부 결정)
    public boolean isEditable(CommentData comment){
        Member commentMember = comment.getMember();
        if(memberUtil.isAdmin() || commentMember ==null){ //관리자는 버튼이 항상 노출
            return true;
        }
        //회원 댓글이면 직접 작성한 댓글만 노출
        if(memberUtil.isLogin() && commentMember != null && commentMember.getUserNo().longValue() == memberUtil.getMember().getUserNo().longValue()){
            return true;
        }
        return false;
    }


    //비회원 비밀번호 확인
    public boolean checkGuestPassword(Long seq , String password){
        CommentData comment = get(seq);
        return passwordEncoder.matches(password,comment.getGuestPw());
    }
}
