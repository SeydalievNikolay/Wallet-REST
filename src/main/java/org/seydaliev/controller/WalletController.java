package org.seydaliev.controller;

import lombok.RequiredArgsConstructor;
import org.seydaliev.dto.WalletDTO;
import org.seydaliev.exception.WalletValidatedException;
import org.seydaliev.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PatchMapping
    public ResponseEntity<Boolean> getWallet(@RequestBody @Valid WalletDTO walletDTO) {
      /*  if (walletDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        boolean result = false;
        try {
            result = walletService.updateWallet(walletDTO);
        } catch (Exception e) {
            throw new WalletValidatedException(e.getMessage());
        }
        return ResponseEntity.ok(result);*/
        boolean result = walletService.updateWallet(walletDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{WALLET_UUID}")
    public BigDecimal getBalance(@PathVariable("WALLET_UUID") UUID walletUuid) {
        return walletService.getBalanceByUUID(walletUuid);
    }
}
