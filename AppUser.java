package com.vijayadurga.rolebased.models;

import jakarta.persistence.*;
//model class is to create the tables for the application
@Entity
@Table(name="users")
public class AppUser {
	//these are the fields for the entity users
	//the database used here is mysql database
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) // id will be auto incremented
	private int id;
	private String firstName;
	private String lastName;
	@Column(unique=true)
	private String username; // unique identifier
	@Column(unique=true,nullable=false)
	private String email; //unique identifier
	private String phone;
	private String address;
	private String password;
	private String role;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
	

}
