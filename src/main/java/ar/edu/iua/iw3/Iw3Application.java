package ar.edu.iua.iw3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.password.PasswordEncoder;

import ar.edu.iua.iw3.cuentas.IUserNegocio;
import ar.edu.iua.iw3.demo.perfiles.IPruebaPerfil;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class Iw3Application extends SpringBootServletInitializer implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(Iw3Application.class, args);
	}

	@Autowired
	private IPruebaPerfil pruebaPerfil;
	
	@Autowired
	private PasswordEncoder pwdEncoder;
	
	//@Autowired
	//private IUserNegocio userNegocio;
	
	@Override
	public void run(String... args) throws Exception {
		pruebaPerfil.mensaje();
		System.out.print(pwdEncoder.encode("123"));
		
		//System.out.print(userNegocio.cargarPorNombreOEmail("user").getEmail());
		 
	}

}
