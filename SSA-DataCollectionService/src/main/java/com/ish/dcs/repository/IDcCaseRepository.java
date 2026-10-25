package com.ish.dcs.repository;

import com.ish.dcs.entity.DcCaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDcCaseRepository extends JpaRepository<DcCaseEntity, Integer> {
    public Integer findByCaseNo(Integer caseNo);
}
