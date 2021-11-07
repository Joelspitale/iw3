package ar.edu.iua.iw3.modelo.persistencia;

import org.springframework.data.domain.Pageable;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import ar.edu.iua.iw3.modelo.dto.ProductoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ar.edu.iua.iw3.modelo.Producto;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

	public Optional<Producto> findFirstByCodigoExterno(String codigoExterno);

	Optional<Producto> findByDescripcion(String descripcion);
	
	List<Producto> findByPrecio(double precio);
	
	List<Producto> findByPrecioBetween(double precio1, double precio2);

	List<Producto> findByPrecioOrderByDescripcion(double precio);

	@Query(nativeQuery = true)
	List<ProductoDTO> findByElPrecioAndDetalleDTO(String componente);

	List<Producto> findByProveedorName(String name);
	
	List<Producto> findByFechaVencimientoIsNotNull();
	
	List<Producto> findByFechaVencimientoAfter(Date fecha);
	
	List <Producto> findFirst2ByFechaVencimientoIsNotNullOrderByFechaVencimientoAsc();
	
	Optional<Producto> findByDetalleDetalle(String detalle);

	Optional<Producto> findByComponenteListDescripcion(String detalle);

	@Query(value = "select * from productos p \n" +
			"\tinner join componentes_de_productos cp on cp.id_producto = p.id\n" +
			"\tinner join componente c on c.id = cp.id_componente\n" +
			"\twhere c.descripcion like %:descripcion%", nativeQuery = true)
	public List<Producto> findByComponenteListDescripcionUserNativeQuery(@Param("descripcion") String descripcion);


	@Modifying
    @Transactional
    @Query(value = " UPDATE productos p SET p.precio=:precio WHERE id=:id", nativeQuery =  true)
	public void updateprecioWithQueryNative(@Param("precio") double precio,
												@Param("id") long id);


	@Query(value = "select * from productos ORDER BY rand()",
			countQuery = "SELECT count(*) FROM productos ",
			nativeQuery = true)
	public Page<Producto> findAll(Pageable pageable);

}




//findFirst2By si se agrega el First2 trae los dos primeros objetos 
//según el orden que hayamos determinado).


