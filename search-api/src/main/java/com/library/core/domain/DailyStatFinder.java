package com.library.core.domain;

import com.library.core.repository.DailyStatRepository;
import com.library.core.repository.QueryStat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyStatFinder {
    private final DailyStatRepository dailyStatRepository;

    public long readDailyCount(String query, LocalDate date) {
        return dailyStatRepository.countByQueryAndEventDataTimeBetween(
                query, date.atStartOfDay(), date.atTime(LocalTime.MAX)
        );
    }

    public List<QueryStat> findTopQuery(LocalDate start, LocalDate end, int size) {
        return dailyStatRepository.findTopQueriesBetween(
                start.atStartOfDay(), end.atTime(LocalTime.MAX), Pageable.ofSize(size));
    }

}
