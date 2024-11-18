package com.project.bankassetor.primary.model.entity.account.save;

import com.project.bankassetor.primary.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Table(name = "saving_product")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingProduct extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "savingDurationId", nullable = false)
    private SavingDuration savingDuration;

    private String name;

    private BigDecimal savingLimit;

    private BigDecimal interestRate;

    private String description;

}
