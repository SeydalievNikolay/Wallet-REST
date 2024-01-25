package org.seydaliev.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
//Когда неправильный Json
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WalletValidatedException extends RuntimeException{
    public WalletValidatedException(String message) {
        super(message);
    }
}
