package com.project.bankassetor.batch;

import com.project.bankassetor.service.perist.SavingProductAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
public class ScheduledBatchConfig {

    private final SavingProductAccountService productAccountService;

    public ScheduledBatchConfig(SavingProductAccountService productAccountService) {
        this.productAccountService = productAccountService;
    }

    // 매일 자정에 만기 계좌 상태를 확인하여 갱신
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void updateExpiredAccounts() {
        log.info("만기된 적금 계좌 상태 업데이트 배치 작업 시작");
        productAccountService.expireMaturedAccounts();
        log.info("만기된 적금 계좌 상태 업데이트 배치 작업 완료");
    }
}