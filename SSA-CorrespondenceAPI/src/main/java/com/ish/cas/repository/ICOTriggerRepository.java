package com.ish.cas.repository;

import com.ish.cas.entity.CoTriggersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ICOTriggerRepository extends JpaRepository<CoTriggersEntity, Integer> {
    public List<CoTriggersEntity> findByTriggerStatus(String Status);
    public CoTriggersEntity findByCaseNo(int caseNo);
}
