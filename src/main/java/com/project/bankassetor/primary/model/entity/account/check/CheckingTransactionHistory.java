package com.project.bankassetor.primary.model.entity.account.check;

import com.project.bankassetor.primary.model.entity.BaseEntity;
import com.project.bankassetor.primary.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "checking_transaction_history")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckingTransactionHistory extends BaseEntity {

    @JoinColumn
    private Long bankAccountId;

    @JoinColumn
    private Long accountId;

    @JoinColumn
    private Long memberId;

    private LocalDateTime transactionTime;  // 거래 시간

    private BigDecimal transactionAmount;  // 거래 금액

    private BigDecimal balance;    // 거래 후 잔액

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // 거래 유형

    // private String transactionPlace; // 사용처

}
