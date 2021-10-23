package ar.edu.iua.iw3.modelo.persistencia;

import ar.edu.iua.iw3.cuentas.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.edu.iua.iw3.modelo.Rubro;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface RubroRepository extends JpaRepository<Rubro, Long> {
    Optional<Rubro> findByRubro(String rubro);

    @Modifying
    @Transactional
    @Query(value = "delete from rubros r where r.id=:id", nativeQuery =  true)
    public void deleteRubroConQueryNative(@Param("id")Long id);
}
