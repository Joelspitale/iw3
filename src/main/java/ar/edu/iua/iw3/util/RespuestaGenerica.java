package ar.edu.iua.iw3.util;

//utilizo esta clase para comunicarse entre cases del back-end con entidades y mensaje
public class RespuestaGenerica<T> {

	private T entidad;
	private MensajeRespuesta mensaje;

	public RespuestaGenerica(T entidad, MensajeRespuesta mensaje) {
		this.entidad = entidad;
		this.mensaje = mensaje;
	}

	public T getEntidad() {
		return entidad;
	}

	public void setEntidad(T entidad) {
		this.entidad = entidad;
	}

	public MensajeRespuesta getMensaje() {
		return mensaje;
	}

	public void setMensaje(MensajeRespuesta mensaje) {
		this.mensaje = mensaje;
	}

}
