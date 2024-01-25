package org.seydaliev.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
//Когда нет денег на счету
@ResponseStatus(value = HttpStatus.PAYMENT_REQUIRED)
public class WalletPaymentException extends RuntimeException{
    public WalletPaymentException(String message) {
        super(message);
    }
}
