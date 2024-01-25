package org.seydaliev.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
@Entity
@Data
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id", nullable = false)
    private Long walletId;
    @Enumerated(EnumType.STRING)
    @Column(length = 8, nullable = false)
    private OperationType operationType;
    private BigDecimal amount;
    @Column(unique = true)
    private String uuid;
}
