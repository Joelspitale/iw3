package ar.edu.iua.iw3.negocio;

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
		   if(null!=findProductBydescripcion(producto.getDescripcion())) 
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
	
	public Producto findProductBydescripcion(String descripcion) {
		return productoDAO.findByDescripcion(descripcion).orElse(null);
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
		Producto productoWithDescription = findProductBydescripcion(producto.getDescripcion());		
		
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

	// @Bean
	// public IProductoNegocio getProductoNegocio() {
	// return new ProductoNegocio();
	// }

}
