package com.ndurance.user_servince.repository;

import com.ndurance.user_servince.entity.PasswordResetTokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long>{
	PasswordResetTokenEntity findByToken(String token);
}
