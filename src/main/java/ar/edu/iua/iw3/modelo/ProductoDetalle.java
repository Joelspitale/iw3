package ar.edu.iua.iw3.modelo;

import java.io.Serializable;
import javax.persistence.*;


@Entity
@Table(name = "ProductoDetalle")
public class ProductoDetalle implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	
	@Column(length = 100, nullable = false)
	private String detalle;
	
	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
}
