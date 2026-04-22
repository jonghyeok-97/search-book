package com.library.core.domain;

import com.library.core.IntegrationSupport;
import com.library.core.repository.DailyStat;
import com.library.core.repository.DailyStatRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DailyStatAppenderTest extends IntegrationSupport {
    @Autowired
    DailyStatRepository dailyStatRepository;

    @Autowired
    DailyStatAppender dailyStatAppender;

    @AfterEach
    void tearDown() {
        dailyStatRepository.deleteAllInBatch();
    }

    @Test
    void save호출_시_DB에_저장된다() {
        // given
        DailyStat given = DailyStat.create("HTTP", LocalDateTime.now());

        // when
        dailyStatAppender.save(given);

        // then
        assertThat(dailyStatRepository.findAll()).hasSize(1);
    }
}