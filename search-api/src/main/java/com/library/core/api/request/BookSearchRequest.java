package com.library.core.api.request;

import com.library.core.support.SortType;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record BookSearchRequest(
        @NotBlank(message = "query는 필수 값입니다.")
        @Length(max = 50, message = "50자를 초과할 수 없습니다.")
        String query,

        @Min(value = 1, message = "page는 1 이상 이어야 합니다.")
        @Max(value = 100, message = "page는 100 이하여야 합니다.")
        int page,

        @Min(value = 1, message = "size는 1 이상이어야 합니다.")
        @Max(value = 50, message = "size는 50 이상이어야 합니다.")
        int size,

        @NotNull(message = "sort 는 SIM/DATE 중 필수여야합니다.")
        SortType sort
) {
}
