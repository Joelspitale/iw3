package ar.edu.iua.iw3.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.iua.iw3.negocio.IRolNegocio;
import ar.edu.iua.iw3.negocio.RolNegocio;

@RestController
public class RolRestController {
	
	@Autowired
	private IRolNegocio rolNegocio;
	
	private Logger log = LoggerFactory.getLogger(RolNegocio.class);

}
