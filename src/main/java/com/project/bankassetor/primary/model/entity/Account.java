package com.project.bankassetor.primary.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.bankassetor.primary.model.enums.AccountStatus;
import com.project.bankassetor.primary.model.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "account")
@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity{

    private String accountNumber;

    private BigDecimal balance;

    private BigDecimal depositLimit;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Enumerated(EnumType.STRING)
    private AccountType type;

}
