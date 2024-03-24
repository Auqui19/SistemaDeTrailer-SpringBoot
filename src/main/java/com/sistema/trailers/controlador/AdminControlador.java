package com.sistema.trailers.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sistema.trailers.modelo.entidade.Genero;
import com.sistema.trailers.modelo.entidade.Pelicula;
import com.sistema.trailers.modelo.dao.GeneroDao;
import com.sistema.trailers.modelo.dao.PeliculaDao;
import com.sistema.trailers.servicio.impl.AlmacenServicioImpl;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

	@Autowired
	private PeliculaDao peliculaDao;

	@Autowired
	private GeneroDao generoDao;

	@Autowired
	private AlmacenServicioImpl servicio;

	@GetMapping("")
	public ModelAndView verPaginaDeInicio(@PageableDefault(sort = "titulo", size = 5) Pageable pageable) {
		Page<Pelicula> peliculas = peliculaDao.findAll(pageable);
		return new ModelAndView("admin/index").addObject("peliculas", peliculas);
	}

	@GetMapping("/peliculas/nuevo")
	public ModelAndView mostrarFormularioDeNuevaPelicula() {
		List<Genero> generos = generoDao.findAll(Sort.by("titulo"));
		return new ModelAndView("admin/nueva-pelicula").addObject("pelicula", new Pelicula()).addObject("generos",
				generos);
	}

	@PostMapping("/peliculas/nuevo")
	public ModelAndView registrarPelicula(@Validated Pelicula pelicula, BindingResult bindingResult) {
		if (bindingResult.hasErrors() || pelicula.getPortada().isEmpty()) {
			if (pelicula.getPortada().isEmpty()) {
				bindingResult.rejectValue("portada", "MultipartNotEmpty");
			}
			List<Genero> generos = generoDao.findAll(Sort.by("titulo"));
			return new ModelAndView("admin/nueva-pelicula").addObject("pelicula", pelicula).addObject("generos",
					generos);
		}
		String rutaPortada = servicio.almacenarArchivo(pelicula.getPortada());
		pelicula.setRutaPortada(rutaPortada);
		peliculaDao.save(pelicula);
		return new ModelAndView("redirect:/admin");
	}

	@GetMapping("/peliculas/{id}/editar")
	public ModelAndView mostrarFormularioeditarPelicula(@PathVariable Integer id) {
		List<Genero> generos = generoDao.findAll(Sort.by("titulo"));
		@SuppressWarnings("deprecation")
		Pelicula pelicula = peliculaDao.getOne(id);
		return new ModelAndView("admin/editar-pelicula").addObject("pelicula", pelicula).addObject("generos", generos);
	}

	@PostMapping("/peliculas/{id}/editar")
	public ModelAndView actualizarPelicula(@PathVariable Integer id, @Validated Pelicula pelicula,
			BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			List<Genero> generos = generoDao.findAll(Sort.by("titulo"));
			return new ModelAndView("admin/editar-pelicula").addObject("pelicula", pelicula).addObject("generos", generos);
		}
		@SuppressWarnings("deprecation")
		Pelicula peliculaDB = peliculaDao.getOne(id);
		peliculaDB.setTitulo(pelicula.getTitulo());
		peliculaDB.setSinopsis(pelicula.getSinopsis());
		peliculaDB.setFechaEstreno(pelicula.getFechaEstreno());
		peliculaDB.setYoutubeTrailerId(pelicula.getYoutubeTrailerId());
		peliculaDB.setGeneros(pelicula.getGeneros());
		
		if(!pelicula.getPortada().isEmpty()) {
			servicio.eliminarArchivo(peliculaDB.getRutaPortada());
			String rutaPortada = servicio.almacenarArchivo(pelicula.getPortada());
			peliculaDB.setRutaPortada(rutaPortada);
		}
		
		peliculaDao.save(peliculaDB);
		return new ModelAndView("redirect:/admin");
	}

	@PostMapping("/peliculas/{id}/eliminar")
	public String eliminarPelicula(@PathVariable Integer id) {
		@SuppressWarnings("deprecation")
		Pelicula pelicula = peliculaDao.getOne(id);
		peliculaDao.delete(pelicula);
		servicio.eliminarArchivo(pelicula.getRutaPortada());
		return "redirect:/admin";
	}
}
