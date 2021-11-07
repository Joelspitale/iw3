package ar.edu.iua.iw3.negocio;

import java.util.List;

import ar.edu.iua.iw3.modelo.OportunidadVenta;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.util.RespuestaGenerica;

public interface IOportunidadVentaNegocio {
	public RespuestaGenerica<OportunidadVenta> recibir(OportunidadVenta oportunidadVenta) throws NegocioException;

	public List<OportunidadVenta> lista() throws NegocioException;
}
