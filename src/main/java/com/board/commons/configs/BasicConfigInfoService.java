package com.board.commons.configs;

import com.board.entities.Configs;
import com.board.repositories.ConfigsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * 추가된 기능에 대한 인터셉터 설정 클래스 --> 메서드이름(get -> BasicGet)
 * 추가적인 인터셉터 설정 --> 차이점은 반환값을 T에서 Optional로 변경
 */
@Service
@RequiredArgsConstructor
public class BasicConfigInfoService {
    private final ConfigsRepository repository;

    public <T> Optional<T> get(String code, Class<T> clazz) {
        return get(code, clazz, null);
    }

    public <T> Optional<T> get(String code, TypeReference<T> typeReference) {
        return get(code, null, typeReference);
    }

    public <T> Optional<T> get(String code, Class<T> clazz, TypeReference<T> typeReference) {
        Configs config = repository.findById(code).orElse(null);
        if (config == null || !StringUtils.hasText(config.getValue())) {
            return Optional.ofNullable(null);
        }

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());

        String jsonString = config.getValue();
        try {
            T data = null;
            if (clazz == null) { // TypeRefernce로 처리
                data = om.readValue(jsonString, new TypeReference<T>() {});
            } else { // Class로 처리
                data = om.readValue(jsonString, clazz);
            }
            return Optional.ofNullable(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.ofNullable(null);
        }
    }
}
