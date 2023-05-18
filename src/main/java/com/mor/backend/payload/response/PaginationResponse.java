package com.mor.backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse {
    private Object contents;
    private long currentPage;
    private long totalItems;
    private long totalPages;

}
