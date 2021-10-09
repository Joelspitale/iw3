package ar.edu.iua.iw3.web;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.iua.iw3.cuentas.User;
import ar.edu.iua.iw3.negocio.IUserNegocio;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

@RestController
public class CoreRestController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IUserNegocio userNegocio;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//https://www.iua.edu.ar/?username=pepe&password=123
	
	//obtengo el token solamente
	@PostMapping(value="/login")
	public ResponseEntity<String> loginToken(@RequestParam(value = "username") String username, 
			@RequestParam(value = "password") String password) {
		try {
			User u = userNegocio.cargarPorNombreOEmail(username); // existe usuario?
			String msg = u.checkAccount(passwordEncoder, password);	//ok cuenta = null
			//devuele nulo checkAccount si todo esta bien
			if (msg != null) {
				u.agregaIntentoFallido(); //seteo en memory el contador de intento fallido de un usuario
				userNegocio.modificar(u); // lo actualizo ese usuario en la bd
				return new ResponseEntity<String>(msg, HttpStatus.UNAUTHORIZED);
			} else {
				u.setIntentosFallidos(0);
				//obtengo el usuario que esta logueado por el contexto y sus roles
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(u, null,
						u.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);
				//creo el token y lo cargo al mismo junto con los datos del usuario que tengo en el contexto en un json
				return new ResponseEntity<String>(userToJson(getUserLogged()).get("authtoken").toString(),
						HttpStatus.OK);
			}
		} catch (NegocioException e) {
			log.error(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoEncontradoException e) {
			return new ResponseEntity<String>("BAD_ACCOUNT_NAME", HttpStatus.UNAUTHORIZED);
		}

	}
	//obtengo el token mas datos del usuario
	@PostMapping(value="/login-json")
	public ResponseEntity<String> loginTokenFullJson(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password) {
		try {
			User u = userNegocio.cargarPorNombreOEmail(username);
			String msg = u.checkAccount(passwordEncoder, password);
			if (msg != null) {
				u.agregaIntentoFallido(); //seteo en memory el contador de intento fallido de un usuario
				userNegocio.modificar(u); // lo actualizo ese usuario en la bd
				return new ResponseEntity<String>(msg, HttpStatus.UNAUTHORIZED);
			} else {
				u.setIntentosFallidos(0);
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(u, null,
						u.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);

				return new ResponseEntity<String>(userToJson(getUserLogged()).toString(),
						HttpStatus.OK);
			}
		} catch (NegocioException e) {
			log.error(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoEncontradoException e) {
			return new ResponseEntity<String>("BAD_ACCOUNT_NAME", HttpStatus.UNAUTHORIZED);
		}

	}

}
