package com.library.core.domain.dailystat;

import com.library.core.IntegrationSupport;
import com.library.core.domain.DailyStatFinder;
import com.library.core.repository.DailyStat;
import com.library.core.repository.DailyStatRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DailyStatFinderTest extends IntegrationSupport {
    @Autowired
    DailyStatFinder dailyStatFinder;

    @Autowired
    DailyStatRepository dailyStatRepository;

    @AfterEach
    void tearDown() {
        dailyStatRepository.deleteAllInBatch();
    }

    @Test
    void 일일_통계를_읽는다() {
        DailyStat http1 = DailyStat.create("HTTP", LocalDateTime.of(2025, 4, 1, 3, 0, 0));
        DailyStat http2 = DailyStat.create("HTTP", LocalDateTime.of(2025, 3, 31, 3, 0, 0));
        DailyStat tcp1 = DailyStat.create("TCP", LocalDateTime.of(2025, 4, 2, 0, 0, 0));
        dailyStatRepository.saveAll(List.of(http1, http2, tcp1));

        long count = dailyStatFinder.readDailyCount(
                "HTTP",
                LocalDate.of(2025, 4, 1)
        );

        assertThat(count).isEqualTo(1);
    }
}