package ar.edu.iua.iw3.negocio;

import ar.edu.iua.iw3.eventos.ProductoEvent;
import ar.edu.iua.iw3.modelo.Rubro;
import ar.edu.iua.iw3.modelo.persistencia.RubroRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ar.edu.iua.iw3.modelo.dto.ProductoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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
	@Autowired
	private RubroRepository rubroDAO;

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
	public List<Rubro> listadoRubros() throws NegocioException {
		try {
			return rubroDAO.findAll();
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
	
	private List<Producto> findProductByPrecio(double precio) {
		return productoDAO.findByPrecio(precio);
	}
	

	@Override
	public List<Producto> listarProductByPrecio(double precio) throws NegocioException, NoEncontradoException {
		if(findProductByPrecio(precio).size() == 0) {
			throw new NoEncontradoException("No se encuentran productos con el precio " + precio);
		}
		return findProductByPrecio(precio);
	}
	
	
	private Producto findProductByDescripcion(String descripcion) {
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
		
		Producto old = cargar(producto.getId()); //Paso 1

		//si ocurre esto disparo el evento
		if(old.getPrecio()<producto.getPrecio()*0.9){
			generarEvento(producto, ProductoEvent.Tipo.SUBE_PRECIO);//genero el evento enviando el producto que lo dispara y el motivo o tipo
		}

		Producto productoWithDescription = findProductByDescripcion(producto.getDescripcion());		
		
		if(null!=productoWithDescription) { //Paso 2 
			
			if (producto.getId() != productoWithDescription.getId()) 
				throw new NegocioException("Ya existe el producto " + productoWithDescription.getId() + "con la descripcion ="
						+ producto.getDescripcion());	//Paso 3_a
			
			return	saveProduct(producto);	//Paso 3_b
		}
		
		return saveProduct(producto);	//Paso 4
	}

	@Autowired
	private ApplicationEventPublisher appEventPublisher;	// es la clase que dispara el evento

	private void generarEvento(Producto producto, ProductoEvent.Tipo tipo) {
		appEventPublisher.publishEvent(new ProductoEvent(producto, tipo));
	}


	private  Producto saveProduct(Producto producto) throws NegocioException {
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
	public List<Producto> listarProductosNoVencidos(Date fecha) throws NegocioException, NoEncontradoException {
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

	
	
	@Override
	public Producto buscarPorDetalle(String detalle) throws NegocioException, NoEncontradoException {
		Optional<Producto> o;
		try {
			o = (productoDAO.findByDetalleDetalle(detalle));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (o.isEmpty()) {
			throw new NoEncontradoException("No hay productos con ese detalle");
		}
		return o.get();
	}

	@Override
	public List<Producto> listarProductosConNombreProveedor(String name) throws NegocioException, NoEncontradoException {
		List<Producto> lista = new ArrayList<Producto>();
		try {
			lista = productoDAO.findByProveedorName(name);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (lista.size() == 0) {
			throw new NoEncontradoException("No hay productos que tengan como proveedor a "+ name);
		}
		return lista;
	}

	@Override
	public Producto buscarPorDetalleComponente(String detalle) throws NegocioException, NoEncontradoException {
		Optional<Producto> o;
		try {
			o = (productoDAO.findByComponenteListDescripcion(detalle));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (o.isEmpty()) {
			throw new NoEncontradoException("No hay componentes con ese detalle");
		}
		return o.get();
	}

	@Override
	public List<Producto> buscarPorDetalleComponentePorNativeQuery(String descripcion) throws NegocioException, NoEncontradoException {
		List<Producto> lista = new ArrayList<Producto>();
		try {
			lista = productoDAO.findByComponenteListDescripcionUserNativeQuery(descripcion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (lista.size() == 0) {
			throw new NoEncontradoException("No hay productos que tengas asignados que contengan componentes " +
					"cuyo como detalle sea: "+ descripcion);
		}
		return lista;
	}

	@Override
	public Producto modificarPrecioPorQueryNative(Producto producto) throws NegocioException, NoEncontradoException {
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

			return	saveWithQueryNative(producto);	//Paso 3_b
		}

		return saveWithQueryNative(producto);	//Paso 4
	}

	@Override
	public List<ProductoDTO> findByElPrecioAndDetalleDTO(String componente ) throws NegocioException, NoEncontradoException {
		List<ProductoDTO> lista = new ArrayList<ProductoDTO>();
		try {
			lista = productoDAO.findByElPrecioAndDetalleDTO(componente);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (lista.size() == 0) {
			throw new NoEncontradoException("No hay productos que tengas asignados que contengan componentes " +
					"cuyo como detalle sea: "+ componente);
		}
		return lista;
	}


	private Producto saveWithQueryNative(Producto producto) throws NegocioException {
		try{
			productoDAO.updateprecioWithQueryNative(producto.getPrecio(),producto.getId());
			return productoDAO.findById(producto.getId()).get();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
	}

	@Override
	public Page<Producto> findAllPage(Pageable pageable) {
		return productoDAO.findAll(pageable);
	}


}
