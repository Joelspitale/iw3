package ar.edu.iua.iw3.negocio;

import java.util.List;

import ar.edu.iua.iw3.cuentas.Rol;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

public interface IRolNegocio {
public Rol cargar(long id) throws NegocioException, NoEncontradoException;
	
	public List <Rol> listar() throws NegocioException;
	
	public Rol agregar(Rol rol) throws NegocioException, EncontradoException;
	
	public Rol modificar (Rol rol) throws NegocioException, NoEncontradoException;
	
	void eliminar(long id) throws NegocioException, NoEncontradoException;
}
