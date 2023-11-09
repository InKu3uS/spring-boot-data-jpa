package com.neftali.springboot.app.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.neftali.springboot.app.model.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long>{
	
	//Consulta manual
	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByName(String term);
	
	//Generado por springboot segun el nombre de la funcion
	public List<Producto> findByNombreLikeIgnoreCase(String term);

}
