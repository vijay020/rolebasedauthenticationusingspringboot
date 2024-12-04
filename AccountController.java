package com.vijayadurga.rolebased.controllers;


import java.time.Instant;

import java.util.HashMap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.vijayadurga.rolebased.models.AppUser;
import com.vijayadurga.rolebased.models.LoginDto;
import com.vijayadurga.rolebased.models.RegisterDto;
import com.vijayadurga.rolebased.repositories.AppUserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/account")
public class AccountController {
@Value("${security.jwt.secret-key}")//reading the secret key from application properties
private String jwtSecretKey;
@Value("${security.jwt.issuer}")
private String jwtIssuer;
@Autowired
private AppUserRepository appUserRepository;
@Autowired
private AuthenticationManager authenticationManager;
//authenticating the user
@GetMapping("/profile")
public ResponseEntity<Object> profile(Authentication auth){
	var response=new HashMap<String,Object>();
	response.put("Username", auth.getName());
	response.put("Authorities", auth.getAuthorities());
	var appUser=appUserRepository.findByUsername(auth.getName());
	response.put("User", appUser);
	return ResponseEntity.ok(response);
	//in here only /profile has been authenticated 
}

//here we post the new user by checking that the user already exist or not
@PostMapping("/register")
public ResponseEntity<Object> register(
	@Valid @RequestBody RegisterDto registerDto,BindingResult result){
	if(result.hasErrors()) {
		var errorList=result.getAllErrors();
		var errorsMap=new HashMap<String,String>();
		for(int i=0;i<errorList.size();i++) {
			var error=(FieldError) errorList.get(i);
			errorsMap.put(error.getField(),error.getDefaultMessage());
			
		}
		return ResponseEntity.badRequest().body(errorsMap);
	}
	
	var bCryptEncoder=new BCryptPasswordEncoder();
	AppUser appUser=new AppUser();
	appUser.setFirstName(registerDto.getFirstName());
	appUser.setLastName(registerDto.getLastName());
	appUser.setUsername(registerDto.getUsername());
	appUser.setEmail(registerDto.getEmail());
	appUser.setRole("client");
	
	appUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));
	
	try {
		var otherUser=appUserRepository.findByUsername(registerDto.getUsername());
	    if(otherUser!=null) {
	    	return ResponseEntity.badRequest().body("username already exist");
	    }
	    otherUser=appUserRepository.findByEmail(registerDto.getEmail());
	    if(otherUser!=null) {
	    	return ResponseEntity.badRequest().body("email already exist");//bad request for the user or email already exist
	    }
	    appUserRepository.save(appUser);
	    
	    String jwtToken=createJwtToken(appUser);
	    var response=new HashMap<String,Object>();
	    response.put("token", jwtToken);
	    response.put("user", appUser);
	    return ResponseEntity.ok(response);
	    //here we assign the token for the user using createJwtToken
	}
	
	catch(Exception e) {
		System.out.println("There is an Exception");
		e.printStackTrace();
	}
	return ResponseEntity.badRequest().body("Error");
	}
	//authenticating the user
@PostMapping("/ nlogin")
public ResponseEntity<Object> login(
	@Valid @RequestBody LoginDto loginDto
	,BindingResult result){
		if(result.hasErrors()) {
			var errorsList=result.getAllErrors();
			var errorsMap=new HashMap<String,String>();
			for(int i=0;i<errorsList.size();i++) {
				var error=(FieldError) errorsList.get(i);
				errorsMap.put(error.getField(),error.getDefaultMessage());
				
			}
			return ResponseEntity.badRequest().body(errorsMap);
		}
		// with the try catch block we check the existence of the user 
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginDto.getUsername(),
							loginDto.getPassword()
							)
					
					);
			AppUser appUser=appUserRepository.findByUsername(loginDto.getUsername());
			String jwtToken=createJwtToken(appUser);
			var response=new HashMap<String,Object>();
			response.put("token", jwtToken);
			response.put("user", appUser);
			return ResponseEntity.ok(response);
			
		}
		catch(Exception e) {
			System.out.println("there is an exception:");
			e.printStackTrace();
		}
		return ResponseEntity.badRequest().body("bad username or password");
	}

private String createJwtToken(AppUser appUser) {
	Instant now =Instant .now();
	JwtClaimsSet claims=JwtClaimsSet.builder()
			.issuer(jwtIssuer)
			.issuedAt(now)
			.expiresAt(now.plusSeconds(24*3600))
			.subject(appUser.getUsername())
			.claim("role",appUser.getRole())
			.build();
	var encoder=new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecretKey.getBytes()));
	var params=JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),claims);
	return encoder.encode(params).getTokenValue();
	
	
}

}
