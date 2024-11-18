package com.project.bankassetor.primary.model.response;

import com.project.bankassetor.primary.model.entity.account.save.SavingProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
public class SavingProductResponse {

    private long id;
    private String name;
    private String savingLimit;
    private String interestRate;
    private int durationInMonths;
    private String description;

    private static String formatRate(BigDecimal rate) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(rate);
    }

    private static String formatLimit(BigDecimal limit) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(limit);
    }

    public static List<SavingProductResponse> of(List<SavingProduct> savingProducts) {
        return savingProducts.stream()
                .map(savingProduct -> SavingProductResponse.builder()
                        .id(savingProduct.getId())
                        .name(savingProduct.getName())
                        .savingLimit(formatLimit(savingProduct.getSavingLimit()))
                        .interestRate(formatRate(savingProduct.getInterestRate()))
                        .durationInMonths(savingProduct.getSavingDuration().getDurationInMonths())
                        .description(savingProduct.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    public static SavingProductResponse of(SavingProduct savingProduct) {
        return SavingProductResponse.builder()
                        .id(savingProduct.getId())
                        .name(savingProduct.getName())
                        .savingLimit(formatLimit(savingProduct.getSavingLimit()))
                        .interestRate(formatRate(savingProduct.getInterestRate()))
                        .durationInMonths(savingProduct.getSavingDuration().getDurationInMonths())
                        .description(savingProduct.getDescription())
                        .build();
    }
}
