package ar.edu.iua.iw3.negocio;


import java.sql.Date;
import java.util.List;
import java.util.Optional;

import ar.edu.iua.iw3.modelo.Producto;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

public interface IProductoNegocio {
	public List<Producto> listado() throws NegocioException;

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
	//ME FALTA AGREGAR EL BETWEEN ACA

	public Producto buscarPorDetalle(String detalle) throws NegocioException, NoEncontradoException;
}
