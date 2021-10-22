package ar.edu.iua.iw3.negocio;

import ar.edu.iua.iw3.modelo.Componente;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

import java.util.List;

public interface IComponenteNegocio {
    public List<Componente> listado() throws NegocioException;

    public Componente cargar(long id) throws NegocioException, NoEncontradoException;

    public Componente agregar(Componente componente) throws NegocioException, EncontradoException;

    public Componente modificar(Componente componente) throws NegocioException, NoEncontradoException;

    public void eliminar(long id) throws NegocioException, NoEncontradoException;

}
