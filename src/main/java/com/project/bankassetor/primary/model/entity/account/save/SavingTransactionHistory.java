package com.project.bankassetor.primary.model.entity.account.save;

import com.project.bankassetor.primary.model.entity.BaseEntity;
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
public class SavingTransactionHistory extends BaseEntity {

    @JoinColumn
    private Long savingAccountId;

    @JoinColumn
    private Long accountId;

    @JoinColumn
    private Long memberId;

    @JoinColumn
    private Long savingProductId;

    @JoinColumn
    private Long savingDurationId;

    private BigDecimal balance;    // 거래 후 잔액

    private BigDecimal transactionAmount;  // 거래 금액

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // 거래 유형

    private LocalDateTime transactionTime;  // 거래 시간

}
