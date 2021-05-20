package com.projectx.ProjectX.repository;

import com.projectx.ProjectX.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long> {
}
