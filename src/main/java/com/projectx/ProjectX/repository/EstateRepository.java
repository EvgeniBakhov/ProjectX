package com.projectx.ProjectX.repository;

import com.projectx.ProjectX.model.Estate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstateRepository extends JpaRepository<Estate, Long> {

    Optional<List<Estate>> findAllByOwner(Long ownerId);
}
