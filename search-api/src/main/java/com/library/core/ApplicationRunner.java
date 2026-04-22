package com.library.core;

import com.library.core.repository.DailyStat;
import com.library.core.repository.DailyStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ApplicationRunner implements CommandLineRunner {
    private final DailyStatRepository dailyStatRepository;

    @Override
    public void run(String... args) throws Exception {
        DailyStat http1 = DailyStat.create("HTTP", LocalDateTime.now());
        DailyStat http2 = DailyStat.create("HTTP", LocalDateTime.now());
        DailyStat http3 = DailyStat.create("HTTP", LocalDateTime.now());
        DailyStat http4 = DailyStat.create("HTTP", LocalDateTime.now());

        DailyStat tcp1 = DailyStat.create("TCP", LocalDateTime.now());
        DailyStat tcp2 = DailyStat.create("TCP", LocalDateTime.now());

        DailyStat ip1 = DailyStat.create("IP", LocalDateTime.now());
        DailyStat ip2 = DailyStat.create("IP", LocalDateTime.now());
        DailyStat ip3 = DailyStat.create("IP", LocalDateTime.now());
        dailyStatRepository.saveAll(List.of(
            http1, http2, http3, http4, tcp1, tcp2, ip2, ip1, ip3
        ));
    }
}
