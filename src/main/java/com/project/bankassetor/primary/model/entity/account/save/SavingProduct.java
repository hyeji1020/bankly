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

    private String name;

    private BigDecimal savingLimit;

    private BigDecimal interestRate;    // 연 이자율

    private BigDecimal termInterestRate;  // 중도 해지 이율

    private String description;

    private int durationInMonths;

}
