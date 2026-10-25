package com.ish.eds.repository;

import com.ish.eds.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlanRepository extends JpaRepository<PlanEntity, Integer> {
}
