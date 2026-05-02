package com.library.client.response;

public record Meta(
        Integer totalCount,
        Integer pageableCount,
        Boolean isEnd
) {
}
