package com.project.bankassetor.model.entity;

import com.project.bankassetor.model.enums.AccountStatus;
import com.project.bankassetor.model.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "account")
@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "account_number", nullable = false)
    private long accountNumber;

    @Column(name = "balance", nullable = false)
    private int balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    private AccountStatus accountStatus;

}
