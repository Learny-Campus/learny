package com.learny.scheduleservice.repositories;

import com.learny.scheduleservice.entities.EventTablet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTabletJpaRepository extends JpaRepository<EventTablet, Long> {
}
