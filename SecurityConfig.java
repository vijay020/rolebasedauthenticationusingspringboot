package com.vijayadurga.rolebased.config;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import com.vijayadurga.rolebased.service.AppUserService;
//for the security configuration of application
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Value("${security.jwt.secret-key}")// reading the secret key for jwt usage
	private String jwtSecretKey;
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			 	.csrf(csrf-> csrf.disable())//csrf is disabled 
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/").permitAll() //for this api permission is given for all
						.requestMatchers("/store/**").permitAll()//for this api permission is given for all
						.requestMatchers("/account").permitAll()//for this api permission is given for all
						.requestMatchers("/account/login").permitAll()//for this api permission is given for all
						.requestMatchers("/account/register").permitAll()//for this api permission is given for all
						.anyRequest().authenticated()//(2)//for any other request user need to be authenticated
						)
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
				.sessionManagement(session -> session.sessionCreationPolicy(
						SessionCreationPolicy.STATELESS))
				.build();
	}
	@Bean
		public   JwtDecoder jwtDecoder() {
			var secretKey=new SecretKeySpec(jwtSecretKey.getBytes(),"");
			return NimbusJwtDecoder.withSecretKey(secretKey)
					.macAlgorithm(MacAlgorithm.HS256).build();
			
		
		}
	//here jwt tokens received from the user will be verified by the bean authentication manager
	 
	@Bean
	public AuthenticationManager authenticationManager(AppUserService appUserService) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(appUserService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		return new ProviderManager(provider);// provider use the encoded 
	}
		
	
	
	

}
