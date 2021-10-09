package ar.edu.iua.iw3.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ar.edu.iua.iw3.cuentas.User;
import ar.edu.iua.iw3.negocio.IUserNegocio;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;
@Service
public class PersistenceUserDetailService implements UserDetailsService {

	@Autowired
	private IUserNegocio userNegocio;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = null;
		
		try {
			user = userNegocio.cargarPorNombreOEmail(username);
		} catch (NegocioException e) {
			e.printStackTrace();
		} catch (NoEncontradoException e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
		
		return user;
	}

}
