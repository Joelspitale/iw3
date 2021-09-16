package ar.edu.iua.iw3.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto implements Serializable {
	private static final long serialVersionUID = -4533737025960198404L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	//auto numerico
	private long id;
	
	@Column(length = 100, nullable = false, unique = true)
	private String descripcion;

	@Column(columnDefinition = "TINYINT DEFAULT 0")
	private boolean enStock;

	@Column(columnDefinition = "DOUBLE DEFAULT 0")
	private double precio;

	@Column(columnDefinition = "Date", nullable = true)
	private Date fechaVencimiento;
	
	public ProductoDetalle getDetalle() {
		return detalle;
	}

	public void setDetalle(ProductoDetalle detalle) {
		this.detalle = detalle;
	}

	@OneToOne(cascade =  CascadeType.ALL)
	@JoinColumn(name = "id_detalle")
	private ProductoDetalle detalle;
	
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	@ManyToOne
	@JoinColumn(name = "id_rubro")
	private Rubro rubro;
	
	public Rubro getRubro() {
		return rubro;
	}

	public void setRubro(Rubro rubro) {
		this.rubro = rubro;
	}

	/*
	@Transient			//indico que este atributo no tiene que ser mapeado en la bd
	private double precioConAumento;
	
	public double getPrecioConAumento() {
		return getPrecio()*2;
	}
	
	public void setPrecioConAumento(double precio) {
	}
	*/
	private String descripcionExtendida;

	public String getDescripcionExtendida() {
		return descripcionExtendida;
	}

	public void setDescripcionExtendida(String descripcionExtendida) {
		this.descripcionExtendida = descripcionExtendida;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public long getId() {
		return id;
	}

	public double getPrecio() {
		return precio;
	}

	public boolean isEnStock() {
		return enStock;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setEnStock(boolean enStock) {
		this.enStock = enStock;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	

}
