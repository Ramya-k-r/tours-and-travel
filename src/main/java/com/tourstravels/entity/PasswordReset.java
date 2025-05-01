package com.tourstravels.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "forgot-password")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordReset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String otp;

	public PasswordReset(Long id, String email, String otp) {
		super();
		this.id = id;
		this.email = email;
		this.otp = otp;
	}

	public PasswordReset() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	@Override
	public String toString() {
		return "PasswordReset [id=" + id + ", email=" + email + ", otp=" + otp + "]";
	}
    
}
