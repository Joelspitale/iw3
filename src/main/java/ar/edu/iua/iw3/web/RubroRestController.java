package ar.edu.iua.iw3.web;

import ar.edu.iua.iw3.cuentas.Rol;
import ar.edu.iua.iw3.modelo.Rubro;
import ar.edu.iua.iw3.negocio.IRolNegocio;
import ar.edu.iua.iw3.negocio.IRubroNegocio;
import ar.edu.iua.iw3.negocio.RolNegocio;
import ar.edu.iua.iw3.negocio.RubroNegocio;
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
public class RubroRestController {
    @Autowired
    private IRubroNegocio rubroNegocio;

    private Logger log = LoggerFactory.getLogger(RubroNegocio.class);

    @GetMapping(value="/rubros")
    public ResponseEntity<List<Rubro>> listado() {
        try {
            return new ResponseEntity<List<Rubro>>(rubroNegocio.listar(), HttpStatus.OK);
        } catch (NegocioException e) {
            return new ResponseEntity<List<Rubro>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //agregar
    @PostMapping(value="/rubros")
    public ResponseEntity<String> agregar(@RequestBody Rubro rubro) {
        try {
            Rubro respuesta=rubroNegocio.agregar(rubro);
            HttpHeaders responseHeaders=new HttpHeaders();
            responseHeaders.set("location", "/roles/"+respuesta.getId());
            return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);
        } catch (NegocioException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EncontradoException e) {
            log.error(e.getMessage(), e);	//Se hace para ver el msj de error cuando el detalle esta duplicado al agregar un rol
            return new ResponseEntity<String>(HttpStatus.FOUND);
        }
    }

    //modificar
    @PutMapping(value="/rubros")
    public ResponseEntity<String> modificar(@RequestBody Rubro rubro) {
        try {
            rubroNegocio.modificar(rubro);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (NegocioException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoEncontradoException e) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
    }

    //eliminar
    @DeleteMapping(value="/rubros/{id}")
    public ResponseEntity<String> eliminar(@PathVariable("id") long id) {
        try {
            rubroNegocio.eliminar(id);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (NegocioException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoEncontradoException e) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
    }

    //eliminar
    @DeleteMapping(value="/rubros-query-native/{id}")
    public ResponseEntity<String> eliminarConQueryNative(@PathVariable("id") long id) {
        try {
            rubroNegocio.eliminarConQueryNative(id);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (NegocioException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoEncontradoException e) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
    }
}
