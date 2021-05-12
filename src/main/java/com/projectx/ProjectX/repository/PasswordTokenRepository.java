package com.projectx.ProjectX.repository;

import com.projectx.ProjectX.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long> {
}
