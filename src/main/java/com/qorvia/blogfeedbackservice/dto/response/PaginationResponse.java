package com.qorvia.blogfeedbackservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginationResponse<T> {
    private List<T> data;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int pageSize;
}