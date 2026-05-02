package com.library.client.response;

import java.util.List;

public record KakaoBookResponse(
        Meta meta,
        List<Document> documents
) {
}
