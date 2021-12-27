package ar.edu.iua.iw3.negocio;

import java.util.List;

import ar.edu.iua.iw3.modelo.Camion;
import ar.edu.iua.iw3.negocio.excepciones.BadRequest;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

public interface ICamionNegocio {

	List<Camion> listado() throws NegocioException;

	Camion cargar(long id) throws NegocioException, NoEncontradoException;

	Camion agregar(Camion camion) throws NegocioException, EncontradoException, BadRequest;

	Camion modificar(Camion camion) throws NegocioException, NoEncontradoException;

	void eliminar(long id) throws NegocioException, NoEncontradoException;

    Camion setearPesoIni(Camion camionRecibido, Camion camionBD ) throws NoEncontradoException,NegocioException;;
}