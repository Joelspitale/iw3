package ar.edu.iua.iw3.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ar.edu.iua.iw3.auth.CustomTokenAuthenticationFilter;
import ar.edu.iua.iw3.negocio.IUserNegocio;
import ar.edu.iua.iw3.security.authtoken.IAuthTokenBusiness;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private IAuthTokenBusiness iAuthTokenBusiness;

	@Autowired
	private IUserNegocio iUserBusiness;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//lista de cuentas en memoria
		 /*auth.inMemoryAuthentication()
		.withUser("user").password(passwordEncoder().encode("123"))
			.roles("USER")
		.and()
		.withUser("admin").password(passwordEncoder().encode("123"))
			.roles("USER","ADMIN");*/

		//se extrae las credenciales de los usuarios desde la bd
		auth.userDetailsService(userDetailsService);
	}
	
	
	//cifrador de credeciales
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		//http.httpBasic();//autenticacion 
		
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/login").permitAll(); //permito a todos los usuarios que ingresen a la pantalla cuya uri empieze con login
		
		http.authorizeRequests().antMatchers("/productos").hasRole("ADMIN"); // los roles se asignan dependiendo de quien se autentifica		
		
		http.authorizeRequests().antMatchers("/test").hasAnyRole("ADMIN","USER");
		
		http.authorizeRequests().antMatchers("/productos").authenticated(); //autorizacion en todas las paginas

		//agregamos nuestro propio filtro que verifica que el token es correcto, valido, etc.
		http.addFilterAfter(new CustomTokenAuthenticationFilter(iAuthTokenBusiness, iUserBusiness), UsernamePasswordAuthenticationFilter.class);

		//lo hacemos al server sin estado, es decir el servidor no se va a encargar de mantener las sesiones
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//desactivo la cooke de JSessionID
		
		
		/*
		//http.formLogin().defaultSuccessUrl("/productos"); //tras el logueo exitoso le indico el servicio que tiene que consumir por defecto, en este caso es "/producto"
		http.formLogin().defaultSuccessUrl("/ui/index.html").and().logout().deleteCookies("JSESSIONID","rememberme-iw3"); 
		
		http.rememberMe().rememberMeCookieName("rememberme-iw3").alwaysRemember(true);
		*/
		}
}
