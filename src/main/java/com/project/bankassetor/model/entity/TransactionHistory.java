package com.project.bankassetor.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount;

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;  // 거래 시간

    @Column(name = "transaction_amount")
    private int transactionAmount;  // 거래 금액

    @Column(name = "balance")
    private int balance;    // 거래 후 잔액
    
    // private String transactionType; // 거래 유형
    // private String transactionPlace; // 사용처

}
