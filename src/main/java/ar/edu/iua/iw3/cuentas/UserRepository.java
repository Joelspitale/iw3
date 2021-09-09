package ar.edu.iua.iw3.cuentas;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.iw3.modelo.Producto;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
 
	public Optional<User> findFirstByUsernameOrEmail(String username, String email);
}
