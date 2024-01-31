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
  /*  private static WalletService walletService;

    public Application(WalletService walletService) {
        this.walletService = walletService;
    }
*/
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        /*WalletDTO walletDTO = new WalletDTO();
        walletDTO.setUuid(UUID.randomUUID());
        walletDTO.setOperationType(OperationType.DEPOSIT);
        walletDTO.setAmount(BigDecimal.valueOf(10));

        for (int i = 0; i < 100; i++) {
            walletService.updateWallet(walletDTO);
            BigDecimal balance = walletService.getBalanceByUUID(walletDTO.getUuid());
            System.out.println(balance);
        }*/
    }
}