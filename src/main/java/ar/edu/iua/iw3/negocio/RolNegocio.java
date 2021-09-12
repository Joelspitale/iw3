package ar.edu.iua.iw3.negocio;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.iw3.cuentas.Rol;
import ar.edu.iua.iw3.modelo.persistencia.RolRepository;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

@Service
public class RolNegocio implements IRolNegocio {

	private Logger log = LoggerFactory.getLogger(RolNegocio.class);
	
	@Autowired
	private RolRepository rolDAO;
	
	@Override
	public Rol cargar(long id) throws NegocioException, NoEncontradoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Rol> listar() throws NegocioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rol agregar(Rol rol) throws NegocioException, EncontradoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rol modificar(Rol rol) throws NegocioException, NoEncontradoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminar(long id) throws NegocioException, NoEncontradoException {
		// TODO Auto-generated method stub
		
	}

	
}
