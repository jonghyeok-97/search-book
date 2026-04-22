package com.library.core.repository;

import com.library.core.IntegrationSupport;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DailyStatRepositoryTest extends IntegrationSupport {
    @Autowired
    DailyStatRepository dailyStatRepository;

    @Autowired
    EntityManager em;

    @AfterEach
    void tearDown() {
        dailyStatRepository.deleteAllInBatch();
    }

    @Test
    void 엔티티를_저장_후_조회한다() {
        DailyStat dailyStat = DailyStat.create("HTTP", LocalDateTime.of(2025, 4, 21, 13, 1, 1));

        DailyStat saved = dailyStatRepository.saveAndFlush(dailyStat);

        assertThat(saved.getId()).isNotNull();

        em.clear();
        Optional<DailyStat> found = dailyStatRepository.findById(saved.getId());
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getQuery()).isEqualTo("HTTP");
    }

    @Test
    void 쿼리마다_일별_카운트를_제공한다() {
        DailyStat http1 = DailyStat.create("HTTP", LocalDateTime.of(2025, 3, 1, 12, 0, 0));
        DailyStat http2 = DailyStat.create("HTTP", LocalDateTime.of(2025, 3, 1, 15, 0, 0));
        DailyStat http3 = DailyStat.create("HTTP", LocalDateTime.of(2025, 3, 2, 0, 0, 0));
        DailyStat tcpIp1 = DailyStat.create("TCP/IP", LocalDateTime.of(2025, 3, 1, 12, 0, 0));
        DailyStat tcpIp2 = DailyStat.create("TCP/IP", LocalDateTime.of(2025, 3, 1, 12, 0, 0));

        dailyStatRepository.saveAll(List.of(http1, http2, http3, tcpIp1, tcpIp2));
        em.clear();

        long countDailyStat = dailyStatRepository.countByQueryAndEventDataTimeBetween(
                "HTTP",
                LocalDateTime.of(2025, 3, 1, 0, 0, 0),
                LocalDateTime.of(2025, 3, 1, 23, 59, 59));
        assertThat(countDailyStat).isEqualTo(2);
    }
}