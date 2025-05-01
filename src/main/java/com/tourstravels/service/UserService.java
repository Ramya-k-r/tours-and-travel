package com.tourstravels.service;

import com.tourstravels.entity.User;
import com.tourstravels.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String name, String email, String password, String mobile) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setMobile(mobile);
        
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByEmailOrMobile(String emailOrMobile) {
        return userRepository.findByEmailOrPhone(emailOrMobile);
    }

	public void updateUserPassword(String email, String encode) {
		// TODO Auto-generated method stub
		
	}
}
