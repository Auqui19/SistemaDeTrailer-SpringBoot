package com.sistema.trailers.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.sistema.trailers.modelo.dao.PeliculaDao;
import com.sistema.trailers.modelo.entidade.Pelicula;
import com.sistema.trailers.modelo.entidade.SearchForm;

@RestController
@RequestMapping("")
public class HomeControlador {

	@Autowired
	private PeliculaDao peliculaDao;

	@GetMapping("")
	public ModelAndView verPaginaDeInicio() {
		List<Pelicula> ultimasPeliculas = peliculaDao
				.findAll(PageRequest.of(0, 8, Sort.by("fechaEstreno").descending())).toList();
		return new ModelAndView("index").addObject("ultimasPeliculas", ultimasPeliculas);
	}

	@GetMapping("/peliculas")
	public ModelAndView listarPeliculas(
			@ModelAttribute("searchForm") SearchForm searchForm,
			@PageableDefault(sort = "fechaEstreno", direction = Sort.Direction.DESC) Pageable pageable) {
		String term = searchForm.getTerm();
		Page<Pelicula> peliculas = peliculaDao.findAll(pageable);
		if (term != null && !term.isEmpty()) {
	        peliculas = peliculaDao.findByTituloContainingIgnoreCase(term, pageable);
	    } else {
	        peliculas = peliculaDao.findAll(pageable);
	    }
		return new ModelAndView("peliculas").addObject("peliculas", peliculas).addObject("searchForm", searchForm);
	}
	
	@GetMapping("/peliculas/{id}")
	public ModelAndView mostrarDetalleDePelicula(@PathVariable Integer id) {
		@SuppressWarnings("deprecation")
		Pelicula pelicula = peliculaDao.getOne(id);
		return new ModelAndView("pelicula").addObject("pelicula",pelicula);
	}
}
