package org.seydaliev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.seydaliev.model.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO {
    private UUID uuid;
    private OperationType operationType;
    private BigDecimal amount;
}
