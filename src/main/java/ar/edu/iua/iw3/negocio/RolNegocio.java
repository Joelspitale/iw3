package ar.edu.iua.iw3.negocio;

import java.util.List;
import java.util.Optional;
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
		Optional<Rol> o;
		try {
			o = rolDAO.findById(id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (!o.isPresent()) {
			throw new NoEncontradoException("No se encuentra el rol con id=" + id);
		}
		return o.get();
	}

	@Override
	public List<Rol> listar() throws NegocioException {
		try {
			return rolDAO.findAll();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
	}
	
	private Rol findRolByName(String nombre) {
		return rolDAO.findByNombre(nombre).orElse(null);
	}

	@Override
	public Rol agregar(Rol rol) throws NegocioException, EncontradoException {
		// El nombre de un rol es unico por lo que busco primero si el nombre del rol ya existe
		try {
			   if(null!=findRolByName(rol.getNombre()))
			        throw new EncontradoException("Ya existe un rol con el nombre =" + rol.getNombre());
				cargar(rol.getId()); 									// tira excepcion sino no lo encuentra
				throw new EncontradoException("Ya existe un rol con id=" + rol.getId());
			} catch (NoEncontradoException e) {
			}
			try {
				return rolDAO.save(rol);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				throw new NegocioException(e);
			}
	}

	@Override
	public Rol modificar(Rol rol) throws NegocioException, NoEncontradoException {
				//me viene un id existe con su nombre
				//Paso 1: busco existencia del id del rol	
				//Paso 2: busco existencia de nombre duplicado 	
				//Paso 3_a:si el nombre del rol esta asignado a un rol con diferente id del que se quiere modificar entonces tengo que generar un error
				//Paso 3_b: si el nombre del rol es el mismo id del rol que me viene, entonces no se debe de generar error
				//Paso 4:  Sino ningun rol tiene asignado el nombre se lo debe de modificar sin problemas
				
				cargar(rol.getId()); //Paso 1
				Rol rolWithDescription = findRolByName(rol.getNombre());		
				
				if(null!=rolWithDescription) { //Paso 2 
					
					if (rol.getId() != rolWithDescription.getId()) 
						throw new NegocioException("Ya existe el producto " + rolWithDescription.getId() + "con la descripcion ="
								+ rol.getDescripcion());	//Paso 3_a
					
					return	saveRol(rol);	//Paso 3_b
				}
				
				return saveRol(rol);	//Paso 4
	}

	private  Rol saveRol(Rol rol) throws NegocioException {
		try {
			return rolDAO.save(rol); // sino existe el rol lo cargo
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
	}
	
	
	@Override
	public void eliminar(long id) throws NegocioException, NoEncontradoException {
		cargar(id);
		try {
			rolDAO.deleteById(id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		
	}

	
}
