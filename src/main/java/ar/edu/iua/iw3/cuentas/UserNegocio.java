package ar.edu.iua.iw3.cuentas;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;



import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;
import java.util.Optional;


@Service
public class UserNegocio implements IUserNegocio {

	@Autowired
	private UserRepository userDAO;

	@Override
	public User cargar(int id) throws NegocioException, NoEncontradoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> lista() throws NegocioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User agregar(User user) throws NegocioException, EncontradoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User modificar(User user) throws NegocioException, EncontradoException {
		// TODO Auto-generated method stub
		return null;
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
	

	

}
