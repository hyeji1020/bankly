package com.project.bankassetor.primary.model.entity.account.save;

import com.project.bankassetor.primary.model.entity.BaseEntity;
import com.project.bankassetor.primary.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "saving_tx_history")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingTransactionHistory extends BaseEntity {

    @JoinColumn
    private Long savingAccountId;

    @JoinColumn
    private Long accountId;

    @JoinColumn
    private Long memberId;

    @JoinColumn
    private Long savingProductId;

    private BigDecimal balance;    // 거래 후 잔액

    private BigDecimal txAmount;  // 거래 금액

    private int depositRound; // 몇 번째 입금인지

    @Enumerated(EnumType.STRING)
    private TransactionType txType; // 거래 유형

    private LocalDateTime txTime;  // 거래 시간

}
