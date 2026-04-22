package com.library.core.domain;

import com.library.core.repository.DailyStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DailyStatFinder {
    private final DailyStatRepository dailyStatRepository;

    public long readDailyCount(String query, LocalDate date) {
        return dailyStatRepository.countByQueryAndEventDataTimeBetween(
                query, date.atStartOfDay(), date.atTime(LocalTime.MAX)
        );
    }

}
