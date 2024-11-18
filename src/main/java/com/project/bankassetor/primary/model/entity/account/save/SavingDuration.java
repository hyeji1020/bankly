package com.project.bankassetor.primary.model.entity.account.save;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.project.bankassetor.primary.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "saving_duration")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingDuration extends BaseEntity {

    private int durationInMonths;

    private String description;
}
