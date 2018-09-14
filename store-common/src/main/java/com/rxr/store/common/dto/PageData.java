package com.rxr.store.common.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageData<T> {
    private List<T> data;
    private Pagination pagination;

    public PageData(Page<T> page) {
        this.data = page.getContent();
        this.pagination = new Pagination(page.getNumber()+1,
                page.getSize(), page.getTotalElements());
    }

    public static <T> PageData<T> bulid(Page<T> page) {
        return new PageData<>(page);
    }
}
