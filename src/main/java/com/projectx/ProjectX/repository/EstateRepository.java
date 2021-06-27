package com.projectx.ProjectX.repository;

import com.projectx.ProjectX.model.Estate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstateRepository extends JpaRepository<Estate, Long> {

    Optional<List<Estate>> findAllByOwner(Long ownerId);

    @Query("FROM Estate e JOIN Address a ON e.address.id = a.id WHERE a.city = :city")
    Optional<List<Estate>> findAllByCity(String city);

    @Query("SELECT e FROM Estate e " +
            "WHERE e.address.city LIKE %?1% " +
            "AND str(e.type) LIKE %?2% " +
            "AND (e.numOfBedrooms BETWEEN ?3 AND ?4) " +
            "AND (e.area BETWEEN ?5 AND ?6) " +
            "AND (e.rentPrice BETWEEN ?7 AND ?8)")
    List<Estate> findAll(String city,
                         String type,
                         int minBeds,
                         int maxBeds,
                         Double minArea,
                         Double maxArea,
                         Double minPrice,
                         Double maxPrice);

    @Query("SELECT DISTINCT e.address.city FROM Estate e")
    List<String> findAllCities();
}
