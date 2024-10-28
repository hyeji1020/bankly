package com.project.bankassetor.primary.model.entity.account.check;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "checking_account")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckingAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JoinColumn(name = "account_id", nullable = false)
    private Long accountId;
}
