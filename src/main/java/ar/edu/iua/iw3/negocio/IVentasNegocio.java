package ar.edu.iua.iw3.negocio;

import ar.edu.iua.iw3.cuentas.User;
import ar.edu.iua.iw3.modelo.Ventas;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

import java.util.List;

public interface IVentasNegocio {
    public Ventas cargar(long id) throws NegocioException, NoEncontradoException;

    public List<Ventas> listar() throws NegocioException;

    public Ventas agregar(Ventas venta) throws NegocioException, EncontradoException;

    public Ventas modificar (Ventas venta) throws NegocioException, NoEncontradoException;

    void eliminar(long id) throws NegocioException, NoEncontradoException;
}
