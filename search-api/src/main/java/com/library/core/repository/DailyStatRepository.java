package com.library.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface DailyStatRepository extends JpaRepository<DailyStat, Long> {
    long countByQueryAndEventDataTimeBetween(String query, LocalDateTime start, LocalDateTime end);
}
