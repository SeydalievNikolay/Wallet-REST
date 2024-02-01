package org.seydaliev;

import org.seydaliev.dto.WalletDTO;
import org.seydaliev.model.OperationType;
import org.seydaliev.service.WalletService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}