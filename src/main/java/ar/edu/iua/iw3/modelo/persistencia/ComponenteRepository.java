package ar.edu.iua.iw3.modelo.persistencia;

import ar.edu.iua.iw3.modelo.Componente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComponenteRepository extends JpaRepository<Componente, Long> {
    Optional<Componente> findByDescripcion(String descripcion);
}