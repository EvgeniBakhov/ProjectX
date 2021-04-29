package com.projectx.ProjectX.repository;

import com.projectx.ProjectX.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
