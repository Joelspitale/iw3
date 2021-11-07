package ar.edu.iua.iw3.negocio;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ar.edu.iua.iw3.modelo.OportunidadVenta;
import ar.edu.iua.iw3.modelo.persistencia.OportunidadVentaRepository;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.util.MensajeRespuesta;
import ar.edu.iua.iw3.util.RespuestaGenerica;

@Service
public class OportunidadVentaNegocio implements IOportunidadVentaNegocio {

	@Autowired
	private IProductoNegocio productoService;
	
	@Autowired
	private OportunidadVentaRepository oportunidadVentaDAO;
	
	@Override
	public RespuestaGenerica<OportunidadVenta> recibir(OportunidadVenta oportunidadVenta) throws NegocioException {
		MensajeRespuesta m=new MensajeRespuesta();
		RespuestaGenerica<OportunidadVenta> r = new RespuestaGenerica<OportunidadVenta>(oportunidadVenta, m);

		//como m ya lo contiene r, puedo setear m y devolver r

		//primero chequeo que la oportunidad de venta esta bien cargada
		//-1 seteamos si hay algo mal
		String mensajeCheck=oportunidadVenta.checkBasicData();
		if(mensajeCheck!=null) {
			m.setCodigo(-1);
			m.setMensaje(mensajeCheck);
			return r;
		}

		//si esta todo bien entonces tengo que actualizar mi oportunidad de venta con los datos externos y guardarla en la bd
		try {
			//actualizo los datos del producto que me envian de mi sistema externo y lo guardo/actualizo en mi bd
			//y despues se lo coloco a mi oportunidad de venta
			oportunidadVenta.setProducto(productoService.asegurarProducto(oportunidadVenta.getProducto()));
			// le agrego los atributos que no me envian los sistemas externos de la oportunidad de venta
			oportunidadVenta.setFechaHora(new Date());
			oportunidadVenta.setConcretada(false);
			oportunidadVentaDAO.save(oportunidadVenta);
		} catch (Exception e) {
			throw new NegocioException(e);
		}
		return r;
	}

	@Override
	public List<OportunidadVenta> lista() throws NegocioException {
		try {			//puedo pasar como parametros el como me ordena el listado , en este caso por concretada,fechaHora
						// y asi me evito crear un metodo nuevo que me ordene por concretada,fechaHora
			return oportunidadVentaDAO.findAll(Sort.by("concretada").and(Sort.by("fechaHora")));
		} catch (Exception e) {
			throw new NegocioException(e);
		}
	}

}
