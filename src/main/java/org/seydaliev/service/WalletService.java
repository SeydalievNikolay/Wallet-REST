package org.seydaliev.service;


import org.seydaliev.dto.WalletDTO;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletService {
    boolean updateWallet(WalletDTO walletDTO);
    BigDecimal getBalanceByUUID(UUID uuid);
}
