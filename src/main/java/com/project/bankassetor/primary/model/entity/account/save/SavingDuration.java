package com.project.bankassetor.primary.model.entity.account.save;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "saving_duration")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingDuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "duration_in_months", nullable = false)
    private int durationInMonths;

    @Column(name = "description")
    private String description;
}
