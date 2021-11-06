package ar.edu.iua.iw3.eventos;

import org.springframework.context.ApplicationEvent;

public class ProductoEvent extends ApplicationEvent {

	private static final long serialVersionUID = -6894319398801260635L;

	public ProductoEvent(Object source, Tipo tipo) {
		super(source);
		this.tipo=tipo;
	}

	public enum Tipo {
		SUBE_PRECIO,
		VENCIMIENTO_PROXIMO
	}
	
	private Tipo tipo;
	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	public ProductoEvent(Object source) {
		super(source);
		
	}

}
