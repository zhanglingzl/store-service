package com.rxr.store.web.common.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageData<T> {
    private List<T> data;
    private Pagination pagination;

    public PageData(Page<T> page) {
        this.data = page.getContent();
        this.pagination = new Pagination(page.getNumber(),
                page.getSize(),page.getTotalElements());
    }
}
