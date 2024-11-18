package com.project.bankassetor.primary.model.entity.account.check;

import com.project.bankassetor.primary.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Table(name = "bank_account")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BankAccount extends BaseEntity {

    @JoinColumn
    private Long accountId;

    @JoinColumn
    private long memberId;

}
