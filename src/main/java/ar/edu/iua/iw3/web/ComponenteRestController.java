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

import ar.edu.iua.iw3.modelo.Componente;
import ar.edu.iua.iw3.modelo.Producto;
import ar.edu.iua.iw3.negocio.ComponenteNegocio;
import ar.edu.iua.iw3.negocio.IComponenteNegocio;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

@RestController
public class ComponenteRestController {

	
	@Autowired
	private IComponenteNegocio componenteNegocio;
	
	
	private Logger log = LoggerFactory.getLogger(ComponenteNegocio.class);

	@GetMapping(value="/componentes")
	public ResponseEntity<List<Componente>> listado() {
		try {
			return new ResponseEntity<List<Componente>>(componenteNegocio.listado(), HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<List<Componente>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PostMapping(value="/componentes")
	public ResponseEntity<String> agregar(@RequestBody Componente componente) {
		try {
			Componente respuesta=componenteNegocio.agregar(componente);
			HttpHeaders responseHeaders=new HttpHeaders();
			responseHeaders.set("location", "/componentes/"+respuesta.getId());
			return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);
		} catch (NegocioException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (EncontradoException e) {
			log.error(e.getMessage(), e);	//Se hace para ver el msj de error cuando el detalle esta duplicado al agregar un componente
			return new ResponseEntity<String>(HttpStatus.FOUND);
		}
	}
	
	
	@PutMapping(value="/componentes")
	public ResponseEntity<String> modificar(@RequestBody Componente componente) {
		try {
			componenteNegocio.modificar(componente);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (NegocioException e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoEncontradoException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping(value="/componentes/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") long id) {
		try {
			componenteNegocio.eliminar(id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoEncontradoException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
	
	
}
