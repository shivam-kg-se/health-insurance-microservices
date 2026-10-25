package com.ish.eds.repository;

import com.ish.eds.entity.DcCaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDcCaseRepository extends JpaRepository<DcCaseEntity, Integer> {
    public Integer findByCaseNo(Integer caseNo);
}
