package com.project.bankassetor.primary.model.response;

import com.project.bankassetor.primary.model.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Getter
public class TerminateResponse {
    BigDecimal finalPayment;
    BigDecimal penaltyAmount;
    BigDecimal interest;
    AccountStatus status;

}
