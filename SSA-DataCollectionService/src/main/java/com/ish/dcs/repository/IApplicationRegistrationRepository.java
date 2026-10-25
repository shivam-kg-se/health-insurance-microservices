package com.ish.dcs.repository;

import com.ish.dcs.entity.CitizenApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IApplicationRegistrationRepository extends JpaRepository<CitizenApplicationEntity, Integer> {
}
