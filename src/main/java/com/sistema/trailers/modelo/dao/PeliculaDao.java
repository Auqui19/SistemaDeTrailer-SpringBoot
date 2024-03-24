package com.sistema.trailers.modelo.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sistema.trailers.modelo.entidade.Pelicula;

public interface PeliculaDao extends JpaRepository<Pelicula, Integer> {

	Page<Pelicula> findByTituloContainingIgnoreCase(String term, Pageable pageable);

}
