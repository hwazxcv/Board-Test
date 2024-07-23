package com.board.models.center.service;

import com.board.commons.ListData;
import com.board.commons.Pagination;
import com.board.commons.Utils;
import com.board.models.center.RequestCenter;
import com.board.models.center.controller.CenterSearch;
import com.board.models.center.entity.CenterInfo;
import com.board.models.center.entity.QCenterInfo;
import com.board.models.center.repositories.CenterInfoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class CenterInfoService {

    private final CenterInfoRepository infoRepository;
    private final HttpServletRequest request;

    public CenterInfo get(Long cCode){
        CenterInfo data = infoRepository.findById(cCode).orElseThrow(CenterNotFoundException::new);

        addCenterInfo(data);
        return data;
    }

    public RequestCenter getForm(Long cCode){
        CenterInfo data = get(cCode);
        RequestCenter form = new ModelMapper().map(data, RequestCenter.class);

        String bookYoil = data.getBookYoil();
        if(StringUtils.hasText(bookYoil)){
            List<String> yoils= Arrays.stream(bookYoil.split(",")).toList();
            form.setBookYoil(yoils);
        }
        form.setMode("edit_center");
        return form;
    }

    public ListData<CenterInfo> getList(CenterSearch search) {
        int page = Utils.getNumber(search.getPage(), 1);
        int limit = Utils.getNumber(search.getLimit(), 20);

        BooleanBuilder andBuilder = new BooleanBuilder();
        QCenterInfo centerInfo = QCenterInfo.centerInfo;

        /* 검색 조건 처리 S */
        String sopt = search.getSopt();
        String skey = search.getSkey();
        sopt = StringUtils.hasText(sopt) ? sopt : "all";
        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

            BooleanExpression cNameCond = centerInfo.cName.contains(skey);
            BooleanExpression addressCond = centerInfo.address.contains(skey);
            BooleanExpression addressSubCond = centerInfo.addressSub.contains(skey);

            if (sopt.equals("cName")) {
                andBuilder.and(cNameCond);
            } else if (sopt.equals("address")) {
                BooleanBuilder orBuilder = new BooleanBuilder();
                andBuilder.and(orBuilder.or(addressCond).or(addressSubCond));
            } else {
                BooleanBuilder orBuilder = new BooleanBuilder();
                andBuilder.and(orBuilder.or(addressCond).or(addressSubCond).or(cNameCond));
            }

        }
        /* 검색 조건 처리 E */

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));

        Page<CenterInfo> data = infoRepository.findAll(andBuilder, pageable);

        // 센터 추가 정보 처리
        data.getContent().forEach(this::addCenterInfo);

        Pagination pagination = new Pagination(page, (int)data.getTotalElements(), 10, limit, request);

        return new ListData<>(data.getContent(), pagination);
    }



    
    private void addCenterInfo(CenterInfo data){
        String bookAvl = data.getBookAvl();

        // Pattern 으로 정규표현식에 대한 문자열 검증
        Pattern pattern = Pattern.compile("(\\d{2}):(\\d{2})-(\\d{2}):(\\d{2})");

        //Matcher로 Pattern클래스를 받아 대상 문자열과 패턴이 일치하는 부분을 찾거나 전체 일치 여부 판단
        Matcher matcher = pattern.matcher(bookAvl);
        if(matcher.find()){
            data.setBookAvlShour(matcher.group(1));
            data.setBookAvlSmin(matcher.group(2));
            data.setBookAvlEhour(matcher.group(3));
            data.setBookAvlEmin(matcher.group(4));
        }

    }
}
