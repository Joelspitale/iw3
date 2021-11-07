package ar.edu.iua.iw3.util;

//utilizo esta clase para que nuestros controladores rest
// respondan a los sistemas externos con codigos y msj
public class MensajeRespuesta {

	public MensajeRespuesta() {
	}
	private int codigo;
	private String mensaje="OK";

	public int getCodigo() {
		return codigo;
	}

	public MensajeRespuesta(int codigo, String mensaje) {
		super();
		this.codigo = codigo;
		this.mensaje = mensaje;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	@Override
	public String toString() {
		return String.format("{\"codigo\":%s,\"mensaje\":\"%s\"}", getCodigo(), getMensaje());
	}
}
