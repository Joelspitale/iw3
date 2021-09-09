package ar.edu.iua.iw3.modelo.persistencia;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import ar.edu.iua.iw3.modelo.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

	Optional<Producto> findByDescripcion(String descripcion);
	
	List<Producto> findByPrecio(double precio);
	
	List<Producto> findByPrecioBetween(double precio1, double precio2);

	List<Producto> findByPrecioOrderByDescripcion(double precio);
	
	//List<Producto> findAllOrderByDescripcion();
	
	List<Producto> findByFechaVencimientoIsNotNull();
	
	List<Producto> findByFechaVencimientoAfter(String fecha);
	
	List <Producto> findFirst2ByFechaVencimientoIsNotNullOrderByFechaVencimientoAsc();
}

//List<Person> findTop3ByCompanyOrderByName(Company company);

//findFirst2By si se agrega el First2 trae los dos primeros objetos 
//según el orden que hayamos determinado).



//(ayuda: se puede usar IsNotNull en el findBy)