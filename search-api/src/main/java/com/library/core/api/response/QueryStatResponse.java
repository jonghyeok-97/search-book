package com.library.core.api.response;

import com.library.core.repository.QueryStat;

public record QueryStatResponse(
        String query,
        long count
) {
    public static QueryStatResponse from(QueryStat queryStat) {
        return new QueryStatResponse(
                queryStat.getQuery(),
                queryStat.getTotal()
        );
    }
}
