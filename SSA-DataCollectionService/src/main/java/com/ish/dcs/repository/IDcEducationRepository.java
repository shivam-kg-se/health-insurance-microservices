package com.ish.dcs.repository;

import com.ish.dcs.entity.DcEducationEntity;
import com.ish.dcs.entity.DcIncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDcEducationRepository extends JpaRepository<DcEducationEntity, Integer> {
    public DcEducationEntity findByCaseNo(Integer caseNo);
}
