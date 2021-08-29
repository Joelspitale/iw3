package ar.edu.iua.iw3.demo.perfiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
@Component
@Profile("mysqldev")
public class PruebaPerfilMySqlDev implements IPruebaPerfil{

	private Logger log = LoggerFactory.getLogger(PruebaPerfilMySqlDev.class);
	
	@Override
	public void mensaje() {
		log.debug("***** MySqlDev *****");
		
	}

	

}
