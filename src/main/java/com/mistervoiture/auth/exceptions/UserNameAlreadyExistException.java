package com.mistervoiture.auth.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class UserNameAlreadyExistException extends RuntimeException{

    private List<String> usernames;

    public UserNameAlreadyExistException(String message){
        super(message);
    }

    public UserNameAlreadyExistException(String message , List<String> usernames){
        super(message);
        this.usernames = usernames;
    }

    public UserNameAlreadyExistException(String message , Throwable throwable){
        super(message , throwable);
    }

    public List<String> getUsernames() {
        return usernames;
    }
}
