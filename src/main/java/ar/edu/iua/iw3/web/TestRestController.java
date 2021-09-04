package ar.edu.iua.iw3.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.iw3.modelo.Producto;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;


@RestController
@RequestMapping("/test")
public class TestRestController {

	@GetMapping(value="")
	public ResponseEntity<String> listado() {
			return new ResponseEntity<String>("Hola desde el servicio test", HttpStatus.OK);
		
	}

}
