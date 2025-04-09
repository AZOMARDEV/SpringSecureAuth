package com.mistervoiture.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class AlreadyExistException extends RuntimeException{

    public AlreadyExistException(String message){
        super(message);
    }

    public AlreadyExistException(String message , Throwable throwable){
        super(message , throwable);
    }
}
