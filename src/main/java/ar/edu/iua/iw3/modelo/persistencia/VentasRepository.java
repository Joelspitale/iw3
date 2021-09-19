package ar.edu.iua.iw3.modelo.persistencia;

import ar.edu.iua.iw3.modelo.Producto;
import ar.edu.iua.iw3.modelo.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
@Repository
public interface VentasRepository extends JpaRepository<Ventas, Long> {

    public Optional<Ventas> findFirstByFechaAndMonto(Date fecha, double monto);

}