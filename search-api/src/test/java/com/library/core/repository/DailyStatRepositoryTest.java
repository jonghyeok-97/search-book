package com.library.core.repository;

import com.library.core.IntegrationSupport;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DailyStatRepositoryTest extends IntegrationSupport {
    @Autowired
    DailyStatRepository dailyStatRepository;

    @Autowired
    EntityManager em;

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
}