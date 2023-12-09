package com.board.models.board;

import com.board.entities.BoardData;
import com.board.repositories.BoardDataRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class BoardInfoService {


    private final BoardDataRepository boardDataRepository;
    private final HttpSession session;
    private final PasswordEncoder encoder;

    //게시글 하나 조회
    public BoardData get(Long seq){
        BoardData data = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new);

        return data;
    }


    public boolean checkGuestPassword(Long seq, String password){
        BoardData data = get(seq);
        String guestPw = data.getGusstPw();
        if(!StringUtils.hasText(guestPw)){
            return false;
        }
        return encoder.matches(password , guestPw);

    }


}
