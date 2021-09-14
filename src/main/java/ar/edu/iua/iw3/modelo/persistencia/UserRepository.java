package ar.edu.iua.iw3.modelo.persistencia;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.iw3.cuentas.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
 
public Optional<User> findFirstByUsernameOrEmail(String username, String email);

				
}
