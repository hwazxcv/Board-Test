package com.board.commons.rest;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class JSONData<T> {

    private boolean success=true;
    private HttpStatus status = HttpStatus.OK;
    @NonNull
    private T data; //성공시 넘어갈 데이터
    private String message; // success가 false시 나오는 메세지


}
