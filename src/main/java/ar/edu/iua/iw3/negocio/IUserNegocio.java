package ar.edu.iua.iw3.negocio;

import java.util.List;


import ar.edu.iua.iw3.cuentas.User;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

public interface IUserNegocio {
	
	public User cargar(long id) throws NegocioException, NoEncontradoException;
	
	public List <User> listar() throws NegocioException;
	
	public User agregar(User user) throws NegocioException, EncontradoException;
	
	public User modificar (User user) throws NegocioException, NoEncontradoException;
	
	public User cargarPorNombreOEmail(String nombreOEmail) throws NegocioException, NoEncontradoException;

	void eliminar(long id) throws NegocioException, NoEncontradoException;
	
}
