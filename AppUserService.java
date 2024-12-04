package com.vijayadurga.rolebased.service;
import com.vijayadurga.rolebased.repositories.AppUserRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vijayadurga.rolebased.models.AppUser;
import com.vijayadurga.rolebased.repositories.AppUserRepository;
@Service
//service layer will provide the data flow
public class AppUserService implements UserDetailsService{
	@Autowired
	private AppUserRepository repo;
	@Override
	public UserDetails loadUserByUsername(String  username) throws UsernameNotFoundException{
		AppUser appUser=repo.findByUsername(username);
	  // user details get by user name
		if (appUser !=null) {
			var springUser=User.withUsername(appUser.getUsername())
					.password(appUser.getPassword())
					.roles(appUser.getRole())
					.build();
			return springUser; // if user exist
		}
		
		return null;// if user donot exist
	}
	
		
	}
	
	


