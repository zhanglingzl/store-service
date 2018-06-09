package com.rxr.store.common.dto;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author zero
 * @date Create in 2018/6/2 14:38
 */
@Data
public class AgencyDto {
    private Long id;
    private Integer type;
    private String telephone;
    private String email;
    private String photo;
    private String cardNo;
    private String addressId;
    private String company;
    private String weChatNo;
    private String parentId;
    private String gender;
    private String name;
    private String status;
    private List<AgencyDto> children = Lists.newArrayList();

}
