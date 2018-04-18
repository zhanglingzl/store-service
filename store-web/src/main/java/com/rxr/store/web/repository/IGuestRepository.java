package com.rxr.store.web.repository;

import com.rxr.store.common.entities.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author zero
 */
@Repository
public interface IGuestRepository extends JpaRepository<Guest, Long>, JpaSpecificationExecutor<Guest> {
}
