package com.rxr.store.biz.service.impl;

import com.google.common.collect.Lists;
import com.rxr.store.biz.repositories.AgencyRepository;
import com.rxr.store.biz.service.AgencyService;
import com.rxr.store.common.dto.AgencyDto;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.Answer;
import com.rxr.store.common.enums.AgencyEnum;
import com.rxr.store.common.form.AgencyForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zero
 * @date Create in 2018/6/2 14:42
 */
@Slf4j
@Service
public class AgencyServiceImpl implements AgencyService{

    @Autowired
    private AgencyRepository agencyRepository;

    @Override
    public List<AgencyDto> getAgencise(AgencyForm agency) {
        List<Agency> agencise = agencyRepository.findAll((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if(StringUtils.isNotBlank(agency.getName())) {
                predicate.getExpressions()
                        .add(criteriaBuilder.like(root.get("name"),"%"+agency.getName()+"%"));
            }
            if(StringUtils.isNotBlank(agency.getTelephone())) {
                predicate.getExpressions()
                        .add(criteriaBuilder.like(root.get("telephone"),"%"+agency.getTelephone()+"%"));
            }
            predicate.getExpressions().add(criteriaBuilder.equal(root.get("status"),0));
            return predicate;
        });
        return getAgenciesDto(agencise);
    }

    @Override
    public List<AgencyDto> getQuasiAgencies(AgencyForm agency, int status) {
        List<Agency> quasiAgencies = agencyRepository.findAll((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if(StringUtils.isNotBlank(agency.getName())) {
                predicate.getExpressions()
                        .add(criteriaBuilder.like(root.get("name"),"%"+agency.getName()+"%"));
            }
            if(StringUtils.isNotBlank(agency.getTelephone())) {
                predicate.getExpressions()
                        .add(criteriaBuilder.like(root.get("telephone"),"%"+agency.getTelephone()+"%"));
            }
            predicate.getExpressions().add(criteriaBuilder.equal(root.get("status"),status));
            Join<Agency, Answer> answerJoin = root.join("answers");
            predicate.getExpressions().add(criteriaBuilder.equal(answerJoin.get("status"),0));
            return predicate;
        });
        return converQuasiAgency(quasiAgencies);
    }

    /**
     * 将代理实体转化为dto
     * @param agencise
     * @return
     */
    private List<AgencyDto> getAgenciesDto(List<Agency> agencise){
        List<AgencyDto> agenciesDto = Lists.newArrayList();
        List<Agency> parentList = agencise.stream().filter(item -> item.getParentId() == 0)
                .collect(Collectors.toList());
        if(parentList.size()<=0){
            parentList = agencise;
        }
        parentList.forEach(parent -> agenciesDto.add(convertAgency(agencise, parent)));
        return agenciesDto;
    }

    /**
     * 递归设置children
     * @param agencise
     * @param parent
     * @return
     */
    private AgencyDto convertAgency(List<Agency> agencise, Agency parent){
        List<Agency> children = filterAgency(agencise, parent.getId());
        AgencyDto parentDto = new AgencyDto();
        BeanUtils.copyProperties(parent, parentDto);
        for(Agency a: children){
            parentDto.getChildren().add(convertAgency(filterAgency(agencise, a.getId()),a));
        }
        return parentDto;
    }

    /**
     * 通过ID获取子节点
     * @param agencise
     * @param id
     * @return
     */
    private List<Agency> filterAgency(List<Agency> agencise, Long id){
        return agencise.stream().filter(item -> item.getParentId().equals(id)).collect(Collectors.toList());
    }

    private List<AgencyDto> converQuasiAgency(List<Agency> quasiAgencies){
        List<AgencyDto> list = Lists.newArrayList();
        quasiAgencies.forEach(item->{
            AgencyDto dto = new AgencyDto();
            BeanUtils.copyProperties(item, dto);
            Answer answer = item.getAnswers().get(0);
            // 消除Json转化递归内存溢出错误
            answer.setAgency(null);
            dto.setAnswer(answer);
            list.add(dto);
        });
        return list;
    }

    @Override
    public Agency findAgencyByWechatId(String wechatId) {
        return agencyRepository.findByWechatId(wechatId);
    }

    @Override
    public void verifyAgency(Long id) {
        agencyRepository.updateAnswerStatusById(AgencyEnum.ANSWER_VERIFY.getValue(), id);
    }

    @Override
    public void verifyUpdate(Long id) {
        Agency agency = agencyRepository.findById(id).get();
        int level = agency.getLevel();
        if(level < AgencyEnum.MAX_LEVEL.getValue()){
            agency.setLevel(level + 1);
            agencyRepository.save(agency);
            agencyRepository.updateAnswerStatusById(AgencyEnum.ANSWER_VERIFY.getValue(), agency.getAnswers().get(0).getId());
        }
    }

