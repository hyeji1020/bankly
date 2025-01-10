package com.project.bankassetor.service.perist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConfigServiceTest {

    @Autowired
    private ConfigService configService;

    private static final int TEST_COUNT = 10000;

    private static final String CODE = "access-log.enabled";

    @Test
    @DisplayName("캐시 동작 검증: 첫 요청과 캐시 조회 시간 비교")
    void testCachePerformance() {

        long startTime1 = System.currentTimeMillis();
        configService.getConfigInCache(CODE); // 첫 번째 요청
        long endTime1 = System.currentTimeMillis();
        System.out.println("First request time: " + (endTime1 - startTime1) + "ms");

        long startTime2 = System.currentTimeMillis();
        configService.getConfigInCache(CODE); // 두 번째 요청 (캐시)
        long endTime2 = System.currentTimeMillis();
        System.out.println("Second request time (cache): " + (endTime2 - startTime2) + "ms");
    }

    @Test
    @DisplayName("DB 조회 vs 캐싱 조회 평균 시간 비교")
    void testDbAndCachePerformance() {

        // 첫 번째 요청: 캐시에 데이터 저장을 위해 실행
        configService.getConfigInCache(CODE);

        // DB 조회 평균 시간 측정
        long dbTotalTime = 0;
        for (int i = 0; i < TEST_COUNT; i++) {
            long startTime = System.nanoTime();
            configService.getConfig(CODE);
            long endTime = System.nanoTime();
            dbTotalTime += (endTime - startTime);
        }
        double dbAverageTime = dbTotalTime / (TEST_COUNT * 1_000_000.0);

        // 캐싱 조회 평균 시간 측정
        long cacheTotalTime = 0;
        for (int i = 0; i < TEST_COUNT; i++) {
            long startTime = System.nanoTime();
            configService.getConfigInCache(CODE); // 캐시에서 데이터 조회
            long endTime = System.nanoTime();
            cacheTotalTime += (endTime - startTime);
        }
        double cacheAverageTime = cacheTotalTime / (TEST_COUNT * 1_000_000.0);

        System.out.printf("DB 조회 평균 시간: %.3f ms\n", dbAverageTime);
        System.out.printf("캐싱 조회 평균 시간: %.3f ms\n", cacheAverageTime);
    }
}
