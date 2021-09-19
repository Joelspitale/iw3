package ar.edu.iua.iw3.web;

import ar.edu.iua.iw3.cuentas.Rol;
import ar.edu.iua.iw3.modelo.Producto;
import ar.edu.iua.iw3.modelo.Ventas;
import ar.edu.iua.iw3.negocio.IVentasNegocio;
import ar.edu.iua.iw3.negocio.ProductoNegocio;
import ar.edu.iua.iw3.negocio.VentasNegocio;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VentasRestController {

    @Autowired
    private IVentasNegocio ventasNegocio;

    private Logger log = LoggerFactory.getLogger(VentasNegocio.class);

    @GetMapping(value="/ventas")
    public ResponseEntity<List<Ventas>> listado() {
        try {
            return new ResponseEntity<List<Ventas>>(ventasNegocio.listar(), HttpStatus.OK);
        } catch (NegocioException e) {
            return new ResponseEntity<List<Ventas>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value="/ventas")
    public ResponseEntity<String> agregar(@RequestBody Ventas ventas) {
        try {
            Ventas respuesta=ventasNegocio.agregar(ventas);
            HttpHeaders responseHeaders=new HttpHeaders();
            responseHeaders.set("location", "/ventas/"+respuesta.getId());
            return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);
        } catch (NegocioException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EncontradoException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<String>(HttpStatus.FOUND);
        }
    }

    @PutMapping(value="/ventas")
    public ResponseEntity<String> modificar(@RequestBody Ventas ventas) {
        try {
            ventasNegocio.modificar(ventas);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (NegocioException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoEncontradoException e) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value="/ventas/{id}")
    public ResponseEntity<String> eliminar(@PathVariable("id") long id) {
        try {
            ventasNegocio.eliminar(id);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (NegocioException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoEncontradoException e) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
    }

}
