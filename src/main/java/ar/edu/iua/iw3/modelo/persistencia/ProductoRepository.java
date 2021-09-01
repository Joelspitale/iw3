package ar.edu.iua.iw3.modelo.persistencia;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.iw3.modelo.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

	Optional<Producto> findByDescripcion(String descripcion);

	Optional<List<Producto>> findByPrecio(double precio);

}
