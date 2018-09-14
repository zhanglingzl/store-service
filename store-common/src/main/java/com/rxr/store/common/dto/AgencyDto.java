package com.rxr.store.common.dto;

import com.google.common.collect.Lists;
import com.rxr.store.common.entity.Answer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class AgencyDto {
    private Long id;
    private Integer type;
    private Integer level;
    private String telephone;
    private String email;
    private String photo;
    private String cardNo;
    private String addressId;
    private String company;
    private String wechatId;
    private Long parentId;
    private String gender;
    private String name;
    private String status;
    private List<AgencyDto> children = Lists.newArrayList();
    private AgencyDto parent;
    private Answer answer;

}
