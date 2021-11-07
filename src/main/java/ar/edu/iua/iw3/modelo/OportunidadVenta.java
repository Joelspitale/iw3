package ar.edu.iua.iw3.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "oportunidades_de_venta")
public class OportunidadVenta implements Serializable {

	private static final long serialVersionUID = 6079652183783334166L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int cantidad;

	@Column(length = 256, nullable = false)
	private String clientePotencial;

	@Column(length = 256, nullable = true)
	private String comentarios;

	@Column(length = 256, nullable = false)
	private String mailCliente;
	@Column(columnDefinition = "TINYINT NOT NULL DEFAULT 0")
	private boolean concretada;

	@Column(columnDefinition = "DATETIME NOT NULL")
	private Date fechaHora;
	
	@ManyToOne
	@JoinColumn(name="id_producto")
	private Producto producto;
	
	public String checkBasicData() {
		if(getCantidad()<=0)
			return "El atributo 'cantidad' debe ser mayor que 0";
		if(getClientePotencial()==null || getClientePotencial().trim().length()==0) 
			return "El atributo 'clientePotencial' es obligatorio";
		if(getMailCliente()==null || getMailCliente().trim().length()==0) 
			return "El atributo 'mailCliente' es obligatorio";
		if(getProducto()==null) 
			return "El atributo 'producto' es obligatorio";
		if(getProducto().getDescripcion()==null || getProducto().getDescripcion().trim().length()==0) 
			return "El atributo 'producto.descripcion' es obligatorio";
		if(getProducto().getPrecio()<=0) 
			return "El atributo 'producto.precio' debe ser mayor que 0";
		if(getProducto().getCodigoExterno()==null || getProducto().getCodigoExterno().trim().length()==0) 
			return "El atributo 'producto.codigoExterno' es obligatorio";
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getClientePotencial() {
		return clientePotencial;
	}

	public void setClientePotencial(String clientePotencial) {
		this.clientePotencial = clientePotencial;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public String getMailCliente() {
		return mailCliente;
	}

	public void setMailCliente(String mailCliente) {
		this.mailCliente = mailCliente;
	}

	public boolean isConcretada() {
		return concretada;
	}

	public void setConcretada(boolean concretada) {
		this.concretada = concretada;
	}

	public Date getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	} 
	
}
