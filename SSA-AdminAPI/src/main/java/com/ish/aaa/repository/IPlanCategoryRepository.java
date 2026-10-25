package com.ish.aaa.repository;

import com.ish.aaa.entity.PlanCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlanCategoryRepository extends JpaRepository<PlanCategory,Integer> {
}
