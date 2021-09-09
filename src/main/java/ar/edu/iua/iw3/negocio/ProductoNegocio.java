package ar.edu.iua.iw3.negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.iw3.demo.perfiles.PruebaPerfilH2Mem;
import ar.edu.iua.iw3.modelo.Producto;
import ar.edu.iua.iw3.modelo.persistencia.ProductoRepository;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

@Service
//@Configuration
public class ProductoNegocio implements IProductoNegocio {
	private Logger log = LoggerFactory.getLogger(ProductoNegocio.class);
	@Autowired
	private ProductoRepository productoDAO;

	// IoC
	// A y B
	// A tiene el control <-- programadora Date fecha=new Date()
	// B <- Spring

	@Override
	public List<Producto> listado() throws NegocioException {
		try {
			return productoDAO.findAll();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
	}

	@Override
	public Producto agregar(Producto producto) throws NegocioException, EncontradoException {
		try {
		   if(null!=findProductByDescripcion(producto.getDescripcion())) 
		        throw new EncontradoException("Ya existe un producto con la descripcion =" + producto.getDescripcion());
			cargar(producto.getId()); 									// tira excepcion sino no lo encuentra
			throw new EncontradoException("Ya existe un producto con id=" + producto.getId());
		} catch (NoEncontradoException e) {
		}
		try {
			return productoDAO.save(producto);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}

	}
	
	public List<Producto> findProductByPrecio(double precio) {
		return productoDAO.findByPrecio(precio);
	}
	

	@Override
	public List<Producto> listarProductByPrecio(double precio) throws NegocioException, NoEncontradoException {
		if(findProductByPrecio(precio).size() == 0) {
			throw new NoEncontradoException("No se encuentran productos con el precio " + precio);
		}
		return findProductByPrecio(precio);
	}
	
	
	public Producto findProductByDescripcion(String descripcion) {
		return productoDAO.findByDescripcion(descripcion).orElse(null);
	}
	
	@Override
	public Producto listarByDescripcion(String descripcion) throws NegocioException, NoEncontradoException {
		if(findProductByDescripcion(descripcion) == null) {
			throw new NoEncontradoException("No se encuentra el producto la descripcion " + descripcion);
		}
		return findProductByDescripcion(descripcion);
	}


	@Override
	public Producto cargar(long id) throws NegocioException, NoEncontradoException {
		Optional<Producto> o;
		try {
			o = productoDAO.findById(id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (!o.isPresent()) {
			throw new NoEncontradoException("No se encuentra el producto con id=" + id);
		}
		return o.get();
	}

	@Override
	public Producto modificar(Producto producto) throws NegocioException, NoEncontradoException {
		//me viene un id existe con su detalle
		//Paso 1: busco existencia del id del producto	
		//Paso 2: busco existencia de detalle duplicado 	
		//Paso 3_a:si el detalle del producto esta asignado a un producto con diferente id del que se quiere modificar entonces tengo se genera un error
		//Paso 3_b: si el detalle del producto es el mismo id del produto entonces no se debe de generar error
		//Paso 4:  Sino ningun producto tiene asignado el detalle se lo debe de modiicar sin problemas
		
		cargar(producto.getId()); //Paso 1
		Producto productoWithDescription = findProductByDescripcion(producto.getDescripcion());		
		
		if(null!=productoWithDescription) { //Paso 2 
			
			if (producto.getId() != productoWithDescription.getId()) 
				throw new NegocioException("Ya existe el producto " + productoWithDescription.getId() + "con la descripcion ="
						+ producto.getDescripcion());	//Paso 3_a
			
			return	saveProduct(producto);	//Paso 3_b
		}
		
		return saveProduct(producto);	//Paso 4
	}
	
	public  Producto saveProduct(Producto producto) throws NegocioException {
		try {
			return productoDAO.save(producto); // sino existe el producto lo cargo
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
	}

	@Override
	public void eliminar(long id) throws NegocioException, NoEncontradoException {
		cargar(id);
		try {
			productoDAO.deleteById(id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
	}

	@Override
	public List<Producto> ordenarPorDescripcion(double precio) throws NegocioException, NoEncontradoException {
		List<Producto> lista = new ArrayList<Producto>();
		try {
			lista = productoDAO.findByPrecioOrderByDescripcion( precio);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (lista.size() == 0) {
			throw new NoEncontradoException("No se encuentra productos con el precio:"+ precio);
		}
		return lista;
		
	}

	@Override
	public List<Producto> findByPrecioBetween(double precio1, double precio2) throws NegocioException, NoEncontradoException {
			return productoDAO.findByPrecioBetween(precio1,precio2);
		}

	

	// @Bean
	// public IProductoNegocio getProductoNegocio() {
	// return new ProductoNegocio();
	// }
	
	
	
	
	@Override
	public List<Producto> listarProductosFechaVencimientoNoNula() throws NegocioException, NoEncontradoException {
		List<Producto> lista = new ArrayList<Producto>();
		try {
			lista = productoDAO.findByFechaVencimientoIsNotNull( );
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (lista.size() == 0) {
			throw new NoEncontradoException("No hay productos con fecha de vencimiento");
		}
		return lista;
		
	}

	@Override
	public List<Producto> listarProductosNoVencidos(String fecha) throws NegocioException, NoEncontradoException {
		List<Producto> lista = new ArrayList<Producto>();
		try {
			lista = productoDAO.findByFechaVencimientoAfter(fecha);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (lista.size() == 0) {
			throw new NoEncontradoException("No hay productos en condicion de consumo");
		}
		return lista;
	}

	@Override
	public List<Producto> listarPrimeros2ProductosEnVencer() throws NegocioException, NoEncontradoException {
		List<Producto> lista = new ArrayList<Producto>();
		try {
			lista = productoDAO.findFirst2ByFechaVencimientoIsNotNullOrderByFechaVencimientoAsc();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (lista.size() == 0) {
			throw new NoEncontradoException("No hay productos con fecha de vencimiento");
		}
		return lista;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
