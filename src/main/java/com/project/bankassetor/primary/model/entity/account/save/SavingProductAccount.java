package com.project.bankassetor.primary.model.entity.account.save;

import com.project.bankassetor.primary.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Table(name = "saving_product_account")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingProductAccount extends BaseEntity {

    @JoinColumn
    private Long accountId;

    @JoinColumn
    private Long memberId;

    @JoinColumn
    private Long savingProductId;

    @JoinColumn
    private Long savingDurationId;

    private BigDecimal monthlyDeposit;

    private LocalDate startDate;

    private LocalDate endDate;

}
