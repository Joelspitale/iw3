package ar.edu.iua.iw3.modelo.persistencia;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.iw3.modelo.Componente;

@Repository
public interface ComponenteRepository extends JpaRepository<Componente, Long> {

	Optional<Componente> findByDescripcion(String descripcion);

}
