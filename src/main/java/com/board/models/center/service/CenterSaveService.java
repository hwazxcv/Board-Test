package com.board.models.center.service;

import com.board.commons.Utils;
import com.board.commons.exceptions.AlertBackException;
import com.board.models.center.RequestCenter;
import com.board.models.center.entity.CenterInfo;
import com.board.models.center.repositories.CenterInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CenterSaveService {

    private final CenterInfoRepository centerInfoRepository;
    private final Utils utils;


    public CenterInfo save(RequestCenter form){
        String mode =form.getMode();
        Long cCode = form.getCCode();

        mode = StringUtils.hasText(mode) ? mode : "add_center";
        CenterInfo data = null;
        if(mode.equals("edit_center") && cCode !=null){
            data = centerInfoRepository.findBycCode(cCode).orElseThrow(CenterNotFoundException::new);
        } else {
            data = new CenterInfo();
        }

        data.setCName(form.getCName());
        data.setZonecode(form.getZonecode());
        data.setAddress(form.getAddress());
        data.setTelNum(form.getTelNum());
        data.setOperHour(form.getOperHour());

        // 예약 요일 결정
        List<String> bookYoil = form.getBookYoil();
        String bookYoilStr = bookYoil == null ? null: bookYoil.stream().collect(Collectors.joining(","));
        data.setBookYoil(bookYoilStr);

        String bookAvl = String.format("%s:%s-%s:%s",
                form.getBookAvlShour(),
                form.getBookAvlSmin(),
                form.getBookAvlEhour(),
                form.getBookAvlEmin()
                );

        data.setBookAvl(bookAvl);
        data.setBookNotAvl(form.getBookNotAvl());
        data.setBookHday(form.isBookHday());
        data.setBookBlock(form.getBookBlock());
        data.setBookCapacity(form.getBookCapacity());

        centerInfoRepository.saveAndFlush(data);
        return data;
    }

    public void saveList(List<Integer> chks) {
        if (chks == null || chks.isEmpty()) {
            throw new AlertBackException("수정할 센터를 선택해주세요", HttpStatus.BAD_REQUEST);
        }
        for (int chk : chks) {
            Long cCode = Long.valueOf(utils.getParam("cCode_" + chk));
            CenterInfo data = centerInfoRepository.findById(cCode).orElse(null);
            if (data == null) continue;

            String bookYoil = Arrays.stream(utils.getParams("bookYoil_" + chk)).collect(Collectors.joining(",")); // 월 , 화 , 수
            data.setBookYoil(bookYoil);

            String bookAvl = String.format("%s:%s-%s:%s",
                    utils.getParam("bookAvlShour_" + chk),
                    utils.getParam("bookAvlSmin_" + chk),
                    utils.getParam("bookAvlEhour_" + chk),
                    utils.getParam("bookAvlEmin_" + chk));
            data.setBookAvl(bookAvl);

            boolean bookHday = Boolean.parseBoolean(utils.getParam("bookHday_"+chk));
            data.setBookHday(bookHday);

            int bookBlock = Integer.parseInt(utils.getParam("bookBlock_"+chk));
            data.setBookBlock(bookBlock);

        }
        centerInfoRepository.flush();
    }
}
