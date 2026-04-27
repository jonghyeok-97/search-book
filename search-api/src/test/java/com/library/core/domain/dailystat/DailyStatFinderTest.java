package com.library.core.domain.dailystat;

import com.library.core.IntegrationSupport;
import com.library.core.domain.DailyStatFinder;
import com.library.core.repository.DailyStatRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class DailyStatFinderTest extends IntegrationSupport {
    @Autowired
    DailyStatFinder dailyStatFinder;

    @MockitoBean
    DailyStatRepository dailyStatRepository;

    @AfterEach
    void tearDown() {
        dailyStatRepository.deleteAllInBatch();
    }

    @Test
    void 쿼리와_날짜로_일일_카운트를_반환한다() {
        given(dailyStatRepository.countByQueryAndEventDataTimeBetween(
                eq("HTTP"),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).willReturn(1L);

        long count = dailyStatFinder.readDailyCount("HTTP", LocalDate.of(2025, 4, 1));

        assertThat(count).isEqualTo(1);
    }

    @Test
    void 일일_카운트_조회시_날짜를_자정부터_하루_끝으로_변환한다() {
        String givenQuery = "HTTP";
        LocalDate givenDate = LocalDate.of(2025, 4, 1);

        dailyStatFinder.readDailyCount(givenQuery, givenDate);

        verify(dailyStatRepository).countByQueryAndEventDataTimeBetween(
                givenQuery,
                givenDate.atStartOfDay(),
                givenDate.atTime(LocalTime.MAX)
        );
    }

    @Test
    void 상위_쿼리_조회시_시작날짜를_자정으로_끝날짜를_하루_끝으로_변환한다() {
        LocalDate start = LocalDate.of(2025, 4, 1);
        LocalDate end = LocalDate.of(2025, 4, 3);
        int size = 5;

        dailyStatFinder.findTopQuery(start, end, size);

        verify(dailyStatRepository).findTopQueriesBetween(
                start.atStartOfDay(),
                end.atTime(LocalTime.MAX),
                Pageable.ofSize(size)
        );
    }
}