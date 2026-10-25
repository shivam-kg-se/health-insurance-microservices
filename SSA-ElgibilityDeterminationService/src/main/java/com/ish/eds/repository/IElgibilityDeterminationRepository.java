package com.ish.eds.repository;

import com.ish.eds.entity.ElgibilityDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IElgibilityDeterminationRepository extends JpaRepository<ElgibilityDetailsEntity, Integer> {

}
