package com.library.core.repository;

import com.library.core.IntegrationSupport;
import jakarta.persistence.EntityManager;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
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

    @Test
    void 검색어_상위5개를_구한다() {
        DailyStat udp1 = DailyStat.create("UDP", LocalDateTime.of(2025, 3, 2, 0, 0, 0));
        DailyStat http1 = DailyStat.create("HTTP", LocalDateTime.of(2025, 3, 1, 12, 0, 0));
        DailyStat http2 = DailyStat.create("HTTP", LocalDateTime.of(2025, 3, 1, 15, 0, 0));
        DailyStat http3 = DailyStat.create("HTTP", LocalDateTime.of(2025, 3, 2, 0, 0, 0));
        DailyStat http4 = DailyStat.create("HTTP", LocalDateTime.of(2025, 3, 2, 0, 0, 0));
        DailyStat ip1 = DailyStat.create("IP", LocalDateTime.of(2025, 3, 1, 12, 0, 0));
        DailyStat ip2 = DailyStat.create("IP", LocalDateTime.of(2025, 3, 1, 15, 0, 0));
        DailyStat tcpIp1 = DailyStat.create("TCP/IP", LocalDateTime.of(2025, 3, 1, 12, 0, 0));
        DailyStat tcpIp2 = DailyStat.create("TCP/IP", LocalDateTime.of(2025, 3, 1, 12, 0, 0));
        DailyStat tcpIp3 = DailyStat.create("TCP/IP", LocalDateTime.of(2025, 3, 1, 12, 0, 0));

        dailyStatRepository.saveAll(List.of(http1, http2, http3, http4, tcpIp1, tcpIp2, tcpIp3, ip1, ip2, udp1));

        LocalDateTime start = LocalDateTime.of(2025, 3, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 3, 2, 0, 0, 0);
        List<QueryStat> byTop5 = dailyStatRepository.findTopQueriesBetween(start, end, PageRequest.of(0, 5));

        assertThat(byTop5)
                .extracting("query", "total")
                .containsExactly(
                        Tuple.tuple("HTTP", 4L),
                        Tuple.tuple("TCP/IP", 3L),
                        Tuple.tuple("IP", 2L),
                        Tuple.tuple("UDP", 1L)
                );
    }
    
    @Test
    void 같은_count의_검색어는_순서가_보장되지_않는다() {
        DailyStat ip1 = DailyStat.create("IP", LocalDateTime.of(2025, 3, 1, 12, 0, 0));
        DailyStat ip2 = DailyStat.create("IP", LocalDateTime.of(2025, 3, 1, 13, 0, 0));
        DailyStat ip3 = DailyStat.create("IP", LocalDateTime.of(2025, 3, 1, 14, 0, 0));
        DailyStat http1 = DailyStat.create("HTTP", LocalDateTime.of(2025, 3, 1, 12, 0, 0));
        DailyStat http2 = DailyStat.create("HTTP", LocalDateTime.of(2025, 3, 1, 15, 0, 0));
        DailyStat tcpIp1 = DailyStat.create("TCP/IP", LocalDateTime.of(2025, 3, 1, 12, 0, 0));
        DailyStat tcpIp2 = DailyStat.create("TCP/IP", LocalDateTime.of(2025, 3, 1, 13, 0, 0));
        DailyStat udp1 = DailyStat.create("UDP", LocalDateTime.of(2025, 3, 1, 12, 0, 0));
        DailyStat udp2 = DailyStat.create("UDP", LocalDateTime.of(2025, 3, 1, 14, 0, 0));

        dailyStatRepository.saveAll(List.of(ip1, ip2, ip3, http1, http2, tcpIp1, tcpIp2, udp1, udp2));

        LocalDateTime start = LocalDateTime.of(2025, 3, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 3, 1, 23, 59, 59);
        List<QueryStat> result = dailyStatRepository.findTopQueriesBetween(start, end, PageRequest.of(0, 5));

        // count가 3인 IP는 1위 확정
        assertThat(result.get(0))
                .extracting(QueryStat::getQuery, QueryStat::getTotal)
                .containsExactly("IP", 3L);

        // count가 동일한(2) HTTP, TCP/IP, UDP는 순서가 보장되지 않음
        assertThat(result.subList(1, 4))
                .extracting("query", "total")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("HTTP", 2L),
                        Tuple.tuple("TCP/IP", 2L),
                        Tuple.tuple("UDP", 2L)
                );
    }
}