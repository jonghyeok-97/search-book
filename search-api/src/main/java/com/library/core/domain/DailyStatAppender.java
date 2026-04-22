package com.library.core.domain;

import com.library.core.repository.DailyStat;
import com.library.core.repository.DailyStatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyStatAppender {
    private final DailyStatRepository dailyStatRepository;

    public void save(DailyStat dailyStat) {
        log.info("[DailyStatAppender.save] {}", dailyStat);
        dailyStatRepository.save(dailyStat);
    }
}
