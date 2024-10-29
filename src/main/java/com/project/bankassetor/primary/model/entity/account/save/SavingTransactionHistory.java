package com.project.bankassetor.primary.model.entity.account.save;

import com.project.bankassetor.primary.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "saving_transaction_history")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingTransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JoinColumn(name = "saving_product_account_id", nullable = false)
    private Long savingProductAccountId;

    @JoinColumn(name = "saving_product_id", nullable = false)
    private Long savingProductId;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @JoinColumn(name = "saving_account_id", nullable = false)
    private Long savingAccountId;

    @JoinColumn(name = "account_id", nullable = false)
    private Long accountId;

    @JoinColumn(name = "saving_duration_id", nullable = false)
    private Long savingDurationId;

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;  // 거래 시간

    @Column(name = "transaction_amount")
    private BigDecimal transactionAmount;  // 거래 금액

    @Column(name = "balance")
    private BigDecimal balance;    // 거래 후 잔액

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType; // 거래 유형
}
