package ar.edu.iua.iw3.negocio;

import ar.edu.iua.iw3.modelo.Rubro;
import ar.edu.iua.iw3.modelo.persistencia.RubroRepository;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RubroNegocio implements IRubroNegocio {

    private Logger log = LoggerFactory.getLogger(RolNegocio.class);

    @Autowired
    private RubroRepository rubroDAO;


    @Override
    public Rubro cargar(long id) throws NegocioException, NoEncontradoException {
        Optional<Rubro> o;
        try {
            o = rubroDAO.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
        if (!o.isPresent()) {
            throw new NoEncontradoException("No se encuentra el rubro con id=" + id);
        }
        return o.get();
    }

    @Override
    public List<Rubro> listar() throws NegocioException {
        try {
            return rubroDAO.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
    }

    private Rubro findRubroByName(String rubro) {
        return rubroDAO.findByRubro(rubro).orElse(null);
    }


    @Override
    public Rubro agregar(Rubro rubro) throws NegocioException, EncontradoException {
        // El nombre de un rubro es unico por lo que busco primero si el nombre del rubro ya existe
        try {
            if(null!=findRubroByName(rubro.getRubro()))
                throw new EncontradoException("Ya existe un rubro con el nombre =" + rubro.getRubro());
            cargar(rubro.getId()); 									// tira excepcion sino no lo encuentra
            throw new EncontradoException("Ya existe un rubro con id=" + rubro.getId());
        } catch (NoEncontradoException e) {
        }
        try {
            return rubroDAO.save(rubro);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
    }

    @Override
    public Rubro modificar(Rubro rubro) throws NegocioException, NoEncontradoException {
        //me viene un id existe con su nombre
        //Paso 1: busco existencia del id del rubro
        //Paso 2: busco existencia de nombre duplicado
        //Paso 3_a:si el nombre del rubro esta asignado a un rubro con diferente id del que se quiere modificar entonces tengo que generar un error
        //Paso 3_b: si el nombre del rubro es el mismo id del rubro que me viene, entonces no se debe de generar error
        //Paso 4:  Sino ningun rubro tiene asignado el nombre se lo debe de modificar sin problemas

        cargar(rubro.getId()); //Paso 1
        Rubro rubroWithDescription = findRubroByName(rubro.getRubro());

        if(null!=rubroWithDescription) { //Paso 2

            if (rubro.getId() != rubroWithDescription.getId())
                throw new NegocioException("Ya existe el rubro " + rubroWithDescription.getId() + "con el nombre ="
                        + rubro.getRubro());	//Paso 3_a

            return	saveRubro(rubro);	//Paso 3_b
        }

        return saveRubro(rubro);	//Paso 4
    }

    private  Rubro saveRubro(Rubro rubro) throws NegocioException {
        try {
            return rubroDAO.save(rubro); // sino existe el rubro lo cargo
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
    }

    @Override
    public void eliminar(long id) throws NegocioException, NoEncontradoException {
        cargar(id);
        try {
            rubroDAO.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
    }

    @Override
    public void eliminarConQueryNative(long id) throws NegocioException, NoEncontradoException {
        cargar(id);
        try {
            rubroDAO.deleteRubroConQueryNative(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
    }
}
