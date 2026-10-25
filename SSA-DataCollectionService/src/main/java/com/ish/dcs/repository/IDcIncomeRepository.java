package com.ish.dcs.repository;

import com.ish.dcs.entity.DcIncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDcIncomeRepository extends JpaRepository<DcIncomeEntity, Integer> {
    public DcIncomeEntity findByCaseNo(Integer caseNo);
}
