package com.rxr.store.web.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Pagination {
    private Integer pageSize;
    private Integer pageNumber;
    private Long total;
}
