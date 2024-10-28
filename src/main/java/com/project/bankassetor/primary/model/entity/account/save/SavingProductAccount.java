package com.project.bankassetor.primary.model.entity.account.save;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @JoinColumn(name = "saving_duration_id", nullable = false)
    private Long savingDurationId;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @JoinColumn(name = "saving_account_id", nullable = false)
    private Long savingAccountId;

    @JoinColumn(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "monthly_deposit", nullable = false)
    private BigDecimal monthlyDeposit;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

}
