package ar.edu.iua.iw3.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.iw3.modelo.Producto;
import ar.edu.iua.iw3.negocio.IProductoNegocio;
import ar.edu.iua.iw3.negocio.ProductoNegocio;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

@RestController
public class ProductosRestController {

	@Autowired
	private IProductoNegocio productoNegocio;

	// curl http://localhost:8080/productos
	
	private Logger log = LoggerFactory.getLogger(ProductoNegocio.class);
	
	@GetMapping(value="/productos")
	public ResponseEntity<List<Producto>> listado() {
		try {
			return new ResponseEntity<List<Producto>>(productoNegocio.listado(), HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<List<Producto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// curl http://localhost:8080/productos/1
	
	@GetMapping(value="/productos/{id}")
	public ResponseEntity<Producto> cargar(@PathVariable("id") long id) {
		try {
			return new ResponseEntity<Producto>(productoNegocio.cargar(id), HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<Producto>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoEncontradoException e) {
			return new ResponseEntity<Producto>(HttpStatus.NOT_FOUND);
		}
	}

	//curl -X POST  http://localhost:8080/productos -H "Content-Type: application/json" -d '{"id":2,"descripcion":"Leche","enStock":false,"precio":104.7,"rubro":{"id":1,"rubro":"Alimentos"},"descripcionExtendida":"Se trata de leche larga vida"}'
	
	@PostMapping(value="/productos")
	public ResponseEntity<String> agregar(@RequestBody Producto producto) {
		try {
			Producto respuesta=productoNegocio.agregar(producto);
			HttpHeaders responseHeaders=new HttpHeaders();
			responseHeaders.set("location", "/productos/"+respuesta.getId());
			return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);
		} catch (NegocioException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (EncontradoException e) {
			log.error(e.getMessage(), e);	//Se hace para ver el msj de error cuando el detalle esta duplicado al agregar un producto
			return new ResponseEntity<String>(HttpStatus.FOUND);
		}
	}
	
	// curl -X PUT  http://localhost:8080/productos -H "Content-Type: application/json" -d '{"id":2,"descripcion":"Leche","enStock":false,"precio":55,"rubro":{"id":1,"rubro":"Alimentos"},"descripcionExtendida":"Se trata de leche larga vida"}' -v
	@PutMapping(value="/productos")
	public ResponseEntity<String> modificar(@RequestBody Producto producto) {
		try {
			productoNegocio.modificar(producto);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (NegocioException e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoEncontradoException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
	
	// curl -X DELETE http://localhost:8080/productos/11 -v
	
	@DeleteMapping(value="/productos/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") long id) {
		try {
			productoNegocio.eliminar(id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoEncontradoException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value="/productos/precio/{precio}")
	public ResponseEntity<List<Producto>> listadoByPrecio(@PathVariable("precio") double precio) throws NoEncontradoException {
		try {
			return new ResponseEntity<List<Producto>>(productoNegocio.findProductByPrecio(precio), HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<List<Producto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


@GetMapping(value="/productos/descripcion/{desc}")
public ResponseEntity<Producto> listadoByDescripcion(@PathVariable("desc") String desc) throws NoEncontradoException {
	try {
		return new ResponseEntity<Producto>(productoNegocio.findProductBydescripcion(desc), HttpStatus.OK);
	} catch (NegocioException e) {
		return new ResponseEntity<Producto>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
}















