package ar.edu.iua.iw3.cuentas;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.Transient;


@Entity
@Table(name = "users")
public class User implements UserDetails, Serializable  {
	
	private static final long serialVersionUID = -8641078820166294877L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(length = 70, nullable = false)
	private String nombre;
	
	@Column(length = 70, nullable = false)
	private String apellido;
	
	@Column(length = 200 , nullable = true, unique = true)
	private String email;
	
	@Column(length = 150 , nullable = true)
	private String password;
	
	@Column(length = 50 , nullable = false, unique = true)
	private String username;
	
	@Column(columnDefinition = "TINYINT DEFAULT 1")
	private boolean accountNonExpired;
	
	@Column(columnDefinition = "TINYINT DEFAULT 1")
	private boolean accountNonLocked;

	@Column(columnDefinition = "TINYINT DEFAULT 1")
	private boolean credentialsNonExpired;
	
	@Column(columnDefinition = "TINYINT DEFAULT 1")
	private boolean enabled;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable( name= "users_roles",
		joinColumns = {	
				@JoinColumn(name="id_user", referencedColumnName = "id")},
				inverseJoinColumns = {@JoinColumn(name = "id_rol", referencedColumnName = "id")}
				)
	
	private Set <Rol> roles;

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}

	@Override	//agrego los roles que tendra un usuario
	public Collection<? extends GrantedAuthority> getAuthorities() {
	/*	List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		
		for(Rol r: getRoles()) {
			roles.add(new SimpleGrantedAuthority(r.getNombre()));
		}*/
		List<GrantedAuthority> roles = getRoles()
				.stream()		//tranforma en flujo
				.map(r -> new SimpleGrantedAuthority(r.getNombre()))	//map los roles como SGA
				.collect(Collectors.toList());	//reduce
		return roles;
	}
	
	/*
	 * -r1		stram()					map											REDUCE		- SGA(r1)	
	 * -r2		--> -	-	-	- 		-----> SGA(r1)	SGA(r2)	SGA(r3)	SGA(r4)      ---->    	- SGA(r2)
	 * -r3			r1	r2	r3	r4																- SGA(r3)
	 * -r4																						- SGA(r4)
	 * */
	
	//si la cuenta esta todo bien me devuele null sino me devuelve que info de la cuenta
	public String checkAccount(PasswordEncoder passwordEncoder, String password) {
		if (!passwordEncoder.matches(password, getPassword()))
			return "BAD_PASSWORD";
		if (!isEnabled())
			return "ACCOUNT_NOT_ENABLED";
		if (!isAccountNonLocked())
			return "ACCOUNT_LOCKED";
		if (!isCredentialsNonExpired())
			return "CREDENTIALS_EXPIRED";
		if (!isAccountNonExpired())
			return "ACCOUNT_EXPIRED";

		return null;
	}
	
	@Transient
	public String getNombreCompleto() {
		return String.format("%s, %s", getApellido(), getNombre());
	}
	

}
