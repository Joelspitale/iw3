package ar.edu.iua.iw3.negocio;

import ar.edu.iua.iw3.modelo.Componente;
import ar.edu.iua.iw3.modelo.persistencia.ComponenteRepository;
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
public class ComponenteNegocio implements IComponenteNegocio{
    @Autowired
    private ComponenteRepository componenteDAO;

    private Logger log = LoggerFactory.getLogger(ComponenteNegocio.class);

    @Override
    public List<Componente> listado() throws NegocioException {
        try {
            return componenteDAO.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
    }

    @Override
    public Componente cargar(long id) throws NegocioException, NoEncontradoException {
        Optional<Componente> o;
        try {
            o = componenteDAO.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
        if (!o.isPresent()) {
            throw new NoEncontradoException("No se encuentra el componente con id=" + id);
        }
        return o.get();
    }

    @Override
    public Componente agregar(Componente componente) throws NegocioException, EncontradoException {
        try {
            if(null!=findComponentByDescripcion(componente.getDescripcion()))
                throw new EncontradoException("Ya existe un componente con la descripcion =" + componente.getDescripcion());
            cargar(componente.getId()); 									// tira excepcion sino no lo encuentra
            throw new EncontradoException("Ya existe un componente con id=" + componente.getId());
        } catch (NoEncontradoException e) {
        }
        try {
            return componenteDAO.save(componente);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
    }

    private Componente findComponentByDescripcion(String descripcion) {
        return componenteDAO.findByDescripcion(descripcion).orElse(null);
    }

    @Override
    public Componente modificar(Componente componente) throws NegocioException, NoEncontradoException {
        //me viene un id existe con su detalle
        //Paso 1: busco existencia del id del componente
        //Paso 2: busco existencia de detalle duplicado
        //Paso 3_a:si el detalle del componente esta asignado a un componente con diferente id del que se quiere modificar entonces tengo se genera un error
        //Paso 3_b: si el detalle del componente es el mismo id del produto entonces no se debe de generar error
        //Paso 4:  Sino ningun componente tiene asignado el detalle se lo debe de modiicar sin problemas

        cargar(componente.getId()); //Paso 1
        Componente componeteWithDescription = findComponentByDescripcion(componente.getDescripcion());

        if(null!=componeteWithDescription) { //Paso 2

            if (componente.getId() != componeteWithDescription.getId())
                throw new NegocioException("Ya existe el componete " + componeteWithDescription.getId() + "con la descripcion ="
                        + componente.getDescripcion());	//Paso 3_a

            return	saveComponent(componente);	//Paso 3_b
        }

        return saveComponent(componente);	//Paso 4
    }

    private  Componente saveComponent(Componente componente) throws NegocioException {
        try {
            return componenteDAO.save(componente); // sino existe el componente lo cargo
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
    }
    @Override
    public void eliminar(long id) throws NegocioException, NoEncontradoException {
        cargar(id);
        try {
            componenteDAO.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
    }
}
