package com.library.core.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyStatRepository extends JpaRepository<DailyStat, Long> {
    long countByQueryAndEventDataTimeBetween(String query, LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT ds.query AS query, count(ds.query) AS total
            FROM DailyStat ds
            WHERE ds.eventDataTime BETWEEN :start AND :end
            GROUP BY query
            ORDER BY total desc
            """)
    List<QueryStat> findTopQueriesBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
