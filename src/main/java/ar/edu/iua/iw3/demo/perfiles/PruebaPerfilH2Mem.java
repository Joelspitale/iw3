package ar.edu.iua.iw3.demo.perfiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
@Component
@Profile("h2mem")
public class PruebaPerfilH2Mem implements IPruebaPerfil{

	private Logger log = LoggerFactory.getLogger(PruebaPerfilH2Mem.class);
	
	@Override
	public void mensaje() {
		log.debug("***** H2Mem *****");
		
	}

	

}
