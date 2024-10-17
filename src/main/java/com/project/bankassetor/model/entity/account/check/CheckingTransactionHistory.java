package com.project.bankassetor.model.entity.account.check;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;  // 거래 시간

    @Column(name = "transaction_amount")
    private int transactionAmount;  // 거래 금액

    @Column(name = "balance")
    private int balance;    // 거래 후 잔액
    
    // private String transactionType; // 거래 유형
    // private String transactionPlace; // 사용처

}
