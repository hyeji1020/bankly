package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.BankException;
import com.project.bankassetor.primary.model.entity.Config;
import com.project.bankassetor.primary.repository.ConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.project.bankassetor.config.CachingConfig.CONFIG_CACHE;
import static com.project.bankassetor.exception.ErrorCode.CONFIG_NOT_FOUND_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigService {

    private final ConfigRepository configRepository;

    public Config getConfig(String code) {
        log.info("searching config in db !! {}", code);
        return configRepository.findByCode(code).orElseThrow(() -> new BankException(CONFIG_NOT_FOUND_ERROR));
    }

    // 스프링 캐시 추상화 부분. key-value 쌍으로 구성이되어 있음.
    @Cacheable(value = CONFIG_CACHE, key = "#code")
    public Config getConfigInCache(String code) {
        log.info("searching config in redis !! {}", code);
        return configRepository.findByCode(code).orElseThrow(() -> new BankException(CONFIG_NOT_FOUND_ERROR));
    }

}