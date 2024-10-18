package com.project.bankassetor.model.entity.account.check;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Table(name = "bank_account")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @JoinColumn(name = "account_id", nullable = false)
    private long accountId;

    @JoinColumn(name = "user_id", nullable = false)
    private long userId;

}
