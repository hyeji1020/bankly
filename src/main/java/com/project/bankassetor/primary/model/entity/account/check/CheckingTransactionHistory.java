package com.project.bankassetor.primary.model.entity.account.check;

import com.project.bankassetor.primary.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "checking_transaction_history")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckingTransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JoinColumn(name = "bank_account_id", nullable = false)
    private Long bankAccountId;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @JoinColumn(name = "checking_account_id", nullable = false)
    private Long checkingAccountId;

    @JoinColumn(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;  // 거래 시간

    @Column(name = "transaction_amount")
    private int transactionAmount;  // 거래 금액

    @Column(name = "balance")
    private int balance;    // 거래 후 잔액

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType; // 거래 유형

    // private String transactionPlace; // 사용처

}
