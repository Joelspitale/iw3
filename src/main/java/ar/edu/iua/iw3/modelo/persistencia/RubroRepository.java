package ar.edu.iua.iw3.modelo.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.iw3.modelo.Rubro;

@Repository
public interface RubroRepository extends JpaRepository<Rubro, Integer> {

}
