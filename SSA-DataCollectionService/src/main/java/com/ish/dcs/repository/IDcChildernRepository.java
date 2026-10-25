package com.ish.dcs.repository;

import com.ish.dcs.entity.DcChildernEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDcChildernRepository extends JpaRepository<DcChildernEntity, Integer> {
    public List<DcChildernEntity> findByCaseNo(Integer caseNo);
}
