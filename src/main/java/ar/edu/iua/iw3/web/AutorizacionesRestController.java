package ar.edu.iua.iw3.web;

import ar.edu.iua.iw3.cuentas.User;
import ar.edu.iua.iw3.negocio.IUserNegocio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

//para habilitar las anotaciones pre y post debo de colocarlo la anotacion @EnableGlobalMethodSecurity(prePostEnabled = true)	en la clase securityCOnfig
@RestController
@RequestMapping(Constantes.URL_AUTH)	//"/api/v1/auth"
public class AutorizacionesRestController extends BaseRestController {

	@PreAuthorize("hasRole('ROLE_ADMIN')")	//indico el rol que puede consumir este end-point
	@GetMapping("/admin")
	public ResponseEntity<String> onlyAdmin() {
		return new ResponseEntity<String>("Servicio admin", HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/user")
	public ResponseEntity<String> onlyUser() {
		return new ResponseEntity<String>("Servicio user", HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/user-o-admin")
	public ResponseEntity<String> rolUserOAdmin() {
		return new ResponseEntity<String>("Servicio user", HttpStatus.OK);
	}

	//pido token con un usuario, me logue y luego con ese usuario puedo pedir los roles de ese usuario
	//Verifico primeramente si el usuario con el que estoy logueado es igual al que me envio
	@PreAuthorize("#username == authentication.principal.username")	// los "parametro de entrada" se obtiene con el #tipo de objeto en el request
	@GetMapping("/mis-roles")			// con getUserLogged obtengo los roles del usuario logueado actualmente
	public ResponseEntity<String> misRoles(@RequestParam("username") String username) {
		return new ResponseEntity<String>(getUserLogged().getAuthorities().toString(), HttpStatus.OK);
	}

	//es el equivalente al metodo anterior pero usando java un poco mas viejo
	@GetMapping("/variable")		//HttpServletRequest --> representa el request en java en sus inicio
	public ResponseEntity<String> variable(HttpServletRequest request) {
		if (request.isUserInRole("ROLE_ADMIN")) {
			return new ResponseEntity<String>("Tenés rol admin", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("No tenés rol admin", HttpStatus.OK);
		}
	}

	//devolvemos el objeto usuario completo si coincide el nombre del usuario
	// logueado con el nombre del usuario que me envian
	@PostAuthorize("returnObject.username == #username") // con returnObject accedo a la respuesta que devuelve mi metodo
	@GetMapping("/datos-full")
	public User datosFull(@RequestParam("username") String username) {
		return getUserLogged();
	}

	@Autowired
	private IUserNegocio usuarioNegocio;

	//obtenemos el listado de todas las cuentas existentes menos el user con el que estoy logueado
	@PostFilter("filterObject != authentication.principal.username")	// aplica filtro sobre los datos, no es que me impide consumir el end-point
	@GetMapping("/filtrar-actual")
	public List<String> filtrarActual() {
		List<String> r = null;
		try {
			//genero una lista de todos los usuarios que estan logueados actualmente
			//con stream hago que pasen los usuarios de a 1
			//con map los hago ir cambiando
			r = usuarioNegocio.listar().stream().map(u -> u.getUsername()).collect(Collectors.toList());
			// a cada uno de los usuarios que devuelve la lista los llamo u
			// y de cada usuario quiero el username
			// y luego los junto a todos los username en una sola lista
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

}
