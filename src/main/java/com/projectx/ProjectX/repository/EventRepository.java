package com.projectx.ProjectX.repository;

import com.projectx.ProjectX.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {


    @Query ("SELECT e FROM Event e " +
            "WHERE e.address.city LIKE %?1% " +
            "AND str(e.type) LIKE %?2% " +
            "AND str(e.placeType) LIKE %?3% " +
            "AND str(e.status) LIKE %?4% " +
            "AND str(e.ageRestrictions) LIKE %?5% " +
            "AND str(e.availableSeats) LIKE %?6%")
    public List<Event> findAll(String city,
                               String type,
                               String placeType,
                               String status,
                               String ageRestrictions,
                               String availableSeats);
}
