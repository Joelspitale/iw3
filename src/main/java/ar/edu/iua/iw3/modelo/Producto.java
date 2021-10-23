package ar.edu.iua.iw3.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

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

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "componentes_de_productos",
			joinColumns = {@JoinColumn(name = "id_producto")},
			inverseJoinColumns = { @JoinColumn(name = "id_componente")})
	private List<Componente> componenteList = new ArrayList<Componente>();

	@Column(columnDefinition = "DOUBLE DEFAULT 0")
	private double precio;

	@Column(columnDefinition = "Date", nullable = true)
	private Date fechaVencimiento;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "proveedor_id")
	private Proveedor proveedor;

	@OneToOne(cascade =  CascadeType.ALL)
	@JoinColumn(name = "id_detalle")
	private ProductoDetalle detalle;
	
	@ManyToOne
	@JoinColumn(name = "id_rubro")
	private Rubro rubro;

	@ManyToMany
	@JoinTable(name = "productos_ventas",
			joinColumns = {@JoinColumn(name = "id_producto")},
			inverseJoinColumns = { @JoinColumn(name = "id_ventas")})
	private List<Ventas> listaVentas = new ArrayList<Ventas>();

	public List<Componente> getComponenteList() {
		return componenteList;
	}

	public void setComponenteList(List<Componente> componenteList) {
		this.componenteList = componenteList;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public ProductoDetalle getDetalle() {
		return detalle;
	}

	public void setDetalle(ProductoDetalle detalle) {
		this.detalle = detalle;
	}

	public Rubro getRubro() {
		return rubro;
	}

	public void setRubro(Rubro rubro) {
		this.rubro = rubro;
	}

	public List<Ventas> getListaVentas() {
		return listaVentas;
	}

	public void setListaVentas(List<Ventas> listaVentas) {
		this.listaVentas = listaVentas;
	}

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
