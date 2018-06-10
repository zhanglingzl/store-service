package com.rxr.store.biz.repositories;

import com.rxr.store.common.entities.Agency;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author zero
 * @date Create in 2018/6/2 14:52
 */
public interface AgencyRepository extends BaseRepository<Agency, Long>{
    Optional<Agency> findByWechatId(String wechatId);

    @Transactional
    @Modifying
    @Query("update Answer set status=?1 where id=?2")
    int updateAnswerStatusById(int status, Long id);


    @Query("select t.id from Answer t left join t.agency tt where t.status=?1 and tt.id=?2")
    Long updateAnswerStatusByAgencyId(int status, Long id);
}
