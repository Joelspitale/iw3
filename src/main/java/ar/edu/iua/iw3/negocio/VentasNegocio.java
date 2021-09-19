package ar.edu.iua.iw3.negocio;

import ar.edu.iua.iw3.modelo.Ventas;
import ar.edu.iua.iw3.modelo.persistencia.VentasRepository;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VentasNegocio implements IVentasNegocio {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VentasRepository ventasDAO;

    @Override
    public Ventas cargar(long id) throws NegocioException, NoEncontradoException {
        Optional<Ventas> o;
        try {
            o = ventasDAO.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
        if (!o.isPresent()) {
            throw new NoEncontradoException("No se encuentra la venta con id=" + id);
        }
        return o.get();
    }



    @Override
    public List<Ventas> listar() throws NegocioException {
        try {
            return ventasDAO.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
    }

    private Ventas findFirstByFechaAndMonto(Date fecha, double monto) {
        Optional <Ventas>  o  = ventasDAO.findFirstByFechaAndMonto(fecha,monto);
        if(o.isPresent())
            return o.get();
        else
            return null;
    }

    @Override
    public Ventas agregar(Ventas venta) throws NegocioException, EncontradoException {
        try {
            if(null!=findFirstByFechaAndMonto(venta.getFecha(),venta.getMonto()))
                throw new EncontradoException("Ya existe una venta con la fecha  =" + venta.getFecha()
                        + " y con el monto ="+ venta.getMonto());
            cargar(venta.getId()); 									// tira excepcion sino no lo encuentra
            throw new EncontradoException("Ya existe una venta con id=" + venta.getId());
        } catch (NoEncontradoException e) {
        }
        try {
            return ventasDAO.save(venta);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
    }

    @Override
    public Ventas modificar(Ventas venta) throws NegocioException, NoEncontradoException {
        //me viene un id existe con su monto y fecha
        //Paso 1: busco existencia del id de  venta
        //Paso 2: busco existencia de "monto" y "fecha" duplicado
        //Paso 3_a: si el "monto" y "fecha" de la venta esta asignado a un user con diferente id del que se quiere modificar entonces tengo que generar un error
        //Paso 3_b: si el "monto" y "fecha" de la venta es el mismo id de la venta que me viene, entonces no se debe de generar error
        //Paso 4:  Sino ninguna venta  tiene asignado el "monto" y "fecha" se lo debe de modificar sin problemas

        cargar(venta.getId()); //Paso 1
        Ventas ventaExist = findFirstByFechaAndMonto(venta.getFecha(),venta.getMonto());

        if(null != ventaExist ) { //Paso 2

            if (venta.getId() != ventaExist.getId())
                throw new NegocioException("Ya existe una venta con el id" + ventaExist.getId() + " con fecha  ="
                        + venta.getFecha() + " y monto" + venta.getMonto() );	//Paso 3_a

            return	saveVenta(venta);	//Paso 3_b
        }

        return saveVenta(venta);	//Paso 4
    }

    private  Ventas saveVenta(Ventas venta) throws NegocioException {
        try {
            return ventasDAO.save(venta); // sino existe el user lo cargo
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
    }

    @Override
    public void eliminar(long id) throws NegocioException, NoEncontradoException {
        cargar(id);
        try {
            ventasDAO.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
    }
}
