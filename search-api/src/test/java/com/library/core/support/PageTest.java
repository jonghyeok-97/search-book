package com.library.core.support;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageTest {

    @Test
    void 페이지_객체_생성() {
        Page<Integer> paged = Page.from(5, List.of(3, 4, 5));

        assertThat(paged.total()).isEqualTo(5);
        assertThat(paged.contents()).containsExactly(3, 4, 5);
    }
}