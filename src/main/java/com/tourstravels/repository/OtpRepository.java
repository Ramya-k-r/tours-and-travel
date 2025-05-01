package com.tourstravels.repository;

import com.tourstravels.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Integer> {
	Optional<Otp> findByContact(String contact);
}
