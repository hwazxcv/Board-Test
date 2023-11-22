package com.board.commons.configs;


import com.board.entities.Configs;
import com.board.repositories.ConfigsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//추가 수정을 위한 클래스
@Service
@RequiredArgsConstructor
public class ConfigSaveService {

    private final ConfigsRepository repository;

    //어떠한 객체가 들어올지 모르니 T(지네릭)클래스를 받는다.
    public <T> void save (String code ,T value){
        //들어온 코드가 있으면 쓰고 없으면 새로운 엔티티를 만들어 사용
        Configs config = repository.findById(code).orElseGet(Configs::new);
        ObjectMapper om = new ObjectMapper();
        //자바 타입패키지에있는 값 변환
        om.registerModule(new JavaTimeModule());

        String json = null;
        try {
            //자바 객체를 문자열로 변환 한다.
            json =om.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        config.setCode(code);
        config.setValue(json);
        repository.saveAndFlush(config );

    }

}
