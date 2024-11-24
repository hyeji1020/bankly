package com.project.bankassetor.primary.model.entity.account.check;

import com.project.bankassetor.primary.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Table(name = "checking_account")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckingAccount extends BaseEntity {

    @JoinColumn
    private Long accountId;

    @JoinColumn
    private long memberId;

}
