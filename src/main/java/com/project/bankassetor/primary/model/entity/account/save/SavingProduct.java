package com.project.bankassetor.primary.model.entity.account.save;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Table(name = "saving_product")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @Column(name = "saving_limit", nullable = false)
    private BigDecimal savingLimit;

    @Column(name = "interest_rate", nullable = false)
    private BigDecimal interestRate;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "saving_duration_id", nullable = false)
    private SavingDuration savingDuration;

}
