package com.project.bankassetor.primary.model.entity.account.save;

import com.project.bankassetor.primary.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Table(name = "saving_account")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingAccount extends BaseEntity {

    @JoinColumn
    private Long accountId;

    @JoinColumn
    private Long memberId;

    @JoinColumn
    private Long savingProductId;

    private BigDecimal monthlyDeposit;

    private int currentDepositCount; // 현재 입금된 회차

    private int totalDepositCount;   // 총 입금해야 할 회차

    private LocalDate startDate;

    private LocalDate endDate;

}
