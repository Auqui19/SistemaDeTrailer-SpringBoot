package com.sistema.trailers.modelo.entidade;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pelicula {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pelicula")
	private Integer idPelicula;

	@NotBlank
	private String titulo;

	@NotBlank
	private String sinopsis;

	@NotNull
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate fechaEstreno;

	@NotBlank
	private String youtubeTrailerId;

	private String rutaPortada;

	/*Usaremos la anotación Muchos a Muchos 
	Usamos fech Lazy solo para que se cargue cuando lo necesitamos */
	@NotEmpty
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "genero_pelicula", 
				joinColumns = @JoinColumn(name = "id_pelicula"), 
				inverseJoinColumns = @JoinColumn(name = "id_genero"))
	private List<Genero> generos;
	
	/*El transient se usará para guardar el dato temporal y no en la base de datos */
	@Transient
	private MultipartFile portada;
	

}
