package com.tourstravels.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "otp_verification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    
    @Column(nullable = false, unique = true)
    private String contact; // Can be email or mobile

    @Column(nullable = false)
    private String otp;

	public Otp(int id, String contact, String otp) {
		super();
		this.id = id;
		this.contact = contact;
		this.otp = otp;
	}

	public Otp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	@Override
	public String toString() {
		return "Otp [id=" + id + ", contact=" + contact + ", otp=" + otp + "]";
	}

	
    
}
