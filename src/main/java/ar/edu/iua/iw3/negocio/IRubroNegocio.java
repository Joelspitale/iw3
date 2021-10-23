package ar.edu.iua.iw3.negocio;

import ar.edu.iua.iw3.cuentas.Rol;
import ar.edu.iua.iw3.modelo.Rubro;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

import java.util.List;

public interface IRubroNegocio {
    public Rubro cargar(long id) throws NegocioException, NoEncontradoException;

    public List<Rubro> listar() throws NegocioException;

    public Rubro agregar(Rubro rubro) throws NegocioException, EncontradoException;

    public Rubro modificar (Rubro rubro) throws NegocioException, NoEncontradoException;

    void eliminar(long id) throws NegocioException, NoEncontradoException;

    public void eliminarConQueryNative(long id) throws NegocioException, NoEncontradoException;
}
