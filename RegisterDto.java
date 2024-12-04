package com.vijayadurga.rolebased.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterDto {
 @NotEmpty
 private String firstName;
 @NotEmpty
 private String lastName;
 @NotEmpty
 private String username;
 @NotEmpty
 private String email;
 @NotEmpty
 private String phone;
 @NotEmpty
 private String address;
 @NotEmpty
 @Size(min=6,message="Minimum password length is 6 characters")
 private String password;
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
}