    @Override
    public Pair<List<Long>, int[]> findAgencyByCount(Agency agency) {
        int[] counts = new int[3];
        List<Long> parentIds = new ArrayList<>();
        //查询一级代理的
        List<Agency> firstAgency = this.agencyRepository.findAgenciesByParentIdIn(Arrays.asList(agency.getId()));
        List<Long> first = firstAgency.stream().map(Agency::getId).collect(Collectors.toList());
        parentIds.addAll(first);
        counts[0] = first.size();
        if(first.size() > 0) {
            List<Agency> secondAgency = this.agencyRepository.findAgenciesByParentIdIn(first);
            List<Long> second = secondAgency.stream().map(Agency::getId).collect(Collectors.toList());
            parentIds.addAll(second);
            counts[1] = second.size();
            if(second.size() > 0) {
                List<Agency> thirdAgency = this.agencyRepository.findAgenciesByParentIdIn(second);
                List<Long> third = thirdAgency.stream().map(Agency::getId).collect(Collectors.toList());
                parentIds.addAll(third);
                counts[2] = third.size();
            }
        }

        return ImmutablePair.of(parentIds, counts);
    }

    @Override
    public Page<Agency> listAgencies(AgencyForm agencyForm, Pageable Pageable) {
        return agencyRepository.findAll((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            switch (agencyForm.getType()) {
                case 1:
                    predicate.getExpressions().add(criteriaBuilder.equal(root.get("parentId"),agencyForm.getId()));
                    break;
                case 2:
                    List<Agency> second = this.agencyRepository.findAgenciesByParentIdIn(Arrays
                            .asList(agencyForm.getId()));
                    if(second.size() > 0) {
                        predicate.getExpressions().add(root.get("parentId").in(second.stream()
                                .map(Agency::getId).collect(Collectors.toList())));
                    } else {
                        predicate.getExpressions().add(criteriaBuilder.equal(root.get("id"), -100));
                    }

                    break;
                case 3:
                    List<Agency> temp = this.agencyRepository.findAgenciesByParentIdIn(Arrays
                            .asList(agencyForm.getId()));
                    if(temp.size() > 0) {
                        List<Agency> third = this.agencyRepository.findAgenciesByParentIdIn(temp.stream()
                                .map(Agency::getId).collect(Collectors.toList()));
                        if(third.size() > 0) {
                            predicate.getExpressions().add(root.get("parentId").in(third.stream()
                                    .map(Agency::getId).collect(Collectors.toList())));
                        } else {
                            predicate.getExpressions().add(criteriaBuilder.equal(root.get("id"),-100));
                        }

                    } else {
                        predicate.getExpressions().add(criteriaBuilder.equal(root.get("id"),-100));
                    }
                    break;
                default:
                    log.warn("没有匹配到代理类型");
            }
            return predicate;
        },Pageable);
    }

    @Override
    public Agency findAgencyById(Long agencyId) {
        return agencyRepository.findAgenciesById(agencyId);
    }

    @Override
    public List<Agency> listAgenciesByParentIds(List<Long> parentIds) {
        return agencyRepository.findAgenciesByParentIdIn(parentIds);
    }

    @Override
    public void updateAgency(AgencyForm agencyForm) {
        Agency agency = this.agencyRepository.findAgenciesById(agencyForm.getId());
        if(StringUtils.isNotBlank(agencyForm.getName())) {
            agency.setName(agencyForm.getName());
        }
        if(StringUtils.isNotBlank(agencyForm.getTelephone())) {
            agency.setTelephone(agencyForm.getTelephone());
        }
        if(StringUtils.isNotBlank(agencyForm.getEmail())) {
            agency.setEmail(agencyForm.getEmail());
        }

        if(StringUtils.isNotBlank(agencyForm.getWechat())) {
            agency.setWechat(agencyForm.getWechat());
        }
        this.agencyRepository.save(agency);

    }

    private void getChildAgencyHql(Root<Agency> root, CriteriaBuilder criteriaBuilder,
                                   Predicate predicate, List<Long> parentId) {
        if (parentId.size() > 0) {
            Iterator<Long> iterator = parentId.iterator();
            CriteriaBuilder.In<Long> in = criteriaBuilder.in(root.get("parentId"));
            while (iterator.hasNext()) {
                in.value(iterator.next());
            }
            predicate.getExpressions().add(in);
        } else {
            predicate.getExpressions().add(criteriaBuilder.equal(root.get("id"),-100));
        }
    }
}
