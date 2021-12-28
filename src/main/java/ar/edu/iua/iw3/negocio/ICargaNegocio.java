package ar.edu.iua.iw3.negocio;

import ar.edu.iua.iw3.modelo.Carga;
import ar.edu.iua.iw3.modelo.dto.CargaDTO;
import ar.edu.iua.iw3.negocio.excepciones.BadRequest;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.UnprocessableException;

import java.util.List;

public interface ICargaNegocio {

    List<Carga> listado() throws NegocioException;

    Carga traerUltimaCarga(String codigoExterno) throws NegocioException, NoEncontradoException;

    Carga cargar(long id) throws NegocioException, NoEncontradoException;

    Carga agregar(Carga carga) throws NegocioException, NoEncontradoException, BadRequest, UnprocessableException;

    Carga modificar(Carga carga) throws NegocioException, NoEncontradoException;

    void eliminar(long id) throws NegocioException, NoEncontradoException;

    CargaDTO getPromedioDensidadAndTemperaturaAndCaudal(String codigoExterno) throws NegocioException,NoEncontradoException;
}
