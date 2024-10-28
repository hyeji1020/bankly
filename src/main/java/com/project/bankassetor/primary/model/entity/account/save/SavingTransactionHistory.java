package com.project.bankassetor.primary.model.entity.account.save;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;

    @Column(name = "transaction_amount")
    private int transactionAmount;

    @Column(name = "balance")
    private int balance;
}
