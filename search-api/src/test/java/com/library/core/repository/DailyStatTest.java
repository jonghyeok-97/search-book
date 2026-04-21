package com.library.core.repository;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DailyStatTest {

    @Test
    void 생성한다() {
        String givenQuery = "HTTP";
        LocalDateTime givenDateTime = LocalDateTime.now();
        DailyStat dailyStat = DailyStat.create(givenQuery, givenDateTime);

        assertThat(dailyStat.getId()).isNull();
        assertThat(dailyStat.getQuery()).isEqualTo(givenQuery);
        assertThat(dailyStat.getEventDataTime()).isEqualTo(givenDateTime);
    }
}