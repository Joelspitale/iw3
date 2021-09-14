package ar.edu.iua.iw3.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.iw3.cuentas.Rol;
import ar.edu.iua.iw3.negocio.IRolNegocio;
import ar.edu.iua.iw3.negocio.RolNegocio;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

@RestController
public class RolRestController {
	
	@Autowired
	private IRolNegocio rolNegocio;
	
	private Logger log = LoggerFactory.getLogger(RolNegocio.class);
	
	//listar todos los roles
	@GetMapping(value="/roles")
	public ResponseEntity<List<Rol>> listado() {
		try {
			return new ResponseEntity<List<Rol>>(rolNegocio.listar(), HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<List<Rol>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//agregar
	@PostMapping(value="/roles")
	public ResponseEntity<String> agregar(@RequestBody Rol rol) {
		try {
			Rol respuesta=rolNegocio.agregar(rol);
			HttpHeaders responseHeaders=new HttpHeaders();
			responseHeaders.set("location", "/roles/"+respuesta.getId());
			return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);
		} catch (NegocioException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (EncontradoException e) {
			log.error(e.getMessage(), e);	//Se hace para ver el msj de error cuando el detalle esta duplicado al agregar un rol
			return new ResponseEntity<String>(HttpStatus.FOUND);
		}
	}
	//modificar
	@PutMapping(value="/roles")
	public ResponseEntity<String> modificar(@RequestBody Rol rol) {
		try {
			rolNegocio.modificar(rol);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (NegocioException e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoEncontradoException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
	//eliminar
	@DeleteMapping(value="/roles/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") long id) {
		try {
			rolNegocio.eliminar(id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoEncontradoException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}

}
