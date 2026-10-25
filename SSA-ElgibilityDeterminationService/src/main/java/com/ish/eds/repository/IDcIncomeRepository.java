package com.ish.eds.repository;

import com.ish.eds.entity.DcIncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDcIncomeRepository extends JpaRepository<DcIncomeEntity, Integer> {
    public DcIncomeEntity findByCaseNo(Integer caseNo);
}
