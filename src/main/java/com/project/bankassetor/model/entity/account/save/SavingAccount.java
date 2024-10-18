package com.project.bankassetor.model.entity.account.save;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "saving_account")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JoinColumn(name = "account_id", nullable = false)
    private Long accountId;
}

