package org.seydaliev.service.impl;

import lombok.RequiredArgsConstructor;
import org.seydaliev.dto.WalletDTO;
import org.seydaliev.exception.NotFoundException;
import org.seydaliev.exception.WalletPaymentException;
import org.seydaliev.exception.WalletValidatedException;
import org.seydaliev.model.OperationType;
import org.seydaliev.model.Wallet;
import org.seydaliev.repository.WalletRepository;
import org.seydaliev.service.WalletService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    public boolean updateWallet(WalletDTO walletDTO) {
        if (walletDTO.getUuid() != null) {
            String uuid = walletDTO.getUuid().toString();
            Wallet wallet = walletRepository.findByUuid(uuid)
                    .orElseThrow(() -> new NotFoundException
                            (String.format("Wallet is not exist" + uuid)));
            if (isValidOperationWallet(walletDTO.getOperationType())) {
                throw new WalletValidatedException("Invalid operation type");
            }
            BigDecimal amount = walletDTO.getAmount();
            if (wallet.getAmount().compareTo(amount) < 0) {
                throw new WalletPaymentException(String.format("Insufficient funds"));
            }
            wallet.setAmount(walletDTO.getAmount());
            walletRepository.save(wallet);
            return true;
        }
        return false;
    }

    private boolean isValidOperationWallet(OperationType operationType) {

        if (operationType.name().equals("WITHDRAW")) {
            return true;
        }
        if (operationType.name().equals("DEPOSIT")) {
            return true;
        }
        return false;
    }

    @Override
    public BigDecimal getBalanceByUUID(UUID uuid) {
        Wallet wallet = null;
        if (uuid != null) {
            String uid = uuid.toString();
            wallet = walletRepository.findByUuid(uid)
                    .orElseThrow(() -> new NotFoundException
                            (String.format("Wallet is not exist" + uuid)));
        }
        return wallet.getAmount();
    }
}
