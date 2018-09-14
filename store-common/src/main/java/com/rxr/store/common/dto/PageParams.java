package com.rxr.store.common.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

@Data
@ToString
public class PageParams {
    private static final Integer PAGE_NUMBER=0;
    private static final Integer PAGE_SIZE=10;
    private Integer pageNumber;
    private Integer pageSize;
    private String direction;
    private String sorter;

    public PageRequest pageable() {

        if(this.pageNumber == null || this.pageNumber <= 1) {
            this.pageNumber = PAGE_NUMBER;
        } else {
            this.pageNumber -=1;
        }


        if(this.pageSize == null) {
            this.pageSize = PAGE_SIZE;
        }

        if(this.direction != null && this.sorter != null) {
            Optional<Sort.Direction> dire = Sort.Direction.fromOptionalString(direction);
            if(dire.isPresent()) {
                Sort sort = new Sort(dire.get(), sorter);
                return PageRequest.of(this.pageNumber, this.pageSize, sort);
            }
        }
        return PageRequest.of(this.pageNumber, this.pageSize);

    }
}
