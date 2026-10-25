package com.ish.eds.repository;

import com.ish.eds.entity.DcChildernEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDcChildernRepository extends JpaRepository<DcChildernEntity, Integer> {
    public List<DcChildernEntity> findByCaseNo(Integer caseNo);
}
