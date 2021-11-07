package ar.edu.iua.iw3.modelo.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.iw3.modelo.OportunidadVenta;

@Repository
public interface OportunidadVentaRepository extends JpaRepository<OportunidadVenta, Long> {
	
}
