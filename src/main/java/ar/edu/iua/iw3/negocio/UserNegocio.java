package ar.edu.iua.iw3.negocio;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ar.edu.iua.iw3.cuentas.User;
import ar.edu.iua.iw3.modelo.Producto;
import ar.edu.iua.iw3.modelo.persistencia.UserRepository;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;
import java.util.Optional;


@Service
public class UserNegocio implements IUserNegocio {

	
	private Logger log = LoggerFactory.getLogger(UserNegocio.class);
	
	@Autowired
	private UserRepository userDAO;

	@Override
	public User cargar(long id) throws NegocioException, NoEncontradoException {
		Optional<User> o;
		try {
			o = userDAO.findById(id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (!o.isPresent()) {
			throw new NoEncontradoException("No se encuentra el usuario con id=" + id);
		}
		return o.get();
	}

	@Override
	public List<User> listar() throws NegocioException {
		try {
			return userDAO.findAll();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
	}

	@Override
	public User agregar(User user) throws NegocioException, EncontradoException {
		try {
				cargar(user.getId()); 	// tira excepcion sino no lo encuentra
				throw new EncontradoException("Ya existe un usuario con ese id=" + user.getId());
			} catch (NoEncontradoException e) {
			}
			try {
				return userDAO.save(user);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				throw new NegocioException(e);
			}
	}

	private User findUserByUsernameOrEmail(User user) {
		return userDAO.findFirstByUsernameOrEmail(user.getUsername(),user.getEmail()).orElse(null);
	}
	
	
	@Override
	public User modificar(User user) throws NegocioException, NoEncontradoException {
		//me viene un id existe con su username o email
		//Paso 1: busco existencia del id del user	
		//Paso 2: busco existencia de "mail" o "username" duplicado 	
		//Paso 3_a: si el "mail" o "username" del user esta asignado a un user con diferente id del que se quiere modificar entonces tengo que generar un error
		//Paso 3_b: si el "mail" o "username" del user es el mismo id del user que me viene, entonces no se debe de generar error
		//Paso 4:  Sino ningun user tiene asignado el "mail" o "username" se lo debe de modificar sin problemas
		
		cargar(user.getId()); //Paso 1
		User userExists = findUserByUsernameOrEmail(user);
		
		if(userExists != null) { //Paso 2 
			
			if (user.getId() != userExists.getId()) 
				throw new NegocioException("Ya existe el usuario " + user + "con el username ="
						+ user.getUsername() + " o el email" + user.getEmail() );	//Paso 3_a
			
			return	userDAO.save(userExists);	//Paso 3_b
		}
		
		return saveUser(user);	//Paso 4
	}
	
	
	private  User saveUser(User user) throws NegocioException {
		try {
			return userDAO.save(user); // sino existe el user lo cargo
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
	}
	
	
	
	@Override
	public User cargarPorNombreOEmail(String nombreOEmail) throws NegocioException, NoEncontradoException {
		Optional <User>  o = null;
		try {
			o = userDAO.findFirstByUsernameOrEmail(nombreOEmail, nombreOEmail);
		}catch (Exception e) {
			throw new NegocioException(e);
		}
		if(!o.isPresent())
			throw new NoEncontradoException(String.format("No se encuentra un user con nombre  o email =  '%s'",nombreOEmail));
	
		return o.get();
	}
	
	
	@Override
	public void eliminar(long id) throws NegocioException, NoEncontradoException {
		cargar(id);
		try {
			userDAO.deleteById(id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
	}
	

}
