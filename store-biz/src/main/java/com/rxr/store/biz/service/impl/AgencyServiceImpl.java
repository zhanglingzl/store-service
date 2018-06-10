package com.rxr.store.biz.service.impl;

import com.google.common.collect.Lists;
import com.rxr.store.biz.repositories.AgencyRepository;
import com.rxr.store.biz.service.AgencyService;
import com.rxr.store.common.dto.AgencyDto;
import com.rxr.store.common.entities.Agency;
import com.rxr.store.common.entities.Answer;
import com.rxr.store.common.enums.AgencyEnum;
import com.rxr.store.common.form.AgencyForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zero
 * @date Create in 2018/6/2 14:42
 */
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
        List<Agency> parentList = agencise.stream().filter(item -> item.getParentId() == 0).collect(Collectors.toList());
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
        return null;
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
}
