package ar.edu.iua.iw3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	//lista de usuarios
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("user").password(passwordEncoder().encode("123"))
			.roles("USER")
		.and()
		.withUser("admin").password(passwordEncoder().encode("123"))
			.roles("USER","ADMIN");
	}
	
	
	//cifrador de credeciales
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic();//autenticacion 
		//http.authorizeRequests().antMatchers("/productos").authenticated(); //autorizacion
		http.authorizeRequests().antMatchers("/productos").hasRole("ADMIN"); // los roles se asignan dependiendo de quien se autentifica
	}
}
