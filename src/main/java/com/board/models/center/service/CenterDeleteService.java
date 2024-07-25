package com.board.models.center.service;

import com.board.commons.Utils;
import com.board.commons.exceptions.AlertBackException;
import com.board.models.center.entity.CenterInfo;
import com.board.models.center.repositories.CenterInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CenterDeleteService {

    private final CenterInfoRepository centerInfoRepository;
    private final Utils utils;

    public void deleteList(List<Integer> chks){
        if(chks == null || chks.isEmpty()){
            throw new AlertBackException("삭제할 센터를 선택 해주세요" , HttpStatus.BAD_REQUEST);
        }

        for(int chk : chks){
            Long cCode = Long.valueOf(utils.getParam("cCode_"+chk));
            CenterInfo data = centerInfoRepository.findById(cCode).orElse(null);
            if(data == null) continue;

            centerInfoRepository.delete(data);
        }
        centerInfoRepository.flush();
    }

}
