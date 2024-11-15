package com.board.models.board.config;

import com.board.commons.Utils;
import com.board.commons.constants.BoardAuthority;
import com.board.commons.exceptions.AlertException;
import com.board.controllers.admins.BoardConfigForm;
import com.board.entities.Board;
import com.board.repositories.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardConfigSaveService {

    private final BoardRepository boardRepository;
    private final Utils utils;

    public void save(BoardConfigForm form) {

        String bId = form.getBId();
        String mode = Objects.requireNonNullElse(form.getMode(),"add");
        Board board = null;
        if (mode.equals("edit") && StringUtils.hasText(bId)) {
            board = boardRepository.findById(bId).orElseThrow(BoardNotFoundException::new);
        } else {
            board = new Board();
            board.setBId(bId);
        }

        board.setBName(form.getBName());
        board.setActive(form.isActive());
        board.setAuthority(BoardAuthority.valueOf(form.getAuthority()));
        board.setCategory(form.getCategory());

        boardRepository.saveAndFlush(board);
    }

    /**
     * 게시판 목록 수정
     *
     * @param idxes
     * */
    public void update(List<Integer> idxes) {
        if (idxes == null || idxes.isEmpty()) {
            throw new AlertException("수정할 게시판을 선택하세요." , HttpStatus.BAD_REQUEST);
        }

        for (int idx : idxes) {
            String bId = utils.getParam("bId_" + idx);
            Board board = boardRepository.findById(bId).orElse(null);
            if (board == null) continue;

            String bName = utils.getParam("bName_" + idx);
            boolean active = Boolean.parseBoolean(utils.getParam("active_" + idx));
            BoardAuthority authority =
                    BoardAuthority.valueOf(utils.getParam("authority_" + idx));

            board.setBName(bName);
            board.setActive(active);
            board.setAuthority(authority);
        }

        boardRepository.flush();
    }
}
