package com.ish.eds.repository;

import com.ish.eds.entity.DcEducationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDcEducationRepository extends JpaRepository<DcEducationEntity, Integer> {
    public DcEducationEntity findByCaseNo(Integer caseNo);
}
