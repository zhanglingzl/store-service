package com.rxr.store.biz.repositories;

import com.rxr.store.common.entity.Agency;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author zero
 * @date Create in 2018/6/2 14:52
 */
public interface AgencyRepository extends BaseRepository<Agency, Long>{
    Agency findByWechatId(String wechatId);

    @Transactional
    @Modifying
    @Query("update Answer set status=?1 where id=?2")
    int updateAnswerStatusById(int status, Long id);


    @Query("select t.id from Answer t left join t.agency tt where t.status=?1 and tt.id=?2")
    Long updateAnswerStatusByAgencyId(int status, Long id);

    List<Agency> findAgenciesByParentIdIn(List<Long> agencyIds);

    Agency findAgenciesById(Long id);

    /**
     * 通过ID跟新代理登录
     * @param id Id
     * @param level 代理等级
     * @return int
     */
    @Transactional
    @Modifying
    @Query("update Agency set level=?1 where id=?2")
    int agencyUpgradeById(Integer level, Long id);

    /**
     * 通过ID跟新父节点
     * @param id Id
     * @param parentId 父级Id
     * @return int
     */
    @Transactional
    @Modifying
    @Query("update Agency set parentId=?1 where id=?2")
    int changeParent(Long parentId, Long id);

    @Modifying
    @Query("update Agency set level = :level where id = :id")
    void updateAgencyLevelById(@Param("level") Integer level, @Param("id") Long id );

}
