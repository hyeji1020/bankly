package com.project.bankassetor.model.entity.account.save;

import com.project.bankassetor.model.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Table(name = "saving_product_account")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingProductAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JoinColumn(name = "saving_product_id", nullable = false)
    private Long savingProductId;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "saving_account_number", nullable = false)
    private Long savingAccountNumber;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    private AccountStatus accountStatus;

    @Column(name = "monthly_deposit", nullable = false)
    private BigDecimal monthlyDeposit;
}
