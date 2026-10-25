package com.ish.cas.repository;

import com.ish.cas.entity.ElgibilityDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IElgibilityDeterminationRepository extends JpaRepository<ElgibilityDetailsEntity, Integer> {

    public ElgibilityDetailsEntity findByCaseNo(int caseNo);
}
