package com.learny.scheduleservice.repositories;

import com.learny.scheduleservice.entities.CasualTablet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CasualTabletJpaRepository extends JpaRepository<CasualTablet, Long> {
}
