package com.library.core.repository;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_statistics")
public class DailyStat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String query;

    @Column(nullable = false)
    private LocalDateTime eventDataTime;

    public static DailyStat create(String query, LocalDateTime eventDataTime) {
        DailyStat dailyStat = new DailyStat();
        dailyStat.query = query;
        dailyStat.eventDataTime = eventDataTime;
        return dailyStat;
    }
}
