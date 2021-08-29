package ar.edu.iua.iw3.demo.perfiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
@Component
@Profile("mysqlprod")
public class PruebaPerfilMySqlProd implements IPruebaPerfil{

	private Logger log = LoggerFactory.getLogger(PruebaPerfilMySqlProd.class);
	
	@Override
	public void mensaje() {
		log.debug("***** MySqlProd *****");
		
	}

	

}
