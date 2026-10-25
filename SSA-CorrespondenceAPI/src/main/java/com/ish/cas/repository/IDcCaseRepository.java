package com.ish.cas.repository;

import com.ish.cas.entity.DcCaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDcCaseRepository extends JpaRepository<DcCaseEntity, Integer> {
    public Integer findByCaseNo(Integer caseNo);

}
