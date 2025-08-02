package com.stride.stride_common.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Paginated response wrapper
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginatedResponse<T> {
    
    private List<T> content;
    private PageInfo pageInfo;
    private long totalElements;
    private int totalPages;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        private int page;
        private int size;
        private boolean hasNext;
        private boolean hasPrevious;
        private boolean isFirst;
        private boolean isLast;
    }
    
    public static <T> PaginatedResponse<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        
        PageInfo pageInfo = new PageInfo();
        pageInfo.page = page;
        pageInfo.size = size;
        pageInfo.hasNext = page < totalPages - 1;
        pageInfo.hasPrevious = page > 0;
        pageInfo.isFirst = page == 0;
        pageInfo.isLast = page == totalPages - 1;
            
        PaginatedResponse<T> response = new PaginatedResponse<>();
        response.content = content;
        response.pageInfo = pageInfo;
        response.totalElements = totalElements;
        response.totalPages = totalPages;
        return response;
    }
}