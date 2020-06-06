package es.uned.foederis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import es.uned.foederis.sesion.service.UserService;

@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserService userService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth
			.userDetailsService(userService)
			.passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		  http
		  	.authorizeRequests()
		  		.antMatchers("/Evento/invitado/**", "/chat_invitado").permitAll()
		  		.anyRequest().authenticated() 
		  		.and() 
		  	.httpBasic()
		  		.and() 
		  	.formLogin() 
		  		.loginPage("/login") 
		  		.permitAll() 
		  		.and() 
		  	.logout()
		  		.permitAll()
		  		.and()
	  		.headers().httpStrictTransportSecurity()
	          	.maxAgeInSeconds(0)
	          	.includeSubDomains(true);
	}

}
