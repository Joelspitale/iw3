package ar.edu.iua.iw3.modelo.persistencia;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ar.edu.iua.iw3.cuentas.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
	
	
	Optional<Rol> findByNombre(String nombre);
}
