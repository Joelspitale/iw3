package ar.edu.iua.iw3.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)	// previa y pos ejecucion coloco filtro para analizar las autorizaciones ver los metodos de rest controller de la clase autorizacionesRestController
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

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));	//seteo los dominio de origen
		configuration.setAllowCredentials(true);				//envio en la cabecera credenciales de tipo http.basic
		configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Access-Control-Allow-Origin",
				"Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin", "Cache-Control",
				"Content-Type", "Authorization", "XAUTHTOKEN", "X-Requested-With", "X-AUTH-TOKEN"));

		//metodos http permitidos
		configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PATCH", "PUT", "OPTIONS"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);	//aplica la confiuracion que antes setee a todos los endpoints
		return source;
	}

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.cors();	//habilito que mi app corra desde otro puerto

		//http.httpBasic();//autenticacion 
		
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/login*").permitAll(); //permito a todos los usuarios que ingresen a la pantalla cuya uri empieze con login
		http.authorizeRequests().antMatchers("/index.html").permitAll();
		http.authorizeRequests().antMatchers("/favicon.png").permitAll();
		http.authorizeRequests().antMatchers("/ui/**").permitAll();
		http.authorizeRequests().antMatchers("/").permitAll();	//seteo que me permita ingresar a la pagina por defecto cuando ingreso la url como "localhost:8080"

		//http.authorizeRequests().antMatchers("/productos").permitAll();//.hasRole("ADMIN"); // los roles se asignan dependiendo de quien se autentifica

		http.authorizeRequests().antMatchers("/test").hasAnyRole("ADMIN","USER");

		//http.authorizeRequests().antMatchers("/productos").permitAll();//authenticated(); //autorizacion sin logueo en el directorio "productos"

		http.authorizeRequests().anyRequest().authenticated(); //tiene que estar autenticado para cualquier directorio de nuestra pagina web
		

		//http.authorizeRequests().antMatchers("/componentes").authenticated(); //autorizacion en todas las paginas

		//agregamos nuestro propio filtro que verifica que el token es correcto, valido, etc.
		http.addFilterAfter(new CustomTokenAuthenticationFilter(iAuthTokenBusiness, iUserBusiness), UsernamePasswordAuthenticationFilter.class);

		//lo hacemos al server sin estado, es decir el servidor no se va a encargar de mantener las sesiones
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//desactivo la cooke de JSessionID


		/*
		//http.formLogin().defaultSuccessUrl("/productos"); //tras el logueo exitoso le indico el servicio que tiene que consumir por defecto, en este caso es "/producto"
		http.rememberMe().rememberMeCookieName("rememberme-iw3").alwaysRemember(true);
		*/
		}
}
