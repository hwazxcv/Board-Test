package com.board.restcontrollers.board;

import com.board.commons.exceptions.BadRequestException;
import com.board.commons.rest.JSONData;
import com.board.controllers.boards.BoardForm;
import com.board.entities.BoardData;
import com.board.models.board.BoardInfoService;
import com.board.models.board.BoardSaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class ApiBoardController {

    private final BoardSaveService saveService;
    private final BoardInfoService infoService;

    //게시글 쓰기
    @PostMapping("/write/{bId}")
    public ResponseEntity<JSONData<Object>> write(@PathVariable String bId , @RequestBody @Valid BoardForm form, Errors errors){
        if(errors.hasErrors()){ // 에러를 JSON으로
            String message = errors.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
            throw new BadRequestException(message, HttpStatus.BAD_REQUEST);
        }
        saveService.save(form);

        return ResponseEntity.status(HttpStatus.CREATED).build(); //응답 코드 200
    }
    @GetMapping("/view/{seq}")
    public JSONData<BoardData> view(@PathVariable Long seq) {
        BoardData data = infoService.get(seq);
        return new JSONData<>(data);
    }

}
