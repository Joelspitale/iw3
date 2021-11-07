package ar.edu.iua.iw3.negocio;


import ar.edu.iua.iw3.modelo.Rubro;
import org.springframework.data.domain.Pageable;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import ar.edu.iua.iw3.modelo.Producto;
import ar.edu.iua.iw3.modelo.dto.ProductoDTO;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;
import org.springframework.data.domain.Page;

public interface IProductoNegocio {
	public List<Producto> listado() throws NegocioException;

	public List<Rubro> listadoRubros() throws NegocioException;

	public Producto cargar(long id) throws NegocioException, NoEncontradoException;

	public Producto agregar(Producto producto) throws NegocioException, EncontradoException;

	public Producto modificar(Producto producto) throws NegocioException, NoEncontradoException;

	public void eliminar(long id) throws NegocioException, NoEncontradoException;

	public List<Producto> listarProductByPrecio(double precio) throws NegocioException, NoEncontradoException;;
	
	public Producto listarByDescripcion(String descripcion) throws NegocioException, NoEncontradoException;
	
	public List<Producto> ordenarPorDescripcion(double precio) throws NegocioException, NoEncontradoException;
	
	public List<Producto> findByPrecioBetween(double precio1, double precio2)throws NegocioException, NoEncontradoException;

	public List<Producto> listarProductosFechaVencimientoNoNula() throws NegocioException, NoEncontradoException;
	
	public List<Producto> listarProductosNoVencidos(Date fecha) throws NegocioException, NoEncontradoException;
	
	public List<Producto> listarPrimeros2ProductosEnVencer() throws NegocioException, NoEncontradoException;

	public Producto buscarPorDetalle(String detalle) throws NegocioException, NoEncontradoException;

	public List<Producto> listarProductosConNombreProveedor(String name) throws NegocioException, NoEncontradoException;

	public Producto buscarPorDetalleComponente(String detalle) throws NegocioException, NoEncontradoException;

	public List<Producto> buscarPorDetalleComponentePorNativeQuery(String descripcion) throws NegocioException, NoEncontradoException;

	public Producto modificarPrecioPorQueryNative(Producto producto) throws NegocioException, NoEncontradoException;

	public List<ProductoDTO> findByElPrecioAndDetalleDTO(String componente) throws NegocioException, NoEncontradoException;

	public Page<Producto> findAllPage(Pageable pageable);

	//cargar producto con condigo externo
	public Producto cargar(String codigoExterno) throws NegocioException, NoEncontradoException;

	//si el producto existe en la bd lo actualizo y sino lo creo---> siempre le paso el producto con el contructor de los atributos mas importantes
	public Producto asegurarProducto(Producto producto) throws NegocioException;
}
