package ar.edu.iua.iw3.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	//lista de usuarios
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/*auth.inMemoryAuthentication()
		.withUser("user").password(passwordEncoder().encode("123"))
			.roles("USER")
		.and()
		.withUser("admin").password(passwordEncoder().encode("123"))
			.roles("USER","ADMIN");*/
	
		auth.userDetailsService(userDetailsService); //se extrae las credenciales de los usuarios desde la bd
	}
	
	
	//cifrador de credeciales
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic();//autenticacion 
		
		http.authorizeRequests().antMatchers("/productos").hasRole("ADMIN"); // los roles se asignan dependiendo de quien se autentifica		
		
		http.authorizeRequests().antMatchers("/test").hasAnyRole("ADMIN","USER");
		
		http.authorizeRequests().antMatchers("/productos").authenticated(); //autorizacion en todas las paginas
		
		//http.formLogin().defaultSuccessUrl("/productos"); //tras el logueo exitoso le indico el servicio que tiene que consumir por defecto, en este caso es "/producto"
		http.formLogin().defaultSuccessUrl("/ui/index.html").and().logout().deleteCookies("JSESSIONID","rememberme-iw3"); 
		
		http.rememberMe().rememberMeCookieName("rememberme-iw3").alwaysRemember(true);
		
		http.csrf().disable();
		}
}
