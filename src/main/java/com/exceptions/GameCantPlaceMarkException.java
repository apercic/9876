package com.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GameCantPlaceMarkException extends RuntimeException {

    public GameCantPlaceMarkException(Integer column) {
        super("Could not place mark in column " + column);
    }
}
