package com.tourstravels.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tourstravels.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.email = :emailOrMobile OR u.mobile = :emailOrMobile")
    Optional<User> findByEmailOrPhone(@Param("emailOrMobile") String emailOrMobile);
}
