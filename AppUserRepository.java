package com.vijayadurga.rolebased.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vijayadurga.rolebased.models.AppUser;
//repository layer It provides an abstraction layer over the database and simplifies the process of interacting with database
public interface AppUserRepository extends JpaRepository<AppUser,Integer> {
	public AppUser findByUsername(String username);
	public AppUser findByEmail(String email);
	

}
