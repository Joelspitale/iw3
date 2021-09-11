package ar.edu.iua.iw3.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.iw3.cuentas.IUserNegocio;
import ar.edu.iua.iw3.cuentas.User;
import ar.edu.iua.iw3.cuentas.UserNegocio;
import ar.edu.iua.iw3.modelo.Producto;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

@RestController
public class UsersRestController {
	
	@Autowired
	private IUserNegocio userNegocio;
	
	private Logger log = LoggerFactory.getLogger(UserNegocio.class);
	
	@GetMapping(value="/usuarios/{id}")
	  public ResponseEntity<User> buscarUsuarioPorId(@PathVariable("id") Long id) throws NoEncontradoException {
	    try {
	      return new ResponseEntity<User>(userNegocio.cargar(id), HttpStatus.OK);
	    } catch (NegocioException e) {
	      return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
	    } catch (NoEncontradoException e) {
		return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
	    }
	  }

	@GetMapping(value="/usuarios")
	public ResponseEntity<List<User>> listado() {
		try {
			return new ResponseEntity<List<User>>(userNegocio.listar(), HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value="/usuarios")
	public ResponseEntity<String> agregar(@RequestBody User usuario) {
		try {
			User respuesta=userNegocio.agregar(usuario);
			HttpHeaders responseHeaders=new HttpHeaders();
			responseHeaders.set("location", "/usuarios/"+respuesta.getId());
			return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);
		} catch (NegocioException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (EncontradoException e) {
			log.error(e.getMessage(), e);	//Se hace para ver el msj de error cuando el detalle esta duplicado al agregar un producto
			return new ResponseEntity<String>(HttpStatus.FOUND);
		}
	}
	
	@PutMapping(value="/usuarios")
	public ResponseEntity<String> modificar(@RequestBody User user) {
		try {
			userNegocio.modificar(user);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (NegocioException e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoEncontradoException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
	
}
