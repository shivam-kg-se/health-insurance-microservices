package com.ish.eds.repository;

import com.ish.eds.entity.CitizenApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IApplicationRegistrationRepository extends JpaRepository<CitizenApplicationEntity, Integer> {
}
